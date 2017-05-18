package de.pfabulist.loracle.buildup;

import de.pfabulist.frex.Frex;
import de.pfabulist.frex.Single;
import de.pfabulist.loracle.license.LOracle;
import de.pfabulist.roast.nio.Files_;

import java.nio.charset.Charset;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static de.pfabulist.roast.NonnullCheck._nn;
import static de.pfabulist.roast.lang.Class_.getClass__;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

public class ExtractSPDXExceptionsFromHTML {

    public ExtractSPDXExceptionsFromHTML( LOracle lOracle ) {
        getExceptionsFromSource().
                forEach( l -> addSpdxException( lOracle, l ) );
    }

    private void addSpdxException( LOracle lOracle, String name ) {
        lOracle.addException( name, true );
    }

    private Stream<String> getExceptionsFromSource() {

        Single doubleQuote = Frex.txt( '"' );
        Frex anyButDoubleQuote = Frex.anyBut( doubleQuote ).oneOrMore();
        Pattern idpat = anyButDoubleQuote.
                then( Frex.txt( "\"./" ) ).
                then( anyButDoubleQuote.var( IdPatVars.id ) ).
                then( Frex.txt( ".html\"" ) ).
                then( Frex.any().zeroOrMore() ).buildPattern();

        //System.out.println(idpat.pattern());

        AtomicReference<Boolean> skip = new AtomicReference<>( true );

        return
                Files_.lines( getClass__( this ).getResourceAsStream_ot( "/de/pfabulist/loracle/spdx-exceptions-html-fragment.txt" ), Charset.forName( "UTF-8" ) ).
                        filter( l -> isLineAfterTR( skip, l ) ).
                        map( l -> {
                            Matcher matcher = idpat.matcher( l );
                            if( !matcher.matches() ) {
                                throw new IllegalArgumentException( "not a spdx line" );
                            }

                            return _nn(matcher.group( "id" ));
                        } );

    }
    private boolean isLineAfterTR( AtomicReference<Boolean> skip, String l ) {
        if( l.trim().equals( "<tr>" ) ) {
            skip.set( false );
            return false;
        } else if( _nn(skip.get()) ) {
            return false;
        } else {
            skip.set( true );
            return true;
        }
    }

    enum IdPatVars {
        id
    }

}
