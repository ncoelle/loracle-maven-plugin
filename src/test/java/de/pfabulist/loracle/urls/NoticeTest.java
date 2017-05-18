package de.pfabulist.loracle.urls;

import de.pfabulist.loracle.buildup.JSONStartup;
import de.pfabulist.loracle.license.FindingsDummy;
import de.pfabulist.loracle.license.LOracle;
import de.pfabulist.loracle.mojo.Downloader;
import de.pfabulist.loracle.license.Findings;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

public class NoticeTest {

    private final LOracle lOracle = JSONStartup.start().spread();
//    private final LicenseIntelligence lIntelligence = new LicenseIntelligence( lOracle, new Findings( UrlTest.mlog ));
    private final Downloader downloader = new Downloader( new FindingsDummy(), lOracle );


    @Test
    public void findSpdx() {
        assertThat( downloader.onPredefLicense( "bsd-2-clause", ( s, i) -> {} )).isPresent();
    }

    @Test
    public void findDeja() {
        assertThat( downloader.onPredefLicense( "acdl-1.0", ( s, i) -> {} )).isPresent();
    }
}
