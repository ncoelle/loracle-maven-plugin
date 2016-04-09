package de.pfabulist.ianalb.model;

import de.pfabulist.ianalb.model.oracle.SPDXParser;
import de.pfabulist.ianalb.model.license.Licenses;
import org.junit.Test;


/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

public class ParseTest {

    @Test
    public void testTok() {
        Licenses licenses = new Licenses();

        new SPDXParser( licenses ).tok( "CDDL-1.0 AND GPL-2.0 WITH Classpath-exception-2.0" ).forEach( System.out::println );

        System.out.println( new SPDXParser( licenses ).parse( "CDDL-1.0 and GPL-2.0 WITH Classpath-exception-2.0" ));
    }
}
