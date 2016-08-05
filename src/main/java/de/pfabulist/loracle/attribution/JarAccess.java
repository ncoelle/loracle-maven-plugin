package de.pfabulist.loracle.attribution;

import de.pfabulist.frex.Frex;
import de.pfabulist.kleinod.nio.Filess;
import de.pfabulist.loracle.Utils;
import de.pfabulist.loracle.license.Coordinates;
import de.pfabulist.loracle.license.Coordinates2License;
import de.pfabulist.loracle.license.LOracle;
import de.pfabulist.loracle.mojo.Findings;
import de.pfabulist.loracle.mojo.MavenLicenseOracle;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;

import static de.pfabulist.nonnullbydefault.NonnullCheck._orElseGet;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */


@SuppressWarnings( {"PMD.UnusedPrivateField"} )
public class JarAccess {

    private final LOracle lOracle;
    private final MavenLicenseOracle mlo;
    private final Findings log;

    private static Pattern licensePattern =
            Frex.or( Frex.txt( "LICENSE" ), Frex.txt( "META-INF/LICENSE" )).then( Frex.any().zeroOrMore() ).
                    buildCaseInsensitivePattern();

    static Pattern copyRightPattern =
            Frex.or( Frex.txt( "Copyright " ) ).
                    then( Frex.txt("(C) ").zeroOrOnce()).
                    then( Frex.or( Frex.number(), Frex.txt( '-' ), Frex.txt( ',' ), Frex.whitespace() ).oneOrMore().var( "year" ) ).
                    then( Frex.txt( ' ' ) ).
                    then( Frex.anyBut( Frex.txt( '\n' ) ).oneOrMore().var( "holder" ) ).
                    buildCaseInsensitivePattern();


    public JarAccess( LOracle lOracle, MavenLicenseOracle mlo, Findings log ) {
        this.lOracle = lOracle;
        this.mlo = mlo;
        this.log = log;
    }


    public void check( Coordinates coo, Coordinates2License.LiCo lico ) {

        Path src = mlo.getArtifact( coo );

        if ( !Files.exists(src)) {
            log.warn( "no jar available " + coo );
            return;
        }

        try( InputStream in = Filess.newInputStream( src ) ) {
            String file = Utils.unzipToString( in, licensePattern );

            if( file.isEmpty() ) {
                log.debug( "artifact source" + coo + " has no license file" );
                return;
            }

            log.debug( coo.toString() + " found license file in jar"  );

            lico.setLicenseTxt( file );
            new LicenseWriter().write( coo, "license", file );

        } catch( IOException e ) {
            log.warn( _orElseGet( e.getMessage(), "pattern problem" ) );
        }
    }

}
