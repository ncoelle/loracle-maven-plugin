package de.pfabulist.ianalb.model.license;

import de.pfabulist.frex.Frex;
import de.pfabulist.ianalb.model.oracle.ExtractSpdxLicenses;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static de.pfabulist.frex.Frex.txt;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

public class Licenses {

    static Frex word = Frex.or( Frex.alphaNum(), txt( '-' ).or( txt( '.' ) ) ).oneOrMore();
    static Pattern namePattern =
            word.var( "name" ).
                    then( txt( '+' ).var( "plus" ).zeroOrOnce() ).
                    then( Frex.whitespace().
                            then( txt( "WITH" ) ).
                            then( Frex.whitespace() ).
                            then( word.var( "exception" ) ).zeroOrOnce() ).
                    buildPattern();

    private final Map<String, SingleSPDXLicense> byName = new HashMap<>();
    private final Map<String, SingleSPDXLicense> byUrl = new HashMap<>();
    private final SPDXExceptions exceptions = new SPDXExceptions();

    public Licenses() {
        new ExtractSpdxLicenses().getSPDXIds().forEach(
                l -> addSpdx( l, "spdx.org/licenses/" + l + ".html" ) );

        // todo
        //add( "CDDL + GPLv2 with classpath exception", "https://glassfish.dev.java.net/nonav/public/CDDL+GPL.html" );
    }

//    private void add( String name, String url ) {
//        IBLicense li = new IBLicense( name, url, false );
//        byName.put( name, li );
//        byUrl.put( url, li );
//    }

    void addSpdx( String name, String url ) {
        SingleSPDXLicense li = new SingleSPDXLicense( name );
        byName.put( name, li );
        byUrl.put( url, li );
    }

//    public Collection<IBLicense> get() {
//        return licenses;
//    }

    public IBLicense getOrThrowByName( String name ) {
        return getByName( name ).orElseThrow( () -> new IllegalArgumentException( "no such license: " + name ) );
    }

    public Optional<IBLicense> getByName( String nameExpr ) {

        Matcher matcher = namePattern.matcher( nameExpr );
        if( !matcher.matches() ) {
            // todo other licenes
            return Optional.empty();
        }

        String name = matcher.group( "name" );
        boolean plus = matcher.group( "plus" ) != null;
        Optional<String> exception = Optional.ofNullable( matcher.group( "exception" ) );

        if( exception.isPresent() ) {
            if( !exceptions.isSpdxException( exception.get() ) ) {
                return Optional.empty();
            }
        }

        Optional<SingleSPDXLicense> ret = Optional.ofNullable( byName.get( name ) );

        return ret.
                map( l -> plus ? l.orLater() : l ).
                map( l -> { if ( exception.isPresent()) { return new SPDXWith( l, exception.get()); } else { return  l;}} );

    }

    public Optional<IBLicense> getByUrl( String url ) {
        return byUrl.keySet().stream().filter( url::contains ).findAny().map( byUrl::get );
    }
}
