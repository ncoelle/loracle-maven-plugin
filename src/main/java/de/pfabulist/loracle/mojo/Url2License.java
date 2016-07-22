package de.pfabulist.loracle.mojo;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.util.HashMap;
import java.util.Map;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

@SuppressFBWarnings( "URF_UNREAD_FIELD" )
@SuppressWarnings( {"PMD.UnusedPrivateField", "PMD.AvoidPrintStackTrace" } )
public class Url2License {

    public static class ULi {
        private String txt = "";
        private String license = "";
    }

    private Map<String, ULi> urls = new HashMap<>();
}
