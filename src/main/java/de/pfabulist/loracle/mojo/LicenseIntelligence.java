package de.pfabulist.loracle.mojo;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import de.pfabulist.loracle.Utils;
import de.pfabulist.loracle.license.And;
import de.pfabulist.loracle.license.ContentToLicense;
import de.pfabulist.loracle.license.Coordinates;
import de.pfabulist.loracle.license.Coordinates2License;
import de.pfabulist.loracle.license.Decider;
import de.pfabulist.loracle.license.LOracle;
import de.pfabulist.loracle.license.LicenseFromText;
import de.pfabulist.loracle.license.MappedLicense;

import java.util.Optional;

import static de.pfabulist.nonnullbydefault.NonnullCheck._nn;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

public class LicenseIntelligence {

    private final LOracle lOracle;
    private final Findings log;
    private final UrlToLicense urlToLicense;

    public LicenseIntelligence( LOracle lOracle, Findings log ) {
        this.lOracle = lOracle;
        this.log = log;
        this.urlToLicense = new UrlToLicense( lOracle, log );
    }

    public void compute( Coordinates coordinates, Coordinates2License.LiCo liCo ) {
        MappedLicense byCoo = lOracle.getByCoordinates( coordinates );

        liCo.setByCoordinates( byCoo );

        MappedLicense byPom = liCo.getMavenLicenses().stream().
                map( this::mavenLicenseToLicense ).
                reduce( MappedLicense.empty(), ( a, b ) -> new And( lOracle, log, true ).and( a, b ) );

        liCo.setPomLicense( byPom );

        LicenseFromText lft = new LicenseFromText( lOracle );
        MappedLicense t1 = lft.getLicense( liCo.getLicenseTxt() );
        if( t1.isPresent() ) {
//            log.debug( "[woo] " + coordinates + " -> " + t1 );
            liCo.setLicenseTxtLicense( t1 );
        }
        MappedLicense t2 = lft.getLicense( liCo.getPomHeader() );
        if( t2.isPresent() ) {
//            log.debug( "[woo] " + coordinates + " -> " + t2 );
            liCo.setPomHeaderLicense( t2 );
        }
        MappedLicense t3 = lft.getLicense( liCo.getHeaderTxt() );
        if( t3.isPresent() ) {
//            log.debug( "[woo] " + coordinates + " -> " + t3 );
            liCo.setHeaderLicense( t3 );
        }
        MappedLicense t4 = lft.getLicense( liCo.getNotice() );
        if( t4.isPresent() ) {
//            log.debug( "[woo] " + coordinates + " -> " + t4 );
            liCo.setNoticeLicense( t4 );
        }

        String txt = "";
        if( liCo.getMavenLicenses().size() > 0 ) {
            String u = _nn( _nn( liCo.getMavenLicenses() ).get( 0 ) ).getUrl();      // todo gen
            if( !u.isEmpty() ) {
                new Downloader( log, new Url2License() ).download( u );
                txt = new Downloader( log, new Url2License() ).get( u );
            }
        }
        MappedLicense t5 = lft.getLicense( txt );
        if( t5.isPresent() ) {
            log.debug( "[woo] " + coordinates + " -> " + t5 );
            liCo.setLicenseTxtLicense( t5 );
        }

        MappedLicense sum = new Decider().decide( byCoo, byPom, t1, t2, t3, t4, t5 );

        if( sum.isPresent() ) {
            liCo.setLicense( sum );
            //log.debug( "coordinates + pom: " + coordinates + " -> " +sum.toString()  );
            // todo overridable by flag
            return;
        }

        log.debug( "searching in in content for " + coordinates );

        MappedLicense byPomHeader = new ContentToLicense( lOracle, "by pom header", log, true ).findLicenses( liCo.getPomHeader() );
        liCo.setPomHeaderLicense( byPomHeader );

        MappedLicense byLicenseTxt = new ContentToLicense( lOracle, "by license file", log, true ).findLicenses( liCo.getLicenseTxt() );
        liCo.setLicenseTxtLicense( byLicenseTxt );

        MappedLicense byHeader = new ContentToLicense( lOracle, "by file header", log, true ).findLicenses( liCo.getHeaderTxt() );
        liCo.setHeaderLicense( byHeader );

        MappedLicense byNotice = new ContentToLicense( lOracle, "by notice", log, true ).findLicenses( liCo.getNotice() );
        liCo.setNoticeLicense( byNotice );

        sum = new Decider().decide( byCoo, byPom, byPomHeader, byLicenseTxt, byHeader, byNotice );
        liCo.setLicense( sum );

        log.debug( "done found?: " + sum.toString() );

    }

    public MappedLicense mavenLicenseToLicense( //Coordinates coo,
                                                 // MappedLicense byCoordinates,
                                                 Coordinates2License.MLicense mavenLicense ) {

        Optional<String> name = Optional.of( mavenLicense.getName() );
        MappedLicense byName = name.map( lOracle::getByName ).orElse( MappedLicense.empty() );

        if ( name.isPresent() && !byName.isPresent()) {
            byName = getLicenseExtension( _nn( name.get() ));
        }

        Optional<String> url = Optional.of( mavenLicense.getUrl() );
        MappedLicense byUrl = url.map( urlToLicense::getLicense ).orElse( MappedLicense.empty() );

        MappedLicense byUrlText = url.map( u -> {
            if ( u.isEmpty()) {
                return MappedLicense.empty();
            }
            new Downloader( log, new Url2License() ).download( u );
            String txt = new Downloader( log, new Url2License() ).get( u );
            if( txt.isEmpty() ) {
                return MappedLicense.empty();
            }
            LicenseFromText lft = new LicenseFromText( lOracle );
            MappedLicense ret = lft.getLicense( txt );
            if ( ret.isPresent() ) {
                log.debug( "[full] " + u );
            }
            return ret;

        } ).orElse( MappedLicense.empty() );

        MappedLicense byComments = new ContentToLicense( lOracle, "comments", log, true ).findLicenses( mavenLicense.getComment() );

        mavenLicense.setByName( byName );
        if ( byUrlText.isPresent() ) {
            mavenLicense.setByUrl( byUrlText );
        } else {
            mavenLicense.setByUrl( byUrl );
        }
        mavenLicense.setByComment( byComments );

        return new Decider().decide( byUrlText, byName, byUrl, byComments );
    }

    private MappedLicense getLicenseExtension( String name ) {
        String licensep = Utils.getResourceAsString( "/de/pfabulist/loracle/licenses/" + name + ".json" );

        if ( licensep.isEmpty() ) {
            log.debug( "no such license: " + name );
            return MappedLicense.empty();
        }

        try {
            LOracle.More more = new Gson().fromJson( licensep, LOracle.More.class );
            MappedLicense ret = MappedLicense.of( lOracle.newSingle( name, more ), "by new addon license" );
            log.info( "added new license " + name );
            return ret;

        } catch( JsonSyntaxException e ) {
            // nothing
        } catch( IllegalArgumentException e ) {
            log.warn( "new license in extension is not new (ignored) " + name );
        }

        return MappedLicense.empty();
    }

}
