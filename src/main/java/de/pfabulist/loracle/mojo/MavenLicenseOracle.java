package de.pfabulist.loracle.mojo;

import de.pfabulist.loracle.license.Coordinates;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.model.License;
import org.apache.maven.model.Model;
import org.apache.maven.model.Parent;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.plugin.logging.Log;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static de.pfabulist.frex.Frex.txt;
import static de.pfabulist.kleinod.text.Strings.newString;
import static de.pfabulist.nonnullbydefault.NonnullCheck._nn;
import static de.pfabulist.unchecked.NullCheck._orElseGet;

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

    public List<License> getMavenLicense( Artifact arti ) {
        return getMavenLicense( Coordinates.valueOf( arti ));
    }

    public List<License> getMavenLicense( Coordinates coo ) {

        while( true ) {
            Path pom = getPom( coo );
            List<License> licenses = extractLicense( pom );

            if( !licenses.isEmpty() ) {
                log.debug( "      licenses found " );
                licenses.forEach( l -> log.debug( "         " + _orElseGet( l.getName(), "-" ) + " : " + _orElseGet( l.getUrl(), "-" ) ) );
                return licenses;
            }

            log.debug( "    no licenses found in " + coo  );

            Optional<Coordinates> parentCoo = extractParent( pom );

            if( parentCoo.isPresent() ) {
                coo = _nn( parentCoo.get() );
                log.debug( "          going to parent: " + coo );
            } else {
                log.debug( "          no parent, i,e. no license found" );
                return Collections.emptyList();
            }

        }
    }

    private Path getPom( Coordinates coords ) {
        Path ret = localRepo;
        ret = _nn( ret.resolve( coords.getGroupId().replace( '.', '/' ) ) );
        ret = _nn( ret.resolve( coords.getArtifactId() ) );
        ret = _nn( ret.resolve( coords.getVersion() ) );
        ret = _nn( ret.resolve( coords.getArtifactId() + "-" + coords.getVersion() + ".pom" ) );
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

    List<License> extractLicense( final Path pom ) {
        MavenXpp3Reader reader = new MavenXpp3Reader();
        try( Reader pathReader = Files.newBufferedReader( pom ) ) {
            Model pomModel = _nn( reader.read( pathReader ) );

            return _nn( pomModel.getLicenses() );

//            if( licenses.isEmpty() ) {
//                return Optional.empty();
//            }
//
//            if( licenses.size() == 1 ) {
//                return Optional.of( _nn( licenses.get( 0 ) ) );
//            }
//
//            return Optional.of( getAndLicense( licenses ));

        } catch( IOException | XmlPullParserException e ) {
            log.warn( "error extracting license from pom " + e );
        }

//        return Optional.empty();
        return Collections.emptyList();

    }

    public static License getAndLicense( List<License> licenses ) {
        License and = new License();
        and.setName( licenses.stream().map( License::getName ).collect( Collectors.joining( " and " ) ) );
        return and;
    }

//    Optional<String> extractLicenseName( final String raw ) {
//
//        final String licenseTagStart = "<license>", licenseTagStop = "</license>";
//        final String nameTagStart = "<name>", nameTagStop = "</name>";
//        if( raw.contains( licenseTagStart ) ) {
//            final String licenseContents = raw.substring( raw.indexOf( licenseTagStart ) + licenseTagStart.length(), raw.indexOf( licenseTagStop ) );
//            final String name = licenseContents.substring( licenseContents.indexOf( nameTagStart ) + nameTagStart.length(), licenseContents.indexOf( nameTagStop ) );
//            return Optional.of( name );
//        }
//        return Optional.empty();
//    }
//
//    private Optional<String> extractUrl( String raw ) {
//        final String licenseTagStart = "<license>", licenseTagStop = "</license>";
//        final String nameTagStart = "<url>", nameTagStop = "</url>";
//        if( raw.contains( licenseTagStart ) ) {
//            final String licenseContents = raw.substring( raw.indexOf( licenseTagStart ) + licenseTagStart.length(), raw.indexOf( licenseTagStop ) );
//            if( !licenseContents.contains( nameTagStart ) ) {
//                return Optional.empty();
//            }
//            final String name = licenseContents.substring( licenseContents.indexOf( nameTagStart ) + nameTagStart.length(), licenseContents.indexOf( nameTagStop ) );
//            return Optional.of( name );
//        }
//        return Optional.empty();
//
//    }

    Optional<Coordinates> extractParent( Path pom ) {
        MavenXpp3Reader reader = new MavenXpp3Reader();
        try( Reader pathReader = Files.newBufferedReader( pom ) ) {
            Model pomModel = _nn( reader.read( pathReader ) );

            @Nullable Parent parent = pomModel.getParent();
            if( parent == null ) {
                return Optional.empty();
            }

            return Optional.of( new Coordinates( _nn( parent.getGroupId() ), _nn( parent.getArtifactId() ), _nn( parent.getVersion() ) ) );

        } catch( IOException | XmlPullParserException e ) {
            log.warn( "error extracting parent from pom " + e );
        }

        return Optional.empty();

    }

//    /**
//     * @param raw
//     * @return
//     */
//    // TODO obviously this code needs a lot of error protection and handling
//    Optional<Coordinates> extractParentCoords( final String raw ) {
//        final String parentTagStart = "<parent>", parentTagStop = "</parent>";
//        final String groupTagStart = "<groupId>", groupTagStop = "</groupId>";
//        final String artifactTagStart = "<artifactId>", artifactTagStop = "</artifactId>";
//        final String versionTagStart = "<version>", versionTagStop = "</version>";
//
//        if( !raw.contains( parentTagStart ) ) {
//            return Optional.empty();
//        }
//        final String contents = raw.substring( raw.indexOf( parentTagStart ) + parentTagStart.length(), raw.indexOf( parentTagStop ) );
//        final String group = contents.substring( contents.indexOf( groupTagStart ) + groupTagStart.length(), contents.indexOf( groupTagStop ) );
//        final String artifact = contents.substring( contents.indexOf( artifactTagStart ) + artifactTagStart.length(), contents.indexOf( artifactTagStop ) );
//        final String version = contents.substring( contents.indexOf( versionTagStart ) + versionTagStart.length(), contents.indexOf( versionTagStop ) );
//        return Optional.of( new Coordinates( group, artifact, version ) );
//    }

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
