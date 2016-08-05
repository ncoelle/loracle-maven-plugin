package de.pfabulist.loracle.license;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    public static List<String> flattenToStrings( LicenseID license ) {
        if ( license instanceof CompositeLicense ) {
            CompositeLicense cl = (CompositeLicense)license;
            List<String> ret = new ArrayList<>();
            ret.addAll( flattenToStrings( cl.getLeft() ) );
            ret.addAll( flattenToStrings( cl.getRight() ) );
            return ret;
        }

        if ( license instanceof SingleLicense ) {
            return Collections.singletonList( license.toString() );
        }

        if ( license instanceof ModifiedSingleLicense ) {
            ModifiedSingleLicense msl = (ModifiedSingleLicense)license;
            List<String> ret = new ArrayList<>();
            ret.add( msl.getBase() );
            msl.getException().ifPresent( ex -> ret.add( ex.toString() ) );
            return ret;

        }

        return Collections.emptyList();
    }
}
