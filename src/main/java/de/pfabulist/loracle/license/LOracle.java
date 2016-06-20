package de.pfabulist.loracle.license;

import com.esotericsoftware.minlog.Log;
import de.pfabulist.frex.Frex;
import de.pfabulist.kleinod.collection.P;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static de.pfabulist.nonnullbydefault.NonnullCheck._nn;
import static de.pfabulist.unchecked.NullCheck._orElseGet;
import static de.pfabulist.unchecked.NullCheck._orElseThrow;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

@SuppressFBWarnings( { "URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD", "UUF_UNUSED_PUBLIC_OR_PROTECTED_FIELD" } )
public class LOracle {

    @SuppressFBWarnings( "URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD" )
    public static class More {
        public LicenseAttributes attributes;
        public List<String> urls = new ArrayList<>();
        public List<String> longNames = new ArrayList<>();
        public List<Coordinates> specific = new ArrayList<>();
        public Set<String> couldbeName = new HashSet<>();
        public Set<String> couldbeUrl = new HashSet<>();

        public More( boolean spdx ) {
            attributes = new LicenseAttributes();
            attributes.setSPDX( spdx );
        }
    }

    private Map<String, More> singles = new TreeMap<>( String::compareTo );
    //new HashMap<>();
    private Map<String, Boolean> licenseExceptions = new HashMap<>();
    private Map<String, More> composites = new HashMap<>();

    private Map<String, P<String, String>> urlsInTime = new TreeMap<>( String::compareTo );

    private final transient Map<String, Set<LicenseID>> couldbeNames = new HashMap<>();
    private final transient Map<String, Set<LicenseID>> couldbeUrls = new HashMap<>();

    private final transient SPDXParser parser;
    private final transient Normalizer normalizer = new Normalizer();
    private transient Map<Coordinates, LicenseID> coordinatesMap = new HashMap<>();
    // new TreeMap<>( (c,d) -> c.matches( d ) ? 0 : (c.hashCode() - d.hashCode()));
    private transient Map<String, LicenseID> longNameMapper = new HashMap<>();
    private transient Map<String, LicenseID> urls = new HashMap<>();

    public LOracle() {
        parser = new SPDXParser( this );
    }

    public LOracle spread() {
        if( !longNameMapper.isEmpty() ) {
            throw new IllegalStateException( "can only be called after json construction" );
        }

        singles.forEach( ( name, more ) -> {
                             LicenseID lid = new SingleLicense( name );
                             more.urls.forEach( u -> urls.put( u, lid ) );
                             more.longNames.forEach( l -> longNameMapper.putIfAbsent( l, lid ) );
                             more.specific.forEach( coo -> coordinatesMap.putIfAbsent( coo, lid ) );
                             more.couldbeName.forEach( n -> {
                                 couldbeNames.putIfAbsent( n, new HashSet<>() );
                                 _nn( couldbeNames.get( n ) ).add( lid );
                             } );
                             more.couldbeUrl.forEach( n -> {
                                 couldbeUrls.putIfAbsent( n, new HashSet<>() );
                                 _nn( couldbeUrls.get( n ) ).add( lid );
                             } );
                         }
        );

        composites.forEach( ( name, more ) -> {

                                LicenseID lid = getOrThrowByName( name );
                                more.urls.forEach( u -> urls.put( u, lid ) );
                                more.longNames.forEach( l -> longNameMapper.putIfAbsent( l, lid ) );
                                more.specific.forEach( coo -> coordinatesMap.putIfAbsent( coo, lid ) );
                                more.couldbeName.forEach( n -> {
                                    couldbeNames.putIfAbsent( n, new HashSet<>() );
                                    _nn( couldbeNames.get( n ) ).add( lid );
                                } );
                                more.couldbeUrl.forEach( n -> {
                                    couldbeUrls.putIfAbsent( n, new HashSet<>() );
                                    _nn( couldbeUrls.get( n ) ).add( lid );
                                } );
                            }
        );

        // todo couldbes

        return this;
    }

    // todo copy left ?
    public LicenseID getOrLater( SingleLicense license, boolean orLater, Optional<LicenseExclude> exception ) {
        if( !orLater && !exception.isPresent() ) {
            return license;
        }

        LicenseID ret = new ModifiedSingleLicense( license, orLater, exception );

        composites.putIfAbsent( ret.getId(), new More( getMore( license ).attributes.isSPDX() ) );
        getMore( ret ).attributes.setCopyLeft( getMore( license ).attributes.isCopyLeftDef() );

        return ret;
    }

    public LicenseExclude getExceptionOrThrow( String ex ) {
        String lower = ex.trim().toLowerCase( Locale.US );
        if( licenseExceptions.containsKey( lower ) ) {
            return new LicenseExclude( lower );
        }

        throw new IllegalArgumentException( "no such exception: " + ex );
    }

    // todo to normalizer
    public static String trim( String in ) {
        return _nn( in.toLowerCase( Locale.US ) ).replaceAll( ",", " " ).trim();
    }

    public SingleLicense newSingle( String name, boolean spdx ) {
        // todo test for extensions ( getAnd ... ?

        String lower = trim( name );

        if( getByName( lower ).isPresent() ) {
            throw new IllegalArgumentException( "not a new single license: " + name );
        }

        Set<LicenseID> guesses = guessByName( lower );
        if( !guesses.isEmpty() ) {
//            throw new IllegalArgumentException( "not a new single license: " + name );
            removeNameGuess( lower );
        }

        singles.put( lower, new More( spdx ) );

        SingleLicense ret = new SingleLicense( lower );

        String lng = Arrays.stream( lower.split( "[- ]" ) ).collect( Collectors.joining( " " ) );

        addLongName( ret, lng );

        return ret;
    }

    private void removeNameGuess( String name ) {
        if( !name.equals( "w3c" ) ) {
            throw new IllegalArgumentException( "not a new single license: " + name );
        }
        Log.warn( "removing could be (so that it can be a new single id) " + name );
        Set<LicenseID> guesses = guessByName( name );
        couldbeNames.remove( name );
        guesses.stream().forEach( l -> getMore( l ).couldbeName.remove( name ) );
    }

    public More getMore( LicenseID licenseID ) {

        return _orElseGet( singles.get( licenseID.getId() ),
                           () -> _orElseThrow( composites.get( licenseID.getId() ),
                                               () -> new IllegalArgumentException( "no such license: " + licenseID ) ) );
    }

    public LicenseAttributes getAttributes( LicenseID l ) {
        return getMore( l ).attributes;
    }

    private static Pattern withVersion = Frex.any().oneOrMore().lazy().var( "base" ).
            then( Frex.txt( ' ' ) ).then( Frex.or( Frex.number(), Frex.txt( '.' ) ).oneOrMore() ).buildCaseInsensitivePattern();

    public void addLongName( LicenseID license, String longName ) {
        String reduced = normalizer.reduce( longName );
        if( longNameMapper.containsKey( reduced ) ) {
            if( license.equals( longNameMapper.get( reduced ) ) ) {
                return;
            }
            throw new IllegalArgumentException( "mapped already <" + longName + "> (" + reduced + ") as " + license + " <" + longNameMapper.get( reduced ) + ">" );
        }

        longNameMapper.put( reduced, license );
        getMore( license ).longNames.add( reduced );

        Matcher versioned = withVersion.matcher( reduced );
        if( versioned.matches() ) {
            addCouldbeName( license, _nn( versioned.group( "base" ) ) );
        }

        // todo refactor
        if( reduced.contains( "gnu" ) ) {
            if( reduced.contains( "lesser" ) ) {
                addCouldbeName( license, "gnu lesser" );
            } else {
                if( reduced.contains( "affero" ) ) {
                    addCouldbeName( license, "affero gnu" );
                    addCouldbeName( license, "affero" );
                } else {
                    addCouldbeName( license, "gnu" );
                }
            }
        } else if( reduced.contains( "affero" ) ) {
            addCouldbeName( license, "affero gnu" );
            addCouldbeName( license, "affero" );
        } else if( reduced.contains( "bsd" ) ) {
            addCouldbeName( license, "bsd" );
        }
    }

    public Optional<SingleLicense> getSingle( String name ) {
        String lower = name.trim().toLowerCase( Locale.US );

        if( !singles.containsKey( lower ) ) {
            return Optional.empty();
        }

        return Optional.of( new SingleLicense( lower ) );

    }

    public Optional<LicenseID> getByName1( String name ) {
        try {
            return Optional.of( parser.parse( name ) );
        } catch( Exception e ) {
            // not found
        }

        return Optional.ofNullable( longNameMapper.get( normalizer.reduce( name ) ) );
    }

    private static final Pattern orLater = Frex.any().oneOrMore().var( "base" ).then( Frex.txt( " or later" ) ).buildCaseInsensitivePattern();

    public MappedLicense getByName( String name ) {
        try {
            return MappedLicense.of( parser.parse( name ), "by parsed name: " + name );
        } catch( Exception e ) {
            // not found
        }

        boolean plus = false;
        String base = name;
        Matcher matcher = orLater.matcher( name );
        if( matcher.matches() ) {
            base = _nn( matcher.group( "base" ) );
            plus = true;
        }

        Optional<LicenseID> ret = Optional.ofNullable( longNameMapper.get( normalizer.reduce( base ) ) );

        if( ret.isPresent() ) {
            if( plus ) {
                if( ret.get() instanceof SingleLicense ) {
                    return MappedLicense.of( getOrLater( (SingleLicense) _nn( ret.get() ), true, Optional.empty() ), "by normalized name and 'or later': " + name );
                } else {
                    // or later with a composite license is strange
                    return MappedLicense.empty();
                }
            }
            return MappedLicense.of( ret, "by normalized name: " + name );
        }

        try {
            return MappedLicense.of( new FuzzyParser( this ).parse( name ), "by fuzzy parsing name: " + name );
        } catch( Exception e ) {
            return MappedLicense.empty();
        }
    }

    public LicenseID getOrThrowByName( String name ) {
        return getByName( name ).orElseThrow( () -> new IllegalArgumentException( "no such license name: " + name ) );
    }

    public void addLicenseForArtifact( Coordinates coo, LicenseID licenseID ) {
        getMore( licenseID ).specific.add( coo );
        coordinatesMap.put( coo, licenseID );
    }

    public MappedLicense getByCoordinates( Coordinates coo ) {

        Optional<LicenseID> ret = Optional.ofNullable( coordinatesMap.get( coo ) );
        if( ret.isPresent() ) {
            return MappedLicense.of( ret, "by direct coordinates" );
        }

        return coordinatesMap.keySet().stream().
                filter( c -> c.matches( coo ) ).
                findAny().
                map( c -> MappedLicense.of( _nn( coordinatesMap.get( c ) ), "by patterned coordinates " + c ) ).
                orElse( MappedLicense.empty() );
    }

    public void addException( String name, boolean spdx ) {
        String lower = name.trim().toLowerCase( Locale.US );

        if( licenseExceptions.containsKey( lower ) ) {
            throw new IllegalArgumentException( "existing exception: " + lower );
        }

        licenseExceptions.put( lower, spdx );
    }

    private static final Pattern urlWithLongname =
            Frex.any().oneOrMore().lazy().
                    then( Frex.txt( '/' ) ).
                    then( Frex.anyBut( Frex.txt( '/' ) ).oneOrMore().var( "fname" ) ).buildCaseInsensitivePattern();

    public MappedLicense getByUrl( String url ) {

        Optional<String> rel = normalizer.normalizeUrl( url );
        if( !rel.isPresent() ) {
            return MappedLicense.empty();
        }

        Optional<LicenseID> ret = Optional.ofNullable( urls.get( rel.get() ) );
        if( ret.isPresent() ) {
            if( urlsInTime.containsKey( rel.get() ) ) {
                return MappedLicense.of( ret, "by url " + url + " checked at: " + _nn( urlsInTime.get( rel.get() ) ).i0 );
            }
            return MappedLicense.of( ret, "by url " + url );
        }

        Matcher end = urlWithLongname.matcher( _nn( rel.get() ) );
        if( !end.matches() ) {
            Log.warn( "not a real url? " + url + " red: " + rel );
            return MappedLicense.empty();
        }

        MappedLicense ml = getByName( _nn( end.group( "fname" ) ) );
        if( ml.isPresent() ) {
            return ml.addReason( "by name match of url: " + url );
        }

        return MappedLicense.empty();

    }

    public void setOsiApproval( LicenseID licenseID, boolean osiApproved ) {
        getMore( licenseID ).attributes.setOsiApproved( osiApproved );
    }

    public CompositeLicense getAnd( LicenseID left, LicenseID right ) {
        CompositeLicense ret = new CompositeLicense( false, left, right );

        More mLeft = getMore( left );
        More mRight = getMore( right );

        composites.putIfAbsent( ret.getId(), new More( mLeft.attributes.isSPDX() && mRight.attributes.isSPDX() ) );
        getMore( ret ).attributes.setCopyLeft( mLeft.attributes.isCopyLeftDef() || mRight.attributes.isCopyLeftDef() );
        return ret;
    }

    public CompositeLicense getOr( LicenseID left, LicenseID right ) {
        CompositeLicense ret = new CompositeLicense( true, left, right );
        More mLeft = getMore( left );
        More mRight = getMore( right );

        composites.putIfAbsent( ret.getId(), new More( mLeft.attributes.isSPDX() && mRight.attributes.isSPDX() ) );
        getMore( ret ).attributes.setCopyLeft( mLeft.attributes.isCopyLeftDef() && mRight.attributes.isCopyLeftDef() );

        return ret;
    }

    public void addCouldbeName( LicenseID license, String couldbe ) {
        getByName( couldbe ).ifPresent(
                l -> new IllegalArgumentException( "name is already set to definitive license: " + couldbe + " -> " + l ) );

        getMore( license ).couldbeName.add( couldbe );

        couldbeNames.putIfAbsent( couldbe, new HashSet<>() );
        //noinspection ConstantConditions
        couldbeNames.get( couldbe ).add( license );

    }

    public Set<LicenseID> guessByName( String name ) {
        return Optional.ofNullable( couldbeNames.get( normalizer.reduce( name ) ) ).orElseGet( Collections::emptySet );
    }

    public Set<LicenseID> guessByUrl( String url ) {

        return normalizer.normalizeUrl( url ).map( u -> _orElseGet( couldbeUrls.get( u ), new HashSet<LicenseID>() ) ).orElseGet( HashSet::new );
    }

    public void addCouldBeUrl( LicenseID license, String url ) {

    }

    public void addUrl( LicenseID license, String url ) {

        String rel = normalizer.normalizeUrl( url ).orElseThrow( () -> new IllegalArgumentException( "not a url" ) );

        if( urls.containsKey( rel ) ) {

            LicenseID old = _nn( urls.get( rel ) );

            if( old.equals( license ) ) {
                return;
            }

            Log.info( "known url: " + url + " as: " + old + "  moving it to couldbe, together with " + license );

            urls.remove( rel );
            More oldMore = getMore( old );
            oldMore.urls.remove( rel );
            oldMore.couldbeUrl.add( rel );

            More thisMore = getMore( license );
            thisMore.couldbeUrl.add( rel );

            couldbeUrls.putIfAbsent( rel, new HashSet<>() );
            //noinspection ConstantConditions
            couldbeUrls.get( rel ).add( license );
            //noinspection ConstantConditions
            couldbeUrls.get( rel ).add( old );

            return;
        }

        if( couldbeUrls.containsKey( rel ) ) {

            Set<LicenseID> could = _nn( couldbeUrls.get( rel ) );

            if( could.size() > 1 ) {
                if( could.contains( license ) ) {
                    // known
                    return;
                }

                could.add( license );
                getMore( license ).couldbeUrl.add( rel );
                return;
            }

            throw new IllegalArgumentException( "unique could be ?: " + rel + " " + license );
//
//
//            couldbeUrls.get( rel ).add( license );
//            getMore( license ).urls.add( rel );
//            Log.info( "known couldbe url: " + url + " for " + license );
//            return;
        }

        getMore( license ).urls.add( rel );
        urls.put( rel, license );
    }

    public void addUrlCheckedAt( LicenseID license, String url, String date ) {
        Log.info( "added url checkedat " + url + " " + license + " " + date );
        String norm = normalizer.normalizeUrl( url ).orElseThrow( () -> new IllegalArgumentException( "can't normalize this url: " + url ) );
        urlsInTime.put( norm, P.of( date, license.toString() ) );
    }

    public void allowUrlsCheckedDaysBefore( int days ) {
        LocalDate now = LocalDate.now();

        urlsInTime.forEach( ( u, p ) -> {
            try {
                LocalDate checked = _nn( LocalDate.parse( p.i0 ) );

                if( ChronoUnit.DAYS.between( checked, now ) < days ) {
                    addUrl( getOrThrowByName( p.i1 ), u );
                    Log.info( "url " + u + " was checked to be " + p.i1 );
                } else {
                    Log.warn( "url " + u + " was checked too long ago: days " + checked );
                }

            } catch( DateTimeParseException e ) {
                Log.warn( "not a date " + p.i0 );
            }
        } );
    }

    public int getSingleLicenseCount() {
        return singles.size();
    }

}
