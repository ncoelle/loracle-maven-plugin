package de.pfabulist.ianalb.model.license;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

public class SingleSPDXLicense extends SPDXLicense {

    private final String name;

    public SingleSPDXLicense( String name ) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public SPDXOrLater orLater() {
        return new SPDXOrLater( this );
    }

}
