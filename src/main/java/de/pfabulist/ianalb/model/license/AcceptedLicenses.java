//package de.pfabulist.ianalb.model.license;
//
//import de.pfabulist.ianalb.model.oracle.SPDXParser;
//
//import java.util.*;
//
///**
// * Copyright (c) 2006 - 2016, Stephan Pfab
// * SPDX-License-Identifier: BSD-2-Clause
// */
//
//public class AcceptedLicenses {
//
//    private Map<String, IBLicense> other = new HashMap<>();
//    private final SPDXParser parser;
//
//    public AcceptedLicenses( SPDXParser parser ) {
//        this.parser = parser;
//
//        other.put( "AOP-PD", new OtherLicense( "AOP-PD" ) );
//    }
//
//    public Optional<IBLicense> get( String name ) {
//        try {
//            return Optional.of( parser.parse( name ) );
//        } catch( Exception e ) {
//            return Optional.ofNullable( other.get( name ) );
//        }
//    }
//
//    public IBLicense getOrThrow( String name ) {
//        return get( name ).orElseThrow( () -> new IllegalArgumentException( "no such accepted license " + name ) );
//    }
//}
