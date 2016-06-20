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
            Frex.whitespace().zeroOrMore().
                    then( Frex.or( Frex.txt( "public" ), Frex.txt( "private" ), Frex.txt( "protected" ) ).zeroOrOnce() ).
                    then( Frex.whitespace().oneOrMore() ).
                    then( Frex.txt( "final " ).zeroOrMore() ).
                    then( Frex.whitespace().zeroOrMore() ).
                    then( Frex.or( Frex.txt( "class" ), Frex.txt( "enum" ), Frex.txt( "interfact" ), Frex.txt( "abstract class" ) ) ).
                    then( Frex.any().zeroOrMore() ).buildCaseInsensitivePattern();

    public static String getHeader( String in ) {
        String[] lines = in.split( "\n" );

        AtomicReference<Boolean> afterClass = new AtomicReference<>( false );

        return Arrays.stream( lines ).
                filter( l -> !l.startsWith( "import" ) ).
                map( l -> {
                    if( _nn( afterClass.get() ) ) {
                        return "";
                    }

                    if( start.matcher( l ).matches() ) {
                        afterClass.set( true );
                        return "";
                    }

                    return l;
                } ).
                filter( l -> !l.trim().isEmpty() ).
                collect( Collectors.joining( "\n" ) );

    }

}
