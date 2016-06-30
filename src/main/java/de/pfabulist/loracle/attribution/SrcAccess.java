package de.pfabulist.loracle.attribution;

import de.pfabulist.frex.Frex;
import de.pfabulist.kleinod.nio.Filess;
import de.pfabulist.loracle.license.ContentToLicense;
import de.pfabulist.loracle.license.Coordinates;
import de.pfabulist.loracle.license.Coordinates2License;
import de.pfabulist.loracle.license.LOracle;
import de.pfabulist.loracle.license.MappedLicense;
import de.pfabulist.loracle.mojo.Findings;
import de.pfabulist.loracle.mojo.MavenLicenseOracle;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.regex.Pattern;

import static de.pfabulist.kleinod.text.Strings.newString;
import static de.pfabulist.nonnullbydefault.NonnullCheck._orElseGet;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */


@SuppressWarnings( {"PMD.UnusedPrivateField"} )
public class SrcAccess {

    private final LOracle lOracle;
    private final MavenLicenseOracle mlo;
    private final Findings log;
    private final boolean andIsOr;

    private static Pattern javaPattern =
            Frex.any().zeroOrMore().
                    then( Frex.or( Frex.txt( ".scala" ), Frex.txt( ".java" ))).buildCaseInsensitivePattern();

    public SrcAccess( LOracle lOracle, MavenLicenseOracle mlo, Findings log, boolean andIsOr ) {
        this.lOracle = lOracle;
        this.mlo = mlo;
        this.log = log;
        this.andIsOr = andIsOr;
    }


    public void check( Coordinates coo, Coordinates2License.LiCo lico ) {

        Path src = mlo.getSrc( coo );

        if ( !Files.exists(src)) {
            log.warn( "no src available " + coo );
            log.warn( "[try] mvn dependency:source" );
            return;
        }

        try( InputStream in = Filess.newInputStream( src ) ) {
            String file = GetHolder.unzipToString( in, javaPattern );

            if( file.isEmpty() ) {
                log.warn( "artifact source" + coo + " has no java file" );
//                log.warn( "artifact source" + coo + " has no license file" );
                return;
            }

            file = Header.getHeader( file );
            lico.setHeaderTxt( file );


//            getHolder( lOracle, log, andIsOr, file ).ifPresent( h ->lico.setHolder( Optional.of( h )));
//
//            extractLicense( lOracle, lico, file, log, andIsOr );

        } catch( IOException e ) {
            log.warn( _orElseGet( e.getMessage(), "pattern problem" ) );
        }
    }

//    static Optional<CopyrightHolder> getHolder( LOracle lOracle, Findings log, boolean andIsOr, String str  ) {
//        return new ContentToLicense( lOracle, "by file header", log, andIsOr ).getHolder( str );
//    }

    static void extractLicense( LOracle lOracle, Coordinates2License.LiCo lico, String file, Findings log, boolean andIsOr ) {

        if ( lico.getLicense().isPresent()) {
            return;
        }

        MappedLicense ml = new ContentToLicense( lOracle, "by file header", log, andIsOr ).toLicense( file );

        lico.setLicense( ml );

//
//        if ( file.contains( "https://glassfish.dev.java.net/public/CDDLv1.0.html" )) {
//            if ( !lico.getLicense().isPresent() ) {
//                lico.setLicense( MappedLicense.of( lOracle.getOrThrowByName( "CDDl-1.0 or GPL-2.0 with Classpath-exception-2.0" ), "by header file" ) );
//            }
//        }
//
//        if ( file.contains( "https://glassfish.dev.java.net/public/CDDL+GPL_1.1.html" )) {
//            if ( !lico.getLicense().isPresent() ) {
//                lico.setLicense( MappedLicense.of( lOracle.getOrThrowByName( "CDDl-1.1 or GPL-2.0 with Classpath-exception-2.0" ), "by header file" ) );
//            }
//        }
//
//        if ( file.contains( "http://www.apache.org/licenses/LICENSE-2.0" )) {
//            if ( !lico.getLicense().isPresent() ) {
//                lico.setLicense( MappedLicense.of( lOracle.getOrThrowByName( "apache-2" ), "by header file" ) );
//            }
//        }
    }

    public void getPomHeader( Coordinates coordinates, Coordinates2License.LiCo liCo ) {
        Path src = mlo.getPom( coordinates );

        if ( !Files.exists(src)) {
            log.warn( "no pom available for " + coordinates );
            return;
        }

        liCo.setPomHeader( Header.getPomHeader( newString( Filess.readAllBytes( src ))));

    }
}
