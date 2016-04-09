package de.pfabulist.ianalb.model;

import de.pfabulist.frex.Frex;

import static de.pfabulist.frex.Frex.txt;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

public class SimplePat {

    Frex fill = Frex.or( txt( ',' ), txt( '-' ), Frex.whitespace() ).zeroOrMore();

    static class Possible {
        final String str;
        final boolean must;

        Possible( String str, boolean must ) {
            this.str = str;
            this.must = must;
        }

        public static Possible maybe( String str ) {
            return new Possible( str, false );
        }

        public static Possible s( String str ) {
            return new Possible( str, true );
        }


    }


    public Frex simple( Possible ... pos ) {
        Frex ret = fill;

        for ( Possible text : pos ) {
            if ( text.must ) {
                ret = ret.then( Frex.txt( text.str ) );
            } else {
                ret = ret.then( Frex.txt( text.str ).zeroOrOnce() );
            }

            ret = ret.then( fill );
        }

        return ret;
    }


}
