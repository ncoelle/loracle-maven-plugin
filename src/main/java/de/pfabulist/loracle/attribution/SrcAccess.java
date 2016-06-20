package de.pfabulist.loracle.attribution;

import de.pfabulist.frex.Frex;
import de.pfabulist.kleinod.collection.P;
import de.pfabulist.kleinod.nio.Filess;
import de.pfabulist.loracle.license.Coordinates;
import de.pfabulist.loracle.license.Coordinates2License;
import de.pfabulist.loracle.license.LOracle;
import de.pfabulist.loracle.mojo.Findings;
import de.pfabulist.loracle.mojo.MavenLicenseOracle;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static de.pfabulist.nonnullbydefault.NonnullCheck._nn;
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

    private static Pattern javaPattern = //Frex.any().then( Frex.txt( "license") ).then( Frex.txt( '.' ).then( Frex.any().zeroOrMore()).zeroOrOnce()).buildCaseInsensitivePattern();
            Frex.any().zeroOrMore().then( Frex.txt( ".java" )).buildCaseInsensitivePattern();
    static Pattern copyRightPattern =
            Frex.or( Frex.txt( "Copyright " ) ).
                    then( Frex.txt("(C) ").zeroOrOnce()).
                    then( Frex.or( Frex.number(), Frex.txt( '-' ), Frex.txt( ',' ), Frex.whitespace() ).oneOrMore().var( "year" ) ).
                    then( Frex.txt( ' ' ) ).
                    then( Frex.anyBut( Frex.txt( '\n' ) ).oneOrMore().var( "holder" ) ).
                    buildCaseInsensitivePattern();


    public SrcAccess( LOracle lOracle, MavenLicenseOracle mlo, Findings log ) {
        this.lOracle = lOracle;
        this.mlo = mlo;
        this.log = log;
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

//            lico.setLicenseTxt( file );
            lico.setHeaderTxt( Header.getHeader( file ));

            Matcher matcher = copyRightPattern.matcher( file );

            if ( matcher.find() ) {
                CopyrightHolder ch = new CopyrightHolder( _nn(matcher.group( "year" )), _nn(matcher.group("holder")));
                log.info( "" + coo + " -> " + ch );
                lico.setHolder( Optional.of( ch ));
                return;
            }


//            Matcher ch = noticeCopyRightPattern.matcher( file );
//            if( !ch.matches() ) {
//                log.warn( "notice file has unexpected pattern" );
//                return;
//            }
//
//            return Optional.of( new CopyrightHolder( _nn( ch.group( "year" ) ), _nn( ch.group( "holder" ) ) ) );
//
        } catch( IOException e ) {
            log.warn( _orElseGet( e.getMessage(), "pattern problem" ) );
        }
    }
}
