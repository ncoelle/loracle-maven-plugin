//package de.pfabulist.loracle.license;
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
//public class Proc<E,R> {
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
//    public Proc( R init, Function<R,Integer> whichState, Stream<E> stream ) {
//        this.state = init;
//        this.stream = stream;
//        this.judge = whichState;
//    }
//
//
//    public <T> Stream<T> proc( BiFunction<Proc<E,R>, E, T>... f ) {
//
//        Proc<E,R> that = this;
//
//        return stream.map( e -> f[judge.apply( state)].apply( that, e ));
//
//    }
//
//    public void setState( R b ) {
//        this.state = b;
//    }
//
//}
