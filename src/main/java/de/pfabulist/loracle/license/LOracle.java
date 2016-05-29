package de.pfabulist.loracle.license;

import com.esotericsoftware.minlog.Log;
import de.pfabulist.frex.Frex;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

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

    private static Pattern urlPattern = Frex.or( Frex.txt( "http://" ), Frex.txt( "https://" ) ).zeroOrOnce().
            then( Frex.txt( "www." ).zeroOrOnce() ).
            then( Frex.any().oneOrMore().lazy().var( "relevant" ) ).
            then( Frex.txt( "." ).then( Frex.alpha().oneOrMore() ).zeroOrOnce() ).
            buildCaseInsensitivePattern();

    @SuppressFBWarnings( "URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD" )
    public static class More {
        public boolean spdx;
        public boolean osiApproved;
        public Optional<Boolean> fedoraApproved = Optional.empty();
        public Optional<Boolean> gpl2Compatible = Optional.empty();
        public Optional<Boolean> gpl3Compatible = Optional.empty();
        public List<String> urls = new ArrayList<>();
        public List<String> longNames = new ArrayList<>();
        public List<Coordinates> specific = new ArrayList<>();
        public Set<String> couldbeName = new HashSet<>();
        public Set<String> couldbeUrl = new HashSet<>();

        public More( boolean spdx ) {
            this.spdx = spdx;
        }
    }

    private Map<String, More> singles = new HashMap<>();
    private Map<String, Boolean> licenseExceptions = new HashMap<>();
    private Map<String, More> composites = new HashMap<>();

    private final transient Map<String, Set<LicenseID>> couldbeNames = new HashMap<>();
    private final transient Map<String, Set<LicenseID>> couldbeUrls = new HashMap<>();

    private final transient SPDXParser parser;
    private final transient AliasBuilder aliasBuilder = new AliasBuilder();
    private transient Map<Coordinates, LicenseID> coordinatesMap = new HashMap<>();
    private transient Map<String, LicenseID> longNameMapper = new HashMap<>();
    private transient Map<String, LicenseID> urls = new HashMap<>();

    public LOracle() {
        parser = new SPDXParser( this );
    }

    public LOracle spread() {
        if( !longNameMapper.isEmpty() ) {
            throw new IllegalStateException( "can only be called after json construction" );
        }

        singles.entrySet().stream().forEach(
                e -> {
                    String name = _nn( e.getKey() );
                    More more = _nn( e.getValue() );
                    LicenseID lid = new SingleLicense( name );
                    more.urls.stream().forEach( u -> urls.put( u, lid ) );
                    more.longNames.stream().forEach( l -> longNameMapper.putIfAbsent( l, lid ) );
                    more.specific.stream().forEach( coo -> coordinatesMap.putIfAbsent( coo, lid ) );
                    more.couldbeName.stream().forEach( n -> { couldbeNames.putIfAbsent( n, new HashSet<>() ); couldbeNames.get( n ).add( lid ); });
                    more.couldbeUrl.stream().forEach( n -> { couldbeUrls.putIfAbsent( n, new HashSet<>() ); couldbeUrls.get( n ).add( lid ); });
                }
        );

        composites.entrySet().stream().forEach(
                e -> {
                    String name = _nn( e.getKey() );
                    More more = _nn( e.getValue() );

                    LicenseID lid = getOrThrowByName( name );
                    more.urls.stream().forEach( u -> urls.put( u, lid ) );
                    more.longNames.stream().forEach( l -> longNameMapper.putIfAbsent( l, lid ) );
                    more.specific.stream().forEach( coo -> coordinatesMap.putIfAbsent( coo, lid ) );
                    more.couldbeName.stream().forEach( n -> { couldbeNames.putIfAbsent( n, new HashSet<>() ); couldbeNames.get( n ).add( lid ); });
                    more.couldbeUrl.stream().forEach( n -> { couldbeUrls.putIfAbsent( n, new HashSet<>() ); couldbeUrls.get( n ).add( lid ); });
                }
        );

        // todo couldbes

        return this;
    }

    public LicenseID getOrLater( SingleLicense license, boolean orLater, Optional<LicenseExclude> exception ) {
        if( !orLater && !exception.isPresent() ) {
            return license;
        }
        LicenseID ret = new ModifiedSingleLicense( license, orLater, exception );

        composites.putIfAbsent( ret.getId(), new More( false ) );

        return ret;
    }

    public LicenseExclude getExceptionOrThrow( String ex ) {
        String lower = ex.trim().toLowerCase( Locale.US );
        if( licenseExceptions.containsKey( lower ) ) {
            return new LicenseExclude( lower );
        }

        throw new IllegalArgumentException( "no such exception: " + ex );
    }

    //private static Pattern WS = Frex.txt( ',' ).or( Frex.txt( '-' )).buildPattern();

    public static String trim( String in ) {
        return _nn( in.toLowerCase( Locale.US ) ).replaceAll( ",", " " ).trim();
    }

    public SingleLicense newSingle( String name, boolean spdx ) {
        // todo test for extensions ( getAnd ... ?

        String lower = trim( name );

//        if( singles.containsKey( lower ) ) {
//            throw new IllegalArgumentException( "not a new single license: " + name );
//        }
//
        if( getByName( lower ).isPresent() ) {
            throw new IllegalArgumentException( "not a new single license: " + name );
        }

        Set<LicenseID> guesses = guessByName( lower );
        if( !guesses.isEmpty() ) {
            removeNameGuess( lower );
        }

        singles.put( lower, new More( spdx ) );

        SingleLicense ret = new SingleLicense( lower );

        String lng = Arrays.stream( lower.split( "[- ]" ) ).collect( Collectors.joining( " " ) );

        addLongName( ret, lng );

        return ret;
    }

    private void removeNameGuess( String name ) {
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

    private static Pattern withVersion = Frex.any().oneOrMore().lazy().var( "base" ).
            then( Frex.txt( ' ' ) ).then( Frex.or( Frex.number(), Frex.txt( '.' ) ).oneOrMore() ).buildCaseInsensitivePattern();

    public void addLongName( LicenseID license, String longName ) {
        String reduced = aliasBuilder.reduce( longName );
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

        return Optional.ofNullable( longNameMapper.get( aliasBuilder.reduce( name ) ) );
    }

    public Optional<LicenseID> getByName( String name ) {
        try {
            return Optional.of( parser.parse( name ) );
        } catch( Exception e ) {
            // not found
        }

        Optional<LicenseID> ret = Optional.ofNullable( longNameMapper.get( aliasBuilder.reduce( name ) ) );

        if( ret.isPresent() ) {
            return ret;
        }

        try {
            return Optional.of( new FuzzyParser( this ).parse( name ) );
        } catch( Exception e ) {
            return Optional.empty();
        }
    }

    public LicenseID getOrThrowByName( String name ) {
        return getByName( name ).orElseThrow( () -> new IllegalArgumentException( "no such license name: " + name ) );
    }

    public void addLicenseForArtifact( Coordinates coo, LicenseID licenseID ) {
        getMore( licenseID ).specific.add( coo );
        coordinatesMap.put( coo, licenseID );
    }

    public Optional<LicenseID> getByCoordinates( Coordinates coo ) {
        return Optional.ofNullable( coordinatesMap.get( coo ) );
    }

    public void addException( String name, boolean spdx ) {
        String lower = name.trim().toLowerCase( Locale.US );

        if( licenseExceptions.containsKey( lower ) ) {
            throw new IllegalArgumentException( "existing exception: " + lower );
        }

        licenseExceptions.put( lower, spdx );
    }

    public Optional<LicenseID> getByUrl( String url ) {
        Matcher matcher = urlPattern.matcher( url );
        if( !matcher.matches() ) {
            // log throw new IllegalArgumentException( "not a url: " + url );
            return Optional.empty();
        }

        String rel = _nn( matcher.group( "relevant" ) ).toLowerCase( Locale.US );

        return Optional.ofNullable( urls.get( rel ) );
    }

    public void setOsiApproval( LicenseID licenseID, boolean osiApproved ) {
        getMore( licenseID ).osiApproved = osiApproved;
    }

    public CompositeLicense getAnd( LicenseID left, LicenseID right ) {
        CompositeLicense ret = new CompositeLicense( false, left, right );
        composites.putIfAbsent( ret.getId(), new More( false ) ); // todo
        return ret;
    }

    public CompositeLicense getOr( LicenseID left, LicenseID right ) {
        CompositeLicense ret = new CompositeLicense( true, left, right );
        composites.putIfAbsent( ret.getId(), new More( false ) ); // todo
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
        return Optional.ofNullable( couldbeNames.get( aliasBuilder.reduce( name ) ) ).orElseGet( Collections::emptySet );
    }

    public void addUrl( LicenseID license, String url ) {
        Matcher matcher = urlPattern.matcher( url );
        if( !matcher.matches() ) {
            throw new IllegalArgumentException( "not a url: " + url );
        }

        String rel = _nn( matcher.group( "relevant" ) ).toLowerCase( Locale.US );

        if( urls.containsKey( rel ) ) {

            LicenseID old = _nn( urls.get( rel ) );

            if( old.equals( license ) ) {
                return;
            }

            Log.info( "known url: " + url + " as: " + old + "  moving it to couldbe, together with " + license );

            urls.remove( rel );

            getMore( license ).couldbeName.add( license.toString() );

            couldbeUrls.putIfAbsent( rel, new HashSet<>() );
            //noinspection ConstantConditions
            couldbeUrls.get( rel ).add( license );
            //noinspection ConstantConditions
            couldbeUrls.get( rel ).add( old );

            getMore( license ).urls.add( rel );

            return;
        }

        if( couldbeUrls.containsKey( rel ) ) {
            couldbeUrls.get( rel ).add( license );
            getMore( license ).urls.add( rel );
            Log.info( "known couldbe url: " + url + " for " + license );
            return;
        }

        getMore( license ).urls.add( rel );
        urls.put( rel, license );
    }

}
