package de.pfabulist.loracle.buildup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.pfabulist.kleinod.nio.Filess;
import de.pfabulist.loracle.license.Coordinates2License;
import de.pfabulist.loracle.license.LOracle;
import de.pfabulist.unchecked.Unchecked;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static de.pfabulist.kleinod.text.Strings.getBytes;
import static de.pfabulist.nonnullbydefault.NonnullCheck._nn;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

public class JSONStartup {

    public static LOracle start() {
        byte[] buf = new byte[ 3000000 ];

        int got = 0;
        try( InputStream in = _nn( JSONStartup.class.getResourceAsStream( "/de/pfabulist/loracle/loracle.json" ) ) ) {
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

        String jsonstr = new String( buf, 0, got, StandardCharsets.UTF_8 );

        return new Gson().fromJson( jsonstr, LOracle.class );
    }

    public static Coordinates2License previous() {

        Path previuos = _nn( Paths.get( "loracle.json").toAbsolutePath() );

        if ( !Files.exists( previuos )) {
            return new Coordinates2License();
        }

        byte[] buf = new byte[ 3000000 ];

        int got = 0;
        try( InputStream in = _nn( Files.newInputStream( previuos ))) {
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

        String jsonstr = new String( buf, 0, got, StandardCharsets.UTF_8 );

        return new Gson().fromJson( jsonstr, Coordinates2License.class );

    }

    public static void previousOut( Coordinates2License c2l ) {
        Path previuos = _nn( Paths.get( "loracle.json").toAbsolutePath() );
        Gson gson = new GsonBuilder().setPrettyPrinting().enableComplexMapKeySerialization().create();
        Filess.write( previuos, getBytes( gson.toJson( c2l ) ));
    }

}
