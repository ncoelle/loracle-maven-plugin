package de.pfabulist.loracle.license;

import de.pfabulist.kleinod.collection.P;
import de.pfabulist.loracle.mojo.Findings;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

        if( l.equals( r ) ) {
            return l;
        }

        if( andIsOr ) {
            Set<MappedLicense> list = new HashSet<>();
            flatten( l, list );
            flatten( r, list );

            return list.stream().reduce( MappedLicense.empty(), this::and2 );
        }

        throw new IllegalStateException( "foo" );
    }

    public MappedLicense and2( MappedLicense l, MappedLicense r ) {

        if( !l.isPresent() ) {
            return r;
        }

        if( !r.isPresent() ) {
            return l;
        }

        if( l.equals( r ) ) {
            return l;
        }

        AtomicReference<MappedLicense> ret = new AtomicReference<>( MappedLicense.empty() );
        l.ifPresent( left -> r.ifPresent( right -> {
            if( andIsOr ) {
                ret.set( MappedLicense.of( lOracle.getOr( left, right ), "or'ed" ) );//  (" + l.getReason() + "), (" + r.getReason() + ")" ) );
            } else {
                log.warn( "is that really <" + ret + "> or should that be <" + lOracle.getOr( left, right ) + ">" );
                ret.set( MappedLicense.of( lOracle.getAnd( left, right ), "and'ed (" + l.getReason() + "), (" + r.getReason() + ")" ) );
            }
        } ) );

        MappedLicense rett = _nn( ret.get() );

        rett.addOverFrom( l );
        rett.addOverFrom( r );

        return rett;

    }

    private void flatten( MappedLicense ml, Set<MappedLicense> list ) {
        ml.ifPresent( l -> {
            if( LicenseIDs.isOr( l ) ) {
                CompositeLicense cl = (CompositeLicense) l;
                flatten( MappedLicense.of( cl.getLeft(), "fl" ), list );
                flatten( MappedLicense.of( cl.getRight(), "fl" ), list );
            } else {
                list.add( ml );
            }
        } );
    }

}