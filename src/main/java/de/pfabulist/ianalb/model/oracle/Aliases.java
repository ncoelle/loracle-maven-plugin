package de.pfabulist.ianalb.model.oracle;

import de.pfabulist.frex.Frex;
import de.pfabulist.frex.Single;
import de.pfabulist.ianalb.model.license.IBLicense;
import de.pfabulist.ianalb.model.license.Licenses;
import de.pfabulist.kleinod.nio.Filess;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static de.pfabulist.frex.Frex.txt;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

public class Aliases {

    private final Map<Pattern, IBLicense> aliases = new HashMap<>();
    private final Map<IBLicense, Pattern> aliasesBack = new HashMap<>();
    private final Licenses licenses;
    private final AliasBuilder aliasBuilder = new AliasBuilder();

    public Aliases( Licenses licenses ) {
        this.licenses = licenses;



//        SimplePat simplePat = new SimplePat();
//
////        add( "BSD-2-Clause", "The BSD 2-Clause License");
////        add( "BSD-3-Clause", "New BSD License"); // todo minlog says this 2 clause but references 3 clause
////        add( "GPL-2.0", "GPL 2.0" );
////        add( "LGPL-3.0", "GNU Lesser Public License" ); // todo: alias is actually "or later"
////        add( "CC-BY-2.5");
////        add( "Apache-2.0", "The Apache Software License, Version 2.0",
////                           "Apache Software License - Version 2.0",
////                           "Apache License, Version 2.0");
////        add( "MIT", "MIT License"); // todo version ? is name enough
////        add( "EPL-1.0", "Eclipse Public License 1.0" );
////
//
//        Frex fill = Frex.or( txt( ',' ), txt( '-' ), Frex.whitespace() ).zeroOrMore();
//
//        add( "Apache-2.0",
//             simplePat.simple( maybe( "The" ),
//                               s( "Apache" ),
//                               maybe( "Software" ),
//                               maybe( "License" ),
//                               maybe( "Version" ),
//                               s( "2.0" ) ).
//                     buildCaseInsensitivePattern() );
//
//        add( "BSD-3-Clause",
//             simplePat.simple( maybe( "The" ),
//                               s( "New" ),
//                               s( "BSD" ),
//                               maybe( "License" ) ).
//                     buildCaseInsensitivePattern() );
//
//        add( "EPL-1.0",
//             simplePat.simple( maybe( "The" ),
//                               s( "Eclipse" ),
//                               maybe( "Public" ),
//                               maybe( "License" ),
//                               maybe( "v" ),
//                               maybe( "Version" ),
//                               s( "1.0" ) ).
//                     buildCaseInsensitivePattern() );
//
//        add( "LGPL-3.0",
//             fill.then( txt( "GNU" ) ).then( fill ).
//                     then( txt( "Lesser" ) ).then( fill ).
//                     then( txt( "Public" ) ).then( fill ).
//                     then( txt( "License" ).zeroOrOnce() ).then( fill ).
//                     then( txt( "3.0" ).zeroOrOnce() ).
//                     then( fill ).buildCaseInsensitivePattern() );
//
//        add( "MIT",
//             fill.then( txt( "MIT" ) ).then( fill ).
//                     then( txt( "License" ).zeroOrOnce() ).then( fill ).
//                     buildCaseInsensitivePattern() );
//
//        add( "JSON",
//             txt( "The JSON License" ).buildCaseInsensitivePattern() );
        Single doubleQuote = Frex.txt( '"' );
        Frex anyButDoubleQuote = Frex.anyBut( doubleQuote ).oneOrMore();
        Pattern idpat = anyButDoubleQuote.
                then( Frex.txt( "\"./" ) ).
                then( anyButDoubleQuote.var( "id" ) ).
                then( Frex.txt( ".html\"" ) ).
                then( Frex.anyBut( Frex.txt( '>')).zeroOrMore() ).
                then( Frex.txt( '>' ) ).
                then( Frex.anyBut( Frex.txt( '<')).oneOrMore().var( "text" ) ).
                then( Frex.any().zeroOrMore()).
                buildPattern();


        AtomicReference<Boolean> skip = new AtomicReference<>( true );

        Filess.lines( getClass().getResourceAsStream( "/de/pfabulist/ianalb/spdx-licenses-html-fragment.txt" ), Charset.forName( "UTF-8" ) ).
                filter( l -> isLineAfterTR( skip, l ) ).
                forEach( l -> {
                    Matcher matcher = idpat.matcher( l );
                    if ( !matcher.matches()) {
                        throw new IllegalArgumentException( "huh" );
                    }

                    add( matcher.group( "id" ), aliasBuilder.buildAlias( matcher.group( "text" ) ).buildCaseInsensitivePattern());

//                    System.out.println( matcher.group( "id" ) + " ---- " +
//                                                matcher.group( "text" ) + " ---- " +
//                                                aliasBuilder.buildAlias( matcher.group( "text" ) ).buildCaseInsensitivePattern());
                } );

        add( "AGPL-3.0", aliasBuilder.buildAlias( "Affero General Public License Version 3.0" ).buildCaseInsensitivePattern());
        add( "AGPL-3.0+", aliasBuilder.buildAlias( "Affero General Public License Version 3.0 or later" ).buildCaseInsensitivePattern());
        add( "GPL-1.0+", aliasBuilder.buildAlias( "GNU General Public License v1.0 or later" ).buildCaseInsensitivePattern());
        add( "GPL-2.0+", aliasBuilder.buildAlias( "GNU General Public License v2.0 or later" ).buildCaseInsensitivePattern());
        add( "GPL-2.0 WITH Classpath-exception-2.0", aliasBuilder.buildAlias( "GNU General Public License v2.0 only, with Classpath exception" ).buildCaseInsensitivePattern());
        add( "CPAL-1.0", aliasBuilder.buildAlias( "CPAL License 1.0" ).buildCaseInsensitivePattern());
        add( "Entessa", aliasBuilder.buildAlias( "Entessa Public License" ).buildCaseInsensitivePattern());

    }

    void add( String name, Pattern pat ) {
        IBLicense li = licenses.getOrThrowByName( name );
        if ( aliasesBack.containsKey( li )) {
            Pattern old = aliasesBack.get( li );
            Pattern nw = Pattern.compile( "(" + old.toString() + ")|(" + pat.toString() + ")", Pattern.CASE_INSENSITIVE );

            aliasesBack.put( li, nw );
            aliases.remove( old );
            aliases.put( nw, li );

        } else {
            aliases.put( pat, li );
            aliasesBack.put( li, pat );
        }
    }

    Optional<IBLicense> getLicense( String name ) {
        String reduced = aliasBuilder.reduce( name );
        return aliases.entrySet().stream().filter( e -> e.getKey().matcher( reduced ).matches() ).findFirst().map( Map.Entry::getValue );
    }

    private boolean isLineAfterTR( AtomicReference<Boolean> skip, String l ) {
        if( l.trim().equals( "<tr>" ) ) {
            skip.set( false );
            return false;
        } else if( skip.get() ) {
            return false;
        } else {
            skip.set( true );
            return true;
        }
    }


}
