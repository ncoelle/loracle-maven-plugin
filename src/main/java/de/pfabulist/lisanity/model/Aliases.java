package de.pfabulist.lisanity.model;

import de.pfabulist.frex.Frex;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

import static de.pfabulist.frex.Frex.txt;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

public class Aliases {

    private final Map<Pattern, LiLicense> aliases = new HashMap<>();
    private final Licenses licenses;

    public Aliases( Licenses licenses ) {
        this.licenses = licenses;

//        add( "BSD-2-Clause", "The BSD 2-Clause License");
//        add( "BSD-3-Clause", "New BSD License"); // todo minlog says this 2 clause but references 3 clause
//        add( "GPL-2.0", "GPL 2.0" );
//        add( "LGPL-3.0", "GNU Lesser Public License" ); // todo: alias is actually "or later"
//        add( "CC-BY-2.5");
//        add( "Apache-2.0", "The Apache Software License, Version 2.0",
//                           "Apache Software License - Version 2.0",
//                           "Apache License, Version 2.0");
//        add( "MIT", "MIT License"); // todo version ? is name enough
//        add( "EPL-1.0", "Eclipse Public License 1.0" );
//

        Frex fill = Frex.or( txt( ',' ), txt( '-' ), Frex.whitespace() ).zeroOrMore();

        add( "Apache-2.0",
             fill.then( txt( "The" ).zeroOrOnce() ).then( fill ).
                     then( txt( "Apache" ) ).then( fill ).
                     then( txt( "Software" ).zeroOrOnce() ).then( fill ).
                     then( txt( "License" ).zeroOrOnce() ).then( fill ).
                     then( txt( "Version" ).zeroOrOnce() ).then( fill ).
                     then( txt( "2.0" ) ).then( fill ).
                     buildCaseInsensitivePattern() );

        add( "BSD-3-Clause",
             fill.then( txt( "The" ).zeroOrOnce() ).then( fill ).
                     then( txt( "New" ) ).then( fill ).
                     then( txt( "BSD" ) ).then( fill ).
                     then( txt( "License" ).zeroOrOnce() ).then( fill ).
                     buildCaseInsensitivePattern() );

        add( "EPL-1.0",
             fill.then( txt( "The" ).zeroOrOnce() ).then( fill ).
                     then( txt( "Eclipse" ) ).then( fill ).
                     then( txt( "Public" ).zeroOrOnce() ).then( fill ).
                     then( txt( "License" ).zeroOrOnce() ).then( fill ).
                     then( txt( "v" ).zeroOrOnce() ).then(fill).
                     then( txt( "Version" ).zeroOrOnce() ).then( fill ).
                     then( txt( "1.0")).
                     buildCaseInsensitivePattern() );

        add( "LGPL-3.0",
             fill.then( txt( "GNU" ) ).then( fill ).
                     then( txt( "Lesser" ) ).then( fill ).
                     then( txt( "Public" ) ).then( fill ).
                     then( txt( "License" ).zeroOrOnce() ).then( fill ).
                     then( txt( "3.0" ).zeroOrOnce() ).
                     then( fill ).buildCaseInsensitivePattern() );

        add( "MIT",
             fill.then( txt( "MIT" ) ).then( fill ).
                     then( txt( "License" ).zeroOrOnce() ).then( fill ).
                     buildCaseInsensitivePattern() );

        add( "JSON",
             txt( "The JSON License" ).buildCaseInsensitivePattern() );
    }

//    public Optional<String> getAlias( String name ) {
//        return Optional.ofNullable( aliases.get( name ) );
//    }

    //    void add( String name, String ... other ) {
//        LiLicense li = licenses.getOrThrowByName( name );
//        aliases.put( name, li );
//        Arrays.stream( other ).forEach( o -> aliases.put( o, li ) );
//    }
    void add( String name, Pattern pat ) {
        LiLicense li = licenses.getOrThrowByName( name );
        aliases.put( pat, li );
        //Arrays.stream( other ).forEach( o -> aliases.put( o, li ) );
    }

    Optional<LiLicense> getLicense( String name, Failures failures ) {
        return aliases.entrySet().stream().filter( e -> e.getKey().matcher( name ).matches() ).findFirst().map( Map.Entry::getValue );
    }

}
