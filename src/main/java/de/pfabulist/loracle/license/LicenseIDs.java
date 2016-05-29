package de.pfabulist.loracle.license;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

public class LicenseIDs {

    public static boolean isOr( LicenseID license ) {
        return license instanceof CompositeLicense && ( (CompositeLicense) license ).isOr();
    }

    public static boolean isAnd( LicenseID license ) {
        return license instanceof CompositeLicense && ( (CompositeLicense) license ).isAnd();
    }
}
