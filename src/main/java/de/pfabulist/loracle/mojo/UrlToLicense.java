package de.pfabulist.loracle.mojo;

import de.pfabulist.loracle.license.LOracle;
import de.pfabulist.loracle.license.LicenseFromText;
import de.pfabulist.loracle.license.MappedLicense;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

public class UrlToLicense {

    private final LOracle lOracle;
    private final Findings log;
    private final Downloader downloader;
    private final LicenseFromText lft;

    public UrlToLicense( LOracle lOracle, Findings log ) {
        this.lOracle = lOracle;
        this.log = log;
        this.downloader = new Downloader( log, lOracle );
        this.lft = new LicenseFromText( lOracle, log );
    }

    public MappedLicense getLicense( String url ) {
        MappedLicense ret = lOracle.getByUrl( url );

        if( ret.isPresent() ) {
            return ret;
        }

        downloader.download( url );
        String txt = downloader.get( url );

        if( txt.isEmpty() ) {
            return MappedLicense.empty();
        }

        ret = lft.getLicense( txt );

        ret.ifPresent( lid -> {
            log.debug( "added extension url for license " + url );
            lOracle.addUrl( lid, url );
        } );

        return ret;
    }
}
