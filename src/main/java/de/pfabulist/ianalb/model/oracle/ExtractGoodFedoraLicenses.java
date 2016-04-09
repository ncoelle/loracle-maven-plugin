package de.pfabulist.ianalb.model.oracle;

import de.pfabulist.frex.Frex;
import de.pfabulist.frex.Single;
import de.pfabulist.kleinod.nio.Filess;

import java.nio.charset.Charset;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static de.pfabulist.frex.Frex.txt;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

public class ExtractGoodFedoraLicenses {

    public Stream<String> getFedorIds() {

//        Single doubleQuote = Frex.txt( '"' );
//        Frex anyButDoubleQuote = Frex.anyBut( doubleQuote ).oneOrMore();
//        Pattern idpat = anyButDoubleQuote.
//                then( Frex.txt( "\"./" ) ).
//                then( anyButDoubleQuote.var( "id" ) ).
//                then( Frex.txt( ".html\"" ) ).
//                then( Frex.any().zeroOrMore() ).buildPattern();


        Pattern idpat = txt("<td>").
                            then( Frex.anyBut( txt('<') ).oneOrMore().var( "id" ) ).
                            then( Frex.whitespace().zeroOrMore() ).
                            then( txt("</td>" )).buildPattern();

        //System.out.println(idpat.pattern());

        AtomicReference<Boolean> skip = new AtomicReference<>( true );

        return
                Filess.lines( getClass().getResourceAsStream( "/de/pfabulist/ianalb/fedora-goodlicenses-html-fragment.txt" ), Charset.forName( "UTF-8" ) ).
//                peek( l -> System.out.println( l + l.trim().equals( "<tr>" )) ).
                    filter( l -> {
                        if( l.trim().equals( "<tr>" ) ) {
                            skip.set( false );
                            return false;
                        } else if( skip.get() ) {
                            return false;
                        } else {
                            skip.set( true );
                            return true;
                        }
                    } ).
//                peek( System.out::println ).
                    map( l -> {
                            Matcher matcher = idpat.matcher( l );
                            if( !matcher.matches() ) {
                                throw new IllegalArgumentException( "not an id" );
                            }

                            return matcher.group( "id" );
                    } );

    }

}
