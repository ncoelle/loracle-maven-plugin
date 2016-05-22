package de.pfabulist.loracle.license;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import javax.annotation.Nullable;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

public class CompositeLicense implements LicenseID {

    private final boolean orNotAnd;
    private final LicenseID left;
    private final LicenseID right;

    CompositeLicense( boolean orNotAnd, LicenseID left, LicenseID right ) {
        this.orNotAnd = orNotAnd;
        this.left = left;
        this.right = right;
    }

    public LicenseID getLeft() {
        return left;
    }

    public LicenseID getRight() {
        return right;
    }

    public boolean isOr() {
        return orNotAnd;
    }

    public boolean isAnd() {
        return !orNotAnd;
    }

    @Override
    public String getId() {
        return encapsulateOr( left ) + (orNotAnd ? " or " : " and ") + encapsulateOr( right );
    }

    private String encapsulateOr( LicenseID id ) {
        if ( LicenseIDs.isOr( id )) {
            return "( " + id + " )";
        }

        return id.getId();
    }

    @Override
    @SuppressFBWarnings( "NP_METHOD_PARAMETER_TIGHTENS_ANNOTATION" )
    public boolean equals( @Nullable Object o ) {
        if( this == o ) { return true; }
        if( o == null || getClass() != o.getClass() ) { return false; }

        CompositeLicense that = (CompositeLicense) o;

        if( orNotAnd != that.orNotAnd ) { return false; }
        if( !left.equals( that.left ) ) { return false; }
        return right.equals( that.right );

    }

    @Override
    public int hashCode() {
        int result = ( orNotAnd ? 1 : 0 );
        result = 31 * result + left.hashCode();
        result = 31 * result + right.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return getId();
    }
}
