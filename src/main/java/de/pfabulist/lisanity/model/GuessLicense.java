package de.pfabulist.lisanity.model;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.model.License;
import org.apache.maven.plugin.logging.Log;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static de.pfabulist.kleinod.text.Strings.newString;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

public class GuessLicense {

    private final Log log;
    private final Path localRepo;
    private final KnownLicenses knownLicense;
    private final Aliases aliases;
    private final Failures failures;
    private final UrlToName urls;
    private final Licenses licenses;

    public GuessLicense( Log log, Path localRepo, KnownLicenses knownLicense, Aliases aliases, Failures failures, UrlToName urls, Licenses licenses ) {
        this.log = log;
        this.localRepo = localRepo;
        this.knownLicense = knownLicense;
        this.aliases = aliases;
        this.failures = failures;
        this.urls = urls;
        this.licenses = licenses;
    }

    public Optional<LiLicense> guess( Coordinates coo, Optional<License> mavenLicense ) {
        return guess( coo, mavenLicense, Optional.empty() );
    }


    private Optional<LiLicense> guess( Coordinates coo, Optional<License> mavenLicense, Optional<LiLicense> known ) {

        Optional<LiLicense> nameLicense = Optional.empty();
        if( mavenLicense.isPresent() && mavenLicense.get().getName() != null ) {

            nameLicense = licenses.getByName( mavenLicense.get().getName() );

            if ( !nameLicense.isPresent()) {
                nameLicense = aliases.getLicense( mavenLicense.get().getName(), failures );

                if ( nameLicense.isPresent() ) {
                    log.debug( "      license name: " + mavenLicense.get().getName() + " is not a SPDX id" );
                } else {
                    log.warn( "      unknown (or not precise enough) license name: " + mavenLicense.get().getName() );
                }
            }
        }

        Optional<LiLicense> urlLicense = Optional.empty();
        if( mavenLicense.isPresent() && mavenLicense.get().getUrl() != null ) {

            urlLicense = licenses.getByUrl( mavenLicense.get().getUrl() );

            if ( !urlLicense.isPresent() ) {

                if ( mavenLicense.get().getUrl().startsWith( "." ) ) {
                    log.warn( "      license relative to pom but not in deliverable: " + mavenLicense.get().getUrl() );
                } else {

                    urlLicense = urls.getLicense( mavenLicense.get().getUrl(), failures );

                    if( urlLicense.isPresent() ) {
                        log.debug( "      license url: " + mavenLicense.get().getUrl() + " is not a SPDX url" );
                    } else {
                        log.warn( "      unknown license url: " + mavenLicense.get().getUrl() );
                    }

                }
            }
        }

        if( nameLicense.isPresent() ) {
            reportStrange( nameLicense.get(), urlLicense, known );
            return nameLicense;
        }

        if( urlLicense.isPresent() ) {
            reportStrange( urlLicense.get(), nameLicense, known );
            return urlLicense;
        }


        if( known.isPresent() ) {
            reportStrange( known.get(), nameLicense, urlLicense );
            return known;
        }

        failures.add( "   no license found for: " + coo );

        return Optional.empty();
    }

    public Optional<LiLicense> guess( Artifact arti ) {

        Optional<License> mavenLicense = getMavenLicense( arti );

        Optional<LiLicense> known = knownLicense.getLicense( arti );

        return guess( Coordinates.fromArtifact( arti ), mavenLicense, known );
    }

    private void reportStrange( LiLicense one, Optional<LiLicense> two, Optional<LiLicense> three ) {
        two.ifPresent( zwei -> {
            if( !one.equals( zwei ) ) {
                log.warn( "      license does not match first license: " + one + " - " + zwei );
            }
        } );

        three.ifPresent( drei -> {
            if( !one.equals( drei ) ) {
                log.warn( "      license does not match first license: " + one + " - " + drei );
            }
        } );
    }

    private Optional<License> getMavenLicense( Artifact arti ) {
        Coordinates coo = Coordinates.fromArtifact( arti );

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

//    Optional<String> getLicenseName( Artifact arti ) {
//
//        Coordinates coo = Coordinates.fromArtifact( arti );
//
//        while( true ) {
//            try {
//                Path pom = getPom( coo );
//                String content = newString( Files.readAllBytes( pom ) );
//                Optional<String> ret = extractLicenseName( content );
//                if( ret.isPresent() ) {
//                    return ret;
//                }
//
//                Optional<Coordinates> parentCoo = extractParentCoords( content );
//
//                if( parentCoo.isPresent() ) {
//                    coo = parentCoo.get();
//                } else {
//                    return Optional.empty();
//                }
//
//
//            } catch( IOException e ) {
//                log.warn( "no pom ?" );
//                return Optional.empty();
//            }
//        }
//    }

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

}
