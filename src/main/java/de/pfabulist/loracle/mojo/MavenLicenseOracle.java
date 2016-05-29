package de.pfabulist.loracle.mojo;

import de.pfabulist.loracle.license.Coordinates;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.model.License;
import org.apache.maven.plugin.logging.Log;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static de.pfabulist.frex.Frex.txt;
import static de.pfabulist.kleinod.text.Strings.newString;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

public class MavenLicenseOracle {

    private final Log log;
    private final Path localRepo;

    public MavenLicenseOracle( Log log, Path localRepo ) {
        this.log = log;
        this.localRepo = localRepo;
    }

    public Optional<License> getMavenLicense( Artifact arti ) {
        Coordinates coo = Coordinates.valueOf( arti );

        while( true ) {
            try {
                Path pom = getPom( coo );
                String content = newString( Files.readAllBytes( pom ) );
                Optional<String> ret = extractLicenseName( content );
                if( ret.isPresent() ) {
                    License license = new License();
                    license.setName( ret.get() );
                    extractUrl( content ).ifPresent( license::setUrl );
                    return Optional.of( license );
                } else {
                    log.debug( "       " + coo + " has no license declaration checking parent " );
                }

                Optional<Coordinates> parentCoo = extractParentCoords( content );

                if( parentCoo.isPresent() ) {
                    coo = parentCoo.get();
                    log.debug( "          parent is" + coo );
                } else {
                    log.debug( "          no parent" );
                    return Optional.empty();
                }

            } catch( IOException e ) {
                log.warn( "no pom ?" );
                return Optional.empty();
            }
        }
    }

    private Path getPom( Coordinates coords ) {
        Path ret = localRepo;
        ret = ret.resolve( coords.getGroupId().replace( '.', '/' ) );
        ret = ret.resolve( coords.getArtifactId() );
        ret = ret.resolve( coords.getVersion() );
        ret = ret.resolve( coords.getArtifactId() + "-" + coords.getVersion() + ".pom" );

//        log.info( "pom exists: " + ret + " " + Files.exists( ret ) );
//
        return ret;
    }

//    private Path getPom( Artifact arti ) {
//        Path ret = localRepo;
//        ret = ret.resolve( arti.getGroupId().replace( '.', '/' ) );
//        ret = ret.resolve( arti.getArtifactId() );
//        ret = ret.resolve( arti.getVersion() );
//        ret = ret.resolve( arti.getArtifactId() + "-" + arti.getVersion() + ".pom" );
//
//        log.info( "pom exists: " + ret + " " + Files.exists( ret ) );
//
//        return ret;
//    }

    Optional<String> extractLicenseName( final String raw ) {
        final String licenseTagStart = "<license>", licenseTagStop = "</license>";
        final String nameTagStart = "<name>", nameTagStop = "</name>";
        if( raw.contains( licenseTagStart ) ) {
            final String licenseContents = raw.substring( raw.indexOf( licenseTagStart ) + licenseTagStart.length(), raw.indexOf( licenseTagStop ) );
            final String name = licenseContents.substring( licenseContents.indexOf( nameTagStart ) + nameTagStart.length(), licenseContents.indexOf( nameTagStop ) );
            return Optional.of( name );
        }
        return Optional.empty();
    }

    private Optional<String> extractUrl( String raw ) {
        final String licenseTagStart = "<license>", licenseTagStop = "</license>";
        final String nameTagStart = "<url>", nameTagStop = "</url>";
        if( raw.contains( licenseTagStart ) ) {
            final String licenseContents = raw.substring( raw.indexOf( licenseTagStart ) + licenseTagStart.length(), raw.indexOf( licenseTagStop ) );
            if ( !licenseContents.contains( nameTagStart )) {
                return Optional.empty();
            }
            final String name = licenseContents.substring( licenseContents.indexOf( nameTagStart ) + nameTagStart.length(), licenseContents.indexOf( nameTagStop ) );
            return Optional.of( name );
        }
        return Optional.empty();
    }

    /**
     * @param raw
     * @return
     */
    // TODO obviously this code needs a lot of error protection and handling
    Optional<Coordinates> extractParentCoords( final String raw ) {
        final String parentTagStart = "<parent>", parentTagStop = "</parent>";
        final String groupTagStart = "<groupId>", groupTagStop = "</groupId>";
        final String artifactTagStart = "<artifactId>", artifactTagStop = "</artifactId>";
        final String versionTagStart = "<version>", versionTagStop = "</version>";

        if( !raw.contains( parentTagStart ) ) {
            return Optional.empty();
        }
        final String contents = raw.substring( raw.indexOf( parentTagStart ) + parentTagStart.length(), raw.indexOf( parentTagStop ) );
        final String group = contents.substring( contents.indexOf( groupTagStart ) + groupTagStart.length(), contents.indexOf( groupTagStop ) );
        final String artifact = contents.substring( contents.indexOf( artifactTagStart ) + artifactTagStart.length(), contents.indexOf( artifactTagStop ) );
        final String version = contents.substring( contents.indexOf( versionTagStart ) + versionTagStart.length(), contents.indexOf( versionTagStop ) );
        return Optional.of( new Coordinates( group, artifact, version ) );
    }

//    private Optional<String> extract( String raw, String outer, String tag ) {
//        Pattern pat = any().zeroOrMore().
//                        then( txt("<" + outer + ">")).
//                        then( any().zeroOrMore() ).
//                        then( txt("<" + tag + ">")).
//                        then( Frex.anyBut( txt('<') ).var( "content" )).
//                        then( txt("</" + tag + ">")).
//                        then( any().zeroOrMore() ).
//                        then( txt("</" + outer + ">")).
//                        then( any().zeroOrMore() ).
//                buildCaseInsensitivePattern();
//
//        Matcher matcher = pat.matcher( raw );
//
//        if ( !matcher.matches()) {
//            return Optional.empty();
//        }
//
//        return Optional.of( matcher.group( "content" ) );
//
////        final String outerStart = "<" + outer + ">";
////        //final String outerStop = "</" + outer + ">";
////        final String tagStart = "<" + tag + ">";
////        final String tagStop = "</" + tag + ">";
////
////        if( raw.contains( outerStart ) ) {
////            final String outerBlock = raw.substring( raw.indexOf( outerStart ) + outerStart.length() ); //, raw.indexOf( outerStop ) );
////            if ( !outerBlock.contains( tagStart  )) {
////                return Optional.empty();
////            }
////
////            final String tagContent = outerBlock.substring( outerBlock.indexOf( tagStart ) + tagStart.length(), outerBlock.indexOf( tagStop ) );
////            return Optional.of( tagContent );
////        }
////        return Optional.empty();
//    }

}
