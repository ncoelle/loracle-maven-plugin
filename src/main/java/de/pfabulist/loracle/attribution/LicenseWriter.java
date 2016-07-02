package de.pfabulist.loracle.attribution;

import de.pfabulist.kleinod.nio.Filess;
import de.pfabulist.loracle.license.Coordinates;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static de.pfabulist.kleinod.text.Strings.getBytes;
import static de.pfabulist.nonnullbydefault.NonnullCheck._nn;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

public class LicenseWriter {

    public void write( Coordinates coo, String name, String txt ) {
        Filess.write( getPath( coo, name ), getBytes( txt ) );
    }

    private Path getPath( Coordinates coo, String name ) {
        Path dir = _nn( Paths.get( "target/loracle/" + coo.toString().replace( ":", "_") ).toAbsolutePath() );
        Filess.createDirectories( dir );
        int idx = 0;
        while ( true ) {
            Path ret = _nn(dir.resolve( name + "-"+ idx + ".txt" ));
            if( !Files.exists( ret )) {
                return ret;
            }

            idx++;
        }
    }


}
