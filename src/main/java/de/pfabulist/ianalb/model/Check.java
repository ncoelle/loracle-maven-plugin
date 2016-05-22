//package de.pfabulist.ianalb.model;
//
//import de.pfabulist.ianalb.model.license.IBLicense;
//import de.pfabulist.ianalb.model.license.Licenses;
//
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Set;
//
///**
// * Copyright (c) 2006 - 2016, Stephan Pfab
// * SPDX-License-Identifier: BSD-2-Clause
// */
//
//public class Check {
//
//    private final IBLicense gpl2;
//    private final IBLicense gpl3;
//    private final IBLicense agpl3;
//
//    private final Map<IBLicense, Set<IBLicense>> exclusiveDown = new HashMap<>();
//
//    public Check( Licenses li ) {
//        gpl2 = li.getOrThrowByName( "GPL-2.0" );
//        gpl3 = li.getOrThrowByName( "GPL-3.0" );
//        agpl3 = li.getOrThrowByName( "AGPL-3.0" );
//
//        exclusiveDown.put( gpl2, Collections.emptySet() );
//        exclusiveDown.put( agpl3, Collections.emptySet() );
//        exclusiveDown.put( gpl3, Collections.singleton( agpl3 ) );
//    }
//
//    public boolean isCompatible( IBLicense project, IBLicense dep ) {
//        if( project.equals( dep ) ) {
//            return true;
//        }
//
//        if( exclusiveDown.containsKey( dep ) ) {
//            if( !exclusiveDown.get( dep ).contains( project ) ) {
//                return false;
//            }
//        }
//
//        return true;
//    }
//
////    private Map<IBLicense, Set<IBLicense>> notCombinable = new HashMap<>();
////
////    public Stream<List<IBLicense>> notCombinable( final List<IBLicense> lots ) {
////        return lots.stream().map(
////                l -> {
////                    if( !notCombinable.containsKey( l ) ) {
////                        return Collections.EMPTY_LIST;
////                    }
////
////                    Set<IBLicense> bad = notCombinable.get( l );
////                    List<IBLicense> found = lots.stream().
////                            //filter( ll -> true /*ll -> bad.contains( ll ) */ ).
////                            collect( Collectors.toList() );
////
////                    if( found.isEmpty() ) {
////                        return found;
////                    }
////
////                    return found.add( l );
////                }
////        );
////    }
//
//}
