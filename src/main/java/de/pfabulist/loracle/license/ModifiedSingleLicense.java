package de.pfabulist.loracle.license;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

/**
 * a single license can be modified by adding (an or several?) exception(s)
 * or a plus for "or later"
 */
public class ModifiedSingleLicense implements LicenseID {

    private final String base;
    private final boolean orLater;
    private final Optional<LicenseExclude> exception;

    ModifiedSingleLicense( SingleLicense singleLicense, boolean orLater, Optional<LicenseExclude> exception ) {
        this.exception = exception;
        this.base = singleLicense.getId();
        this.orLater = orLater;
    }


    @Override
    public String getId() {
        return base + (orLater ? "+" : "" ) + exception.map( e -> " with " + e ).orElse( "" );
    }

    @Override
    @SuppressFBWarnings( "NP_METHOD_PARAMETER_TIGHTENS_ANNOTATION" )
    public boolean equals( @Nullable Object o ) {
        if( this == o ) { return true; }
        if( o == null || getClass() != o.getClass() ) { return false; }

        ModifiedSingleLicense that = (ModifiedSingleLicense) o;

        if( orLater != that.orLater ) { return false; }
        if( !base.equals( that.base ) ) { return false; }
        return exception.equals( that.exception );

    }

    @Override
    public int hashCode() {
        int result = base.hashCode();
        result = 31 * result + ( orLater ? 1 : 0 );
        result = 31 * result + exception.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return base + (orLater ? "+" : "" ) + exception.map( e -> " with " + e ).orElse( "" );
    }

    public Optional<LicenseExclude> getException() {
        return exception;
    }

    public String getBase() {
        return base;
    }
}
