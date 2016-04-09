package de.pfabulist.ianalb.model.license;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

public abstract class IBLicense {

    abstract String getName();

    @Override
    public String toString() {
        return getName();
    }
}
