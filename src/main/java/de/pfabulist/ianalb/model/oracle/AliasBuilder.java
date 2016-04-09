package de.pfabulist.ianalb.model.oracle;

import com.esotericsoftware.minlog.Log;
import de.pfabulist.frex.Frex;

import java.util.regex.Pattern;

import static de.pfabulist.frex.Frex.fullWord;
import static de.pfabulist.frex.Frex.or;
import static de.pfabulist.frex.Frex.txt;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

public class AliasBuilder {

    static private Frex fill = or( txt( ',' ), txt( '-' ), Frex.whitespace() ).zeroOrMore();
    static private Pattern maybe = or( fullWord( "License" ),
                                       fullWord( "The" ),
                                       fullWord( "Version" ),
                                       fullWord( "Software" ),
                                       fullWord( "Agreement" ),
                                       fullWord( "Free" ),
                                       fullWord( "Public" ),
                                       fullWord( "v" )).buildCaseInsensitivePattern();
    static private Pattern vVersion = txt( "v" ).then( or( Frex.number(), txt( '.' ), txt( ',' ) ).oneOrMore() ).buildCaseInsensitivePattern();

    public Frex buildAlias( String text ) {

        boolean foundOneNonVersion = false;
        Frex ret = fill;

        for( String word : text.split( " " ) ) {
            if( word.isEmpty() ) {
                // ignore
            } else if( maybe.matcher( word ).matches() ) {
                // ignore ret = ret.then( txt( word).zeroOrOnce() );
            } else if( vVersion.matcher( word ).matches() ) {
                ret = ret.then( txt( 'v' ).zeroOrOnce().then( txt( word.substring( 1 ) ) ) );
            } else {
                ret = ret.then( txt( word ) );
                foundOneNonVersion = true;
            }

            ret = ret.then( fill );
        }

        if( foundOneNonVersion ) {
            return ret;
        }

        Log.warn( "license composed of fill words only" );

        return txt( text );
    }

    public String reduce( String in ) {
        return maybe.matcher( in ).replaceAll( "" );
    }
}
