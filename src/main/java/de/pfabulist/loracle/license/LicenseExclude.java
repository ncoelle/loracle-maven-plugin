package de.pfabulist.loracle.license;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import javax.annotation.Nullable;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

public class LicenseExclude {

    private final String name;

    LicenseExclude( String name ) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    @SuppressFBWarnings( "NP_METHOD_PARAMETER_TIGHTENS_ANNOTATION" )
    public boolean equals( @Nullable Object o ) {
        if( this == o ) { return true; }
        if( o == null || getClass() != o.getClass() ) { return false; }

        LicenseExclude that = (LicenseExclude) o;

        return name.equals( that.name );

    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
