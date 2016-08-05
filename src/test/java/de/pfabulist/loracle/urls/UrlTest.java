package de.pfabulist.loracle.urls;

import de.pfabulist.loracle.buildup.JSONStartup;
import de.pfabulist.loracle.license.LOracle;
import de.pfabulist.loracle.license.LicenseFromText;
import de.pfabulist.loracle.license.MappedLicense;
import de.pfabulist.loracle.mojo.Downloader;
import de.pfabulist.loracle.mojo.Findings;
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
        LOracle lOracle = JSONStartup.start().spread();

        LicenseFromText lft = new LicenseFromText( lOracle );
        Downloader dl = new Downloader( log, lOracle );
        dl.download( "repo.aduna-software.org/legal/aduna-bsd" );


        assertThat( lft.getLicense( dl.get("http://repo.aduna-software.org/legal/aduna-bsd.txt") ) ).
                isEqualTo( MappedLicense.of( lOracle.getOrThrowByName( "bsd-3-clause" ), "test"));
    }

    public static final Log mlog = new Log() {
        @Override
        public boolean isDebugEnabled() {
            return false;
        }

        @Override
        public void debug( CharSequence content ) {

        }

        @Override
        public void debug( CharSequence content, Throwable error ) {

        }

        @Override
        public void debug( Throwable error ) {

        }

        @Override
        public boolean isInfoEnabled() {
            return false;
        }

        @Override
        public void info( CharSequence content ) {

        }

        @Override
        public void info( CharSequence content, Throwable error ) {

        }

        @Override
        public void info( Throwable error ) {

        }

        @Override
        public boolean isWarnEnabled() {
            return false;
        }

        @Override
        public void warn( CharSequence content ) {

        }

        @Override
        public void warn( CharSequence content, Throwable error ) {

        }

        @Override
        public void warn( Throwable error ) {

        }

        @Override
        public boolean isErrorEnabled() {
            return false;
        }

        @Override
        public void error( CharSequence content ) {

        }

        @Override
        public void error( CharSequence content, Throwable error ) {

        }

        @Override
        public void error( Throwable error ) {

        }
    };

    private static Findings log = new Findings( mlog );

}
