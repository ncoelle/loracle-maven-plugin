package de.pfabulist.ianalb.model;

import de.pfabulist.ianalb.model.oracle.Aliases;
import de.pfabulist.ianalb.model.oracle.ExtractGoodFedoraLicenses;
import de.pfabulist.ianalb.model.oracle.LicenseOracle;
import de.pfabulist.ianalb.model.license.IBLicense;
import de.pfabulist.ianalb.model.license.Licenses;
import org.junit.Test;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

public class GetLicensesTest {

    @Test
    public void fedora() {

        Licenses licenses = new Licenses();
        final LicenseOracle li = new LicenseOracle( new Aliases( licenses ), licenses, new IgnoreLog() );

        ExtractGoodFedoraLicenses extract = new ExtractGoodFedoraLicenses();

        extract.getFedorIds().
                peek( System.out::println ).
                forEach( n -> System.out.println( "   " + li.guessLicenseByName( n ).map( IBLicense::toString ).orElseGet( () -> "--")));


    }
}
