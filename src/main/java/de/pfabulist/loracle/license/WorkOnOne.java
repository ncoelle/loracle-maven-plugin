//package de.pfabulist.loracle.license;
//
//import de.pfabulist.roast.functiontypes.BiFunction_;
//import de.pfabulist.roast.functiontypes.Function_;
//
//import java.util.Collection;
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//
///**
// * Copyright (c) 2006 - 2016, Stephan Pfab
// * SPDX-License-Identifier: BSD-2-Clause
// */
//
//public class WorkOnOne<E> {
//    private final Stream<E> stream;
//    private boolean state = false;
//
//    public WorkOnOne( Stream<E> stream ) {
//        this.stream = stream;
//    }
//
//    public <R> List<E> work( Function_<E, Optional<R>> test,
//                             BiFunction_<R, E, List<E>> yes ) {
//
//        return stream.
//                map( e -> {
//                    System.out.println( "++++++ state " + state );
//                    System.out.println( "++++++ state " + state );
//                    System.out.println( "++++++ state " + state );
//
//                    if( state ) {
//                        return Collections.singletonList( e );
//                    }
//
//                    return test.apply_( e ).
//                            map( hmm -> setState( yes.apply_( hmm, e ) ) ).
//                            orElse( Collections.singletonList( e ) );
//
//                } ).
//                flatMap( Collection::stream ).
//                collect( Collectors.toList() );
//    }
//
//    private List<E> setState( List<E> ll ) {
//        state = true;
//        return ll;
//    }
//
//    public boolean foundp() {
//        return state;
//    }
//}
