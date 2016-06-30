package de.pfabulist.loracle.license;

import java.util.Optional;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

public class LicenseAttributes {

    private String attis;

    //    transient private Optional<Boolean> copyLeft = Optional.empty();
//    transient private Optional<Boolean> spdx = Optional.empty();
//    transient private Optional<Boolean> osiApproved = Optional.empty();;
//    transient private Optional<Boolean> fedora = Optional.empty();
//
    public LicenseAttributes() {
        attis = "S O 2 3 F L W  ";
    }

    public boolean isSPDX() {
        return attis.charAt( 1 ) == 's';
    }

    public void setSPDX( boolean on ) {
        if ( on ) {
            set( 1, 's' );
        }
    }

    public void setFromFedora() {
        set( 1, 'f' );
    }

    public void setFromDeja() {
        set( 1, 'd' );
    }

    public void setFromIFross() {
        set( 1, 'i' );
    }

    public Optional<Boolean> isCopyLeft() {
        return get( 11 );
    }

    public boolean isCopyLeftDef() {
        return isCopyLeft().orElse( false );
    }

    public void setCopyLeft( boolean on ) {
        set( 11, on );
    }

    public Optional<Boolean> isOsiApproved() {
        return get( 3 );
    }

    public void setOsiApproved( boolean on ) {
        set( 3, on );
    }

    public Optional<Boolean> isFedoraApproved() {
        return get( 9 );
    }

    public void setFedoraApproved( boolean on ) {
        set( 9, on );
    }

    public Optional<Boolean> isGpl2Compatible() {
        return get( 5 );
    }

    public void setGpl2Compatible( boolean on ) {
        set( 5, on );
    }

    public Optional<Boolean> isGpl3Compatible() {
        return get( 7 );
    }

    public void setGpl3Compatible( boolean on ) {
        set( 7, on );
    }

    public void setWeakCopyLeft( boolean on ) {
        set( 13, on );
    }

    private Optional<Boolean> get( int idx ) {
        switch( attis.charAt( idx ) ) {
            case '+':
                return Optional.of( true );
            case '-':
                return Optional.of( false );
            default:
                return Optional.empty();
        }
    }

    private void set( int idx, boolean on ) {
        attis = attis.substring( 0, idx ) + ( on ? "+" : "-" ) + attis.substring( idx + 1, attis.length() );
    }

    private void set( int idx, char on ) {
        attis = attis.substring( 0, idx ) + on + attis.substring( idx + 1, attis.length() );
    }

}
