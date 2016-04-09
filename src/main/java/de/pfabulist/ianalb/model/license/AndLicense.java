package de.pfabulist.ianalb.model.license;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

public class AndLicense extends SPDXLicense {

    private final SPDXLicense left;
    private final SPDXLicense right;

    public AndLicense( SPDXLicense left, SPDXLicense right ) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String getName() {
        return encapsulateOr( left ) + " AND " + encapsulateOr( right );
    }

    private String encapsulateOr( SPDXLicense inner ) {
        if( inner instanceof OrLicense ) {
            return "(" + inner.getName() + ")";
        }

        return inner.getName();
    }
}
