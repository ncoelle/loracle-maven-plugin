package de.pfabulist.loracle.attribution;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

public class CopyrightHolder {

    private final String years;
    private final String name;

    public CopyrightHolder( String years, String name ) {
        this.years = years;
        this.name = name;
    }

    public String getYears() {
        return years;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Copyright (c) " + years + ", " + name;
    }
}
