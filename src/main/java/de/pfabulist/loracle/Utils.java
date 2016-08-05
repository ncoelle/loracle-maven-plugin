package de.pfabulist.loracle;

import de.pfabulist.kleinod.nio.IO;
import de.pfabulist.loracle.buildup.JSONStartup;
import de.pfabulist.unchecked.Unchecked;

import javax.annotation.Nullable;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static de.pfabulist.kleinod.text.Strings.newString;
import static de.pfabulist.unchecked.Unchecked.u;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

public class Utils {

    public static String getResourceAsString( String res ) {
        byte[] buf = new byte[ 3000000 ];

        int got = 0;
        try( @Nullable InputStream in = JSONStartup.class.getResourceAsStream( res) )  {
            if ( in == null ) {
                return "";
            }
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

    public static String unzipToString( InputStream is, Pattern pat ) {
        try( ZipInputStream zin = new ZipInputStream( is ) ) {

            @Nullable ZipEntry ze;
            while( ( ze = zin.getNextEntry() ) != null ) {

                if( pat.matcher( ze.getName() ).matches() ) {

                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    IO.copy( zin, out );

                    return newString( out.toByteArray() );
                }
                zin.closeEntry();
            }
        } catch( IOException e ) {
            throw u( e );
        }

        return "";

    }
}
