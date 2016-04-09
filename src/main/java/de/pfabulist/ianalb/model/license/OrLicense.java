package de.pfabulist.ianalb.model.license;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

public class OrLicense extends SPDXLicense {
    private final SPDXLicense left;
    private final SPDXLicense right;

    public OrLicense( SPDXLicense left, SPDXLicense right ) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String getName() {
        return left.getName() + " OR " + right.getName();
    }

    public SPDXLicense getLeft() {
        return left;
    }

    public SPDXLicense getRight() {
        return right;
    }
}
