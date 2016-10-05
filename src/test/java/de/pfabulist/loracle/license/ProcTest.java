//package de.pfabulist.loracle.license;
//
//import org.junit.Test;
//
//import java.util.Arrays;
//import java.util.Collection;
//import java.util.Collections;
//import java.util.stream.Stream;
//
///**
// * Copyright (c) 2006 - 2016, Stephan Pfab
// * SPDX-License-Identifier: BSD-2-Clause
// */
//
//public class ProcTest {
//
//    @Test
//    public void foo() {
//
//        Stream<Integer> stream = Stream.of( 1, 2, 3, 4, 5, 6, 7, 8, 9 );
//
//        Proc<Integer, Boolean> proc = new Proc<>( false, b -> b ? 1 : 0, stream );
//
//        proc.proc( ( p, e ) -> {
//                       if( e.equals( 4 ) ) {
//                           p.setState( true );
//                           return Arrays.asList( 42, 74 );
//                       }
//                       return Collections.singletonList( e );
//                   },
//                   ( p, e ) -> Collections.singletonList( e ) ).
//                flatMap( Collection::stream ).
//                forEach( System.out::println );
//
//    }
//}
