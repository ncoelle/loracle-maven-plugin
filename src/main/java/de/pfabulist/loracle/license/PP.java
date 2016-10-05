//package de.pfabulist.loracle.license;
//
//import de.pfabulist.unchecked.functiontypes.BiFunctionE;
//
//import java.util.function.BiFunction;
//import java.util.function.Function;
//import java.util.stream.Stream;
//
///**
// * Copyright (c) 2006 - 2016, Stephan Pfab
// * SPDX-License-Identifier: BSD-2-Clause
// */
//
//public class PP<E,R> {
//
//    private final Stream<E> stream;
//    private R state;
//
//    private Function<R,Integer> judge;
//
//
////    private Optional<R> state;
////
////    Optional<R> get() {
////        return state;
////    }
//
//    public PP( R init, Function<R,Integer> whichState, Stream<E> stream ) {
//        this.state = init;
//        this.stream = stream;
//        this.judge = whichState;
//    }
//
//
//    public <T> Stream<T> proc( BiFunction<Proc<E,R>, E, Boolean> test,
//                               BiFunction<Proc<E,R>, E, T> nop,
//                               BiFunction<Proc<E,R>, E, T> yes ) {
//
//        PP<E,R> that = this;
//
//        return stream.map( e -> {
//            if( test.apply( that, e ) ) {
//                return yes.apply( that, e );
//
//            }
//            }
//
//    }
//
//    public void setState( R b ) {
//        this.state = b;
//    }
//
//}
//
