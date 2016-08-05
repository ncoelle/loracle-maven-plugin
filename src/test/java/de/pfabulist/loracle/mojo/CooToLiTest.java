package de.pfabulist.loracle.mojo;

import com.google.gson.Gson;
import de.pfabulist.loracle.attribution.SrcTest;
import de.pfabulist.loracle.buildup.JSONStartup;
import de.pfabulist.loracle.license.Coordinates;
import de.pfabulist.loracle.license.Coordinates2License;
import de.pfabulist.loracle.license.LOracle;
import de.pfabulist.unchecked.Unchecked;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static de.pfabulist.nonnullbydefault.NonnullCheck._nn;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

public class CooToLiTest {

    private final LOracle lOracle = JSONStartup.start().spread();
    private final LicenseIntelligence lIntelligence = new LicenseIntelligence( lOracle, new Findings( SrcTest.dummy ));
    private final Downloader downloader = new Downloader( new Findings( SrcTest.dummy ), lOracle );

    @Test
    public void sunTools() {
        Coordinates coo = Coordinates.valueOf( "com.sun:tools:1.6" );
        Coordinates2License.LiCo lico = getLico( coo );
        assertThat( lico.getLicense().isPresent()).isFalse();

        lIntelligence.compute( coo, lico );

        assertThat( lico.getLicense() ).isPresent();
    }

    @Test
    public void scribeJava() {
        Coordinates coo = Coordinates.valueOf( "com.github.scribejava:scribejava-core:2.7.0" );
        Coordinates2License.LiCo lico = getLico( coo );
        assertThat( lico.getLicense().isPresent()).isFalse();

        lIntelligence.compute( coo, lico );

        assertThat( lico.getLicense() ).
                isEqualTo( Optional.of( "mit" ));
    }


    @Test
    public void scribeOro() {
        Coordinates coo = Coordinates.valueOf( "oro:oro:2.0.8" );
        Coordinates2License.LiCo lico = getLico( coo );

        lIntelligence.compute( coo, lico );

        assertThat( lico.getLicense() ).
                isEqualTo( Optional.of( "apache-1.1" ));
    }


    @Test
    public void asm() {
        Coordinates coo = Coordinates.valueOf( "org.ow2.asm:asm:4.1" );
        Coordinates2License.LiCo lico = getLico( coo );

        downloader.setNoInternet();
        downloader.get( coo, lico );

        lIntelligence.compute( coo, lico );

        assertThat( lico.getLicense() ).
                isEqualTo( Optional.of( "bsd-3-clause" ));
    }



    public static Coordinates2License.LiCo getLico( Coordinates coo ) {
        byte[] buf = new byte[ 3000000 ];

        int got = 0;
        try( InputStream in = _nn( JSONStartup.class.getResourceAsStream( "/de/pfabulist/loracle/mojo/"+ coo.toFilename() +".json" ) ) ) {
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

        return new Gson().fromJson( jsonstr, Coordinates2License.LiCo.class );
    }

}
