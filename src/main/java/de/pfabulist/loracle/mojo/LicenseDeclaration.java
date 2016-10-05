package de.pfabulist.loracle.mojo;

import de.pfabulist.loracle.license.Coordinates;
import org.apache.maven.plugin.MojoFailureException;

import javax.annotation.Nullable;

import static de.pfabulist.roast.NonnullCheck._orElseThrow;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

public class LicenseDeclaration {

    private @Nullable String coordinates;
    private @Nullable String license;

    public Coordinates getCoordinates() {
        return Coordinates.valueOf( _orElseThrow( coordinates,
                () -> new MojoFailureException( "no coordinates set in configuration of LicenseDeclarations in LOracle Plugin" ) ) );
    }

    public void setCoordinates( String coordinates ) {
        this.coordinates = coordinates;
    }

    public String getLicense() {
        return _orElseThrow( license, () -> new IllegalArgumentException( "no license set in LicenseDeclaration") );
    }

    public void setLicense( String license ) {
        this.license = license;
    }
}
