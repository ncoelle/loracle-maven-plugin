package de.pfabulist.loracle.urls;

import com.google.gson.Gson;
import de.pfabulist.loracle.buildup.JSONStartup;
import de.pfabulist.loracle.maven.Coordinates;
import de.pfabulist.loracle.license.Coordinates2License;
import de.pfabulist.loracle.license.FindingsDummy;
import de.pfabulist.loracle.license.LOracle;
import de.pfabulist.loracle.mojo.Downloader;
import de.pfabulist.loracle.mojo.LicenseIntelligence;
import de.pfabulist.roast.unchecked.Unchecked;
import org.junit.Test;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

public class CooTest {


    private final LOracle lOracle = JSONStartup.start().spread();
    private final LicenseIntelligence lIntelligence = new LicenseIntelligence( lOracle, new FindingsDummy());
    private final Downloader downloader = new Downloader( new FindingsDummy(), lOracle );

    // todo extra package
//    @Test
//    public void relax() {
//        Coordinates coo = Coordinates.valueOf( "relaxngDatatype:relaxngDatatype:20020414" );
//        Coordinates2License.LiCo lico = getLico( coo );
//        assertThat( lico.getLicense().isPresent()).isFalse();
//        //downloader.get( coo, lico );
//
//        lIntelligence.compute( coo, lico );
//
//        assertThat( lico.getLicense() ).isPresent();
//    }

    // todo extra package
//    @Test
//    public void jdepend() {
//        Coordinates coo = Coordinates.valueOf( "jdepend:jdepend:2.4" );
//        Coordinates2License.LiCo lico = getNix();
//        assertThat( lico.getLicense().isPresent()).isFalse();
//        //downloader.get( coo, lico );
//
//        lIntelligence.compute( coo, lico );
//
//        assertThat( lico.getLicense() ).isPresent();
//    }

    @Test
    public void jamon() {
        Coordinates coo = Coordinates.valueOf( "com.jamonapi:jamon:2.4" );
        Coordinates2License.LiCo lico = getNix();
//        assertThat( lico.getLicense().isPresent()).isFalse();
//        downloader.get( coo, lico );

        lIntelligence.compute( coo, lico );

        assertThat( lico.getLicense() ).isPresent();
    }


    // todo extra package
//    @Test
//    public void hibernatejpa() {
//        Coordinates coo = Coordinates.valueOf( "org.hibernate.javax.persistence:hibernate-jpa-2.0-api:1.0.0.Final" );
//        Coordinates2License.LiCo lico = getNix();
//        assertThat( lico.getLicense().isPresent()).isFalse();
////        downloader.getExtension( coo, lico );
//
//        lIntelligence.compute( coo, lico );
//
//        assertThat( lico.getLicense() ).isPresent();
//    }

//    @Test
//    public void relaxViaUrl() {
//        String u = downloader.get( "https://github.com/orbeon/msv/blob/master/relaxngdatatype/copying.txt" );
//        LicenseFromText lft = new LicenseFromText( lOracle );
//        assertThat( lft.getLicense( u ) ).
//                isEqualTo( MappedLicense.of( lOracle.getOrThrowByName( "bsd-3-clause" ), "tt"));
//    }

//    @Test
//    public void asmViaUrl() {
//        Coordinates coo = Coordinates.valueOf( "org.ow2.asm:asm:4.1" );
//        Coordinates2License.LiCo lico = getNix();
//
//        downloader.getExtension( coo, lico );
//
//        lIntelligence.compute( coo, lico );
//
//        assertThat( lico.getLicense() ).
//                isEqualTo( Optional.of( "bsd-3-clause" ));
//    }






    public static Coordinates2License.LiCo getLico( Coordinates coo ) {
        byte[] buf = new byte[ 3000000 ];

        int got = 0;
        try( @Nullable InputStream in = JSONStartup.class.getResourceAsStream( "/de/pfabulist/loracle/tests/coli/"+ coo.toFilename() +".json" ) )  {
            if ( in == null ) {
                throw new IllegalStateException( "no such resource: " + "/de/pfabulist/loracle/tests/coli/"+ coo.toFilename() +".json" );
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

        String jsonstr = new String( buf, 0, got, StandardCharsets.UTF_8 );

        return new Gson().fromJson( jsonstr, Coordinates2License.LiCo.class );
    }

    public static Coordinates2License.LiCo getNix() {
        byte[] buf = new byte[ 3000000 ];

        int got = 0;
        try( @Nullable InputStream in = JSONStartup.class.getResourceAsStream( "/de/pfabulist/loracle/tests/coli/nix.json" ) )  {
            if ( in == null ) {
                throw new IllegalStateException( "no such resource: nix" );
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

        String jsonstr = new String( buf, 0, got, StandardCharsets.UTF_8 );

        return new Gson().fromJson( jsonstr, Coordinates2License.LiCo.class );
    }
}
