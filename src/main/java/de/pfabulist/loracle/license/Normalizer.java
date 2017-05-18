package de.pfabulist.loracle.license;

import com.esotericsoftware.minlog.Log;
import de.pfabulist.frex.Frex;

import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static de.pfabulist.frex.Frex.fullWord;
import static de.pfabulist.frex.Frex.or;
import static de.pfabulist.frex.Frex.txt;
import static de.pfabulist.loracle.license.Normalizer.UrlVeriables.relevant;
import static de.pfabulist.loracle.license.Normalizer.WordVersionVariables.word;
import static de.pfabulist.roast.NonnullCheck._nn;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

public class Normalizer {

    static private final String WHITESPACE = Frex.or( Frex.whitespace(),
                                                      Frex.txt( ',' ),
                                                      Frex.txt( '-' ),
                                                      Frex.txt( '_' ),
                                                      Frex.txt( '!' ),
                                                      Frex.txt( '"' ),
                                                      Frex.txt( '\'' ),
                                                      Frex.txt( '/' ),
                                                      Frex.txt( '(' ),
                                                      Frex.txt( '*' ),
                                                      Frex.txt( ')') ).
            buildPattern().toString();
    static private final Pattern maybe = or( fullWord( "License" ),
                                             fullWord( "Source" ),
                                             fullWord( "Code" ),
                                             fullWord( "The" ),
                                             fullWord( "Version" ),
                                             fullWord( "Vesion" ),
                                             fullWord( "Software" ),
                                             fullWord( "General" ),
                                             fullWord( "Agreement" ),
                                             fullWord( "Free" ),
                                             fullWord( "Open" ),
                                             fullWord( "Public" ),
                                             fullWord( "General" ),
                                             fullWord( "Copyright" ),
                                             fullWord( "Like" ), // todo hmm
                                             fullWord( "v" ) ).buildCaseInsensitivePattern();
    static public final Pattern spaces = Frex.whitespace().atLeast( 2 ).buildPattern();
    static private final Pattern vVersion = txt( "v" ).then( or( Frex.number(), txt( '.' ), txt( ',' ) ).oneOrMore() ).buildCaseInsensitivePattern();
    static private final Pattern version = or( Frex.number(), txt( '.' ) ).oneOrMore().buildCaseInsensitivePattern();

    enum WordVersionVariables {
        word,
        version
    }

    static private final Pattern wordVversion = Frex.alpha().oneOrMore().var( word ).
            then( Frex.txt( "v" ) ).
            then( or( Frex.number(), txt( '.' ) ).oneOrMore().var( WordVersionVariables.version ) ).
            buildCaseInsensitivePattern();

    static private final Pattern wordVersion = Frex.alpha().oneOrMore().var( word ).
            then( or( Frex.number(), txt( '.' ) ).oneOrMore().var( WordVersionVariables.version ) ).
            buildCaseInsensitivePattern();

    enum UrlVeriables {
        relevant
    }

    private static Pattern urlPattern = Frex.or( Frex.txt( "http://" ), Frex.txt( "https://" ) ).zeroOrOnce().
            then( Frex.txt( "www." ).zeroOrOnce() ).
            then( Frex.any().oneOrMore().lazy().var( relevant ) ).
            then( Frex.txt( "." ).then( Frex.alpha().oneOrMore() ).zeroOrOnce() ).
            //then( Frex.txt( '/' )).zeroOrOnce().
                    buildCaseInsensitivePattern();


    private void addNumber( StringBuilder sb, String num ) {
        if ( num.endsWith( ".0" )) {
            sb.append( num.substring( 0, num.length() - 2 ) );
        } else {
            sb.append( num );
        }
    }

    public String reduce( String in ) {
        StringBuilder sb = new StringBuilder();

        for( String word : in.toLowerCase( Locale.US ).split( WHITESPACE ) ) {
            if( word.isEmpty() ) {
                // ignore
            } else if( maybe.matcher( word ).matches() ) {
                // ignore ret = ret.then( txt( word).zeroOrOnce() );
            } else if( vVersion.matcher( word ).matches() ) {
                if ( !word.equals( "v." )) {
                    addNumber( sb, word.substring( 1 ) );
                }
                //else { skip }

            } else {
                Matcher matcher = wordVversion.matcher( word );
                if( matcher.matches() ) {
                    sb.append( matcher.group( "word" ) );
                    sb.append( " " );
                    addNumber( sb, _nn( matcher.group( "version" )));
                } else {

                    Matcher wv = wordVersion.matcher( word );
                    if ( wv.matches() ) {
                        sb.append( wv.group( "word" ) );
                        sb.append( " " );
                        addNumber( sb, _nn( wv.group( "version" )));
                    } else {
                        addNumber( sb, word );
                    }
                }
            }

            sb.append( " " );
        }

        String ret = sb.toString().trim();

        if( !ret.isEmpty() && !version.matcher( ret ).matches() ) {
            return spaces.matcher( ret ).replaceAll( " " );
        }

        Log.debug( "license name composed of fill words only: " + in );
        return spaces.matcher( in.toLowerCase( Locale.US )).replaceAll( " " );
    }

    public Optional<String> normalizeUrl( String url ) {
        Matcher matcher = urlPattern.matcher( url );
        if( !matcher.matches() ) {
            return Optional.empty();
        }

        return Optional.of( _nn( matcher.group( "relevant" ) ).toLowerCase( Locale.US ));

    }

    private final static Pattern htmlws = Frex.or( Frex.whitespace(), Frex.txt( '\r' ), Frex.txt( '\n' ) ).oneOrMore().buildPattern();

    public String norm( String txt ) {
        return Arrays.stream( txt.split( "\n" ) ).
                map( l -> {
                    l = l.trim();
                    if( l.startsWith( "*" ) ) {
                        return l.substring( 1 ).trim();
                    } else if( l.startsWith( "//" ) ) {
                        return l.substring( 2 ).trim();
                    } else if( l.startsWith( "!" ) ) {
                        return l.substring( 1 ).trim();
                    } else {
                        return l;
                    }
                } ).
                collect( Collectors.joining( " ") ).
                replaceAll( htmlws.toString(), " " );
    }

    private static final String urlspecial = txt( ':' ).or( txt( '/' )).or( txt( '*' )).or( txt( '"')).or( txt( '<')).or( txt('>' )).or( txt( '?')).or( txt( '\\') ).or( txt(' ') ).buildPattern().toString();

    public static String toFilename( String str ) {
        return str.replaceAll( urlspecial, "_" );
    }

}
