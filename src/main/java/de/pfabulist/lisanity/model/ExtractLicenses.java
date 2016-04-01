package de.pfabulist.lisanity.model;

import de.pfabulist.frex.Frex;
import de.pfabulist.frex.Single;
import de.pfabulist.kleinod.nio.Filess;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

public class ExtractLicenses {

    public Stream<String> getSPDXIds() {

        Single doubleQuote = Frex.txt( '"' );
        Frex   anyButDoubleQuote = Frex.anyBut( doubleQuote ).oneOrMore();
        Pattern idpat = anyButDoubleQuote.
                        then( Frex.txt( "\"./") ).
                        then( anyButDoubleQuote.var( "id" )).
                        then( Frex.txt( ".html\"" ) ).
                        then( Frex.any().zeroOrMore()).buildPattern();

        //System.out.println(idpat.pattern());

        AtomicReference<Boolean> skip = new AtomicReference<>( true );

        return
        Filess.lines( getClass().getResourceAsStream( "/de/pfabulist/lisanity/spdx-licenses-html-fragment.txt" ), Charset.forName( "UTF-8" )).
//                peek( l -> System.out.println( l + l.trim().equals( "<tr>" )) ).
                filter( l -> { if ( l.trim().equals( "<tr>" )) {
                                    skip.set( false );
                                    return false;
                                } else if ( skip.get() ) {
                                    return false;
                                } else {
                                    skip.set( true );
                                    return true;
                                }}).
//                peek( System.out::println ).
                map( l -> {
                    Matcher matcher = idpat.matcher( l );
                    if ( !matcher.matches() ) {
                        throw new IllegalArgumentException( "not a spdx line" );
                    }

                    return matcher.group( "id" );});


    }


}
