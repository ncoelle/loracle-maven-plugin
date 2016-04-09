package de.pfabulist.ianalb.model;

import de.pfabulist.frex.Frex;
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static de.pfabulist.frex.Frex.txt;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

public class Expressions {

    @Test
    public void testPlusWith() {
        Frex word = Frex.or( Frex.alphaNum(), txt( '-' ).or( txt( '.' ) ) ).oneOrMore();

        System.out.println(word.buildPatternString());

        assertThat( word.buildPattern().matcher( "GPL2.0" ).matches()).isTrue();

        Pattern pat =
                word.var( "name" ).
                        then( txt( '+' ).var( "plus" ).zeroOrOnce()).
                        then( Frex.whitespace().
                                then( txt( "WITH" ) ).
                                then( Frex.whitespace() ).
                                then( word.var( "exception" )).zeroOrOnce() ).
                        buildPattern();

        System.out.println( pat );

        Matcher matcher = pat.matcher( "GPL2.0" );
        matcher.matches();

        assertThat( matcher.group( "name" )).isEqualTo( "GPL2.0" );
        assertThat( matcher.group( "plus" ) ).isNull();

        matcher = pat.matcher( "GPL2.0+" );
        matcher.matches();

        assertThat( matcher.group( "name" )).isEqualTo( "GPL2.0" );
        assertThat( matcher.group( "plus" )).isEqualTo( "+" );

        matcher = pat.matcher( "GPL2.0 WITH foo" );
        matcher.matches();

        assertThat( matcher.group( "name" )).isEqualTo( "GPL2.0" );
        assertThat( matcher.group( "exception" )).isEqualTo( "foo" );

        matcher = pat.matcher( "GPL2.0+ WITH foo" );
        matcher.matches();

        assertThat( matcher.group( "name" )).isEqualTo( "GPL2.0" );
        assertThat( matcher.group( "plus" )).isEqualTo( "+" );
        assertThat( matcher.group( "exception" )).isEqualTo( "foo" );

    }
}
