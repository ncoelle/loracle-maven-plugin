package de.pfabulist.loracle.attribution;

import de.pfabulist.frex.Frex;
import de.pfabulist.loracle.Utils;
import de.pfabulist.loracle.maven.Coordinates;
import de.pfabulist.loracle.license.Coordinates2License;
import de.pfabulist.loracle.license.LOracle;
import de.pfabulist.loracle.license.Findings;
import de.pfabulist.loracle.mojo.MavenLicenseOracle;
import de.pfabulist.roast.nio.Files_;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;

import static de.pfabulist.kleinod.text.Strings.newString;
import static de.pfabulist.roast.NonnullCheck.n_or;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */


@SuppressWarnings( {"PMD.UnusedPrivateField"} )
public class SrcAccess {

    private final LOracle lOracle;
    private final MavenLicenseOracle mlo;
    private final Findings log;

    private static Pattern javaPattern =
            Frex.any().zeroOrMore().
                    then( Frex.or( Frex.txt( ".scala" ), Frex.txt( ".java" ))).buildCaseInsensitivePattern();

    public SrcAccess( LOracle lOracle, MavenLicenseOracle mlo, Findings log ) {
        this.lOracle = lOracle;
        this.mlo = mlo;
        this.log = log;
    }


    public void check( Coordinates coo, Coordinates2License.LiCo lico ) {

        Path src = mlo.getSrc( coo );

        if ( !Files.exists(src)) {
            log.warn( "no src available " + coo + "  at " + src );
            log.warn( "[try] mvn dependency:source" );
            return;
        }

        try( InputStream in = Files_.newInputStream( src ) ) {
            String file = Utils.unzipToString( in, javaPattern );

            if( file.isEmpty() ) {
                log.warn( "artifact source" + coo + " has no java file" );
//                log.warn( "artifact source" + coo + " has no license file" );
                return;
            }

            file = Header.getSrcHeader( file );
            lico.setHeaderTxt( file );
            if ( !file.isEmpty()) {
                new LicenseWriter().write( coo, "src-header", file );
            }



//            getHolder( lOracle, log, andIsOr, file ).ifPresent( h ->lico.setHolder( Optional.of( h )));
//
//            extractLicense( lOracle, lico, file, log, andIsOr );

        } catch( IOException e ) {
            log.warn( n_or( e.getMessage(), "pattern problem" ) );
        }
    }


    public void getPomHeader( Coordinates coordinates, Coordinates2License.LiCo liCo ) {
        Path src = mlo.getPom( coordinates );

        if ( !Files.exists(src)) {
            log.warn( "no pom available for " + coordinates + " at " + src );
            return;
        }

        String header = Header.getPomHeader( newString( Files_.readAllBytes( src )));
        liCo.setPomHeader( header );
        if ( !header.isEmpty()) {
            new LicenseWriter().write( coordinates, "pom-header", header );
        }

    }
}
