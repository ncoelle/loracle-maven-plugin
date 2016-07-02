package de.pfabulist.loracle.license;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

public class CoordinatesTest {

    @Test
    public void matches() {
        assertThat( Coordinates.valueOf( "a:b*c:1.0" ).matches( Coordinates.valueOf( "a:bc:1.0" ) )).isTrue();
        assertThat( Coordinates.valueOf( "a:b-*:1.0" ).matches( Coordinates.valueOf( "a:b-foo:1.0" ) )).isTrue();
        assertThat( Coordinates.valueOf( "a:b:*" ).matches( Coordinates.valueOf( "a:b:1.0" ) )).isTrue();
    }

    @Test
    public void snapshot() {
        Coordinates coo = Coordinates.valueOf( "commons-io:commons-io:2.6-20160630.123456-42" );
        assertThat( coo.isSnapshot() ).isTrue();
    }
}
