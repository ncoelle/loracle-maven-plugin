//package de.pfabulist.ianalb.model;
//
//import de.pfabulist.frex.Frex;
//import de.pfabulist.frex.Single;
//import de.pfabulist.loracle.license.AliasBuilder;
//import de.pfabulist.kleinod.nio.Filess;
//import org.junit.Test;
//
//import java.nio.charset.Charset;
//import java.util.concurrent.atomic.AtomicReference;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
///**
// * Copyright (c) 2006 - 2016, Stephan Pfab
// * SPDX-License-Identifier: BSD-2-Clause
// */
//
//public class SpA {
//
//    @Test
//    public void groupBy() {
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
//        AliasBuilder aliasBuilder = new AliasBuilder();
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
//                    System.out.println( matcher.group( "id" ) + " ---- " +
//                                                matcher.group( "text" ) + " ---- " +
//                                                aliasBuilder.buildAlias( matcher.group( "text" ) ).buildCaseInsensitivePattern());
//                } );
//
//        //forEach( System.out::println );
//
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
//}
