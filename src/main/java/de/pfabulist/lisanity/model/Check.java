package de.pfabulist.lisanity.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

public class Check {

    private final LiLicense gpl2;
    private final LiLicense gpl3;
    private final LiLicense agpl3;

    private final Map<LiLicense, Set<LiLicense>> exclusiveDown = new HashMap<>();

    public Check( Licenses li ) {
        gpl2 = li.getOrThrowByName( "GPL-2.0" );
        gpl3 = li.getOrThrowByName( "GPL-3.0" );
        agpl3 = li.getOrThrowByName( "AGPL-3.0" );

        exclusiveDown.put( gpl2, Collections.emptySet() );
        exclusiveDown.put( agpl3, Collections.emptySet() );
        exclusiveDown.put( gpl3, Collections.singleton( agpl3 ) );
    }

    public boolean isCompatible( LiLicense project, LiLicense dep ) {
        if( project.equals( dep ) ) {
            return true;
        }

        if( exclusiveDown.containsKey( dep ) ) {
            if( !exclusiveDown.get( dep ).contains( project ) ) {
                return false;
            }
        }

        return true;
    }

//    private Map<LiLicense, Set<LiLicense>> notCombinable = new HashMap<>();
//
//    public Stream<List<LiLicense>> notCombinable( final List<LiLicense> lots ) {
//        return lots.stream().map(
//                l -> {
//                    if( !notCombinable.containsKey( l ) ) {
//                        return Collections.EMPTY_LIST;
//                    }
//
//                    Set<LiLicense> bad = notCombinable.get( l );
//                    List<LiLicense> found = lots.stream().
//                            //filter( ll -> true /*ll -> bad.contains( ll ) */ ).
//                            collect( Collectors.toList() );
//
//                    if( found.isEmpty() ) {
//                        return found;
//                    }
//
//                    return found.add( l );
//                }
//        );
//    }

}
