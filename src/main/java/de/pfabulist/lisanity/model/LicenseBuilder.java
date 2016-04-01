//package de.pfabulist.lisanity.model;
//
//import de.pfabulist.unchecked.Unchecked;
//import org.apache.maven.plugin.MojoFailureException;
//
///**
// * Copyright (c) 2006 - 2016, Stephan Pfab
// * SPDX-License-Identifier: BSD-2-Clause
// */
//
//public class LicenseBuilder {
//
//    private final Aliases aliases;
//
//    public LicenseBuilder( Aliases aliases ) {
//        this.aliases = aliases;
//    }
//
//    public LiLicense withName( String name )  {
//        try {
//            String known = aliases.getAlias( name ).orElseThrow( () -> new MojoFailureException( "unknown license: " + name ));
//            return new LiLicense( known, url, isSpdx );
//        } catch( MojoFailureException e ) {
//            throw Unchecked.u(e);
//        }
//    }
//}
