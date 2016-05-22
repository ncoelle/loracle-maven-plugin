package de.pfabulist.loracle.mojo;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

public class LicenseDeclaration {

    private @Nullable String coordinates;
    private @Nullable String license;

    public Optional<String> getCoordinates() {
        return Optional.ofNullable( coordinates );
    }

    public void setCoordinates( String coordinates ) {
        this.coordinates = coordinates;
    }

    public Optional<String> getLicense() {
        return Optional.ofNullable( license );
    }

    public void setLicense( String license ) {
        this.license = license;
    }
}
