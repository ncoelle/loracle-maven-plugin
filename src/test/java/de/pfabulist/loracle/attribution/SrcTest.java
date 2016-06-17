package de.pfabulist.loracle.attribution;

import org.junit.Test;

import java.util.regex.Matcher;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

public class SrcTest {

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

        Matcher matcher = SrcAccess.copyRightPattern.matcher( file );

        assertThat( matcher.find() ).isTrue();

        System.out.println( matcher.group("holder"));
    }
}
