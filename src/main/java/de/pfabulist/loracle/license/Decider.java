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
        if( byCoordinates.isPresent() ) {
            return decideWithCoordinates( byCoordinates, byName, byUrl );
        }

        if( byName.isPresent() ) {
            //noinspection ConstantConditions
            return decideWithName( byName, byUrl );
        }

        byUrl.ifPresent( this::warnOnAnd );
        if( byUrl.isPresent() ) {
            log.debug( "      license by url " );
            return byUrl.addReason( "no name or coordinates" );
        } else {
            log.debug( "      no license " );
            return MappedLicense.empty();
        }

    }

    private MappedLicense decideWithName( MappedLicense licenseID, MappedLicense byUrl ) {
        LicenseID nameLi = licenseID.orElseThrow( () -> new IllegalStateException( "huh" ) );

        MappedLicense ret = licenseID.addReason( "priority name" );

        return byUrl.orElse( l -> {
                                 if( !l.equals( nameLi ) ) {
                                     return ret.addReason( " (over " + byUrl + ")" );
                                 } else {
                                     return ret;
                                 }
                             },
                             ret );
    }

    private MappedLicense decideWithCoordinates( MappedLicense licenseID, MappedLicense byName, MappedLicense byUrl ) {

        LicenseID cooLi = licenseID.orElseThrow( () -> new IllegalStateException( "huh" ) );

        MappedLicense ret = licenseID.addReason( "coordinates priority" );

        MappedLicense ret1 = byName.orElse( l -> {
                                                if( !l.equals( cooLi ) ) {
                                                    return ret.addReason( " (over " + byName + ")" );
                                                } else {
                                                    return ret;
                                                }
                                            },
                                            ret );

        return byUrl.orElse( l -> {
                                 if( !l.equals( cooLi ) ) {
                                     return ret1.addReason( " (over " + byUrl + ")" );
                                 } else {
                                     return ret1;
                                 }
                             },
                             ret1 );

    }

    private void warnOnAnd( LicenseID licenseID ) {
        if( LicenseIDs.isAnd( licenseID ) ) {
            log.error( "   fulfilling the constraints of 2 license is unlikely, was 'or' meant? or really " + licenseID );
            log.error( "   set it directly in plugin configuration" );
        }
    }

}
