package de.pfabulist.loracle.license;

import com.esotericsoftware.minlog.Log;
import de.pfabulist.frex.Frex;

import java.util.Locale;
import java.util.regex.Pattern;

import static de.pfabulist.frex.Frex.fullWord;
import static de.pfabulist.frex.Frex.or;
import static de.pfabulist.frex.Frex.txt;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

public class AliasBuilder {

    static private final String WHITESPACE = Frex.or( Frex.whitespace(), Frex.txt( ',' ), Frex.txt( '-' ) ).buildPattern().toString();
    static private final Pattern maybe = or( fullWord( "License" ),
                                       fullWord( "The" ),
                                       fullWord( "Version" ),
                                       fullWord( "Software" ),
                                       fullWord( "Agreement" ),
                                       fullWord( "Free" ),
                                       fullWord( "Open" ),
                                       fullWord( "Public" ),
                                       fullWord( "v" ) ).buildCaseInsensitivePattern();
    static private final Pattern spaces = Frex.whitespace().atLeast( 2 ).buildPattern();
    static private final Pattern vVersion = txt( "v" ).then( or( Frex.number(), txt( '.' ), txt( ',' ) ).oneOrMore() ).buildCaseInsensitivePattern();
    static private final Pattern version = or( Frex.number(), txt( '.' ) ).oneOrMore().buildCaseInsensitivePattern();


    public String reduce( String in ) {
        // todo handle , getAnd extra -
        StringBuilder sb = new StringBuilder();

        for( String word : in.toLowerCase( Locale.US ).split( WHITESPACE ) ) {
            if( word.isEmpty() ) {
                // ignore
            } else if( maybe.matcher( word ).matches() ) {
                // ignore ret = ret.then( txt( word).zeroOrOnce() );
            } else if( vVersion.matcher( word ).matches() ) {
                sb.append( word.substring( 1 ) );
            } else {
                sb.append( word );
            }

            sb.append( " " );
        }

        String ret = sb.toString().trim();

        if( !ret.isEmpty() && !version.matcher( ret ).matches() ) {
            return spaces.matcher( ret ).replaceAll( " " );
        }

        Log.warn( "license name composed of fill words only: " + in );

        return in;
    }
}
