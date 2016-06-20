package de.pfabulist.loracle.license;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

public class NormalizationTest {


    @Test
    public void noSpace() {
        assertThat( new Normalizer().reduce( "ab1.0" )).isEqualTo( "ab 1" );
    }

}
