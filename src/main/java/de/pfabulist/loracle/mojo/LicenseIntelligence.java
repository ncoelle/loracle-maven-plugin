package de.pfabulist.loracle.mojo;

import de.pfabulist.loracle.license.And;
import de.pfabulist.loracle.license.ContentToLicense;
import de.pfabulist.loracle.license.Coordinates;
import de.pfabulist.loracle.license.Coordinates2License;
import de.pfabulist.loracle.license.LOracle;
import de.pfabulist.loracle.license.LicenseFromText;
import de.pfabulist.loracle.license.MappedLicense;

import java.util.Collections;
import java.util.Optional;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

public class LicenseIntelligence {

    private final LOracle lOracle;
    private final Findings log;
    private final UrlToLicense urlToLicense;
    private final LicenseFromText lft;
    private final ContentToLicense contentToLicense;

    public LicenseIntelligence( LOracle lOracle, Findings log ) {
        this.lOracle = lOracle;
        this.log = log;
        this.urlToLicense = new UrlToLicense( lOracle, log );
        lft = new LicenseFromText( lOracle, log );
        contentToLicense = new ContentToLicense( lOracle, log );
    }

    public void compute( Coordinates coordinates, Coordinates2License.LiCo liCo ) {

        MappedLicense byCoo = computeByCoordinates( coordinates, liCo );
        MappedLicense byPom = computeByPom( liCo );
        MappedLicense byLicenseFile = computeByLicenseFile( liCo );
        MappedLicense byPomHeader = computeByPomHeader( liCo );
        MappedLicense bySrcHeader = computeBySrcHeader( liCo );
        MappedLicense byNotice = computeByNotice( liCo );

        MappedLicense sum = MappedLicense.decide( byCoo, byPom, byLicenseFile, byPomHeader, bySrcHeader, byNotice /*, t5 */ );

        if( sum.isPresent() ) {
            liCo.setLicense( sum );
            //log.debug( "coordinates + pom: " + coordinates + " -> " +sum.toString()  );
            // todo overridable by flag
            return;
        }

        log.debug( "searching in in content for " + coordinates );

        MappedLicense byPomHeaderWords = contentToLicense.findLicenses( liCo.getPomHeader(), "by pom header" );
        liCo.setPomHeaderLicense( byPomHeaderWords );

        MappedLicense byLicenseFileWords = contentToLicense.findLicenses( liCo.getLicenseTxt(), "by license file" );
        liCo.setLicenseTxtLicense( byLicenseFileWords );

        MappedLicense byHeaderWords = contentToLicense.findLicenses( liCo.getHeaderTxt(), "by file header" );
        liCo.setHeaderLicense( byHeaderWords );

        MappedLicense byNoticeWords = contentToLicense.findLicenses( liCo.getNotice(), "by notice" );
        liCo.setNoticeLicense( byNoticeWords );

        sum = MappedLicense.decide( byCoo, byPom, byPomHeaderWords, byLicenseFileWords, byHeaderWords, byNoticeWords );
        liCo.setLicense( sum );

        log.debug( "done found?: " + sum.toString() );

    }

    private MappedLicense computeByNotice( Coordinates2License.LiCo liCo ) {
        MappedLicense byNotice = lft.getLicense( liCo.getNotice() );
        if( byNotice.isPresent() ) {
//            log.debug( "[woo] " + coordinates + " -> " + t4 );
            liCo.setNoticeLicense( byNotice );
        }
        return byNotice;
    }

    private MappedLicense computeBySrcHeader( Coordinates2License.LiCo liCo ) {
        MappedLicense bySrcHeader = lft.getLicense( liCo.getHeaderTxt() );
        if( bySrcHeader.isPresent() ) {
//            log.debug( "[woo] " + coordinates + " -> " + t3 );
            liCo.setHeaderLicense( bySrcHeader );
        }
        return bySrcHeader;
    }

    private MappedLicense computeByPomHeader( Coordinates2License.LiCo liCo ) {
        MappedLicense license = lft.getLicense( liCo.getPomHeader() );
        if( license.isPresent() ) {
//            log.debug( "[woo] " + coordinates + " -> " + niotest );
            liCo.setPomHeaderLicense( license );
        }
        return license;
    }

    private MappedLicense computeByLicenseFile( Coordinates2License.LiCo liCo ) {
        MappedLicense byLicenseFile = lft.getLicense( liCo.getLicenseTxt() );
        if( byLicenseFile.isPresent() ) {
//            log.debug( "[woo] " + coordinates + " -> " + t1 );
            liCo.setLicenseTxtLicense( byLicenseFile );
        }
        return byLicenseFile;
    }

    private MappedLicense computeByPom( Coordinates2License.LiCo liCo ) {
        MappedLicense byPom = liCo.getMavenLicenses().stream().
                map( this::mavenLicenseToLicense ).
                reduce( MappedLicense.empty(), ( a, b ) -> new And( lOracle, log, true ).and( a, b ) );

        liCo.setPomLicense( byPom );
        return byPom;
    }

    private MappedLicense computeByCoordinates( Coordinates coordinates, Coordinates2License.LiCo liCo ) {
        MappedLicense byCoo = lOracle.getByCoordinates( coordinates );
        liCo.setByCoordinates( byCoo );

        if( !byCoo.isPresent() ) {
            // only set the url, to be used later
            Optional<String> url = lOracle.getUrlFromCoordinates( coordinates );
            url.ifPresent( u -> liCo.setMavenLicenses( Collections.singletonList( new Coordinates2License.MLicense( "", u, "" ) ) ) );
        }

        return byCoo;
    }

    public MappedLicense mavenLicenseToLicense( Coordinates2License.MLicense mavenLicense ) {

        Optional<String> name = Optional.of( mavenLicense.getName() );
        MappedLicense byName = name.map( lOracle::getByName ).orElse( MappedLicense.empty() );

        Optional<String> url = Optional.of( mavenLicense.getUrl() );
        MappedLicense byUrl = url.map( urlToLicense::getLicense ).orElse( MappedLicense.empty() );

        MappedLicense byComments = contentToLicense.findLicenses( mavenLicense.getComment(), "comments" );

        mavenLicense.setByName( byName );
        mavenLicense.setByUrl( byUrl );
        mavenLicense.setByComment( byComments );

        return MappedLicense.decide( byName, byUrl, byComments );
    }

}
