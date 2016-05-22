//package de.pfabulist.ianalb.model.oracle;
//
//import de.pfabulist.ianalb.model.license.AcceptedLicenses;
//import de.pfabulist.ianalb.model.license.IBLicense;
//import de.pfabulist.ianalb.model.license.Licenses;
//import de.pfabulist.loracle.license.Coordinates;
//import org.apache.maven.artifact.Artifact;
//import org.apache.maven.model.License;
//import org.apache.maven.plugin.logging.Log;
//
//import java.nio.file.Path;
//import java.util.Optional;
//
///**
// * Copyright (c) 2006 - 2016, Stephan Pfab
// * SPDX-License-Identifier: BSD-2-Clause
// */
//
//public class GuessLicense {
//
//    private final Log log;
//    private final Path localRepo;
//    private final KnownLicenses knownLicense;
//    private final Aliases aliases;
//    private final UrlToName urls;
//    private final Licenses licenses;
//    private final SPDXParser parser;
//    private final AcceptedLicenses acceptedLicenses;
//
//    public GuessLicense( Log log, Path localRepo, KnownLicenses knownLicense, Aliases aliases, UrlToName urls, Licenses licenses ) {
//        this.log = log;
//        this.localRepo = localRepo;
//        this.knownLicense = knownLicense;
//        this.aliases = aliases;
//        this.urls = urls;
//        this.licenses = licenses;
//        this.parser = new SPDXParser( licenses );
//        this.acceptedLicenses = new AcceptedLicenses( parser );
//    }
//
//    public Optional<IBLicense> guess( Coordinates coo, Optional<License> mavenLicense ) {
//        return guess( coo, mavenLicense, Optional.empty() );
//    }
//
//
//    private Optional<IBLicense> guess( Coordinates coo, Optional<License> mavenLicense, Optional<IBLicense> known ) {
//
//        Optional<IBLicense> nameLicense = guessLicenseByName( mavenLicense );
//
//        Optional<IBLicense> urlLicense = Optional.empty();
//        if( mavenLicense.isPresent() && mavenLicense.get().getUrl() != null ) {
//
//            urlLicense = licenses.getByUrl( mavenLicense.get().getUrl() );
//
//            if ( !urlLicense.isPresent() ) {
//
//                if ( mavenLicense.get().getUrl().startsWith( "." ) ) {
//                    log.warn( "      license relative to pom but not in deliverable: " + mavenLicense.get().getUrl() );
//                } else {
//
//                    urlLicense = urls.getLicense( mavenLicense.get().getUrl(), log );
//
//                    if( urlLicense.isPresent() ) {
//                        log.debug( "      license url: " + mavenLicense.get().getUrl() + " is not a SPDX url" );
//                    } else {
//                        log.warn( "      unknown license url: " + mavenLicense.get().getUrl() );
//                    }
//
//                }
//            }
//        }
//
//        if( nameLicense.isPresent() ) {
//            reportStrange( nameLicense.get(), urlLicense, known );
//            return nameLicense;
//        }
//
//        if( urlLicense.isPresent() ) {
//            reportStrange( urlLicense.get(), nameLicense, known );
//            return urlLicense;
//        }
//
//
//        if( known.isPresent() ) {
//            reportStrange( known.get(), nameLicense, urlLicense );
//            return known;
//        }
//
//        log.error( "   no license found for: " + coo );
//
//        return Optional.empty();
//    }
//
//    private Optional<IBLicense> guessLicenseByName( Optional<License> mavenLicense ) {
//        Optional<IBLicense> nameLicense = Optional.empty();
//        if( mavenLicense.isPresent() && mavenLicense.get().getName() != null ) {
//
//            nameLicense = guessLicenseByName( mavenLicense.get().getName() );
//        }
//        return nameLicense;
//    }
//
//    private Optional<IBLicense> guessLicenseByName( String name ) {
//        Optional<IBLicense> nameLicense;
//
//        try {
//            nameLicense = acceptedLicenses.get( name );
//        } catch( Exception e ) {
//            nameLicense = Optional.empty();
//        }
//        //licenses.getByName( name );
//
//        if ( !nameLicense.isPresent()) {
//            nameLicense = aliases.getLicense( name );
//
//            if ( nameLicense.isPresent() ) {
//                log.debug( "      license name: " + name + " is not a SPDX id" );
//            } else {
//                log.warn( "      unknown (getOr not precise enough) license name: " + name );
//            }
//        }
//        return nameLicense;
//    }
//
//    public Optional<IBLicense> guess( Artifact arti ) {
//
//        MavenLicenseOracle mlo = new MavenLicenseOracle( log, localRepo );
//
//        Optional<License> mavenLicense = mlo.getMavenLicense( arti );
//
//        Optional<IBLicense> known = knownLicense.getLicense( arti );
//
//        return guess( Coordinates.valueOf( arti ), mavenLicense, known );
//    }
//
//    private void reportStrange( IBLicense one, Optional<IBLicense> two, Optional<IBLicense> three ) {
//        two.ifPresent( zwei -> {
//            if( !one.equals( zwei ) ) {
//                log.warn( "      license does not match first license: " + one + " - " + zwei );
//            }
//        } );
//
//        three.ifPresent( drei -> {
//            if( !one.equals( drei ) ) {
//                log.warn( "      license does not match first license: " + one + " - " + drei );
//            }
//        } );
//    }
//
//
////    Optional<String> getLicenseName( Artifact arti ) {
////
////        Coordinates coo = Coordinates.valueOf( arti );
////
////        while( true ) {
////            try {
////                Path pom = getPom( coo );
////                String content = newString( Files.readAllBytes( pom ) );
////                Optional<String> ret = extractLicenseName( content );
////                if( ret.isPresent() ) {
////                    return ret;
////                }
////
////                Optional<Coordinates> parentCoo = extractParentCoords( content );
////
////                if( parentCoo.isPresent() ) {
////                    coo = parentCoo.get();
////                } else {
////                    return Optional.empty();
////                }
////
////
////            } catch( IOException e ) {
////                log.warn( "no pom ?" );
////                return Optional.empty();
////            }
////        }
////    }
//
//
//}
