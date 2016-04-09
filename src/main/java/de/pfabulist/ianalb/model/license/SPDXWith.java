package de.pfabulist.ianalb.model.license;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

public class SPDXWith extends SPDXLicense {

    private final SPDXLicense base;
    private final String exceptionName;

    public SPDXWith( SPDXLicense base, String exceptionName ) {
        this.base = base;
        this.exceptionName = exceptionName;

        if ( !(base instanceof SPDXOrLater || base instanceof SingleSPDXLicense  )) {
            throw new IllegalArgumentException( "not a single or orlater spdx license as base" );
        }
    }

    @Override
    public String getName() {
        return base.getName() + " WITH " + exceptionName;
    }
}
