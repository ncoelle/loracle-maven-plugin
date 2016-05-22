package de.pfabulist.loracle.license;

import de.pfabulist.frex.Frex;
import de.pfabulist.unchecked.functiontypes.SupplierE;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static de.pfabulist.nonnullbydefault.NonnullCheck._nn;
import static de.pfabulist.nonnullbydefault.NonnullCheck._orElseExpectedThrow;
import static de.pfabulist.nonnullbydefault.NonnullCheck._orElseGet;
import static de.pfabulist.unchecked.Unchecked.u;

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
    private static class More {
        public boolean spdx;
        public boolean osiApproved;
        public List<String> urls = new ArrayList<>();
        public List<String> longNames = new ArrayList<>();
        public List<Coordinates> specific = new ArrayList<>();

        public More( boolean spdx ) {
            this.spdx = spdx;
        }
    }

    private final transient SPDXParser parser;
    private final transient AliasBuilder aliasBuilder = new AliasBuilder();
    private Map<String, More> singles = new HashMap<>();
    private transient Map<Coordinates, LicenseID> coordinatesMap = new HashMap<>();
    private transient Map<String, LicenseID> longNameMapper = new HashMap<>();
    private Map<String, Boolean> licenseExceptions = new HashMap<>();
    private transient Map<String, LicenseID> urls = new HashMap<>();
    private Map<String, More> composites = new HashMap<>();

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
                }
        );

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

    public SingleLicense newSingle( String name, boolean spdx ) {
        // todo test for extensions ( getAnd ... ?

        String lower = name.trim().toLowerCase( Locale.US );

        if( singles.containsKey( lower ) ) {
            throw new IllegalArgumentException( "no a new single license: " + name );
        }

        singles.put( lower, new More( true ) );
        return new SingleLicense( lower );
    }

    public More getMore( LicenseID licenseID ) {

        @Nullable More ret = singles.get( licenseID.getId() );
        if( ret != null ) {
            return ret;
        }

        ret = composites.get( licenseID.getId() );
        if( ret != null ) {
            return ret;
        }

        throw new IllegalArgumentException( "no such license: " + licenseID );

//        try {
//            return _orElseGet( singles.get( licenseID.getId() ),
//                               SupplierE.u( () -> Optional.ofNullable( composites.get( licenseID.getId() )).orElseThrow(
//                                                           () -> new IllegalArgumentException( "no such license: " + licenseID ) ) ));
//        } catch( Exception ex ) {
//            throw u(ex);
//        }
    }

    public void addLongName( LicenseID license, String longName ) {
        String reduced = aliasBuilder.reduce( longName );
        if( longNameMapper.containsKey( reduced ) ) {
            throw new IllegalArgumentException( "mapped already " + longName + " (" + reduced + ") " + license + " " + longNameMapper.get( reduced ) );
        }

        longNameMapper.put( reduced, license );
        getMore( license ).longNames.add( reduced );
    }

    public Optional<SingleLicense> getSingle( String name ) {
        String lower = name.trim().toLowerCase( Locale.US );

        if( !singles.containsKey( lower ) ) {
            return Optional.empty();
        }

        return Optional.of( new SingleLicense( lower ) );

    }

    public Optional<LicenseID> getByName( String name ) {
        try {
            return Optional.of( parser.parse( name ) );
        } catch( Exception e ) {
            // not found
        }

        return Optional.ofNullable( longNameMapper.get( aliasBuilder.reduce( name ) ) );
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

    public void addUrl( LicenseID license, String url ) {
        Matcher matcher = urlPattern.matcher( url );
        if( !matcher.matches() ) {
            throw new IllegalArgumentException( "not a url: " + url );
        }

        String rel = _nn( matcher.group( "relevant" ) ).toLowerCase( Locale.US );

        if( urls.containsKey( rel ) ) {
            if( _nn( urls.get( rel ) ).equals( license ) ) {
                return;
            }
            throw new IllegalArgumentException( "known url: " + url );
        }

        getMore( license ).urls.add( rel );
        urls.put( rel, license );
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

}
