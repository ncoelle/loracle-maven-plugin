package de.pfabulist.loracle.license;

import org.apache.maven.plugin.logging.Log;

import java.util.Optional;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

public class Decider {


    private final Log log;

    public Decider( Log log ) {
        this.log = log;
    }

    public Optional<LicenseID> decide( Optional<LicenseID> byCoordinates, Optional<LicenseID> byName, Optional<LicenseID> byUrl ) {
        if ( byCoordinates.isPresent()) {
            //noinspection ConstantConditions
            return Optional.of( decideWithCoordinates( byCoordinates.get(), byName, byUrl ));
        }

        if ( byName.isPresent()) {
            //noinspection ConstantConditions
            return decideWithName( byName.get(), byUrl );
        }

        byUrl.ifPresent( this::warnOnAnd );
        if ( byUrl.isPresent()) {
            log.debug( "      license by url " );
        } else {
            log.debug( "      no license " );
        }

        return byUrl;
    }

    private Optional<LicenseID> decideWithName( LicenseID licenseID, Optional<LicenseID> byUrl ) {
        byUrl.ifPresent( name -> {
            if ( !name.equals( licenseID )) {
                log.warn( "   license by url differs " + name );
            }
        } );

        warnOnAnd( licenseID );

        log.debug( "      license by name " );

        return Optional.of( licenseID );
    }

    private LicenseID decideWithCoordinates( LicenseID licenseID, Optional<LicenseID> byName, Optional<LicenseID> byUrl ) {

        byName.ifPresent( name -> {
            if ( !name.equals( licenseID )) {
                log.warn( "   license by name differs " + name );
            }
        } );

        byUrl.ifPresent( name -> {
            if ( !name.equals( licenseID )) {
                log.warn( "   license by url differs " + name );
            }
        } );

        log.debug( "      license on coordinates " );

        return licenseID;
    }

    private void warnOnAnd( LicenseID licenseID ) {
        if ( LicenseIDs.isAnd( licenseID )) {
            log.error( "   fulfilling the constraints of 2 license is unlikely, was 'or' meant? or really "  + licenseID );
            log.error( "   set it directly in plugin configuration" );
        }
    }


}
