package de.pfabulist.loracle.mojo;

import javax.annotation.Nullable;

import static de.pfabulist.unchecked.NullCheck._orElseThrow;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

public class UrlDeclaration {

    @Nullable String url;
    @Nullable String license;
    @Nullable String checkedAt;

    public String getUrl() {
        return _orElseThrow( url, () -> new IllegalArgumentException( "no url in url declaration" ));
    }

    public void setUrl( @Nullable String url ) {
        this.url = url;
    }

    public String getLicense() {
        return _orElseThrow( license, () -> new IllegalArgumentException( "no license in url declaration" ));
    }

    public void setLicense( @Nullable String license ) {
        this.license = license;
    }

    public String getCheckedAt() {
        return _orElseThrow( checkedAt, () -> new IllegalArgumentException( "no date in url declaration" ));
    }

    public void setCheckedAt( @Nullable String checkedAt ) {
        this.checkedAt = checkedAt;
    }
}
