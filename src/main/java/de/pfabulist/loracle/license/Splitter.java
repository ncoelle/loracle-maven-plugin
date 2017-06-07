//package de.pfabulist.loracle.license;
//
//
//import de.pfabulist.roast.collection.P;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Copyright (c) 2006 - 2016, Stephan Pfab
// * SPDX-License-Identifier: BSD-2-Clause
// */
//
//public class Splitter {
//
//    public static List<P<String,MappedLicense>> cutMiddle( String str, int from, int to, MappedLicense l) {
//        List<P<String,MappedLicense>> res = new ArrayList<>();
//        if( from > 0 ) {
//            res.add( P.of( str.substring( 0, from ), MappedLicense.empty() ));
//        }
//
//        res.add( P.of( str.substring( from, to ), l ));
//
//        if( from < str.length() ) {
//            String sub = str.substring( to, str.length() );
//            if ( !sub.trim().isEmpty()) {
//                res.add( P.of( sub, MappedLicense.empty() ) );
//            }
//        }
//        return res;
//    }
//}
