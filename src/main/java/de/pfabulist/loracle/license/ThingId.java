package de.pfabulist.loracle.license;

import static de.pfabulist.roast.NonnullCheck.n_;

/**
 * Copyright (c) 2006 - 2017, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

public class ThingId {

    private String address;

    public ThingId( String groupId, String artifactId, String version ) {
        address = groupId + ":" + artifactId + ":" + version;

        if( groupId.isEmpty() || artifactId.isEmpty() || version.isEmpty() ) {
            throw new IllegalArgumentException( "not a legal coordinates, one of group, artifact, version is empty" );
        }
        if( groupId.contains( ":" ) || artifactId.contains( ":" ) || version.contains( ":" ) ) {
            throw new IllegalArgumentException( "not a legal coordinates, one of group, artifact, contains a ':' " );
        }
    }


    public String getGroupId() {
        String[] parts = address.split( ":" );
        return n_( parts[ 0 ] );
    }

    public String getArtifactId() {
        String[] parts = address.split( ":" );
        return n_( parts[ 1 ] );

    }

    public String getVersion() {
        String[] parts = address.split( ":" );
        return n_( parts[ 2 ] );
    }

}
