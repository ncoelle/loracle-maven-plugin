package de.pfabulist.loracle.mavencaps;

import de.pfabulist.loracle.maven.Coordinates;


/**
 * Copyright (c) 2006 - 2017, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

public class ArtifactPtr {

    private final Coordinates coo; // weak copy

    public ArtifactPtr( Coordinates coo ) {
        this.coo = coo;
        if ( coo.toString().contains( "*" ) || coo.toString().contains( "[" ) ) {
            throw new IllegalArgumentException( "not a ptr but range" );
        }
    }

    public String getArtifactId() {
        return coo.getArtifactId();
    }



}
