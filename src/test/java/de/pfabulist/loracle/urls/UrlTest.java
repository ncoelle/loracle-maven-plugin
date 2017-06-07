package de.pfabulist.loracle.urls;

import de.pfabulist.loracle.buildup.JSONStartup;
import de.pfabulist.loracle.license.FindingsDummy;
import de.pfabulist.loracle.license.LOracle;
import de.pfabulist.loracle.license.LicenseFromText;
import de.pfabulist.loracle.license.MappedLicense;
import de.pfabulist.loracle.mojo.Downloader;
import de.pfabulist.loracle.license.Findings;
import org.apache.maven.plugin.logging.Log;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

public class UrlTest {

    @Test
    public void aduna() {
        String txt = "Copyright Aduna (http://www.aduna-software.com/) 2001-2013\n" +
                "All rights reserved.\n" +
                "\n" +
                "Redistribution and use in source and binary forms, with or without modification,\n" +
                "are permitted provided that the following conditions are met:\n" +
                "\n" +
                "    * Redistributions of source code must retain the above copyright notice,\n" +
                "      this list of conditions and the following disclaimer.\n" +
                "    * Redistributions in binary form must reproduce the above copyright notice,\n" +
                "      this list of conditions and the following disclaimer in the documentation\n" +
                "      and/or other materials provided with the distribution.\n" +
                "    * Neither the name of the copyright holder nor the names of its contributors\n" +
                "      may be used to endorse or promote products derived from this software\n" +
                "      without specific prior written permission.\n" +
                "\n" +
                "THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS \"AS IS\" AND\n" +
                "ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED\n" +
                "WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE\n" +
                "DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR\n" +
                "ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES\n" +
                "(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;\n" +
                "ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT\n" +
                "(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS\n" +
                "SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.";

        LOracle lOracle = JSONStartup.start();

        Downloader dl = new Downloader( log, lOracle );
        dl.download( "repo.aduna-software.org/legal/aduna-bsd" );

        assertThat( dl.get("http://repo.aduna-software.org/legal/aduna-bsd.txt").equals( txt ) );

    }

    private static Findings log = new FindingsDummy();

}
