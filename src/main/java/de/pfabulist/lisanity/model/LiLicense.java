package de.pfabulist.lisanity.model;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

public class LiLicense {
    private final String name;
    private final String url;
    private final boolean isSpdx;
    private final boolean orLater;
    private final Optional<String> exception;
    // osi approved
    // spdx approved
    // fsf approved

    LiLicense( String known, String url, boolean isSpdx ) {
        name = known;
        this.url = url;
        this.isSpdx = isSpdx;
        this.orLater = false;
        this.exception = Optional.empty();

    }

    LiLicense( LiLicense base ) {
        this.name = base + "+";
        this.url = base.url;
        this.isSpdx = base.isSpdx;
        this.exception = base.exception;
        this.orLater = true;
    }

    LiLicense( LiLicense base, String exception ) {
        this.name = base + "+";
        this.url = base.url;
        this.isSpdx = base.isSpdx;
        this.orLater = base.orLater;
        this.exception = Optional.of( exception );
    }

    @Override
    @SuppressFBWarnings( "NP_METHOD_PARAMETER_TIGHTENS_ANNOTATION" )
    public boolean equals( @Nullable Object o ) {
        if( this == o ) { return true; }
        if ( o == null ) { return false; }
        if( !( o instanceof LiLicense ) ) { return false; }

        LiLicense liLicense = (LiLicense) o;

        return name.equals( liLicense.name );

    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        if ( isSpdx ) {
            return "SPDX license id: " + name;
        }
        return "non SPDX license name: " + name;
    }

    public String getUrl() {
        return url;
    }

    public boolean isSpdx() {
        return isSpdx;
    }

    public LiLicense orLater() {
        return new LiLicense( this );
    }

    public LiLicense withException( Optional<String> exception ) {
        if ( exception.isPresent() ) {
            return new LiLicense( this, exception.get() );
        }

        return this;
    }
}
