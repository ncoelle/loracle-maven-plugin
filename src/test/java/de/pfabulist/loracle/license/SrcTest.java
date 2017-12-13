//package de.pfabulist.loracle.license;
//
//import de.pfabulist.loracle.attribution.Header;
//import de.pfabulist.loracle.buildup.JSONStartup;
//import de.pfabulist.loracle.license.known.LOracleKnown;
//import org.junit.Test;
//
//import java.util.regex.Matcher;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
///**
// * Copyright (c) 2006 - 2016, Stephan Pfab
// * SPDX-License-Identifier: BSD-2-Clause
// */
//
//public class SrcTest {
//
//    private LOracleKnown lOracle = JSONStartup.start(); //.spread();
//    private final ContentToLicense ctl = new ContentToLicense( lOracle, new FindingsDummy() );
// //   private Log log = SrcTest.dummy;
//
//
//
//
//    @Test
//    public void tt() {
//        String file = "/*\n" +
//                " * Bytecode Analysis Framework\n" +
//                " * Copyright (C) 2005 University of Maryland\n" +
//                " *\n" +
//                " * This library is free software; you can redistribute it and/or\n" +
//                " * modify it under the terms of the GNU Lesser General Public\n" +
//                " * License as published by the Free Software Foundation; either\n" +
//                " * version 2.1 of the License, or (at your option) any later version.\n" +
//                " *\n" +
//                " * This library is distributed in the hope that it will be useful,\n" +
//                " * but WITHOUT ANY WARRANTY; without even the implied warranty of\n" +
//                " * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU\n" +
//                " * Lesser General Public License for more details.\n" +
//                " *\n" +
//                " * You should have received a copy of the GNU Lesser General Public\n" +
//                " * License along with this library; if not, write to the Free Software\n" +
//                " * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA\n" +
//                " */\n" +
//                "package edu.umd.cs.findbugs.annotations;\n" +
//                "\n" +
//                "import java.lang.annotation.Documented;\n" +
//                "import java.lang.annotation.ElementType;\n" +
//                "import java.lang.annotation.Retention;\n" +
//                "import java.lang.annotation.RetentionPolicy;\n" +
//                "import java.lang.annotation.Target;\n" +
//                "\n" +
//                "import javax.annotation.meta.TypeQualifierNickname;\n" +
//                "import javax.annotation.meta.When;\n" +
//                "\n" +
//                "/**\n" +
//                " * The annotated element might be null, and uses of the element should check for\n" +
//                " * null.\n" +
//                " *\n" +
//                " * When this annotation is applied to a method it applies to the method return\n" +
//                " * value.\n" +
//                " *\n" +
//                " * @deprecated - use {@link javax.annotation.CheckForNull} instead.\n" +
//                " **/\n" +
//                "@Documented\n" +
//                "@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE })\n" +
//                "@Retention(RetentionPolicy.CLASS)\n" +
//                "@javax.annotation.Nonnull(when = When.MAYBE)\n" +
//                "@TypeQualifierNickname\n" +
//                "@Deprecated\n" +
//                "public @interface CheckForNull {\n" +
//                "\n" +
//                "}\n";
//
//        Matcher matcher = ContentToLicense.copyRightPattern.matcher( file );
//
//        assertThat( matcher.find() ).isTrue();
//
//        System.out.println( matcher.group( "holder" ) );
//    }
//
////    @Test
////    public void testCddl1() {
////        String in = "/*\n" +
////                " * The contents of this file are subject to the terms\n" +
////                " * of the Common Development and Distribution License\n" +
////                " * (the \"License\").  You may not use this file except\n" +
////                " * in compliance with the License.\n" +
////                " *\n" +
////                " * You can obtain a copy of the license at\n" +
////                " * glassfish/bootstrap/legal/CDDLv1.0.txt or\n" +
////                " * https://glassfish.dev.java.net/public/CDDLv1.0.html.\n" +
////                " * See the License for the specific language governing\n" +
////                " * permissions and limitations under the License.\n" +
////                " *\n" +
////                " * When distributing Covered Code, include this CDDL\n" +
////                " * HEADER in each file and include the License file at\n" +
////                " * glassfish/bootstrap/legal/CDDLv1.0.txt.  If applicable,\n" +
////                " * add the following below this CDDL HEADER, with the\n" +
////                " * fields enclosed by brackets \"[]\" replaced with your\n" +
////                " * own identifying information: Portions Copyright [yyyy]\n" +
////                " * [name of copyright owner]\n" +
////                " *\n" +
////                " * Copyright 2005 Sun Microsystems, Inc. All rights reserved.\n" +
////                " *\n" +
////                " * Portions Copyright Apache Software Foundation.\n" +
////                " */\n" +
////                "package javax.servlet;\n" +
////                "\t/** \n" +
////                "\t* A filter is an object that performs filtering tasks on either the request to a resource (a servlet or static content), or on the response from a resource, or both.\n" +
////                "        * \u003cbr\u003e\u003cbr\u003e\n" +
////                "\t* Filters perform filtering in the \u003ccode\u003edoFilter\u003c/code\u003e method. Every Filter has access to \n" +
////                "\t** a FilterConfig object from which it can obtain its initialization parameters, a\n" +
////                "\t** reference to the ServletContext which it can use, for example, to load resources\n" +
////                "\t** needed for filtering tasks.\n" +
////                "\t** \u003cp\u003e\n" +
////                "\t** Filters are configured in the deployment descriptor of a web application\n" +
////                "\t** \u003cp\u003e\n" +
////                "\t** Examples that have been identified for this design are\u003cbr\u003e\n" +
////                "\t** 1) Authentication Filters \u003cbr\u003e\n" +
////                "\t** 2) Logging and Auditing Filters \u003cbr\u003e\n" +
////                "\t** 3) Image conversion Filters \u003cbr\u003e\n" +
////                "    \t** 4) Data compression Filters \u003cbr\u003e\n" +
////                "\t** 5) Encryption Filters \u003cbr\u003e\n" +
////                "\t** 6) Tokenizing Filters \u003cbr\u003e\n" +
////                "\t** 7) Filters that trigger resource access events \u003cbr\u003e\n" +
////                "\t** 8) XSL/T filters \u003cbr\u003e\n" +
////                "\t** 9) Mime-type chain Filter \u003cbr\u003e\n" +
////                "\t * @since\tServlet 2.3\n" +
////                "\t*/\n";
////
////        Coordinates2License.LiCo lico = new Coordinates2License.LiCo();
////        SrcAccess.extractLicense( lOracle, lico, in, new Findings( dummy ), true );
////
//////        assertThat( lico.getLicense() ).
//////                isEqualTo( Optional.of( "cddl-1.0 or gpl-2.0 with classpath-exception-2.0" ));
////
////
////    }
//
////    @Test
////    public void testBSDStart() {
////        String in = "Maven\nJAXB2 Plugin license is the 3-clause BSD license which is compatible with GPL.\nMaven JAXB2 Plugin license does not require you to include any acknowledgments for Maven JAXB2 Plugin in advertising materials for your software.";
////
////        MappedLicense ml = new ContentToLicense( lOracle, "duda", new Findings( dummy ), true ).byLongNameSearch( in );
////
////        assertThat( ml.isPresent() ).isTrue();
////    }
//
//    @Test
//    public void testTT() {
//        String hh =
//                "// HTMLParser Library - A java-based parser for HTML\r\n" +
//                        "// http://htmlparser.org\r\n" +
//                        "// Copyright (C) 2006 Derrick Oswald\r\n" +
//                        "//\r\n" +
//                        "// Revision Control Information\r\n" +
//                        "//\r\n" +
//                        "// $URL: https://htmlparser.svn.sourceforge.net/svnroot/htmlparser/tags/HTMLParserProject-2.1/lexer/src/main/java/org/htmlparser/Attribute.java $\r\n" +
//                        "// $Author: derrickoswald $\r\n" +
//                        "// $Date: 2006-09-16 16:44:17 +0200 (Sat, 16 Sep 2006) $\r\n" +
//                        "// $Revision: 4 $\r\n" +
//                        "//\r\n" +
//                        "// This library is free software; you can redistribute it and/or\r\n" +
//                        "// modify it under the terms of the Common Public License; either\r\n" +
//                        "// version 1.0 of the License, or (at your option) any later version.\r\n" +
//                        "//\r\n" +
//                        "// This library is distributed in the hope that it will be useful,\r\n" +
//                        "// but WITHOUT ANY WARRANTY; without even the implied warranty of\r\n" +
//                        "// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the\r\n" +
//                        "// Common Public License for more details.\r\n" +
//                        "//\r\n" +
//                        "// You should have received a copy of the Common Public License\r\n" +
//                        "// along with this library; if not, the license is available from\r\n" +
//                        "// the Open Source Initiative (OSI) website:\r\n" +
//                        "//   http://opensource.org/licenses/cpl1.0.php\r\n" +
//                        "package org.htmlparser;\r\n" +
//                        "/**\r\n" +
//                        " * An attribute within a tag.\r\n" +
//                        " * Holds the name, assignment string, value and quote character.\r\n" +
//                        " * \u003cp\u003e\r\n" +
//                        " * This class was made deliberately simple. Except for\r\n" +
//                        " * {@link #setRawValue RawValue}, the properties are completely orthogonal,\r\n" +
//                        " * that is: each property is independant of the others. This means you have\r\n" +
//                        " * enough rope here to hang yourself, and it\u0027s very easy to create\r\n" +
//                        " * malformed HTML. Where it\u0027s obvious, warnings and notes have been provided\r\n" +
//                        " * in the setters javadocs, but it is up to you -- the programmer --\r\n" +
//                        " * to ensure that the contents of the four fields will yield valid HTML\r\n" +
//                        " * (if that\u0027s what you want).\r\n" +
//                        " * \u003cp\u003e\r\n" +
//                        " * Be especially mindful of quotes and assignment strings. These are handled\r\n" +
//                        " * by the constructors where it\u0027s obvious, but in general, you need to set\r\n" +
//                        " * them explicitly when building an attribute. For example to construct\r\n" +
//                        " * the attribute \u003cb\u003e\u003ccode\u003elabel\u003d\"A multi word value.\"\u003c/code\u003e\u003c/b\u003e you could use:\r\n" +
//                        " * \u003cpre\u003e\r\n" +
//                        " *     attribute \u003d new Attribute ();\r\n" +
//                        " *     attribute.setName (\"label\");\r\n" +
//                        " *     attribute.setAssignment (\"\u003d\");\r\n" +
//                        " *     attribute.setValue (\"A multi word value.\");\r\n" +
//                        " *     attribute.setQuote (\u0027\"\u0027);\r\n" +
//                        " * \u003c/pre\u003e\r\n" +
//                        " * or\r\n" +
//                        " * \u003cpre\u003e\r\n" +
//                        " *     attribute \u003d new Attribute ();\r\n" +
//                        " *     attribute.setName (\"label\");\r\n" +
//                        " *     attribute.setAssignment (\"\u003d\");\r\n" +
//                        " *     attribute.setRawValue (\"A multi word value.\");\r\n" +
//                        " * \u003c/pre\u003e\r\n" +
//                        " * or\r\n" +
//                        " * \u003cpre\u003e\r\n" +
//                        " *     attribute \u003d new Attribute (\"label\", \"A multi word value.\");\r\n" +
//                        " * \u003c/pre\u003e\r\n" +
//                        " * Note that the assignment value and quoting need to be set separately when\r\n" +
//                        " * building the attribute from scratch using the properties.\r\n" +
//                        " * \u003cp\u003e\r\n" +
//                        " * \u003ctable width\u003d\"100.0%\" align\u003d\"Center\" border\u003d\"1\"\u003e\r\n" +
//                        " *   \u003ccaption\u003eValid States for Attributes.\u003c/caption\u003e\r\n" +
//                        " *   \u003ctr\u003e\r\n" +
//                        " *     \u003cth align\u003d\"Center\"\u003eDescription\u003c/th\u003e\r\n" +
//                        " *     \u003cth align\u003d\"Center\"\u003etoString()\u003c/th\u003e\r\n" +
//                        " *     \u003cth align\u003d\"Center\"\u003eName\u003c/th\u003e\r\n" +
//                        " *     \u003cth align\u003d\"Center\"\u003eAssignment\u003c/th\u003e\r\n" +
//                        " *     \u003cth align\u003d\"Center\"\u003eValue\u003c/th\u003e\r\n" +
//                        " *     \u003cth align\u003d\"Center\"\u003eQuote\u003c/th\u003e\r\n" +
//                        " *   \u003c/tr\u003e\r\n" +
//                        " *   \u003ctr\u003e\r\n" +
//                        " *     \u003ctd align\u003d\"Center\"\u003ewhitespace attribute\u003c/td\u003e\r\n" +
//                        " *     \u003ctd align\u003d\"Center\"\u003evalue\u003c/td\u003e\r\n" +
//                        " *     \u003ctd align\u003d\"Center\"\u003e\u003ccode\u003enull\u003c/code\u003e\u003c/td\u003e\r\n" +
//                        " *     \u003ctd align\u003d\"Center\"\u003e\u003ccode\u003enull\u003c/code\u003e\u003c/td\u003e\r\n" +
//                        " *     \u003ctd align\u003d\"Center\"\u003e\"value\"\u003c/td\u003e\r\n" +
//                        " *     \u003ctd align\u003d\"Center\"\u003e\u003ccode\u003e0\u003c/code\u003e\u003c/td\u003e\r\n" +
//                        " *   \u003c/tr\u003e\r\n" +
//                        " *   \u003ctr\u003e\r\n" +
//                        " *     \u003ctd align\u003d\"Center\"\u003estandalone attribute\u003c/td\u003e\r\n" +
//                        " *     \u003ctd align\u003d\"Center\"\u003ename\u003c/td\u003e\r\n" +
//                        " *     \u003ctd align\u003d\"Center\"\u003e\"name\"\u003c/td\u003e\r\n" +
//                        " *     \u003ctd align\u003d\"Center\"\u003e\u003ccode\u003enull\u003c/code\u003e\u003c/td\u003e\r\n" +
//                        " *     \u003ctd align\u003d\"Center\"\u003e\u003ccode\u003enull\u003c/code\u003e\u003c/td\u003e\r\n" +
//                        " *     \u003ctd align\u003d\"Center\"\u003e\u003ccode\u003e0\u003c/code\u003e\u003c/td\u003e\r\n" +
//                        " *   \u003c/tr\u003e\r\n" +
//                        " *   \u003ctr\u003e\r\n" +
//                        " *     \u003ctd align\u003d\"Center\"\u003eempty attribute\u003c/td\u003e\r\n" +
//                        " *     \u003ctd align\u003d\"Center\"\u003ename\u003d\u003c/td\u003e\r\n" +
//                        " *     \u003ctd align\u003d\"Center\"\u003e\"name\"\u003c/td\u003e\r\n" +
//                        " *     \u003ctd align\u003d\"Center\"\u003e\"\u003d\"\u003c/td\u003e\r\n" +
//                        " *     \u003ctd align\u003d\"Center\"\u003e\u003ccode\u003enull\u003c/code\u003e\u003c/td\u003e\r\n" +
//                        " *     \u003ctd align\u003d\"Center\"\u003e\u003ccode\u003e0\u003c/code\u003e\u003c/td\u003e\r\n" +
//                        " *   \u003c/tr\u003e\r\n" +
//                        " *   \u003ctr\u003e\r\n" +
//                        " *     \u003ctd align\u003d\"Center\"\u003eempty single quoted attribute\u003c/td\u003e\r\n" +
//                        " *     \u003ctd align\u003d\"Center\"\u003ename\u003d\u0027\u0027\u003c/td\u003e\r\n" +
//                        " *     \u003ctd align\u003d\"Center\"\u003e\"name\"\u003c/td\u003e\r\n" +
//                        " *     \u003ctd align\u003d\"Center\"\u003e\"\u003d\"\u003c/td\u003e\r\n" +
//                        " *     \u003ctd align\u003d\"Center\"\u003e\u003ccode\u003enull\u003c/code\u003e\u003c/td\u003e\r\n" +
//                        " *     \u003ctd align\u003d\"Center\"\u003e\u003ccode\u003e\u0027\u003c/code\u003e\u003c/td\u003e\r\n" +
//                        " *   \u003c/tr\u003e\r\n" +
//                        " *   \u003ctr\u003e\r\n" +
//                        " *     \u003ctd align\u003d\"Center\"\u003eempty double quoted attribute\u003c/td\u003e\r\n" +
//                        " *     \u003ctd align\u003d\"Center\"\u003ename\u003d\"\"\u003c/td\u003e\r\n" +
//                        " *     \u003ctd align\u003d\"Center\"\u003e\"name\"\u003c/td\u003e\r\n" +
//                        " *     \u003ctd align\u003d\"Center\"\u003e\"\u003d\"\u003c/td\u003e\r\n" +
//                        " *     \u003ctd align\u003d\"Center\"\u003e\u003ccode\u003enull\u003c/code\u003e\u003c/td\u003e\r\n" +
//                        " *     \u003ctd align\u003d\"Center\"\u003e\u003ccode\u003e\"\u003c/code\u003e\u003c/td\u003e\r\n" +
//                        " *   \u003c/tr\u003e\r\n" +
//                        " *   \u003ctr\u003e\r\n" +
//                        " *     \u003ctd align\u003d\"Center\"\u003enaked attribute\u003c/td\u003e\r\n" +
//                        " *     \u003ctd align\u003d\"Center\"\u003ename\u003dvalue\u003c/td\u003e\r\n" +
//                        " *     \u003ctd align\u003d\"Center\"\u003e\"name\"\u003c/td\u003e\r\n" +
//                        " *     \u003ctd align\u003d\"Center\"\u003e\"\u003d\"\u003c/td\u003e\r\n" +
//                        " *     \u003ctd align\u003d\"Center\"\u003e\"value\"\u003c/td\u003e\r\n" +
//                        " *     \u003ctd align\u003d\"Center\"\u003e\u003ccode\u003e0\u003c/code\u003e\u003c/td\u003e\r\n" +
//                        " *   \u003c/tr\u003e\r\n" +
//                        " *   \u003ctr\u003e\r\n" +
//                        " *     \u003ctd align\u003d\"Center\"\u003esingle quoted attribute\u003c/td\u003e\r\n" +
//                        " *     \u003ctd align\u003d\"Center\"\u003ename\u003d\u0027value\u0027\u003c/td\u003e\r\n" +
//                        " *     \u003ctd align\u003d\"Center\"\u003e\"name\"\u003c/td\u003e\r\n" +
//                        " *     \u003ctd align\u003d\"Center\"\u003e\"\u003d\"\u003c/td\u003e\r\n" +
//                        " *     \u003ctd align\u003d\"Center\"\u003e\"value\"\u003c/td\u003e\r\n" +
//                        " *     \u003ctd align\u003d\"Center\"\u003e\u003ccode\u003e\u0027\u003c/code\u003e\u003c/td\u003e\r\n" +
//                        " *   \u003c/tr\u003e\r\n" +
//                        " *   \u003ctr\u003e\r\n" +
//                        " *     \u003ctd align\u003d\"Center\"\u003edouble quoted attribute\u003c/td\u003e\r\n" +
//                        " *     \u003ctd align\u003d\"Center\"\u003ename\u003d\"value\"\u003c/td\u003e\r\n" +
//                        " *     \u003ctd align\u003d\"Center\"\u003e\"name\"\u003c/td\u003e\r\n" +
//                        " *     \u003ctd align\u003d\"Center\"\u003e\"\u003d\"\u003c/td\u003e\r\n" +
//                        " *     \u003ctd align\u003d\"Center\"\u003e\"value\"\u003c/td\u003e\r\n" +
//                        " *     \u003ctd align\u003d\"Center\"\u003e\u003ccode\u003e\"\u003c/code\u003e\u003c/td\u003e\r\n" +
//                        " *   \u003c/tr\u003e\r\n" +
//                        " * \u003c/table\u003e\r\n" +
//                        " * \u003cbr\u003eIn words:\r\n" +
//                        " * \u003cbr\u003eIf Name is null, and Assignment is null, and Quote is zero,\r\n" +
//                        " *   it\u0027s whitepace and Value has the whitespace text -- value\r\n" +
//                        " * \u003cbr\u003eIf Name is not null, and both Assignment and Value are null\r\n" +
//                        " *   it\u0027s a standalone attribute -- name\r\n" +
//                        " * \u003cbr\u003eIf Name is not null, and Assignment is an equals sign, and Quote is zero\r\n" +
//                        " *   it\u0027s an empty attribute -- name\u003d\r\n" +
//                        " * \u003cbr\u003eIf Name is not null, and Assignment is an equals sign,\r\n" +
//                        " *   and Value is \"\" or null, and Quote is \u0027\r\n" +
//                        " *   it\u0027s an empty single quoted attribute -- name\u003d\u0027\u0027\r\n" +
//                        " * \u003cbr\u003eIf Name is not null, and Assignment is an equals sign,\r\n" +
//                        " *   and Value is \"\" or null, and Quote is \"\r\n" +
//                        " *   it\u0027s an empty double quoted attribute -- name\u003d\"\"\r\n" +
//                        " * \u003cbr\u003eIf Name is not null, and Assignment is an equals sign,\r\n" +
//                        " *   and Value is something, and Quote is zero\r\n" +
//                        " *   it\u0027s a naked attribute -- name\u003dvalue\r\n" +
//                        " * \u003cbr\u003eIf Name is not null, and Assignment is an equals sign,\r\n" +
//                        " *   and Value is something, and Quote is \u0027\r\n" +
//                        " *   it\u0027s a single quoted attribute -- name\u003d\u0027value\u0027\r\n" +
//                        " * \u003cbr\u003eIf Name is not null, and Assignment is an equals sign,\r\n" +
//                        " *   and Value is something, and Quote is \"\r\n" +
//                        " *   it\u0027s a double quoted attribute -- name\u003d\"value\"\r\n" +
//                        " * \u003cbr\u003eAll other states are invalid HTML.\r\n" +
//                        " * \u003cp\u003e\r\n" +
//                        " * From the \u003ca href\u003d\"http://www.w3.org/TR/html4/intro/sgmltut.html#h-3.2.2\"\u003e\r\n" +
//                        " * HTML 4.01 Specification, W3C Recommendation 24 December 1999\u003c/a\u003e\r\n" +
//                        " * http://www.w3.org/TR/html4/intro/sgmltut.html#h-3.2.2:\u003cp\u003e\r\n" +
//                        " * \u003ccite\u003e\r\n" +
//                        " * 3.2.2 Attributes\u003cp\u003e\r\n" +
//                        " * Elements may have associated properties, called attributes, which may\r\n" +
//                        " * have values (by default, or set by authors or scripts). Attribute/value\r\n" +
//                        " * pairs appear before the final \"\u003e\" of an element\u0027s start tag. Any number\r\n" +
//                        " * of (legal) attribute value pairs, separated by spaces, may appear in an\r\n" +
//                        " * element\u0027s start tag. They may appear in any order.\u003cp\u003e\r\n" +
//                        " * In this example, the id attribute is set for an H1 element:\r\n" +
//                        " * \u003cpre\u003e\r\n" +
//                        " * \u003ccode\u003e\r\n" +
//                        " * {@.html\r\n" +
//                        " *  \u003cH1 id\u003d\"section1\"\u003e\r\n" +
//                        " *  This is an identified heading thanks to the id attribute\r\n" +
//                        " *  \u003c/H1\u003e}\r\n" +
//                        " * \u003c/code\u003e\r\n" +
//                        " * \u003c/pre\u003e\r\n" +
//                        " * By default, SGML requires that all attribute values be delimited using\r\n" +
//                        " * either double quotation marks (ASCII decimal 34) or single quotation\r\n" +
//                        " * marks (ASCII decimal 39). Single quote marks can be included within the\r\n" +
//                        " * attribute value when the value is delimited by double quote marks, and\r\n" +
//                        " * vice versa. Authors may also use numeric character references to\r\n" +
//                        " * represent double quotes (\u0026amp;#34;) and single quotes (\u0026amp;#39;).\r\n" +
//                        " * For doublequotes authors can also use the character entity reference\r\n" +
//                        " * \u0026amp;quot;.\u003cp\u003e\r\n" +
//                        " * In certain cases, authors may specify the value of an attribute without\r\n" +
//                        " * any quotation marks. The attribute value may only contain letters\r\n" +
//                        " * (a-z and A-Z), digits (0-9), hyphens (ASCII decimal 45),\r\n" +
//                        " * periods (ASCII decimal 46), underscores (ASCII decimal 95),\r\n" +
//                        " * and colons (ASCII decimal 58). We recommend using quotation marks even\r\n" +
//                        " * when it is possible to eliminate them.\u003cp\u003e\r\n" +
//                        " * Attribute names are always case-insensitive.\u003cp\u003e\r\n" +
//                        " * Attribute values are generally case-insensitive. The definition of each\r\n" +
//                        " * attribute in the reference manual indicates whether its value is\r\n" +
//                        " * case-insensitive.\u003cp\u003e\r\n" +
//                        " * All the attributes defined by this specification are listed in the\r\n" +
//                        " * \u003ca href\u003d\"http://www.w3.org/TR/html4/index/attributes.html\"\u003eattribute\r\n" +
//                        " * index\u003c/a\u003e.\u003cp\u003e\r\n" +
//                        " * \u003c/cite\u003e\r\n" +
//                        " * \u003cp\u003e\r\n" +
//                        " */\r\n" +
//                        "public class Attribute\r\n" +
//                        "    implements\r\n" +
//                        "        Serializable\r\n" +
//                        "{\r\n" +
//                        "    /**\r\n" +
//                        "     * The name of this attribute.\r\n" +
//                        "     * The part before the equals sign, or the stand-alone attribute.\r\n" +
//                        "     * This will be \u003ccode\u003enull\u003c/code\u003e if the attribute is whitespace.\r\n" +
//                        "     */\r\n" +
//                        "    protected String mName;\r\n" +
//                        "    /**\r\n" +
//                        "     * The assignment string of the attribute.\r\n" +
//                        "     * The equals sign.\r\n" +
//                        "     * This will be \u003ccode\u003enull\u003c/code\u003e if the attribute is a\r\n" +
//                        "     * stand-alone attribute.\r\n" +
//                        "     */\r\n" +
//                        "    protected String mAssignment;\r\n" +
//                        "    /**\r\n" +
//                        "     * The value of the attribute.\r\n" +
//                        "     * The part after the equals sign.\r\n" +
//                        "     * This will be \u003ccode\u003enull\u003c/code\u003e if the attribute is an empty or\r\n" +
//                        "     * stand-alone attribute.\r\n" +
//                        "     */\r\n" +
//                        "    protected String mValue;\r\n" +
//                        "    /**\r\n" +
//                        "     * The quote, if any, surrounding the value of the attribute, if any.\r\n" +
//                        "     * This will be zero if there are no quotes around the value.\r\n" +
//                        "     */\r\n" +
//                        "    protected char mQuote;\r\n" +
//                        "    /**\r\n" +
//                        "     * Create an attribute with the name, assignment, value and quote given.\r\n" +
//                        "     * If the quote value is zero, assigns the value using {@link #setRawValue}\r\n" +
//                        "     * which sets the quote character to a proper value if necessary.\r\n" +
//                        "     * @param name The name of this attribute.\r\n" +
//                        "     * @param assignment The assignment string of this attribute.\r\n" +
//                        "     * @param value The value of this attribute.\r\n" +
//                        "     * @param quote The quote around the value of this attribute.\r\n" +
//                        "     */\r\n" +
//                        "    public Attribute (String name, String assignment, String value, char quote)\r\n" +
//                        "    {\r\n" +
//                        "        setName (name);\r\n" +
//                        "        setAssignment (assignment);\r\n" +
//                        "        if (0 \u003d\u003d quote)\r\n" +
//                        "            setRawValue (value);\r\n" +
//                        "        else\r\n" +
//                        "        {\r\n" +
//                        "            setValue (value);\r\n" +
//                        "            setQuote (quote);\r\n" +
//                        "        }\r\n" +
//                        "    }\r\n" +
//                        "    /**\r\n" +
//                        "     * Create an attribute with the name, value and quote given.\r\n" +
//                        "     * Uses an equals sign as the assignment string if the value is not\r\n" +
//                        "     * \u003ccode\u003enull\u003c/code\u003e, and calls {@link #setRawValue} to get the\r\n" +
//                        "     * correct quoting if \u003ccode\u003equote\u003c/code\u003e is zero.\r\n" +
//                        "     * @param name The name of this attribute.\r\n" +
//                        "     * @param value The value of this attribute.\r\n" +
//                        "     * @param quote The quote around the value of this attribute.\r\n" +
//                        "     */\r\n" +
//                        "    public Attribute (String name, String value, char quote)\r\n" +
//                        "    {\r\n" +
//                        "        this (name, (null \u003d\u003d value ? \"\" : \"\u003d\"), value, quote);\r\n" +
//                        "    }\r\n" +
//                        "    /**\r\n" +
//                        "     * Create a whitespace attribute with the value given.\r\n" +
//                        "     * @param value The value of this attribute.\r\n" +
//                        "     * @exception IllegalArgumentException if the value contains other than\r\n" +
//                        "     * whitespace. To set a real value use {@link #Attribute(String,String)}.\r\n" +
//                        "     */\r\n" +
//                        "    public Attribute (String value)\r\n" +
//                        "        throws\r\n" +
//                        "            IllegalArgumentException\r\n" +
//                        "    {\r\n" +
//                        "        if (0 !\u003d value.trim ().length ())\r\n" +
//                        "            throw new IllegalArgumentException (\"non whitespace value\");\r\n" +
//                        "        else\r\n" +
//                        "        {\r\n" +
//                        "            setName (null);\r\n" +
//                        "            setAssignment (null);\r\n" +
//                        "            setValue (value);\r\n" +
//                        "            setQuote ((char)0);\r\n" +
//                        "        }\r\n" +
//                        "    }\r\n" +
//                        "    /**\r\n" +
//                        "     * Create an attribute with the name and value given.\r\n" +
//                        "     * Uses an equals sign as the assignment string if the value is not\r\n" +
//                        "     * \u003ccode\u003enull\u003c/code\u003e, and calls {@link #setRawValue} to get the\r\n" +
//                        "     * correct quoting.\r\n" +
//                        "     * @param name The name of this attribute.\r\n" +
//                        "     * @param value The value of this attribute.\r\n" +
//                        "     */\r\n" +
//                        "    public Attribute (String name, String value)\r\n" +
//                        "    {\r\n" +
//                        "        this (name, (null \u003d\u003d value ? \"\" : \"\u003d\"), value, (char)0);\r\n" +
//                        "    }\r\n" +
//                        "    /**\r\n" +
//                        "     * Create an attribute with the name, assignment string and value given.\r\n" +
//                        "     * Calls {@link #setRawValue} to get the correct quoting.\r\n" +
//                        "     * @param name The name of this attribute.\r\n" +
//                        "     * @param assignment The assignment string of this attribute.\r\n" +
//                        "     * @param value The value of this attribute.\r\n" +
//                        "     */\r\n" +
//                        "    public Attribute (String name, String assignment, String value)\r\n" +
//                        "    {\r\n" +
//                        "        this (name, assignment, value, (char)0);\r\n" +
//                        "    }\r\n" +
//                        "    /**\r\n" +
//                        "     * Create an empty attribute.\r\n" +
//                        "     * This will provide \"\" from the {@link #toString} and\r\n" +
//                        "     * {@link #toString(StringBuffer)} methods.\r\n" +
//                        "     */\r\n" +
//                        "    public Attribute ()\r\n" +
//                        "    {\r\n" +
//                        "        this (null, null, null, (char)0);\r\n" +
//                        "    }\r\n" +
//                        "    /**\r\n" +
//                        "     * Get the name of this attribute.\r\n" +
//                        "     * The part before the equals sign, or the contents of the\r\n" +
//                        "     * stand-alone attribute.\r\n" +
//                        "     * @return The name, or \u003ccode\u003enull\u003c/code\u003e if it\u0027s just a whitepace\r\n" +
//                        "     * \u0027attribute\u0027.\r\n" +
//                        "     * @see #setName\r\n" +
//                        "     */\r\n" +
//                        "    public String getName ()\r\n" +
//                        "    {\r\n" +
//                        "        return (mName);\r\n" +
//                        "    }\r\n" +
//                        "    /**\r\n" +
//                        "     * Get the name of this attribute.\r\n" +
//                        "     * @param buffer The buffer to place the name in.\r\n" +
//                        "     * @see #getName()\r\n" +
//                        "     * @see #setName\r\n" +
//                        "     */\r\n" +
//                        "    public void getName (StringBuffer buffer)\r\n" +
//                        "    {\r\n" +
//                        "        if (null !\u003d mName)\r\n" +
//                        "            buffer.append (mName);\r\n" +
//                        "    }\r\n" +
//                        "    /**\r\n" +
//                        "     * Set the name of this attribute.\r\n" +
//                        "     * Set the part before the equals sign, or the contents of the\r\n" +
//                        "     * stand-alone attribute.\r\n" +
//                        "     * \u003cem\u003eWARNING:\u003c/em\u003e Setting this to \u003ccode\u003enull\u003c/code\u003e can result in\r\n" +
//                        "     * malformed HTML if the assignment string is not \u003ccode\u003enull\u003c/code\u003e.\r\n" +
//                        "     * @param name The new name.\r\n" +
//                        "     * @see #getName\r\n" +
//                        "     * @see #getName(StringBuffer)\r\n" +
//                        "     */\r\n" +
//                        "    public void setName (String name)\r\n" +
//                        "    {\r\n" +
//                        "        mName \u003d name;\r\n" +
//                        "    }\r\n" +
//                        "    /**\r\n" +
//                        "     * Get the assignment string of this attribute.\r\n" +
//                        "     * This is usually just an equals sign, but in poorly formed attributes it\r\n" +
//                        "     * can include whitespace on either or both sides of an equals sign.\r\n" +
//                        "     * @return The assignment string.\r\n" +
//                        "     * @see #setAssignment\r\n" +
//                        "     */\r\n" +
//                        "    public String getAssignment ()\r\n" +
//                        "    {\r\n" +
//                        "        return (mAssignment);\r\n" +
//                        "    }\r\n" +
//                        "    /**\r\n" +
//                        "     * Get the assignment string of this attribute.\r\n" +
//                        "     * @param buffer The buffer to place the assignment string in.\r\n" +
//                        "     * @see #getAssignment()\r\n" +
//                        "     * @see #setAssignment\r\n" +
//                        "     */\r\n" +
//                        "    public void getAssignment (StringBuffer buffer)\r\n" +
//                        "    {\r\n" +
//                        "        if (null !\u003d mAssignment)\r\n" +
//                        "            buffer.append (mAssignment);\r\n" +
//                        "    }\r\n" +
//                        "    /**\r\n" +
//                        "     * Set the assignment string of this attribute.\r\n" +
//                        "     * \u003cem\u003eWARNING:\u003c/em\u003e Setting this property to other than an equals sign\r\n" +
//                        "     * or \u003ccode\u003enull\u003c/code\u003e will result in malformed HTML. In the case of a\r\n" +
//                        "     * \u003ccode\u003enull\u003c/code\u003e, the {@link  #setValue value} should also be set to\r\n" +
//                        "     * \u003ccode\u003enull\u003c/code\u003e.\r\n" +
//                        "     * @param assignment The new assignment string.\r\n" +
//                        "     * @see #getAssignment\r\n" +
//                        "     * @see #getAssignment(StringBuffer)\r\n" +
//                        "     */\r\n" +
//                        "    public void setAssignment (String assignment)\r\n" +
//                        "    {\r\n" +
//                        "        mAssignment \u003d assignment;\r\n" +
//                        "    }\r\n" +
//                        "    /**\r\n" +
//                        "     * Get the value of the attribute.\r\n" +
//                        "     * The part after the equals sign, or the text if it\u0027s just a whitepace\r\n" +
//                        "     * \u0027attribute\u0027.\r\n" +
//                        "     * \u003cem\u003eNOTE:\u003c/em\u003e This does not include any quotes that may have enclosed\r\n" +
//                        "     * the value when it was read. To get the un-stripped value use\r\n" +
//                        "     * {@link  #getRawValue}.\r\n" +
//                        "     * @return The value, or \u003ccode\u003enull\u003c/code\u003e if it\u0027s a stand-alone or\r\n" +
//                        "     * empty attribute, or the text if it\u0027s just a whitepace \u0027attribute\u0027.\r\n" +
//                        "     * @see #setValue\r\n" +
//                        "     */\r\n" +
//                        "    public String getValue ()\r\n" +
//                        "    {\r\n" +
//                        "        return (mValue);\r\n" +
//                        "    }\r\n" +
//                        "    /**\r\n" +
//                        "     * Get the value of the attribute.\r\n" +
//                        "     * @param buffer The buffer to place the value in.\r\n" +
//                        "     * @see #getValue()\r\n" +
//                        "     * @see #setValue\r\n" +
//                        "     */\r\n" +
//                        "    public void getValue (StringBuffer buffer)\r\n" +
//                        "    {\r\n" +
//                        "        if (null !\u003d mValue)\r\n" +
//                        "            buffer.append (mValue);\r\n" +
//                        "    }\r\n" +
//                        "    /**\r\n" +
//                        "     * Set the value of the attribute.\r\n" +
//                        "     * The part after the equals sign, or the text if it\u0027s a whitepace\r\n" +
//                        "     * \u0027attribute\u0027.\r\n" +
//                        "     * \u003cem\u003eWARNING:\u003c/em\u003e Setting this property to a value that needs to be\r\n" +
//                        "     * quoted without also setting the quote character will result in malformed\r\n" +
//                        "     * HTML.\r\n" +
//                        "     * @param value The new value.\r\n" +
//                        "     * @see #getValue\r\n" +
//                        "     * @see #getValue(StringBuffer)\r\n" +
//                        "     */\r\n" +
//                        "    public void setValue (String value)\r\n" +
//                        "    {\r\n" +
//                        "        mValue \u003d value;\r\n" +
//                        "    }\r\n" +
//                        "    /**\r\n" +
//                        "     * Get the quote, if any, surrounding the value of the attribute, if any.\r\n" +
//                        "     * @return Either \u0027 or \" if the attribute value was quoted, or zero\r\n" +
//                        "     * if there are no quotes around it.\r\n" +
//                        "     * @see #setQuote\r\n" +
//                        "     */\r\n" +
//                        "    public char getQuote ()\r\n" +
//                        "    {\r\n" +
//                        "        return (mQuote);\r\n" +
//                        "    }\r\n" +
//                        "    /**\r\n" +
//                        "     * Get the quote, if any, surrounding the value of the attribute, if any.\r\n" +
//                        "     * @param buffer The buffer to place the quote in.\r\n" +
//                        "     * @see #getQuote()\r\n" +
//                        "     * @see #setQuote\r\n" +
//                        "     */\r\n" +
//                        "    public void getQuote (StringBuffer buffer)\r\n" +
//                        "    {\r\n" +
//                        "        if (0 !\u003d mQuote)\r\n" +
//                        "            buffer.append (mQuote);\r\n" +
//                        "    }\r\n" +
//                        "    /**\r\n" +
//                        "     * Set the quote surrounding the value of the attribute.\r\n" +
//                        "     * \u003cem\u003eWARNING:\u003c/em\u003e Setting this property to zero will result in malformed\r\n" +
//                        "     * HTML if the {@link  #getValue value} needs to be quoted (i.e. contains\r\n" +
//                        "     * whitespace).\r\n" +
//                        "     * @param quote The new quote value.\r\n" +
//                        "     * @see #getQuote\r\n" +
//                        "     * @see #getQuote(StringBuffer)\r\n" +
//                        "     */\r\n" +
//                        "    public void setQuote (char quote)\r\n" +
//                        "    {\r\n" +
//                        "        mQuote \u003d quote;\r\n" +
//                        "    }\r\n" +
//                        "    /**\r\n" +
//                        "     * Get the raw value of the attribute.\r\n" +
//                        "     * The part after the equals sign, or the text if it\u0027s just a whitepace\r\n" +
//                        "     * \u0027attribute\u0027. This includes the quotes around the value if any.\r\n" +
//                        "     * @return The value, or \u003ccode\u003enull\u003c/code\u003e if it\u0027s a stand-alone attribute,\r\n" +
//                        "     * or the text if it\u0027s just a whitepace \u0027attribute\u0027.\r\n" +
//                        "     * @see #setRawValue\r\n" +
//                        "     */\r\n" +
//                        "    public String getRawValue ()\r\n" +
//                        "    {\r\n" +
//                        "        char quote;\r\n" +
//                        "        StringBuffer buffer;\r\n" +
//                        "        String ret;\r\n" +
//                        "        if (isValued ())\r\n" +
//                        "        {\r\n" +
//                        "            quote \u003d getQuote ();\r\n" +
//                        "            if (0 !\u003d quote)\r\n" +
//                        "            {\r\n" +
//                        "                buffer \u003d new StringBuffer (); // todo: what is the value length?\r\n" +
//                        "                buffer.append (quote);\r\n" +
//                        "                getValue (buffer);\r\n" +
//                        "                buffer.append (quote);\r\n" +
//                        "                ret \u003d buffer.toString ();\r\n" +
//                        "            }\r\n" +
//                        "            else\r\n" +
//                        "                ret \u003d getValue ();\r\n" +
//                        "        }\r\n" +
//                        "        else\r\n" +
//                        "            ret \u003d null;\r\n" +
//                        "        return (ret);\r\n" +
//                        "    }\r\n" +
//                        "    /**\r\n" +
//                        "     * Get the raw value of the attribute.\r\n" +
//                        "     * The part after the equals sign, or the text if it\u0027s just a whitepace\r\n" +
//                        "     * \u0027attribute\u0027. This includes the quotes around the value if any.\r\n" +
//                        "     * @param buffer The string buffer to append the attribute value to.\r\n" +
//                        "     * @see #getRawValue()\r\n" +
//                        "     * @see #setRawValue\r\n" +
//                        "     */\r\n" +
//                        "    public void getRawValue (StringBuffer buffer)\r\n" +
//                        "    {\r\n" +
//                        "        getQuote (buffer);\r\n" +
//                        "        getValue (buffer);\r\n" +
//                        "        getQuote (buffer);\r\n" +
//                        "    }\r\n" +
//                        "    /**\r\n" +
//                        "     * Set the value of the attribute and the quote character.\r\n" +
//                        "     * If the value is pure whitespace, assign it \u0027as is\u0027 and reset the\r\n" +
//                        "     * quote character. If not, check for leading and trailing double or\r\n" +
//                        "     * single quotes, and if found use this as the quote character and\r\n" +
//                        "     * the inner contents of \u003ccode\u003evalue\u003c/code\u003e as the real value.\r\n" +
//                        "     * Otherwise, examine the string to determine if quotes are needed\r\n" +
//                        "     * and an appropriate quote character if so. This may involve changing\r\n" +
//                        "     * double quotes within the string to character references.\r\n" +
//                        "     * @param value The new value.\r\n" +
//                        "     * @see #getRawValue\r\n" +
//                        "     * @see #getRawValue(StringBuffer)\r\n" +
//                        "     */\r\n" +
//                        "    public void setRawValue (String value)\r\n" +
//                        "    {\r\n" +
//                        "        char ch;\r\n" +
//                        "        boolean needed;\r\n" +
//                        "        boolean singleq;\r\n" +
//                        "        boolean doubleq;\r\n" +
//                        "        String ref;\r\n" +
//                        "        StringBuffer buffer;\r\n" +
//                        "        char quote;\r\n" +
//                        "        quote \u003d 0;\r\n" +
//                        "        if ((null !\u003d value) \u0026\u0026 (0 !\u003d value.trim ().length ()))\r\n" +
//                        "        {\r\n" +
//                        "            if (value.startsWith (\"\u0027\") \u0026\u0026 value.endsWith (\"\u0027\")\r\n" +
//                        "                \u0026\u0026 (2 \u003c\u003d value.length ()))\r\n" +
//                        "            {\r\n" +
//                        "                quote \u003d \u0027\\\u0027\u0027;\r\n" +
//                        "                value \u003d value.substring (1, value.length () - 1);\r\n" +
//                        "            }\r\n" +
//                        "            else if (value.startsWith (\"\\\"\") \u0026\u0026 value.endsWith (\"\\\"\")\r\n" +
//                        "                \u0026\u0026 (2 \u003c\u003d value.length ()))\r\n" +
//                        "            {\r\n" +
//                        "                quote \u003d \u0027\"\u0027;\r\n" +
//                        "                value \u003d value.substring (1, value.length () - 1);\r\n" +
//                        "            }\r\n" +
//                        "            else\r\n" +
//                        "            {\r\n" +
//                        "                // first determine if there\u0027s whitespace in the value\r\n" +
//                        "                // and while we\u0027re at it find a suitable quote character\r\n" +
//                        "                needed \u003d false;\r\n" +
//                        "                singleq \u003d true;\r\n" +
//                        "                doubleq \u003d true;\r\n" +
//                        "                for (int i \u003d 0; i \u003c value.length (); i++)\r\n" +
//                        "                {\r\n" +
//                        "                    ch \u003d value.charAt (i);\r\n" +
//                        "                    if (\u0027\\\u0027\u0027 \u003d\u003d ch)\r\n" +
//                        "                    {\r\n" +
//                        "                        singleq  \u003d false;\r\n" +
//                        "                        needed \u003d true;\r\n" +
//                        "                    }\r\n" +
//                        "                    else if (\u0027\"\u0027 \u003d\u003d ch)\r\n" +
//                        "                    {\r\n" +
//                        "                        doubleq \u003d false;\r\n" +
//                        "                        needed \u003d true;\r\n" +
//                        "                    }\r\n" +
//                        "                    else if (!(\u0027-\u0027 \u003d\u003d ch) \u0026\u0026 !(\u0027.\u0027 \u003d\u003d ch) \u0026\u0026 !(\u0027_\u0027 \u003d\u003d ch)\r\n" +
//                        "                       \u0026\u0026 !(\u0027:\u0027 \u003d\u003d ch) \u0026\u0026 !Character.isLetterOrDigit (ch))\r\n" +
//                        "                    {\r\n" +
//                        "                        needed \u003d true;\r\n" +
//                        "                    }\r\n" +
//                        "                }\r\n" +
//                        "                // now apply quoting\r\n" +
//                        "                if (needed)\r\n" +
//                        "                {\r\n" +
//                        "                    if (doubleq)\r\n" +
//                        "                        quote \u003d \u0027\"\u0027;\r\n" +
//                        "                    else if (singleq)\r\n" +
//                        "                        quote \u003d \u0027\\\u0027\u0027;\r\n" +
//                        "                    else\r\n" +
//                        "                    {\r\n" +
//                        "                        // uh-oh, we need to convert some quotes into character\r\n" +
//                        "                        // references, so convert all double quotes into \u0026#34;\r\n" +
//                        "                        quote \u003d \u0027\"\u0027;\r\n" +
//                        "                        ref \u003d \"\u0026quot;\"; // Translate.encode (quote);\r\n" +
//                        "                        // JDK 1.4: value \u003d value.replaceAll (\"\\\"\", ref);\r\n" +
//                        "                        buffer \u003d new StringBuffer (\r\n" +
//                        "                                value.length() * (ref.length () - 1));\r\n" +
//                        "                        for (int i \u003d 0; i \u003c value.length (); i++)\r\n" +
//                        "                        {\r\n" +
//                        "                            ch \u003d value.charAt (i);\r\n" +
//                        "                            if (quote \u003d\u003d ch)\r\n" +
//                        "                                buffer.append (ref);\r\n" +
//                        "                            else\r\n" +
//                        "                                buffer.append (ch);\r\n" +
//                        "                        }\r\n" +
//                        "                        value \u003d buffer.toString ();\r\n" +
//                        "                    }\r\n" +
//                        "                }\r\n" +
//                        "            }\r\n" +
//                        "        }\r\n" +
//                        "        setValue (value);\r\n" +
//                        "        setQuote (quote);\r\n" +
//                        "    }\r\n" +
//                        "    /**\r\n" +
//                        "     * Predicate to determine if this attribute is whitespace.\r\n" +
//                        "     * @return \u003ccode\u003etrue\u003c/code\u003e if this attribute is whitespace,\r\n" +
//                        "     * \u003ccode\u003efalse\u003c/code\u003e if it is a real attribute.\r\n" +
//                        "     */\r\n" +
//                        "    public boolean isWhitespace ()\r\n" +
//                        "    {\r\n" +
//                        "        return (null \u003d\u003d getName ());\r\n" +
//                        "    }\r\n" +
//                        "    /**\r\n" +
//                        "     * Predicate to determine if this attribute has no equals sign (or value).\r\n" +
//                        "     * @return \u003ccode\u003etrue\u003c/code\u003e if this attribute is a standalone attribute.\r\n" +
//                        "     * \u003ccode\u003efalse\u003c/code\u003e if has an equals sign.\r\n" +
//                        "     */\r\n" +
//                        "    public boolean isStandAlone ()\r\n" +
//                        "    {\r\n" +
//                        "        return ((null !\u003d getName ()) \u0026\u0026 (null \u003d\u003d getAssignment ()));\r\n" +
//                        "    }\r\n" +
//                        "    /**\r\n" +
//                        "     * Predicate to determine if this attribute has an equals sign but no value.\r\n" +
//                        "     * @return \u003ccode\u003etrue\u003c/code\u003e if this attribute is an empty attribute.\r\n" +
//                        "     * \u003ccode\u003efalse\u003c/code\u003e if has an equals sign and a value.\r\n" +
//                        "     */\r\n" +
//                        "    public boolean isEmpty ()\r\n" +
//                        "    {\r\n" +
//                        "        return ((null !\u003d getAssignment ()) \u0026\u0026 (null \u003d\u003d getValue ()));\r\n" +
//                        "    }\r\n" +
//                        "    /**\r\n" +
//                        "     * Predicate to determine if this attribute has a value.\r\n" +
//                        "     * @return \u003ccode\u003etrue\u003c/code\u003e if this attribute has a value.\r\n" +
//                        "     * \u003ccode\u003efalse\u003c/code\u003e if it is empty or standalone.\r\n" +
//                        "     */\r\n" +
//                        "    public boolean isValued ()\r\n" +
//                        "    {\r\n" +
//                        "        return (null !\u003d getValue ());\r\n" +
//                        "    }\r\n" +
//                        "    /**\r\n" +
//                        "     * Get the length of the string value of this attribute.\r\n" +
//                        "     * @return The number of characters required to express this attribute.\r\n" +
//                        "     */\r\n" +
//                        "    public int getLength ()\r\n" +
//                        "    {\r\n" +
//                        "        String name;\r\n" +
//                        "        String assignment;\r\n" +
//                        "        String value;\r\n" +
//                        "        char quote;\r\n" +
//                        "        int ret;\r\n" +
//                        "        ret \u003d 0;\r\n" +
//                        "        name \u003d getName ();\r\n" +
//                        "        if (null !\u003d name)\r\n" +
//                        "            ret +\u003d name.length ();\r\n" +
//                        "        assignment \u003d getAssignment ();\r\n" +
//                        "        if (null !\u003d assignment)\r\n" +
//                        "            ret +\u003d assignment.length ();\r\n" +
//                        "        value \u003d getValue ();\r\n" +
//                        "        if (null !\u003d value)\r\n" +
//                        "            ret +\u003d value.length ();\r\n" +
//                        "        quote \u003d getQuote ();\r\n" +
//                        "        if (0 !\u003d quote)\r\n" +
//                        "            ret +\u003d 2;\r\n" +
//                        "        return (ret);\r\n" +
//                        "    }\r\n" +
//                        "    /**\r\n" +
//                        "     * Get a text representation of this attribute.\r\n" +
//                        "     * Suitable for insertion into a tag, the output is one of\r\n" +
//                        "     * the forms:\r\n" +
//                        "     * \u003ccode\u003e\r\n" +
//                        "     * \u003cpre\u003e\r\n" +
//                        "     * value\r\n" +
//                        "     * name\r\n" +
//                        "     * name\u003d\r\n" +
//                        "     * name\u003dvalue\r\n" +
//                        "     * name\u003d\u0027value\u0027\r\n" +
//                        "     * name\u003d\"value\"\r\n" +
//                        "     * \u003c/pre\u003e\r\n" +
//                        "     * \u003c/code\u003e\r\n" +
//                        "     * @return A string that can be used within a tag.\r\n" +
//                        "     */\r\n" +
//                        "    public String toString ()\r\n" +
//                        "    {\r\n" +
//                        "        int length;\r\n" +
//                        "        StringBuffer ret;\r\n" +
//                        "        // get the size to avoid extra StringBuffer allocations\r\n" +
//                        "        length \u003d getLength ();\r\n" +
//                        "        ret \u003d new StringBuffer (length);\r\n" +
//                        "        toString (ret);\r\n" +
//                        "        return (ret.toString ());\r\n" +
//                        "    }\r\n" +
//                        "    /**\r\n" +
//                        "     * Get a text representation of this attribute.\r\n" +
//                        "     * @param buffer The accumulator for placing the text into.\r\n" +
//                        "     * @see #toString()\r\n" +
//                        "     */\r\n" +
//                        "    public void toString (StringBuffer buffer)\r\n" +
//                        "    {\r\n" +
//                        "        getName (buffer);\r\n" +
//                        "        getAssignment (buffer);\r\n" +
//                        "        getRawValue (buffer);\r\n" +
//                        "    }\r\n" +
//                        "}\r";
//
//        hh = Header.getSrcHeader( hh );
//
////        Matcher matcher = ContentToLicense.page.matcher( hh );
////        if ( matcher.find() ) {
////            System.out.println( matcher.group( "addr" ));
////        }
//
//        Matcher matcher = ContentToLicense.page.matcher( "//   http://opensource.org/licenses/cpl1.0.php\r\n" );
//        assertThat( matcher.find() ).isTrue();
//
//        System.out.println( ctl.byUrl( hh, "testing" ) );
//        assertThat( ctl.byUrl( hh, "testing" ).isPresent() ).isTrue();
//
//    }
//
//    @Test
//    public void cddlLiTest() {
//        String in = " * https://glassfish.dev.java.net/public/CDDLv1.0.html.\n";
//
//        Matcher matcher = ContentToLicense.page.matcher( in );
//        assertThat( matcher.find() ).isTrue();
//        System.out.println( matcher.group( "addr" ) );
//
//        assertThat( ctl.byUrl( in, "testing" ).isPresent() ).isTrue();
//    }
//
//    @Test
//    public void apache1Huh() {
//        String in = "/* \u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\n" +
//                " * The Apache Software License, Version 1.1\n" +
//                " *\n" +
//                " * Copyright (c) 2000-2002 The Apache Software Foundation.  All rights\n" +
//                " * reserved.\n" +
//                " *\n" +
//                " * Redistribution and use in source and binary forms, with or without\n" +
//                " * modification, are permitted provided that the following conditions\n" +
//                " * are met:\n" +
//                " *\n" +
//                " * 1. Redistributions of source code must retain the above copyright\n" +
//                " *    notice, this list of conditions and the following disclaimer.\n" +
//                " *\n" +
//                " * 2. Redistributions in binary form must reproduce the above copyright\n" +
//                " *    notice, this list of conditions and the following disclaimer in\n" +
//                " *    the documentation and/or other materials provided with the\n" +
//                " *    distribution.\n" +
//                " *\n" +
//                " * 3. The end-user documentation included with the redistribution,\n" +
//                " *    if any, must include the following acknowledgment:\n" +
//                " *       \"This product includes software developed by the\n" +
//                " *        Apache Software Foundation (http://www.apache.org/).\"\n" +
//                " *    Alternately, this acknowledgment may appear in the software itself,\n" +
//                " *    if and wherever such third-party acknowledgments normally appear.\n" +
//                " *\n" +
//                " * 4. The names \"Apache\" and \"Apache Software Foundation\", \"Jakarta-Oro\" \n" +
//                " *    must not be used to endorse or promote products derived from this\n" +
//                " *    software without prior written permission. For written\n" +
//                " *    permission, please contact apache@apache.org.\n" +
//                " *\n" +
//                " * 5. Products derived from this software may not be called \"Apache\" \n" +
//                " *    or \"Jakarta-Oro\", nor may \"Apache\" or \"Jakarta-Oro\" appear in their \n" +
//                " *    name, without prior written permission of the Apache Software Foundation.\n" +
//                " *\n" +
//                " * THIS SOFTWARE IS PROVIDED ``AS IS\u0027\u0027 AND ANY EXPRESSED OR IMPLIED\n" +
//                " * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES\n" +
//                " * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE\n" +
//                " * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR\n" +
//                " * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,\n" +
//                " * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT\n" +
//                " * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF\n" +
//                " * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND\n" +
//                " * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,\n" +
//                " * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT\n" +
//                " * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF\n" +
//                " * SUCH DAMAGE.\n" +
//                " * \u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\n" +
//                " *\n" +
//                " * This software consists of voluntary contributions made by many\n" +
//                " * individuals on behalf of the Apache Software Foundation.  For more\n" +
//                " * information on the Apache Software Foundation, please see\n" +
//                " * \u003chttp://www.apache.org/\u003e.\n" +
//                " */\n";
//    }
//
//
//
////    @Test
////    public void ap2Test1() {
////
//////        System.out.println( new LicenseFromText( lOracle ).firstDiff( lots ));
////
//////        System.out.println( new LicenseFromText( lOracle ).firstDiff( lots ));
////
////        assertThat( new LicenseFromText( lOracle, new FindingsDummy() ).getLicense( lots ).isPresent() ).isTrue();
////
////    }
//
////    public static final Log dummy = new Log() {
////        @Override
////        public boolean isDebugEnabled() {
////            return false;
////        }
////
////        @Override
////        public void debug( CharSequence content ) {
////
////        }
////
////        @Override
////        public void debug( CharSequence content, Throwable error ) {
////
////        }
////
////        @Override
////        public void debug( Throwable error ) {
////
////        }
////
////        @Override
////        public boolean isInfoEnabled() {
////            return false;
////        }
////
////        @Override
////        public void info( CharSequence content ) {
////
////        }
////
////        @Override
////        public void info( CharSequence content, Throwable error ) {
////
////        }
////
////        @Override
////        public void info( Throwable error ) {
////
////        }
////
////        @Override
////        public boolean isWarnEnabled() {
////            return false;
////        }
////
////        @Override
////        public void warn( CharSequence content ) {
////
////        }
////
////        @Override
////        public void warn( CharSequence content, Throwable error ) {
////
////        }
////
////        @Override
////        public void warn( Throwable error ) {
////
////        }
////
////        @Override
////        public boolean isErrorEnabled() {
////            return false;
////        }
////
////        @Override
////        public void error( CharSequence content ) {
////
////        }
////
////        @Override
////        public void error( CharSequence content, Throwable error ) {
////
////        }
////
////        @Override
////        public void error( Throwable error ) {
////
////        }
////    };
//
//
//    private static String lots = "\n" +
//            "                                 Apache License\n" +
//            "                           Version 2.0, January 2004\n" +
//            "                        http://www.apache.org/licenses/\n" +
//            "\n" +
//            "   TERMS AND CONDITIONS FOR USE, REPRODUCTION, AND DISTRIBUTION\n" +
//            "\n" +
//            "   1. Definitions.\n" +
//            "\n" +
//            "      \"License\" shall mean the terms and conditions for use, reproduction,\n" +
//            "      and distribution as defined by Sections 1 through 9 of this document.\n" +
//            "\n" +
//            "      \"Licensor\" shall mean the copyright owner or entity authorized by\n" +
//            "      the copyright owner that is granting the License.\n" +
//            "\n" +
//            "      \"Legal Entity\" shall mean the union of the acting entity and all\n" +
//            "      other entities that control, are controlled by, or are under common\n" +
//            "      control with that entity. For the purposes of this definition,\n" +
//            "      \"control\" means (i) the power, direct or indirect, to cause the\n" +
//            "      direction or management of such entity, whether by contract or\n" +
//            "      otherwise, or (ii) ownership of fifty percent (50%) or more of the\n" +
//            "      outstanding shares, or (iii) beneficial ownership of such entity.\n" +
//            "\n" +
//            "      \"You\" (or \"Your\") shall mean an individual or Legal Entity\n" +
//            "      exercising permissions granted by this License.\n" +
//            "\n" +
//            "      \"Source\" form shall mean the preferred form for making modifications,\n" +
//            "      including but not limited to software source code, documentation\n" +
//            "      source, and configuration files.\n" +
//            "\n" +
//            "      \"Object\" form shall mean any form resulting from mechanical\n" +
//            "      transformation or translation of a Source form, including but\n" +
//            "      not limited to compiled object code, generated documentation,\n" +
//            "      and conversions to other media types.\n" +
//            "\n" +
//            "      \"Work\" shall mean the work of authorship, whether in Source or\n" +
//            "      Object form, made available under the License, as indicated by a\n" +
//            "      copyright notice that is included in or attached to the work\n" +
//            "      (an example is provided in the Appendix below).\n" +
//            "\n" +
//            "      \"Derivative Works\" shall mean any work, whether in Source or Object\n" +
//            "      form, that is based on (or derived from) the Work and for which the\n" +
//            "      editorial revisions, annotations, elaborations, or other modifications\n" +
//            "      represent, as a whole, an original work of authorship. For the purposes\n" +
//            "      of this License, Derivative Works shall not include works that remain\n" +
//            "      separable from, or merely link (or bind by name) to the interfaces of,\n" +
//            "      the Work and Derivative Works thereof.\n" +
//            "\n" +
//            "      \"Contribution\" shall mean any work of authorship, including\n" +
//            "      the original version of the Work and any modifications or additions\n" +
//            "      to that Work or Derivative Works thereof, that is intentionally\n" +
//            "      submitted to Licensor for inclusion in the Work by the copyright owner\n" +
//            "      or by an individual or Legal Entity authorized to submit on behalf of\n" +
//            "      the copyright owner. For the purposes of this definition, \"submitted\"\n" +
//            "      means any form of electronic, verbal, or written communication sent\n" +
//            "      to the Licensor or its representatives, including but not limited to\n" +
//            "      communication on electronic mailing lists, source code control systems,\n" +
//            "      and issue tracking systems that are managed by, or on behalf of, the\n" +
//            "      Licensor for the purpose of discussing and improving the Work, but\n" +
//            "      excluding communication that is conspicuously marked or otherwise\n" +
//            "      designated in writing by the copyright owner as \"Not a Contribution.\"\n" +
//            "\n" +
//            "      \"Contributor\" shall mean Licensor and any individual or Legal Entity\n" +
//            "      on behalf of whom a Contribution has been received by Licensor and\n" +
//            "      subsequently incorporated within the Work.\n" +
//            "\n" +
//            "   2. Grant of Copyright License. Subject to the terms and conditions of\n" +
//            "      this License, each Contributor hereby grants to You a perpetual,\n" +
//            "      worldwide, non-exclusive, no-charge, royalty-free, irrevocable\n" +
//            "      copyright license to reproduce, prepare Derivative Works of,\n" +
//            "      publicly display, publicly perform, sublicense, and distribute the\n" +
//            "      Work and such Derivative Works in Source or Object form.\n" +
//            "\n" +
//            "   3. Grant of Patent License. Subject to the terms and conditions of\n" +
//            "      this License, each Contributor hereby grants to You a perpetual,\n" +
//            "      worldwide, non-exclusive, no-charge, royalty-free, irrevocable\n" +
//            "      (except as stated in this section) patent license to make, have made,\n" +
//            "      use, offer to sell, sell, import, and otherwise transfer the Work,\n" +
//            "      where such license applies only to those patent claims licensable\n" +
//            "      by such Contributor that are necessarily infringed by their\n" +
//            "      Contribution(s) alone or by combination of their Contribution(s)\n" +
//            "      with the Work to which such Contribution(s) was submitted. If You\n" +
//            "      institute patent litigation against any entity (including a\n" +
//            "      cross-claim or counterclaim in a lawsuit) alleging that the Work\n" +
//            "      or a Contribution incorporated within the Work constitutes direct\n" +
//            "      or contributory patent infringement, then any patent licenses\n" +
//            "      granted to You under this License for that Work shall terminate\n" +
//            "      as of the date such litigation is filed.\n" +
//            "\n" +
//            "   4. Redistribution. You may reproduce and distribute copies of the\n" +
//            "      Work or Derivative Works thereof in any medium, with or without\n" +
//            "      modifications, and in Source or Object form, provided that You\n" +
//            "      meet the following conditions:\n" +
//            "\n" +
//            "      (a) You must give any other recipients of the Work or\n" +
//            "          Derivative Works a copy of this License; and\n" +
//            "\n" +
//            "      (b) You must cause any modified files to carry prominent notices\n" +
//            "          stating that You changed the files; and\n" +
//            "\n" +
//            "      (c) You must retain, in the Source form of any Derivative Works\n" +
//            "          that You distribute, all copyright, patent, trademark, and\n" +
//            "          attribution notices from the Source form of the Work,\n" +
//            "          excluding those notices that do not pertain to any part of\n" +
//            "          the Derivative Works; and\n" +
//            "\n" +
//            "      (d) If the Work includes a \"NOTICE\" text file as part of its\n" +
//            "          distribution, then any Derivative Works that You distribute must\n" +
//            "          include a readable copy of the attribution notices contained\n" +
//            "          within such NOTICE file, excluding those notices that do not\n" +
//            "          pertain to any part of the Derivative Works, in at least one\n" +
//            "          of the following places: within a NOTICE text file distributed\n" +
//            "          as part of the Derivative Works; within the Source form or\n" +
//            "          documentation, if provided along with the Derivative Works; or,\n" +
//            "          within a display generated by the Derivative Works, if and\n" +
//            "          wherever such third-party notices normally appear. The contents\n" +
//            "          of the NOTICE file are for informational purposes only and\n" +
//            "          do not modify the License. You may add Your own attribution\n" +
//            "          notices within Derivative Works that You distribute, alongside\n" +
//            "          or as an addendum to the NOTICE text from the Work, provided\n" +
//            "          that such additional attribution notices cannot be construed\n" +
//            "          as modifying the License.\n" +
//            "\n" +
//            "      You may add Your own copyright statement to Your modifications and\n" +
//            "      may provide additional or different license terms and conditions\n" +
//            "      for use, reproduction, or distribution of Your modifications, or\n" +
//            "      for any such Derivative Works as a whole, provided Your use,\n" +
//            "      reproduction, and distribution of the Work otherwise complies with\n" +
//            "      the conditions stated in this License.\n" +
//            "\n" +
//            "   5. Submission of Contributions. Unless You explicitly state otherwise,\n" +
//            "      any Contribution intentionally submitted for inclusion in the Work\n" +
//            "      by You to the Licensor shall be under the terms and conditions of\n" +
//            "      this License, without any additional terms or conditions.\n" +
//            "      Notwithstanding the above, nothing herein shall supersede or modify\n" +
//            "      the terms of any separate license agreement you may have executed\n" +
//            "      with Licensor regarding such Contributions.\n" +
//            "\n" +
//            "   6. Trademarks. This License does not grant permission to use the trade\n" +
//            "      names, trademarks, service marks, or product names of the Licensor,\n" +
//            "      except as required for reasonable and customary use in describing the\n" +
//            "      origin of the Work and reproducing the content of the NOTICE file.\n" +
//            "\n" +
//            "   7. Disclaimer of Warranty. Unless required by applicable law or\n" +
//            "      agreed to in writing, Licensor provides the Work (and each\n" +
//            "      Contributor provides its Contributions) on an \"AS IS\" BASIS,\n" +
//            "      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or\n" +
//            "      implied, including, without limitation, any warranties or conditions\n" +
//            "      of TITLE, NON-INFRINGEMENT, MERCHANTABILITY, or FITNESS FOR A\n" +
//            "      PARTICULAR PURPOSE. You are solely responsible for determining the\n" +
//            "      appropriateness of using or redistributing the Work and assume any\n" +
//            "      risks associated with Your exercise of permissions under this License.\n" +
//            "\n" +
//            "   8. Limitation of Liability. In no event and under no legal theory,\n" +
//            "      whether in tort (including negligence), contract, or otherwise,\n" +
//            "      unless required by applicable law (such as deliberate and grossly\n" +
//            "      negligent acts) or agreed to in writing, shall any Contributor be\n" +
//            "      liable to You for damages, including any direct, indirect, special,\n" +
//            "      incidental, or consequential damages of any character arising as a\n" +
//            "      result of this License or out of the use or inability to use the\n" +
//            "      Work (including but not limited to damages for loss of goodwill,\n" +
//            "      work stoppage, computer failure or malfunction, or any and all\n" +
//            "      other commercial damages or losses), even if such Contributor\n" +
//            "      has been advised of the possibility of such damages.\n" +
//            "\n" +
//            "   9. Accepting Warranty or Additional Liability. While redistributing\n" +
//            "      the Work or Derivative Works thereof, You may choose to offer,\n" +
//            "      and charge a fee for, acceptance of support, warranty, indemnity,\n" +
//            "      or other liability obligations and/or rights consistent with this\n" +
//            "      License. However, in accepting such obligations, You may act only\n" +
//            "      on Your own behalf and on Your sole responsibility, not on behalf\n" +
//            "      of any other Contributor, and only if You agree to indemnify,\n" +
//            "      defend, and hold each Contributor harmless for any liability\n" +
//            "      incurred by, or claims asserted against, such Contributor by reason\n" +
//            "      of your accepting any such warranty or additional liability.\n" +
//            "\n" +
//            "   END OF TERMS AND CONDITIONS\n" +
//            "\n" +
//            "   APPENDIX: How to apply the Apache License to your work.\n" +
//            "\n" +
//            "      To apply the Apache License to your work, attach the following\n" +
//            "      boilerplate notice, with the fields enclosed by brackets \"[]\"\n" +
//            "      replaced with your own identifying information. (Don't include\n" +
//            "      the brackets!)  The text should be enclosed in the appropriate\n" +
//            "      comment syntax for the file format. We also recommend that a\n" +
//            "      file or class name and description of purpose be included on the\n" +
//            "      same \"printed page\" as the copyright notice for easier\n" +
//            "      identification within third-party archives.\n" +
//            "\n" +
//            "   Copyright [yyyy] [name of copyright owner]\n" +
//            "\n" +
//            "   Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
//            "   you may not use this file except in compliance with the License.\n" +
//            "   You may obtain a copy of the License at\n" +
//            "\n" +
//            "       http://www.apache.org/licenses/LICENSE-2.0\n" +
//            "\n" +
//            "   Unless required by applicable law or agreed to in writing, software\n" +
//            "   distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
//            "   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
//            "   See the License for the specific language governing permissions and\n" +
//            "   limitations under the License.\n" +
//            "\n" +
//            "\n" +
//            "\n" +
//            "Some code in core/src/java/org/apache/lucene/util/UnicodeUtil.java was\n" +
//            "derived from unicode conversion examples available at\n" +
//            "http://www.unicode.org/Public/PROGRAMS/CVTUTF.  Here is the copyright\n" +
//            "from those sources:\n" +
//            "\n" +
//            "/*\n" +
//            " * Copyright 2001-2004 Unicode, Inc.\n" +
//            " * \n" +
//            " * Disclaimer\n" +
//            " * \n" +
//            " * This source code is provided as is by Unicode, Inc. No claims are\n" +
//            " * made as to fitness for any particular purpose. No warranties of any\n" +
//            " * kind are expressed or implied. The recipient agrees to determine\n" +
//            " * applicability of information provided. If this file has been\n" +
//            " * purchased on magnetic or optical media from Unicode, Inc., the\n" +
//            " * sole remedy for any claim will be exchange of defective media\n" +
//            " * within 90 days of receipt.\n" +
//            " * \n" +
//            " * Limitations on Rights to Redistribute This Code\n" +
//            " * \n" +
//            " * Unicode, Inc. hereby grants the right to freely use the information\n" +
//            " * supplied in this file in the creation of products supporting the\n" +
//            " * Unicode Standard, and to make copies of this file in any form\n" +
//            " * for internal or external distribution as long as this notice\n" +
//            " * remains attached.\n" +
//            " */\n" +
//            "\n" +
//            "\n" +
//            "Some code in core/src/java/org/apache/lucene/util/ArrayUtil.java was\n" +
//            "derived from Python 2.4.2 sources available at\n" +
//            "http://www.python.org. Full license is here:\n" +
//            "\n" +
//            "  http://www.python.org/download/releases/2.4.2/license/\n" +
//            "\n" +
//            "Some code in core/src/java/org/apache/lucene/util/UnicodeUtil.java was\n" +
//            "derived from Python 3.1.2 sources available at\n" +
//            "http://www.python.org. Full license is here:\n" +
//            "\n" +
//            "  http://www.python.org/download/releases/3.1.2/license/\n" +
//            "\n" +
//            "Some code in core/src/java/org/apache/lucene/util/automaton was\n" +
//            "derived from Brics automaton sources available at\n" +
//            "www.brics.dk/automaton/. Here is the copyright from those sources:\n" +
//            "\n" +
//            "/*\n" +
//            " * Copyright (c) 2001-2009 Anders Moeller\n" +
//            " * All rights reserved.\n" +
//            " * \n" +
//            " * Redistribution and use in source and binary forms, with or without\n" +
//            " * modification, are permitted provided that the following conditions\n" +
//            " * are met:\n" +
//            " * 1. Redistributions of source code must retain the above copyright\n" +
//            " *    notice, this list of conditions and the following disclaimer.\n" +
//            " * 2. Redistributions in binary form must reproduce the above copyright\n" +
//            " *    notice, this list of conditions and the following disclaimer in the\n" +
//            " *    documentation and/or other materials provided with the distribution.\n" +
//            " * 3. The name of the author may not be used to endorse or promote products\n" +
//            " *    derived from this software without specific prior written permission.\n" +
//            " * \n" +
//            " * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR\n" +
//            " * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES\n" +
//            " * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.\n" +
//            " * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,\n" +
//            " * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT\n" +
//            " * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,\n" +
//            " * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY\n" +
//            " * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT\n" +
//            " * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF\n" +
//            " * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.\n" +
//            " */\n" +
//            " \n" +
//            "The levenshtein automata tables in core/src/java/org/apache/lucene/util/automaton \n" +
//            "were automatically generated with the moman/finenight FSA package.\n" +
//            "Here is the copyright for those sources:\n" +
//            "\n" +
//            "# Copyright (c) 2010, Jean-Philippe Barrette-LaPierre, <jpb@rrette.com>\n" +
//            "#\n" +
//            "# Permission is hereby granted, free of charge, to any person\n" +
//            "# obtaining a copy of this software and associated documentation\n" +
//            "# files (the \"Software\"), to deal in the Software without\n" +
//            "# restriction, including without limitation the rights to use,\n" +
//            "# copy, modify, merge, publish, distribute, sublicense, and/or sell\n" +
//            "# copies of the Software, and to permit persons to whom the\n" +
//            "# Software is furnished to do so, subject to the following\n" +
//            "# conditions:\n" +
//            "#\n" +
//            "# The above copyright notice and this permission notice shall be\n" +
//            "# included in all copies or substantial portions of the Software.\n" +
//            "#\n" +
//            "# THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND,\n" +
//            "# EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES\n" +
//            "# OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND\n" +
//            "# NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT\n" +
//            "# HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,\n" +
//            "# WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING\n" +
//            "# FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR\n" +
//            "# OTHER DEALINGS IN THE SOFTWARE.\n" +
//            "\n" +
//            "Some code in core/src/java/org/apache/lucene/util/UnicodeUtil.java was\n" +
//            "derived from ICU (http://www.icu-project.org)\n" +
//            "The full license is available here: \n" +
//            "  http://source.icu-project.org/repos/icu/icu/trunk/license.html\n" +
//            "\n" +
//            "/*\n" +
//            " * Copyright (C) 1999-2010, International Business Machines\n" +
//            " * Corporation and others.  All Rights Reserved.\n" +
//            " *\n" +
//            " * Permission is hereby granted, free of charge, to any person obtaining a copy \n" +
//            " * of this software and associated documentation files (the \"Software\"), to deal\n" +
//            " * in the Software without restriction, including without limitation the rights \n" +
//            " * to use, copy, modify, merge, publish, distribute, and/or sell copies of the \n" +
//            " * Software, and to permit persons to whom the Software is furnished to do so, \n" +
//            " * provided that the above copyright notice(s) and this permission notice appear \n" +
//            " * in all copies of the Software and that both the above copyright notice(s) and\n" +
//            " * this permission notice appear in supporting documentation.\n" +
//            " * \n" +
//            " * THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR \n" +
//            " * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, \n" +
//            " * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT OF THIRD PARTY RIGHTS. \n" +
//            " * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR HOLDERS INCLUDED IN THIS NOTICE BE \n" +
//            " * LIABLE FOR ANY CLAIM, OR ANY SPECIAL INDIRECT OR CONSEQUENTIAL DAMAGES, OR \n" +
//            " * ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER \n" +
//            " * IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT \n" +
//            " * OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.\n" +
//            " *\n" +
//            " * Except as contained in this notice, the name of a copyright holder shall not \n" +
//            " * be used in advertising or otherwise to promote the sale, use or other \n" +
//            " * dealings in this Software without prior written authorization of the \n" +
//            " * copyright holder.\n" +
//            " */\n" +
//            " \n" +
//            "The following license applies to the Snowball stemmers:\n" +
//            "\n" +
//            "Copyright (c) 2001, Dr Martin Porter\n" +
//            "Copyright (c) 2002, Richard Boulton\n" +
//            "All rights reserved.\n" +
//            "\n" +
//            "Redistribution and use in source and binary forms, with or without\n" +
//            "modification, are permitted provided that the following conditions are met:\n" +
//            "\n" +
//            "    * Redistributions of source code must retain the above copyright notice,\n" +
//            "    * this list of conditions and the following disclaimer.\n" +
//            "    * Redistributions in binary form must reproduce the above copyright\n" +
//            "    * notice, this list of conditions and the following disclaimer in the\n" +
//            "    * documentation and/or other materials provided with the distribution.\n" +
//            "    * Neither the name of the copyright holders nor the names of its contributors\n" +
//            "    * may be used to endorse or promote products derived from this software\n" +
//            "    * without specific prior written permission.\n" +
//            "\n" +
//            "THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS \"AS IS\"\n" +
//            "AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE\n" +
//            "IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE\n" +
//            "DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE\n" +
//            "FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL\n" +
//            "DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR\n" +
//            "SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER\n" +
//            "CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,\n" +
//            "OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE\n" +
//            "OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.\n" +
//            "\n" +
//            "The following license applies to the KStemmer:\n" +
//            "\n" +
//            "Copyright Â© 2003,\n" +
//            "Center for Intelligent Information Retrieval,\n" +
//            "University of Massachusetts, Amherst.\n" +
//            "All rights reserved.\n" +
//            "\n" +
//            "Redistribution and use in source and binary forms, with or without modification,\n" +
//            "are permitted provided that the following conditions are met:\n" +
//            "\n" +
//            "1. Redistributions of source code must retain the above copyright notice, this\n" +
//            "list of conditions and the following disclaimer.\n" +
//            "\n" +
//            "2. Redistributions in binary form must reproduce the above copyright notice,\n" +
//            "this list of conditions and the following disclaimer in the documentation\n" +
//            "and/or other materials provided with the distribution.\n" +
//            "\n" +
//            "3. The names \"Center for Intelligent Information Retrieval\" and\n" +
//            "\"University of Massachusetts\" must not be used to endorse or promote products\n" +
//            "derived from this software without prior written permission. To obtain\n" +
//            "permission, contact info@ciir.cs.umass.edu.\n" +
//            "\n" +
//            "THIS SOFTWARE IS PROVIDED BY UNIVERSITY OF MASSACHUSETTS AND OTHER CONTRIBUTORS\n" +
//            "\"AS IS\" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,\n" +
//            "THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE\n" +
//            "ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE\n" +
//            "LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR\n" +
//            "CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE\n" +
//            "GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)\n" +
//            "HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT\n" +
//            "LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY\n" +
//            "OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF\n" +
//            "SUCH DAMAGE.\n" +
//            "\n" +
//            "The following license applies to the Morfologik project:\n" +
//            "\n" +
//            "Copyright (c) 2006 Dawid Weiss\n" +
//            "Copyright (c) 2007-2011 Dawid Weiss, Marcin MiÅ\u0082kowski\n" +
//            "All rights reserved.\n" +
//            "\n" +
//            "Redistribution and use in source and binary forms, with or without modification, \n" +
//            "are permitted provided that the following conditions are met:\n" +
//            "\n" +
//            "    * Redistributions of source code must retain the above copyright notice, \n" +
//            "    this list of conditions and the following disclaimer.\n" +
//            "    \n" +
//            "    * Redistributions in binary form must reproduce the above copyright notice, \n" +
//            "    this list of conditions and the following disclaimer in the documentation \n" +
//            "    and/or other materials provided with the distribution.\n" +
//            "    \n" +
//            "    * Neither the name of Morfologik nor the names of its contributors \n" +
//            "    may be used to endorse or promote products derived from this software \n" +
//            "    without specific prior written permission.\n" +
//            "\n" +
//            "THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS \"AS IS\" AND \n" +
//            "ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED \n" +
//            "WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE \n" +
//            "DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR \n" +
//            "ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES \n" +
//            "(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; \n" +
//            "LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON \n" +
//            "ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT \n" +
//            "(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS \n" +
//            "SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.\n" +
//            "\n" +
//            "---\n" +
//            "\n" +
//            "The dictionary comes from Morfologik project. Morfologik uses data from \n" +
//            "Polish ispell/myspell dictionary hosted at http://www.sjp.pl/slownik/en/ and \n" +
//            "is licenced on the terms of (inter alia) LGPL and Creative Commons \n" +
//            "ShareAlike. The part-of-speech tags were added in Morfologik project and\n" +
//            "are not found in the data from sjp.pl. The tagset is similar to IPI PAN\n" +
//            "tagset.\n" +
//            "\n" +
//            "---\n" +
//            "\n" +
//            "The following license applies to the Morfeusz project,\n" +
//            "used by org.apache.lucene.analysis.morfologik.\n" +
//            "\n" +
//            "BSD-licensed dictionary of Polish (SGJP)\n" +
//            "http://sgjp.pl/morfeusz/\n" +
//            "\n" +
//            "Copyright Â© 2011 Zygmunt Saloni, WÅ\u0082odzimierz GruszczyÅ\u0084ski, \n" +
//            "\t    \t Marcin WoliÅ\u0084ski, Robert WoÅ\u0082osz\n" +
//            "\n" +
//            "All rights reserved.\n" +
//            "\n" +
//            "Redistribution and  use in  source and binary  forms, with  or without\n" +
//            "modification, are permitted provided that the following conditions are\n" +
//            "met:\n" +
//            "\n" +
//            "1. Redistributions of source code must retain the above copyright\n" +
//            "   notice, this list of conditions and the following disclaimer.\n" +
//            "\n" +
//            "2. Redistributions in binary form must reproduce the above copyright\n" +
//            "   notice, this list of conditions and the following disclaimer in the\n" +
//            "   documentation and/or other materials provided with the\n" +
//            "   distribution.\n" +
//            "\n" +
//            "THIS SOFTWARE IS PROVIDED BY COPYRIGHT HOLDERS â\u0080\u009CAS ISâ\u0080\u009D AND ANY EXPRESS\n" +
//            "OR  IMPLIED WARRANTIES,  INCLUDING, BUT  NOT LIMITED  TO,  THE IMPLIED\n" +
//            "WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE\n" +
//            "DISCLAIMED.  IN NO EVENT  SHALL COPYRIGHT  HOLDERS OR  CONTRIBUTORS BE\n" +
//            "LIABLE FOR  ANY DIRECT,  INDIRECT, INCIDENTAL, SPECIAL,  EXEMPLARY, OR\n" +
//            "CONSEQUENTIAL DAMAGES  (INCLUDING, BUT NOT LIMITED  TO, PROCUREMENT OF\n" +
//            "SUBSTITUTE  GOODS OR  SERVICES;  LOSS  OF USE,  DATA,  OR PROFITS;  OR\n" +
//            "BUSINESS INTERRUPTION) HOWEVER CAUSED  AND ON ANY THEORY OF LIABILITY,\n" +
//            "WHETHER IN  CONTRACT, STRICT LIABILITY, OR  TORT (INCLUDING NEGLIGENCE\n" +
//            "OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN\n" +
//            "IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.\n";
//
//    @Test
//    public void oro() {
//        String in = "/* \u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\n" +
//                " * The Apache Software License, Version 1.1\n" +
//                " *\n" +
//                " * Copyright (c) 2000-2002 The Apache Software Foundation.  All rights\n" +
//                " * reserved.\n" +
//                " *\n" +
//                " * Redistribution and use in source and binary forms, with or without\n" +
//                " * modification, are permitted provided that the following conditions\n" +
//                " * are met:\n" +
//                " *\n" +
//                " * 1. Redistributions of source code must retain the above copyright\n" +
//                " *    notice, this list of conditions and the following disclaimer.\n" +
//                " *\n" +
//                " * 2. Redistributions in binary form must reproduce the above copyright\n" +
//                " *    notice, this list of conditions and the following disclaimer in\n" +
//                " *    the documentation and/or other materials provided with the\n" +
//                " *    distribution.\n" +
//                " *\n" +
//                " * 3. The end-user documentation included with the redistribution,\n" +
//                " *    if any, must include the following acknowledgment:\n" +
//                " *       \"This product includes software developed by the\n" +
//                " *        Apache Software Foundation (http://www.apache.org/).\"\n" +
//                " *    Alternately, this acknowledgment may appear in the software itself,\n" +
//                " *    if and wherever such third-party acknowledgments normally appear.\n" +
//                " *\n" +
//                " * 4. The names \"Apache\" and \"Apache Software Foundation\", \"Jakarta-Oro\" \n" +
//                " *    must not be used to endorse or promote products derived from this\n" +
//                " *    software without prior written permission. For written\n" +
//                " *    permission, please contact apache@apache.org.\n" +
//                " *\n" +
//                " * 5. Products derived from this software may not be called \"Apache\" \n" +
//                " *    or \"Jakarta-Oro\", nor may \"Apache\" or \"Jakarta-Oro\" appear in their \n" +
//                " *    name, without prior written permission of the Apache Software Foundation.\n" +
//                " *\n" +
//                " * THIS SOFTWARE IS PROVIDED ``AS IS\u0027\u0027 AND ANY EXPRESSED OR IMPLIED\n" +
//                " * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES\n" +
//                " * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE\n" +
//                " * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR\n" +
//                " * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,\n" +
//                " * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT\n" +
//                " * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF\n" +
//                " * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND\n" +
//                " * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,\n" +
//                " * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT\n" +
//                " * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF\n" +
//                " * SUCH DAMAGE.\n" +
//                " * \u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\u003d\n" +
//                " *\n" +
//                " * This software consists of voluntary contributions made by many\n" +
//                " * individuals on behalf of the Apache Software Foundation.  For more\n" +
//                " * information on the Apache Software Foundation, please see\n" +
//                " * \u003chttp://www.apache.org/\u003e.\n" +
//                " */\n";
//    }
//
//
//
//
////    @Test
////    public void fff() {
////        String in = "Copyright (C) 1999-2004 Clarkware Consulting, Inc.\n" +
////                "All Rights Reserved.\n" +
////                "\n" +
////                "Redistribution and use in source and binary forms, with or without\n" +
////                "modification, are permitted provided that the following conditions\n" +
////                "are met:\n" +
////                "\n" +
////                "   1. Redistributions of source code must retain the above copyright\n" +
////                "      notice, this list of conditions and the following disclaimer.\n" +
////                "\n" +
////                "   2. Redistributions in binary form must reproduce the above copyright\n" +
////                "      notice, this list of conditions and the following disclaimer in the\n" +
////                "      documentation and/or other materials provided with the distribution.\n" +
////                "\n" +
////                "   3. Neither the name of Clarkware Consulting, Inc. nor the names of its\n" +
////                "      contributors may be used to endorse or promote products derived\n" +
////                "      from this software without prior written permission. For written\n" +
////                "      permission, please contact clarkware@clarkware.com.\n" +
////                "\n" +
////                "THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,\n" +
////                "INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND\n" +
////                "FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL\n" +
////                "CLARKWARE CONSULTING OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,\n" +
////                "INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT\n" +
////                "LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,\n" +
////                "OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF\n" +
////                "LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING\n" +
////                "NEGLIGENCE OR OTHERWISE) ARISING IN  ANY WAY OUT OF THE USE OF THIS SOFTWARE,\n" +
//////        new LicenseFromText( lOracle ).firstDiff( in );
//////
//////        assertThat( new LicenseFromText( lOracle ).getLicense( in ) ).isEqualTo( MappedLicense.of( lOracle.getOrThrowByName( "bsd-3-clause" ), "tt" ) );
////
////        List<String> txts = new ArrayList<>();
////        txts.add( in );
////        new LicenseFromText( lOracle ).match3( txts );
////    }
//}
