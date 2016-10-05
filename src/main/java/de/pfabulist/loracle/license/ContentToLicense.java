package de.pfabulist.loracle.license;

import de.pfabulist.frex.Frex;
import de.pfabulist.loracle.attribution.CopyrightHolder;
import de.pfabulist.loracle.mojo.Findings;
import de.pfabulist.loracle.mojo.UrlToLicense;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static de.pfabulist.roast.NonnullCheck._nn;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

@SuppressWarnings( "PMD.UnusedPrivateField" )
public class ContentToLicense {

    static public final Pattern copyRightPattern =
            Frex.or( Frex.txt( "Copyright " ) ).
                    then( Frex.txt( "(C) " ).zeroOrOnce() ).
                    then( Frex.or( Frex.number(), Frex.txt( '-' ), Frex.txt( ',' ), Frex.whitespace() ).oneOrMore().var( "year" ) ).
                    then( Frex.txt( ' ' ) ).
                    then( Frex.txt( "(C) " ).zeroOrOnce() ).
                    then( Frex.anyBut( Frex.txt( '\n' ) ).oneOrMore().var( "holder" ) ).
                    buildCaseInsensitivePattern();

    private final LOracle lOracle;
    private final And and;
    private final Findings log;
    private final UrlToLicense urlToLicense;

    public ContentToLicense( LOracle lOracle, Findings log ) {
        this.lOracle = lOracle;
        this.and = new And( lOracle, log, true );
        this.log = log;
        this.urlToLicense = new UrlToLicense( lOracle, log );
    }

    public final static Pattern page =
            Frex.or( Frex.txt( "http://" ), Frex.txt( "https://" ) ).
                    then( Frex.any().oneOrMore().lazy() ).group( "addr" ).
                    then( Frex.txt( '.' ).zeroOrOnce()).
                    then( Frex.or( Frex.txt( ' ' ), Frex.txt( '\n' ), Frex.txt( '\r' ) ) ).
                    //But( Frex.txt( ' ' ) ).oneOrMore().group( "addr" ) ).
                            buildCaseInsensitivePattern();

    public MappedLicense byUrl( String str, String dscr ) {
        Matcher matcher = page.matcher( str );

        MappedLicense ret = MappedLicense.empty();

        while( matcher.find() ) {
            ret = and.and( ret, urlToLicense.getLicense( _nn( matcher.group( "addr" ) ) ).addReason( dscr ) );
        }

        return ret;
    }

    private final static Frex ws = Frex.or( Frex.whitespace(), Frex.txt( '\n' ), Frex.txt( '\r' ) ).oneOrMore();
    private final static Pattern apache2 =
            Frex.txt( "Apache" ).then( ws ).
                    then( Frex.txt( "License" ) ).then( ws ).
                    then( Frex.txt( "Version" ) ).then( ws ).
                    then( Frex.txt( "2.0" ) ).buildCaseInsensitivePattern();

//    public MappedLicense byNamePattern( String str ) {
//        if( apache2.matcher( str ).find() ) {
//            return MappedLicense.of( lOracle.getOrThrowByName( "apache-2" ), dscr );
//        }
//
//        if( str.contains( "COMMON DEVELOPMENT AND DISTRIBUTION LICENSE (CDDL) Version 1.0" ) ) {
//            return MappedLicense.of( lOracle.getOrThrowByName( "cddl-1.0" ), dscr );
//        }
//
//        if( str.contains( "The Apache Software License, Version 1.1" ) ) {
//            return MappedLicense.of( lOracle.getOrThrowByName( "apache-1.1" ), dscr );
//        }
//
//        return MappedLicense.empty();
//    }
//

    public MappedLicense findLicenses( String str, String dscr ) {

        MappedLicense name = lOracle.findLongNames( and, str );
        MappedLicense url = byUrl( str, dscr );

        // todo merge

        return and.and( name, url );
    }

//
//        return
//                Arrays.stream( str.cutMiddle( "\n" ) ).
//                        map( this::byLongNameSearchInLine ).
//                        filter( MappedLicense::isPresent ).
//                        findFirst().
//                        orElse( MappedLicense.empty() );
//
//    }

//    private MappedLicense byLongNameSearchInLine( String s ) {
//        String line = new Normalizer().reduce( s );
//        while( true ) {
//            MappedLicense ret = lOracle.geByLongNameStart( line );
//
//            if( ret.isPresent() ) {
//                return ret;
//            }
//
//            int pos = line.indexOf( ' ' );
//            if( pos <= 0 ) {
//                return MappedLicense.empty();
//            }
//
//            line = line.substring( pos + 1 );
//
//        }
//    }

    public Optional<CopyrightHolder> getHolder( String str ) {
        Matcher matcher = copyRightPattern.matcher( str );

        if( matcher.find() ) {
            CopyrightHolder ch = new CopyrightHolder( _nn( matcher.group( "year" ) ), _nn( matcher.group( "holder" ) ) );
//            log.info( "" + coo + " -> " + ch );
            return Optional.of( ch );
        }

        return Optional.empty();

    }
}
