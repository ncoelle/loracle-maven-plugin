package de.pfabulist.loracle.urls;

import de.pfabulist.unchecked.Unchecked;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

public class Utils {

    private static byte[] buf = new byte[ 3000000 ];

    public static String toString( InputStream in ) {

        int got = 0;
        try {
            while( true ) {
                int once = in.read( buf, got, 3000000 - got );
                if( once < 0 ) {
                    break;
                }
                got += once;
            }
        } catch( IOException e ) {
            throw Unchecked.u( e );
        }

        return new String( buf, 0, got, StandardCharsets.UTF_8 );

    }
}
