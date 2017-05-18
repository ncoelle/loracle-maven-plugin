package de.pfabulist.loracle.buildup;

import de.pfabulist.frex.Frex;
import de.pfabulist.frex.Single;
import de.pfabulist.loracle.license.LOracle;
import de.pfabulist.loracle.license.LicenseID;
import de.pfabulist.roast.nio.Files_;

import java.nio.charset.Charset;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static de.pfabulist.roast.NonnullCheck._nn;
import static de.pfabulist.roast.lang.Class_.getClass__;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

public class ExtractSpdxLicensesFromHTML {

//    private Stream<String> getSPDXIds() {
//
//        Single doubleQuote = Frex.txt( '"' );
//        Frex   anyButDoubleQuote = Frex.anyBut( doubleQuote ).oneOrMore();
//        Pattern idpat = anyButDoubleQuote.
//                        then( Frex.txt( "\"./") ).
//                        then( anyButDoubleQuote.var( "id" )).
//                        then( Frex.txt( ".html\"" ) ).
//                        then( Frex.any().zeroOrMore()).buildPattern();
//
//        //System.out.println(idpat.pattern());
//
//        AtomicReference<Boolean> skip = new AtomicReference<>( true );
//
//        return
//        Filess.lines( getClass().getResourceAsStream( "/de/pfabulist/ianalb/spdx-licenses-html-fragment.txt" ), Charset.forName( "UTF-8" )).
////                peek( l -> System.out.println( l + l.trim().equals( "<tr>" )) ).
//                filter( l -> { if ( l.trim().equals( "<tr>" )) {
//                                    skip.set( false );
//                                    return false;
//                                } else if ( skip.get() ) {
//                                    return false;
//                                } else {
//                                    skip.set( true );
//                                    return true;
//                                }}).
////                peek( System.out::println ).
//                map( l -> {
//                    Matcher matcher = idpat.matcher( l );
//                    if ( !matcher.matches() ) {
//                        throw new IllegalArgumentException( "not a spdx line" );
//                    }
//
//                    return matcher.group( "id" );});
//
//
//    }

    enum IdPatVars {
        id,
        text
    }


    public void getLicenses( LOracle lOracle ) {

        Single doubleQuote = Frex.txt( '"' );
        Frex anyButDoubleQuote = Frex.anyBut( doubleQuote ).oneOrMore();
        Pattern idpat = anyButDoubleQuote.
                then( Frex.txt( "\"./" ) ).
                then( anyButDoubleQuote.var( IdPatVars.id ) ).
                then( Frex.txt( ".html\"" ) ).
                then( Frex.anyBut( Frex.txt( '>' ) ).zeroOrMore() ).
                then( Frex.txt( '>' ) ).
                then( Frex.anyBut( Frex.txt( '<' ) ).oneOrMore().var( IdPatVars.text ) ).
                then( Frex.any().zeroOrMore() ).
                buildPattern();

        AtomicReference<Boolean> skip = new AtomicReference<>( true );

        Files_.lines( getClass__( this ).getResourceAsStream_ot( "/de/pfabulist/loracle/spdx-licenses-html-fragment.txt" ), Charset.forName( "UTF-8" ) ).
        filter( l -> isLineAfterTR( skip, l ) ).
                forEach( l -> {
                    Matcher matcher = idpat.matcher( l );
                    if( !matcher.matches() ) {
                        throw new IllegalArgumentException( "huh" );
                    }

                    String name = _nn( matcher.group( "id" ) );

//                    System.out.println(name);

                    LicenseID license = lOracle.newSingle( name, true );
                    lOracle.addLongName( license, _nn( matcher.group( "text" ) ) );
//                    return IANALicense.simple( Optional.of( name.toLowerCase( Locale.US ) ), Collections.singletonList( aliasBuilder.reduce( matcher.group( "text" ) ) ));

//                    oracle.addLicense( new SingleSPDXLicense( name ));
//
//                    oracle.addAlias( name, aliasBuilder.buildAlias( matcher.group( "text" ) ).buildCaseInsensitivePattern());

//                    System.out.println( matcher.group( "id" ) + " ---- " +
//                                                matcher.group( "text" ) + " ---- " +
//                                                aliasBuilder.buildAlias( matcher.group( "text" ) ).buildCaseInsensitivePattern());
                } );
    }

    private boolean isLineAfterTR( AtomicReference<Boolean> skip, String l ) {
        if( l.trim().equals( "<tr>" ) ) {
            skip.set( false );
            return false;
        } else if( _nn( skip.get() ) ) {
            return false;
        } else {
            skip.set( true );
            return true;
        }
    }

}
