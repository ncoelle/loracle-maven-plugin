//package de.pfabulist.ianalb.model;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import de.pfabulist.unchecked.Unchecked;
//import org.apache.maven.plugin.MojoFailureException;
//import org.apache.maven.plugin.logging.Log;
//
///**
// * Copyright (c) 2006 - 2016, Stephan Pfab
// * SPDX-License-Identifier: BSD-2-Clause
// */
//
//public class Failures {
//
//    private final List<String> messages = new ArrayList<>();
//    private final Log log;
//
//    public Failures( Log log ) {
//        this.log = log;
//    }
//
//    public void add( String message ) {
//        log.warn( message );
//        messages.add( message );
//    }
//
//    public void throwIfErrors() {
//        if( !messages.isEmpty() ) {
//            throw Unchecked.u( new MojoFailureException( messages.get( 0 ) ) );
//        }
//    }
//}
