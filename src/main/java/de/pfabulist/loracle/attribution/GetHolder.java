package de.pfabulist.loracle.attribution;

import de.pfabulist.frex.Frex;
import de.pfabulist.loracle.Utils;
import de.pfabulist.loracle.license.ContentToLicense;
import de.pfabulist.loracle.license.Coordinates;
import de.pfabulist.loracle.license.Coordinates2License;
import de.pfabulist.loracle.license.LOracle;
import de.pfabulist.loracle.license.Findings;
import de.pfabulist.loracle.mojo.MavenLicenseOracle;
import de.pfabulist.roast.nio.Files_;
import org.apache.maven.model.License;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static de.pfabulist.roast.NonnullCheck._nn;
import static de.pfabulist.roast.NonnullCheck.n_or;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

public class GetHolder {

    private static Pattern noticePattern =
            Frex.txt( "META-INF/NOTICE" ).then( Frex.any().zeroOrMore() ).
                    buildCaseInsensitivePattern();

//    static Pattern noticeCopyRightPattern =
//            Frex.or( Frex.any(), Frex.txt( '\n' ) ).zeroOrMore().
//                    then( Frex.txt( "Copyright " ) ).
//                    then( Frex.or( Frex.number(), Frex.txt( '-' ), Frex.txt( ',' ), Frex.whitespace() ).oneOrMore().var( "year" ) ).
//                    then( Frex.txt( ' ' ) ).
//                    then( Frex.anyBut( Frex.txt( '\n' ) ).oneOrMore().var( "holder" ) ).
//                    then( Frex.or( Frex.any(), Frex.txt( '\n' ) ).zeroOrMore() ).buildCaseInsensitivePattern();

    enum CopyrightVariables {
        holder,
        year
    }

    static Pattern commentsCopyRightPattern =
            Frex.or( Frex.txt( "Copyright " ) ).
                    then( Frex.or( Frex.number(), Frex.txt( '-' ), Frex.txt( ',' ), Frex.whitespace() ).oneOrMore().var( CopyrightVariables.year ) ).
                    then( Frex.txt( ' ' ) ).
                    then( Frex.any().oneOrMore().var( CopyrightVariables.holder ) ).buildCaseInsensitivePattern();

    private final LOracle lOracle;
    private final MavenLicenseOracle mlo;
    private final Findings log;

    public GetHolder( LOracle lOracle, MavenLicenseOracle mlo, Findings log ) {
        this.lOracle = lOracle;
        this.mlo = mlo;
        this.log = log;
    }

    public Optional<CopyrightHolder> getHolder( Coordinates coo, String license ) {

        List<License> mavenLicenses = mlo.getMavenLicense( coo );

        Optional<CopyrightHolder> ret =
                mavenLicenses.stream().
                        map( ml -> commentsCopyRightPattern.matcher( n_or( ml.getComments(), "" ) ) ).
                        filter( Matcher::matches ).
                        map( m -> new CopyrightHolder( _nn( m.group( "year" ) ), _nn( m.group( "holder" ) ) ) ).
                        findAny();

        if ( ret.isPresent()) {
            return ret;
        }

        if( !lOracle.getOrThrowByName( license ).equals( lOracle.getOrThrowByName( "apache-2" ) ) ) {
            return Optional.empty();
        }

        return getApacheCopyrightHolder( coo );
    }

    public void getNotice( Coordinates coo, Coordinates2License.LiCo lico ) {
        Path jar = mlo.getArtifact( coo );

        try( InputStream in = Files_.newInputStream( jar ) ) {
            String notice = Utils.unzipToString( in, noticePattern );

            lico.setNotice( notice );
            new LicenseWriter().write( coo, "notice", notice );

        } catch( IOException e ) {
            log.warn( n_or( e.getMessage(), "pattern problem" ) );
        }
    }


    private Optional<CopyrightHolder> getApacheCopyrightHolder( Coordinates coo ) {
        Path jar = mlo.getArtifact( coo );

        try( InputStream in = Files_.newInputStream( jar ) ) {
            String notice = Utils.unzipToString( in, noticePattern );

            if( notice.isEmpty() ) {
                log.warn( "artifact " + coo + " licensed to apache-2 has not notice.txt file" );
                return Optional.empty();
            }

            Matcher ch = ContentToLicense.copyRightPattern.matcher( notice );
            if( !ch.find() ) {
                log.warn( "notice file has no recognizable holder" );
                return Optional.empty();
            }

            return Optional.of( new CopyrightHolder( _nn( ch.group( "year" ) ), _nn( ch.group( "holder" ) ) ) );

        } catch( IOException e ) {
            log.warn( n_or( e.getMessage(), "pattern problem" ) );
        }

        return Optional.empty();
    }

//    public static void onUnzip( InputStream is, Predicate<String> filter, BiConsumer<String, InputStream> action ) {
//        try( ZipInputStream zin = new ZipInputStream( is ) ) {
//
//            @Nullable ZipEntry ze;
//            while( ( ze = zin.getNextEntry() ) != null ) {
//
////                if( pat.matcher( ze.getName() ).matches() ) {
////
////                    ByteArrayOutputStream out = new ByteArrayOutputStream();
////                    IO.copy( zin, out );
////
////                    return newString( out.toByteArray() );
////                }
//                zin.closeEntry();
//            }
//        } catch( IOException e ) {
//            throw u( e );
//        }
//
//    }
//
}
