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

    public MappedLicense decide( MappedLicense byCoordinates, MappedLicense byName, MappedLicense byUrl ) {
        if ( byCoordinates.isPresent()) {
            return decideWithCoordinates( byCoordinates, byName, byUrl );
        }

        if ( byName.isPresent()) {
            //noinspection ConstantConditions
            return decideWithName( byName, byUrl );
        }

        byUrl.ifPresent( this::warnOnAnd );
        if ( byUrl.isPresent()) {
            log.debug( "      license by url " );
            return byUrl.addReason( "no name or coordinates" );
        } else {
            log.debug( "      no license " );
            return MappedLicense.empty();
        }

    }

    private MappedLicense decideWithName( MappedLicense licenseID, MappedLicense byUrl ) {
        byUrl.ifPresent( name -> {
            if ( !name.equals( licenseID )) {
                log.warn( "   license by url differs " + name );
            }
        } );

        licenseID.ifPresent( this::warnOnAnd );

        log.debug( "      license by name " );

        return licenseID.addReason( "priority name" );
    }

    private MappedLicense decideWithCoordinates( MappedLicense licenseID, MappedLicense byName, MappedLicense byUrl ) {

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

        return licenseID.addReason( "coordinates priority" );  // name and url ?
    }

    private void warnOnAnd( LicenseID licenseID ) {
        if ( LicenseIDs.isAnd( licenseID )) {
            log.error( "   fulfilling the constraints of 2 license is unlikely, was 'or' meant? or really "  + licenseID );
            log.error( "   set it directly in plugin configuration" );
        }
    }


}
