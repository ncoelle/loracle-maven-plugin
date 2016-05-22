//package de.pfabulist.ianalb.model.oracle;
//
//import de.pfabulist.frex.Frex;
//import de.pfabulist.ianalb.model.license.AcceptedLicenses;
//import de.pfabulist.ianalb.model.license.IBLicense;
//import de.pfabulist.ianalb.model.license.Licenses;
//import org.apache.maven.plugin.logging.Log;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Optional;
//import java.util.regex.Pattern;
//
///**
// * Copyright (c) 2006 - 2016, Stephan Pfab
// * SPDX-License-Identifier: BSD-2-Clause
// */
//
//public class UrlToName {
//
//    private final Map<Pattern, IBLicense> urlToName = new HashMap<>();
//    private final AcceptedLicenses acceptedLicenses;
//
//    public UrlToName( Licenses licenses ) {
//        this.acceptedLicenses = new AcceptedLicenses( new SPDXParser( licenses ));
//        add( "BSD-2-Clause", "opensource.org/licenses/BSD-2-Clause" );
//        add( "BSD-3-Clause", "opensource.org/licenses/bsd-license", "asm.ow2.org/license.html", "asm.objectweb.org/license.html" );
//        add( "EPL-1.0", "eclipse.org/legal/epl-v10" );
//        add( "Apache-2.0", "apache.org/licenses/LICENSE-2.0" );
//        add( "LGPL-2.1", "gnu.org/licenses/lgpl-2.1" );
//        add( "LGPL-3.0", "gnu.org/licenses/lgpl.html" );
//        add( "MIT", "opensource.org/licenses/mit-license" );
//        add( "JSON", "json.org/license.html" );
//
//        add( "CDDL-1.0 OR GPL-2.0 WITH Classpath-exception-2.0", "glassfish.dev.java.net/nonav/public/CDDL+GPL" );
//        add( "CDDL-1.1 OR GPL-2.0 WITH Classpath-exception-2.0", "glassfish.java.net/public/CDDL+GPL_1_1" );
//
//    }
//
//    private void add( String name, String... urls ) {
//        for( String url : urls ) {
//            urlToName.put( Frex.contains( url ).buildCaseInsensitivePattern(), acceptedLicenses.getOrThrow( name ) );
//        }
//    }
//
//    public Optional<IBLicense> getLicense( String url, Log failures ) {
//        return urlToName.entrySet().stream().filter( e -> e.getKey().matcher( url ).matches() ).findFirst().map( Map.Entry::getValue );
//    }
//
//}
