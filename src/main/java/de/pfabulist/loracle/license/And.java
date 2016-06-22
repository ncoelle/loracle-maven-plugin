package de.pfabulist.loracle.license;

import de.pfabulist.loracle.mojo.Findings;

import java.util.concurrent.atomic.AtomicReference;

import static de.pfabulist.nonnullbydefault.NonnullCheck._nn;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

public class And {

    private final LOracle lOracle;
    private final Findings log;
    private final boolean andIsOr;

    public And( LOracle lOracle, Findings log, boolean andIsOr ) {
        this.lOracle = lOracle;
        this.log = log;
        this.andIsOr = andIsOr;
    }

    public MappedLicense and( MappedLicense l, MappedLicense r ) {

        if( !l.isPresent() ) {
            return r;
        }

        if( !r.isPresent() ) {
            return l;
        }

        AtomicReference<MappedLicense> ret = new AtomicReference<>( MappedLicense.empty() );
        l.ifPresent( left -> r.ifPresent( right -> {
            if( andIsOr ) {
                ret.set( MappedLicense.of( lOracle.getOr( left, right ), "dual license or'ed (" + l.getReason() + "), (" + r.getReason() + ")" ) );
            } else {
                log.warn( "is that really <" + ret + "> or should that be <" + lOracle.getOr( left, right ) + ">" );
                ret.set( MappedLicense.of( lOracle.getAnd( left, right ), "dual license and'ed (" + l.getReason() + "), (" + r.getReason() + ")" ) );
            }
        } ) );
        return _nn( ret.get() );

    }


}
