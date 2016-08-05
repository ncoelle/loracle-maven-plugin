package de.pfabulist.loracle.attribution;

import de.pfabulist.frex.Frex;
import de.pfabulist.loracle.license.ContentToLicense;
import org.junit.Test;

import java.util.regex.Matcher;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

public class HolderTest {

    @Test
    public void testPattern() {

        System.out.println( Frex.or( Frex.any(), Frex.txt( '\n' )).zeroOrMore().then( Frex.txt( "hah" ) ).buildCaseInsensitivePattern().matcher( "\n\n\nhah" ).matches());


        String in = "\n" +
                "Apache Maven PMD Plugin\n" +
                "Copyright 2005-2015 The Apache Software Foundation\n" +
                "\n" +
                "This product includes software developed at\n" +
                "The Apache Software Foundation (http://www.apache.org/).\n";

        System.out.println( ContentToLicense.copyRightPattern );

        Matcher matcher = ContentToLicense.copyRightPattern.matcher( in );

        assertThat( matcher.find() ).isTrue();

        System.out.println(matcher.group( "year" ));
        System.out.println(matcher.group( "holder" ));
    }
}
