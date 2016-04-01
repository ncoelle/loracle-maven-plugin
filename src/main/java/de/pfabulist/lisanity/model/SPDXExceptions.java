package de.pfabulist.lisanity.model;

import java.util.HashSet;
import java.util.Set;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

public class SPDXExceptions {

    private final Set<String> names = new HashSet<>();

    public SPDXExceptions() {
//        new ExtractLicenses().getSPDXIds().forEach(
//                l -> addSpdx( l, "spdx.org/licenses/" + l + ".html" ));

        // todo
//        add( "CDDL + GPLv2 with classpath exception", "https://glassfish.dev.java.net/nonav/public/CDDL+GPL.html" );
        add( "Classpath exception 2.0"  );
    }

    private void add( String name ) {
        names.add( name );
    }



    public boolean isSpdxException( String name ) {
        return names.contains( name );
    }

}
