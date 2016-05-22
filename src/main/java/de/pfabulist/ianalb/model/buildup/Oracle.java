//package de.pfabulist.ianalb.model.buildup;
//
//import de.pfabulist.ianalb.model.license.IBLicense;
//import de.pfabulist.loracle.license.AliasBuilder;
//import de.pfabulist.ianalb.model.oracle.ExtractSpdxLicenses;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Optional;
//import java.util.regex.Pattern;
//
///**
// * Copyright (c) 2006 - 2016, Stephan Pfab
// * SPDX-License-Identifier: BSD-2-Clause
// */
//
//public class Oracle {
//
//    private Map<String, IBLicense> byName = new HashMap<>();
//
//    private final Map<Pattern, IBLicense> aliases = new HashMap<>();
//    private final Map<IBLicense, Pattern> aliasesBack = new HashMap<>();
//
//    private final AliasBuilder aliasBuilder = new AliasBuilder();
//
//    //private List<Pattern> exceptions = new ArrayList<>();
//
//    Oracle( String testing ) {}
//
//
//    public Oracle() {
//        importSPDXLicenses();
//        importSPDXExceptions();
//    }
//
//
//    final void importSPDXLicenses() {
//        new ExtractSpdxLicenses().addSPDX( this );
//    }
//    final void importSPDXExceptions() {
//        //exceptions = new ExtractSPDXExceptions().getExceptions();
//    }
//
//    public void addLicense( IBLicense license ) {
//
//        if ( byName.containsKey( license.getName() )) {
//            throw new IllegalArgumentException( "license exists already: " + license.getName() );
//        }
//
//        byName.put( license.getName(), license );
//    }
//
//    public void addAlias( String name, Pattern pat ) {
//        IBLicense license = Optional.ofNullable( byName.get( name ) ).orElseThrow( () -> new IllegalArgumentException( "no such license: " + name ) );
//
//        if ( aliasesBack.containsKey( license )) {
//            Pattern old = aliasesBack.get( license );
//            Pattern nw = Pattern.compile( "(" + old.toString() + ")|(" + pat.toString() + ")", Pattern.CASE_INSENSITIVE );
//
//            aliasesBack.put( license, nw );
//            aliases.remove( old );
//            aliases.put( nw, license );
//
//        } else {
//            aliases.put( pat, license );
//            aliasesBack.put( license, pat );
//        }
//
//    }
//
//    public Optional<IBLicense> getPrecise( String id ) {
//        return Optional.ofNullable( byName.get( id ) );
//    }
//
//    public Optional<IBLicense> guessByName( String name ) {
//        String reduced = aliasBuilder.reduce( name );
//        return aliases.entrySet().stream().filter( e -> e.getKey().matcher( reduced ).matches() ).findFirst().map( Map.Entry::getValue );
//    }
//
//}
