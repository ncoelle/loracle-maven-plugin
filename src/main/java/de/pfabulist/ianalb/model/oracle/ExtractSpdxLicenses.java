//package de.pfabulist.ianalb.model.oracle;
//
//import de.pfabulist.frex.Frex;
//import de.pfabulist.frex.Single;
//import de.pfabulist.ianalb.model.buildup.Oracle;
//import de.pfabulist.ianalb.model.license.SingleSPDXLicense;
//import de.pfabulist.kleinod.nio.Filess;
//import de.pfabulist.loracle.license.AliasBuilder;
//
//import java.nio.charset.Charset;
//import java.util.concurrent.atomic.AtomicReference;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//import java.util.stream.Stream;
//
///**
// * Copyright (c) 2006 - 2016, Stephan Pfab
// * SPDX-License-Identifier: BSD-2-Clause
// */
//
//public class ExtractSpdxLicenses {
//
//    public Stream<String> getSPDXIds() {
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
//
//    public void addSPDX( Oracle oracle ) {
//
//        AliasBuilder aliasBuilder = new AliasBuilder();
//
//        Single doubleQuote = Frex.txt( '"' );
//        Frex anyButDoubleQuote = Frex.anyBut( doubleQuote ).oneOrMore();
//        Pattern idpat = anyButDoubleQuote.
//                then( Frex.txt( "\"./" ) ).
//                then( anyButDoubleQuote.var( "id" ) ).
//                then( Frex.txt( ".html\"" ) ).
//                then( Frex.anyBut( Frex.txt( '>')).zeroOrMore() ).
//                then( Frex.txt( '>' ) ).
//                then( Frex.anyBut( Frex.txt( '<')).oneOrMore().var( "text" ) ).
//                then( Frex.any().zeroOrMore()).
//                buildPattern();
//
//
//        AtomicReference<Boolean> skip = new AtomicReference<>( true );
//
//        Filess.lines( getClass().getResourceAsStream( "/de/pfabulist/ianalb/spdx-licenses-html-fragment.txt" ), Charset.forName( "UTF-8" ) ).
//                filter( l -> isLineAfterTR( skip, l ) ).
//                forEach( l -> {
//                    Matcher matcher = idpat.matcher( l );
//                    if ( !matcher.matches()) {
//                        throw new IllegalArgumentException( "huh" );
//                    }
//
//                    String name = matcher.group( "id" );
//
//                    //System.out.println(name);
//
//                    oracle.addLicense( new SingleSPDXLicense( name ));
//
//                    oracle.addAlias( name, aliasBuilder.buildAlias( matcher.group( "text" ) ).buildCaseInsensitivePattern());
//
////                    System.out.println( matcher.group( "id" ) + " ---- " +
////                                                matcher.group( "text" ) + " ---- " +
////                                                aliasBuilder.buildAlias( matcher.group( "text" ) ).buildCaseInsensitivePattern());
//                } );
//    }
//
//    private boolean isLineAfterTR( AtomicReference<Boolean> skip, String l ) {
//        if( l.trim().equals( "<tr>" ) ) {
//            skip.set( false );
//            return false;
//        } else if( skip.get() ) {
//            return false;
//        } else {
//            skip.set( true );
//            return true;
//        }
//    }
//
//
//
//
//}
