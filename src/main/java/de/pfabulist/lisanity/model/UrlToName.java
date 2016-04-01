package de.pfabulist.lisanity.model;

import de.pfabulist.frex.Frex;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

public class UrlToName {

    private final Map<Pattern, LiLicense> urlToName = new HashMap<>();
    private final Licenses licenses;

    public UrlToName( Licenses licenses ) {
        this.licenses = licenses;
        add( "BSD-2-Clause", "opensource.org/licenses/BSD-2-Clause" );
        add( "BSD-3-Clause", "opensource.org/licenses/bsd-license", "asm.ow2.org/license.html", "asm.objectweb.org/license.html" );
        add( "EPL-1.0", "eclipse.org/legal/epl-v10" );
        add( "Apache-2.0", "apache.org/licenses/LICENSE-2.0" );
        add( "LGPL-3.0", "gnu.org/licenses/lgpl.html" );
        add( "MIT", "opensource.org/licenses/mit-license" );
        add( "JSON", "json.org/license.html" );
    }

    private void add( String name, String... urls ) {
        for( String url : urls ) {
            urlToName.put( Frex.contains( url ).buildCaseInsensitivePattern(), licenses.getOrThrowByName( name ) );
        }
    }

    public Optional<LiLicense> getLicense( String url, Failures failures ) {
        return urlToName.entrySet().stream().filter( e -> e.getKey().matcher( url ).matches() ).findFirst().map( Map.Entry::getValue );
    }

}
