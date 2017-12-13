package de.pfabulist.loracle.attribution;

import de.pfabulist.loracle.license.known.LOracleKnown;
import de.pfabulist.loracle.maven.Coordinates;
import de.pfabulist.loracle.license.Coordinates2License;
import de.pfabulist.roast.nio.Files_;
import de.pfabulist.roast.nio.Paths_;

import java.nio.file.Files;
import java.nio.file.Path;

import static de.pfabulist.kleinod.text.Strings.getBytes;
import static de.pfabulist.roast.NonnullCheck._nn;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

public class LicenseWriter {

    public void write( Coordinates coo, String name, String txt ) {
        if ( !txt.isEmpty()) {
            Files_.write( getPath( coo, name ), getBytes( txt ) );
        }
    }

    public static Path getPath( Coordinates coo, String name ) {
        Path dir = Paths_.get__( "target/generated-sources/loracle/coordinates/" + coo.toFilename() ).toAbsolutePath_();
        Files_.createDirectories( dir );
        int idx = 0;
        while ( true ) {
            Path ret = _nn(dir.resolve( name + "-"+ idx + ".txt" ));
            if( !Files.exists( ret )) {
                return ret;
            }

            idx++;
        }
    }

//    private Path getGeneratedPath( Coordinates2License.LiCo lico ) {
//        Path dir = _nn( Paths.get( "target/generated-sources/loracle/coordinates/" + coo.toFilename() ).toAbsolutePath() );
//        Filess.createDirectories( dir );
//        int idx = 0;
//        while ( true ) {
//            Path ret = _nn(dir.resolve( name + "-"+ idx + ".txt" ));
//            if( !Files.exists( ret )) {
//                return ret;
//            }
//
//            idx++;
//        }
//    }



    public static String writeLicense( LOracleKnown lOracle, Coordinates coo, Coordinates2License.LiCo lico ) {

        if ( !lico.getLicenseTxt().isEmpty()) {
            String licenseName = coo.toFilename() + "-license.txt";
            Path path = Paths_.get__( "target/generated-sources/loracle/licenses/" + licenseName ).toAbsolutePath_();
            Files_.createDirectories( _nn(path.getParent() ));

            Files_.write( path, getBytes( lico.getLicenseTxt() ) );
            return licenseName;
        }

        return "";
//        if ( !lico.getLicense().isPresent()) {
//            return "";
//        }

//        String licenseName = _nn( lico.getLicense().get());
//
//        lico.getLicense().ifPresent( lid -> {
//            lOracle.getMore( lOracle.getOrThrowByName( lid ) ).urls
//        } );
//
//        return licenseName;

//        if ( !txt.isEmpty()) {
//            Filess.write( getPath( coo, name ), getBytes( txt ) );
//        }
    }


}
