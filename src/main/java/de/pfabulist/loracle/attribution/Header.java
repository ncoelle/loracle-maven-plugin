package de.pfabulist.loracle.attribution;

import de.pfabulist.frex.Frex;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static de.pfabulist.nonnullbydefault.NonnullCheck._nn;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

public class Header {

    private static Pattern start =
            Frex.txt( "//" ).zeroOrOnce().
                    then( Frex.whitespace().zeroOrMore() ).
                    then( Frex.or( Frex.txt( "public" ), Frex.txt( "private" ), Frex.txt( "protected" ) ).zeroOrOnce() ).
                    then( Frex.whitespace().oneOrMore() ).
                    then( Frex.txt( "final " ).zeroOrOnce() ).
                    then( Frex.whitespace().zeroOrMore() ).
                    then( Frex.or( Frex.txt( "class" ), Frex.txt( "enum" ), Frex.txt( "interface" ), Frex.txt( "abstract class" ) ) ).
                    then( Frex.any().zeroOrMore() ).buildCaseInsensitivePattern();

//    private static Pattern pomproject =
//            Frex.txt( "<project" ).buildCaseInsensitivePattern();

    public static String getHeader( String in ) {
        String[] lines = in.split( "\n" );

        AtomicReference<Boolean> afterClass = new AtomicReference<>( false );
        AtomicReference<Integer> count = new AtomicReference<>( 0 );

        return Arrays.stream( lines ).
                filter( l -> !l.startsWith( "import" ) ).
                peek( l -> count.set( _nn(count.get()) + l.length()) ).
                map( l -> {
                    if( _nn( afterClass.get() ) ) {
                        return "";
                    }

                    if( start.matcher( l ).matches() || _nn(count.get()) > 3000 ) {
                        afterClass.set( true );
                        return "";
                    }

                    return l;
                } ).
                filter( l -> !l.trim().isEmpty() ).
                collect( Collectors.joining( "\n" ) );

    }

    public static String getPomHeader( String in ) {
        int start = in.indexOf( "<project" );
        if ( start <= 0 ) {
            return "";
        }

        String ret = in.substring( 0, start );

        if ( ret.startsWith( "<?xml version=\"1.0\" encoding=\"UTF-8\"?>")) {
            ret = ret.substring( "<?xml version=\"1.0\" encoding=\"UTF-8\"?>".length() ).trim();
        }

        return ret;
    }

}
