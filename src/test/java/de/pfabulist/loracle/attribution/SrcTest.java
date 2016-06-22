package de.pfabulist.loracle.attribution;

import de.pfabulist.loracle.buildup.JSONStartup;
import de.pfabulist.loracle.license.ContentToLicense;
import de.pfabulist.loracle.license.Coordinates2License;
import de.pfabulist.loracle.license.LOracle;
import de.pfabulist.loracle.mojo.Findings;
import org.apache.maven.plugin.logging.Log;
import org.junit.Test;

import java.util.Optional;
import java.util.regex.Matcher;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

public class SrcTest {

    private LOracle lOracle = JSONStartup.start().spread();

    @Test
    public void tt() {
        String file = "/*\n" +
                " * Bytecode Analysis Framework\n" +
                " * Copyright (C) 2005 University of Maryland\n" +
                " *\n" +
                " * This library is free software; you can redistribute it and/or\n" +
                " * modify it under the terms of the GNU Lesser General Public\n" +
                " * License as published by the Free Software Foundation; either\n" +
                " * version 2.1 of the License, or (at your option) any later version.\n" +
                " *\n" +
                " * This library is distributed in the hope that it will be useful,\n" +
                " * but WITHOUT ANY WARRANTY; without even the implied warranty of\n" +
                " * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU\n" +
                " * Lesser General Public License for more details.\n" +
                " *\n" +
                " * You should have received a copy of the GNU Lesser General Public\n" +
                " * License along with this library; if not, write to the Free Software\n" +
                " * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA\n" +
                " */\n" +
                "package edu.umd.cs.findbugs.annotations;\n" +
                "\n" +
                "import java.lang.annotation.Documented;\n" +
                "import java.lang.annotation.ElementType;\n" +
                "import java.lang.annotation.Retention;\n" +
                "import java.lang.annotation.RetentionPolicy;\n" +
                "import java.lang.annotation.Target;\n" +
                "\n" +
                "import javax.annotation.meta.TypeQualifierNickname;\n" +
                "import javax.annotation.meta.When;\n" +
                "\n" +
                "/**\n" +
                " * The annotated element might be null, and uses of the element should check for\n" +
                " * null.\n" +
                " *\n" +
                " * When this annotation is applied to a method it applies to the method return\n" +
                " * value.\n" +
                " *\n" +
                " * @deprecated - use {@link javax.annotation.CheckForNull} instead.\n" +
                " **/\n" +
                "@Documented\n" +
                "@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE })\n" +
                "@Retention(RetentionPolicy.CLASS)\n" +
                "@javax.annotation.Nonnull(when = When.MAYBE)\n" +
                "@TypeQualifierNickname\n" +
                "@Deprecated\n" +
                "public @interface CheckForNull {\n" +
                "\n" +
                "}\n";

        Matcher matcher = ContentToLicense.copyRightPattern.matcher( file );

        assertThat( matcher.find() ).isTrue();

        System.out.println( matcher.group("holder"));
    }

    @Test
    public void testCddl1() {
        String in = "/*\n" +
                " * The contents of this file are subject to the terms\n" +
                " * of the Common Development and Distribution License\n" +
                " * (the \"License\").  You may not use this file except\n" +
                " * in compliance with the License.\n" +
                " *\n" +
                " * You can obtain a copy of the license at\n" +
                " * glassfish/bootstrap/legal/CDDLv1.0.txt or\n" +
                " * https://glassfish.dev.java.net/public/CDDLv1.0.html.\n" +
                " * See the License for the specific language governing\n" +
                " * permissions and limitations under the License.\n" +
                " *\n" +
                " * When distributing Covered Code, include this CDDL\n" +
                " * HEADER in each file and include the License file at\n" +
                " * glassfish/bootstrap/legal/CDDLv1.0.txt.  If applicable,\n" +
                " * add the following below this CDDL HEADER, with the\n" +
                " * fields enclosed by brackets \"[]\" replaced with your\n" +
                " * own identifying information: Portions Copyright [yyyy]\n" +
                " * [name of copyright owner]\n" +
                " *\n" +
                " * Copyright 2005 Sun Microsystems, Inc. All rights reserved.\n" +
                " *\n" +
                " * Portions Copyright Apache Software Foundation.\n" +
                " */\n" +
                "package javax.servlet;\n" +
                "\t/** \n" +
                "\t* A filter is an object that performs filtering tasks on either the request to a resource (a servlet or static content), or on the response from a resource, or both.\n" +
                "        * \u003cbr\u003e\u003cbr\u003e\n" +
                "\t* Filters perform filtering in the \u003ccode\u003edoFilter\u003c/code\u003e method. Every Filter has access to \n" +
                "\t** a FilterConfig object from which it can obtain its initialization parameters, a\n" +
                "\t** reference to the ServletContext which it can use, for example, to load resources\n" +
                "\t** needed for filtering tasks.\n" +
                "\t** \u003cp\u003e\n" +
                "\t** Filters are configured in the deployment descriptor of a web application\n" +
                "\t** \u003cp\u003e\n" +
                "\t** Examples that have been identified for this design are\u003cbr\u003e\n" +
                "\t** 1) Authentication Filters \u003cbr\u003e\n" +
                "\t** 2) Logging and Auditing Filters \u003cbr\u003e\n" +
                "\t** 3) Image conversion Filters \u003cbr\u003e\n" +
                "    \t** 4) Data compression Filters \u003cbr\u003e\n" +
                "\t** 5) Encryption Filters \u003cbr\u003e\n" +
                "\t** 6) Tokenizing Filters \u003cbr\u003e\n" +
                "\t** 7) Filters that trigger resource access events \u003cbr\u003e\n" +
                "\t** 8) XSL/T filters \u003cbr\u003e\n" +
                "\t** 9) Mime-type chain Filter \u003cbr\u003e\n" +
                "\t * @since\tServlet 2.3\n" +
                "\t*/\n";

        Coordinates2License.LiCo lico = new Coordinates2License.LiCo();
        SrcAccess.extractLicense( lOracle, lico, in, new Findings( dummy ), true );

//        assertThat( lico.getLicense() ).
//                isEqualTo( Optional.of( "cddl-1.0 or gpl-2.0 with classpath-exception-2.0" ));


    }

    private static Log dummy = new Log() {
        @Override
        public boolean isDebugEnabled() {
            return false;
        }

        @Override
        public void debug( CharSequence content ) {

        }

        @Override
        public void debug( CharSequence content, Throwable error ) {

        }

        @Override
        public void debug( Throwable error ) {

        }

        @Override
        public boolean isInfoEnabled() {
            return false;
        }

        @Override
        public void info( CharSequence content ) {

        }

        @Override
        public void info( CharSequence content, Throwable error ) {

        }

        @Override
        public void info( Throwable error ) {

        }

        @Override
        public boolean isWarnEnabled() {
            return false;
        }

        @Override
        public void warn( CharSequence content ) {

        }

        @Override
        public void warn( CharSequence content, Throwable error ) {

        }

        @Override
        public void warn( Throwable error ) {

        }

        @Override
        public boolean isErrorEnabled() {
            return false;
        }

        @Override
        public void error( CharSequence content ) {

        }

        @Override
        public void error( CharSequence content, Throwable error ) {

        }

        @Override
        public void error( Throwable error ) {

        }
    };
}
