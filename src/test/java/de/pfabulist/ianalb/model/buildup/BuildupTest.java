//package de.pfabulist.ianalb.model.buildup;
//
//import org.junit.Test;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
///**
// * Copyright (c) 2006 - 2016, Stephan Pfab
// * SPDX-License-Identifier: BSD-2-Clause
// */
//
//public class BuildupTest {
//
//    @Test
//    public void buildupSingleSPDX() {
//
//        Oracle oracle = new Oracle( "testing" );
//        oracle.importSPDXLicenses();
//
//        assertThat( oracle.getPrecise( "ZPL-2.1" )).isPresent();
//        assertThat( oracle.getPrecise( "Glide" )).isPresent();
//
//        assertThat( oracle.guessByName( "cecill Free Software 1.0" )).isPresent();
//    }
//
////    @Test
////    public void buildupSPDXWithException() {
////        Oracle oracle = new Oracle( "testing" );
////        oracle.importSPDXLicenses();
////        oracle.importSPDXExceptions();
////
////        assertThat( oracle.getPrecise( "ZPL-2.1 with " )).isPresent();
////
////    }
//}
