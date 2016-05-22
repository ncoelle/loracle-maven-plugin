//package de.pfabulist.ianalb.model;
//
//import de.pfabulist.ianalb.model.license.FedoraGoodLicenses;
//import de.pfabulist.ianalb.model.oracle.Aliases;
//import de.pfabulist.ianalb.model.oracle.ExtractGoodFedoraLicenses;
//import de.pfabulist.ianalb.model.oracle.LicenseOracle;
//import de.pfabulist.ianalb.model.license.Licenses;
//import de.pfabulist.ianalb.model.oracle.UrlToName;
//import org.junit.Test;
//
///**
// * Copyright (c) 2006 - 2016, Stephan Pfab
// * SPDX-License-Identifier: BSD-2-Clause
// */
//
//public class GetLicensesTest {
//
//    @Test
//    public void fedora() {
//
//        Licenses licenses = new Licenses();
//        final LicenseOracle li = new LicenseOracle( new Aliases( licenses ), licenses, new IgnoreLog(), new UrlToName( licenses ) );
//
//        ExtractGoodFedoraLicenses extract = new ExtractGoodFedoraLicenses();
//
//        extract.getFedoraIds().
//                //peek( System.out::println ).
//                        filter( l -> !li.guessLicenseByName( l ).isPresent() ).
////                filter( ).
//        map( FedoraGoodLicenses::new ).
//                forEach( System.out::println );
//
//
//    }
//
//    @Test
//    public void fedora2() {
//
//        Licenses licenses = new Licenses();
//        final LicenseOracle li = new LicenseOracle(
//                new Aliases( licenses ),
//                licenses,
//                new IgnoreLog(),
//                new UrlToName( licenses ) );
//
//        ExtractGoodFedoraLicenses extract = new ExtractGoodFedoraLicenses();
//
//        extract.getFedoraInfo().forEach( f -> {
//            System.out.println( li.guessLicenseByName( f.name ));
//            System.out.println( f.url );
//            System.out.println( li.guessByUrl( f.url ) );
//        });
//
////        extract.getFedoraIds().
////                //peek( System.out::println ).
////                        filter( l -> !li.guessLicenseByName( l ).isPresent() ).
//////                filter( ).
////        map( FedoraGoodLicenses::new ).
////                forEach( System.out::println );
//
//
//    }
//
//    private boolean pass( String name ) {
//        return false;
//    }
//}
