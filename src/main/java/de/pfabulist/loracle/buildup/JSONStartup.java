package de.pfabulist.loracle.buildup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.pfabulist.loracle.license.Coordinates2License;
import de.pfabulist.loracle.license.known.LOracleKnown;
import de.pfabulist.loracle.util.GsonFromResource;
import de.pfabulist.roast.nio.Files_;
import de.pfabulist.roast.nio.Path_;
import de.pfabulist.roast.nio.Paths_;
import de.pfabulist.roast.unchecked.Unchecked;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static de.pfabulist.kleinod.text.Strings.getBytes;
import static de.pfabulist.roast.NonnullCheck._nn;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

public class JSONStartup {


    public static LOracleKnown start() {
//        byte[] buf = new byte[ 3000000 ];
//
//        int got = 0;
//        try( InputStream in = _nn( JSONStartup.class.getResourceAsStream( "/de/pfabulist/loracle/loracle.json" ) ) ) {
//            while( true ) {
//                int once = in.read( buf, got, 3000000 - got );
//                if( once < 0 ) {
//                    break;
//                }
//                got += once;
//            }
//        } catch( IOException e ) {
//            throw Unchecked.u( e );
//        }

        String jsonstr = GsonFromResource.readResource( "/de/pfabulist/loracle/loracle-collected.json" );//new String( buf, 0, got, StandardCharsets.UTF_8 );

        return new Gson().fromJson( jsonstr, LOracleKnown.class );
    }

//    public static LOracle start() {
////        byte[] buf = new byte[ 3000000 ];
////
////        int got = 0;
////        try( InputStream in = _nn( JSONStartup.class.getResourceAsStream( "/de/pfabulist/loracle/loracle.json" ) ) ) {
////            while( true ) {
////                int once = in.read( buf, got, 3000000 - got );
////                if( once < 0 ) {
////                    break;
////                }
////                got += once;
////            }
////        } catch( IOException e ) {
////            throw Unchecked.u( e );
////        }
//
//        String jsonstr = GsonFromResource.readResource( "/de/pfabulist/loracle/loracle-collected.json" );//new String( buf, 0, got, StandardCharsets.UTF_8 );
//
//        return new Gson().fromJson( jsonstr, LOracle.class );
//    }

//    public static LOracle startSpdx() {
////        byte[] buf = new byte[ 3000000 ];
////
////        int got = 0;
////        try( InputStream in = _nn( JSONStartup.class.getResourceAsStream( "/de/pfabulist/loracle/loracle.json" ) ) ) {
////            while( true ) {
////                int once = in.read( buf, got, 3000000 - got );
////                if( once < 0 ) {
////                    break;
////                }
////                got += once;
////            }
////        } catch( IOException e ) {
////            throw Unchecked.u( e );
////        }
//
//        String jsonstr = GsonFromResource.readResource( "/de/pfabulist/loracle/loracle-collected.json" );//new String( buf, 0, got, StandardCharsets.UTF_8 );
//
//        return new Gson().fromJson( jsonstr, LOracle.class );
//    }

    public static Coordinates2License previous( ) {

        Path previuos = getLoracleJson();

        if ( !Files.exists( previuos )) {
            Coordinates2License ret = new Coordinates2License();
            ret.setAndIsOr( true );
            return ret;
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

        Coordinates2License ret = new Gson().fromJson( jsonstr, Coordinates2License.class );
        ret.setAndIsOr( true );
        return ret;

    }

    public static void previousOut( Coordinates2License c2l ) {
        Path previous = getLoracleJson();
        Gson gson = new GsonBuilder().setPrettyPrinting().enableComplexMapKeySerialization().create();
        Files_.write( previous, getBytes( gson.toJson( c2l ) ));
    }

    private static Path getLoracleJson() {
        Path_ previous = Paths_.get__( "target/generated-sources/loracle/loracle.json").toAbsolutePath__();
        Files_.createDirectories_( previous.getParent_ot() );
        return previous._r();
    }

}
