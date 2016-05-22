//package de.pfabulist.ianalb.model.oracle;
//
//import de.pfabulist.ianalb.model.license.IBLicense;
//import de.pfabulist.ianalb.model.license.Licenses;
//import org.apache.maven.plugin.logging.Log;
//
//import java.util.Optional;
//
///**
// * Copyright (c) 2006 - 2016, Stephan Pfab
// * SPDX-License-Identifier: BSD-2-Clause
// */
//
//public class LicenseOracle {
//
//    private final Aliases aliases;
//    private final Licenses licenses;
//    private final Log log;
//
//    private final UrlToName urls;
//
//
//    public LicenseOracle( Aliases aliases, Licenses licenses, Log log, UrlToName urls ) {
//        this.aliases = aliases;
//        this.licenses = licenses;
//        this.log = log;
//        this.urls = urls;
//    }
//
//    public Optional<IBLicense> guessLicenseByName( String name ) {
//
//        Optional<IBLicense> nameLicense = licenses.getByName( name );
//
//        if ( !nameLicense.isPresent()) {
//            log.debug( "      license name: " + name + " is not a SPDX id" );
//
//            nameLicense = aliases.getLicense( name );
//
//            if ( !nameLicense.isPresent() ) {
//                log.warn( "      unknown (getOr not precise enough) license name: " + name );
//            }
//        }
//
//        return nameLicense;
//    }
//
//    public Optional<IBLicense> guessByUrl( String url ) {
//        return urls.getLicense( url, log );
//    }
//
//}
