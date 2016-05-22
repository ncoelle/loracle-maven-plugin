package de.pfabulist.loracle.buildup;

import de.pfabulist.frex.Frex;
import de.pfabulist.frex.Single;
import de.pfabulist.kleinod.nio.Filess;
import de.pfabulist.loracle.license.LOracle;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

public class ExtractSPDXExceptionsFromHTML {

    private final List<Pattern> names = new ArrayList<>();

    public ExtractSPDXExceptionsFromHTML( LOracle lOracle ) {
        getExceptionsFromSource().
//                peek( System.out::println ).
            forEach( l -> addSpdxException( lOracle, l ) );

        // todo
//        add( "CDDL + GPLv2 with classpath exception", "https://glassfish.dev.java.net/nonav/public/CDDL+GPL.html" );
//        add( "Classpath exception 2.0"  );
    }

    private void addSpdxException( LOracle lOracle, String name ) {
        lOracle.addException( name, true );
    }

//    private void add( String name ) {
//        names.add( name );
//    }

    public boolean isSpdxException( String name ) {
        return names.contains( Frex.txt( name ).buildCaseInsensitivePattern());
    }

    private Stream<String> getExceptionsFromSource() {

        Single doubleQuote = Frex.txt( '"' );
        Frex anyButDoubleQuote = Frex.anyBut( doubleQuote ).oneOrMore();
        Pattern idpat = anyButDoubleQuote.
                then( Frex.txt( "\"./" ) ).
                then( anyButDoubleQuote.var( "id" ) ).
                then( Frex.txt( ".html\"" ) ).
                then( Frex.any().zeroOrMore() ).buildPattern();

        //System.out.println(idpat.pattern());

        AtomicReference<Boolean> skip = new AtomicReference<>( true );

        return
                Filess.lines( getClass().getResourceAsStream( "/de/pfabulist/loracle/spdx-exceptions-html-fragment.txt" ), Charset.forName( "UTF-8" ) ).
                        filter( l -> isLineAfterTR( skip, l ) ).
                        map( l -> {
                            Matcher matcher = idpat.matcher( l );
                            if( !matcher.matches() ) {
                                throw new IllegalArgumentException( "not a spdx line" );
                            }

                            return matcher.group( "id" );
                        } );

    }

    private boolean isLineAfterTR( AtomicReference<Boolean> skip, String l ) {
        if( l.trim().equals( "<tr>" ) ) {
            skip.set( false );
            return false;
        } else if( skip.get() ) {
            return false;
        } else {
            skip.set( true );
            return true;
        }
    }

    public List<Pattern> getExceptions() {
        return names;
    }
}
