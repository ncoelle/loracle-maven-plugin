//package de.pfabulist.ianalb.model;
//
//import de.pfabulist.loracle.license.AliasBuilder;
//import org.junit.Test;
//
//import java.util.regex.Pattern;
//
//import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
//
///**
// * Copyright (c) 2006 - 2016, Stephan Pfab
// * SPDX-License-Identifier: BSD-2-Clause
// */
//
//public class AliasBuilderTest {
//
//    @Test
//    public void testApache() {
//        AliasBuilder ab = new AliasBuilder();
//
//        Pattern pat = ab.buildAlias( "The Apache Software License Version 2.0" ).buildCaseInsensitivePattern();
//
//        System.out.println(pat);
//
//        assertThat( pat.matcher( ab.reduce( "The Apache Software License, Version 2.0" )).matches()).isTrue();
//
//        assertThat( ab.reduce( "The Apache Software License, Version 2.0" )).isEqualTo( ab.reduce( "Apache 2.0" ) );
//    }
//
//}
