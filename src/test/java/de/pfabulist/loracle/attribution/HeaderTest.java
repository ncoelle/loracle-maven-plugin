package de.pfabulist.loracle.attribution;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

public class HeaderTest {

    @Test
    public void testAbstractClass() {
        String in = "abstract class AbstractEntry<TypeK,TypeV> implements Map.Entry<TypeK,TypeV> {\n";

        assertThat( Header.start.matcher( in ).matches()).isTrue();
    }
}
