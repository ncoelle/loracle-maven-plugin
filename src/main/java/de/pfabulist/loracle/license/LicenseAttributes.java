package de.pfabulist.loracle.license;

import java.util.Optional;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

public class LicenseAttributes {

    private String attis;

    transient private Optional<Boolean> copyLeft = Optional.empty();
    transient private Optional<Boolean> spdx = Optional.empty();
    transient private Optional<Boolean> osiApproved = Optional.empty();;
    transient private Optional<Boolean> fedora = Optional.empty();

//    public boolean spdx;
//    public boolean osiApproved;
//    public Optional<Boolean> fedoraApproved = Optional.empty();
//    public Optional<Boolean> gpl2Compatible = Optional.empty();
//    public Optional<Boolean> gpl3Compatible = Optional.empty();
//    public boolean copyLeft = false;
//    public boolean weakCopyLeft = false;
//

    public LicenseAttributes() {
        attis = "S-O 2 3 F L W  ";
    }

    public Optional<Boolean> isCopyLeft() {
        if( !copyLeft.isPresent() ) {
            copyLeft = get( 11 );
        }

        return copyLeft;
    }

    public boolean isCopyLeftDef() {
        return isCopyLeft().orElse( false );
    }

    public void setCopyLeft( boolean on ) {
        set( 11, on );
        copyLeft = Optional.empty();
    }

    public boolean isSPDX() {
        if( !spdx.isPresent() ) {
            spdx = get( 1 );
        }

        return spdx.orElseThrow( () -> new IllegalStateException( "spdx state not known ?" ) );
    }

    public void setSPDX( boolean on ) {
        set( 1, on );
        spdx = Optional.empty();
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

    public Optional<Boolean> isOsiApproved() {
        if( !osiApproved.isPresent() ) {
            osiApproved = get( 3 );
        }

        return osiApproved;
    }

    public void setOsiApproved( boolean on ) {
        set( 3, on );
        copyLeft = Optional.empty();
    }

    public Optional<Boolean> isFedoraApproved() {
        if( !fedora.isPresent() ) {
            fedora = get( 9 );
        }

        return fedora;
    }

    public void setFedoraApproved( boolean on ) {
        set( 9, on );
        fedora = Optional.empty();
    }

    public Optional<Boolean> isGpl2Compatible() {
        return get(5);
    }

    public void setGpl2Compatible( boolean on ) {
        set( 5, on );
    }

    public Optional<Boolean> isGpl3Compatible() {
        return get(7);
    }

    public void setGpl3Compatible( boolean on ) {
        set( 7, on );
    }

    public void setWeakCopyLeft( boolean on ) {
        set( 13, on );
    }
}
