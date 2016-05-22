//package de.pfabulist.ianalb.model.oracle;
//
//import de.pfabulist.frex.Frex;
//import de.pfabulist.frex.Single;
//import de.pfabulist.kleinod.collection.P;
//import de.pfabulist.kleinod.nio.Filess;
//import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
//
//import java.nio.charset.Charset;
//import java.util.concurrent.atomic.AtomicReference;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//import java.util.stream.Stream;
//
//import static de.pfabulist.frex.Frex.chars;
//import static de.pfabulist.frex.Frex.txt;
//
///**
// * Copyright (c) 2006 - 2016, Stephan Pfab
// * SPDX-License-Identifier: BSD-2-Clause
// */
//
//@SuppressFBWarnings(  )
//public class ExtractGoodFedoraLicenses {
//
//    public static class FedoraInfo {
//        public int count = 0;
//        public String name = "";
//        public boolean fsf;
//        public boolean gpl2Compatible;
//        public boolean gpl3Compatible;
//        public String url = "";
//
//    }
//
//    //<td><a class="external free" href="http://www.openoffice.org/licenses/sissl_license.html">http://www.openoffice.org/licenses/sissl_license.html</a>
//    private Pattern pat =
//            Frex.any().zeroOrMore().
//                    then( Frex.txt( "href=\"" ) ).
//                    then( Frex.anyBut( Frex.txt( '"' )).oneOrMore().var( "url" )).
//                    then( Frex.any().zeroOrMore()).
//                    buildCaseInsensitivePattern();
//
//    Pattern namepat = txt( "<td>" ).
//            then( Frex.anyBut( txt( '<' ) ).oneOrMore().var( "id" ) ).
//            then( Frex.whitespace().zeroOrMore() ).
//            then( txt( "</td>" ) ).buildPattern();
//
//
//    public Stream<FedoraInfo> getFedoraInfo() {
//        AtomicReference<FedoraInfo> fi = new AtomicReference<>( new FedoraInfo() );
//
//        return Filess.lines( getClass().getResourceAsStream( "/de/pfabulist/ianalb/fedora-goodlicenses-html-fragment.txt" ), Charset.forName( "UTF-8" ) ).
//                map( l -> {
//
//                    switch( fi.get().count ) {
//                        case 0:
//                            break;
//                        case 1:
//                            Matcher nameMather = namepat.matcher( l );
//                            if ( nameMather.matches() ) {
//                                fi.get().name = nameMather.group( "id" ).trim();
//                            }
//                            break;
//                        case 2:
//                            break;
//                        case 3:
//                            fi.get().fsf = l.contains( "YES" ); // todo comments
//                            break;
//                        case 4:
//                            fi.get().gpl2Compatible = l.contains( "YES" );
//                            break;
//                        case 5:
//                            fi.get().gpl3Compatible = l.contains( "YES" );
//                            break;
//                        case 6:
//                            Matcher matcher = pat.matcher( l );
//                            if ( matcher.matches()) {
//                                fi.get().url = matcher.group( "url" );
//                            }
//                            break;
//                        case 7:
//                            break;
//                        default:
//                            throw new IllegalArgumentException( "huh" );
//                    }
//
//                    FedoraInfo ret = fi.get();
//                    ret.count++;
//
//                    if( ret.count == 8 ) {
//                        fi.set( new FedoraInfo() );
//                    }
//                    return ret;
//                } ).
//                filter( f -> f.count == 8 );
////                forEach( f -> System.out.println( f.name ) );
//
//    }
//
//    public Stream<String> getFedoraIds() {
//
//        Pattern idpat = txt( "<td>" ).
//                then( Frex.anyBut( txt( '<' ) ).oneOrMore().var( "id" ) ).
//                then( Frex.whitespace().zeroOrMore() ).
//                then( txt( "</td>" ) ).buildPattern();
//
//        //System.out.println(idpat.pattern());
//
//        AtomicReference<Boolean> skip = new AtomicReference<>( true );
//
//        return
//                Filess.lines( getClass().getResourceAsStream( "/de/pfabulist/ianalb/fedora-goodlicenses-html-fragment.txt" ), Charset.forName( "UTF-8" ) ).
////                peek( l -> System.out.println( l + l.trim().equals( "<tr>" )) ).
//        filter( l -> {
//    if( l.trim().equals( "<tr>" ) ) {
//        skip.set( false );
//        return false;
//    } else if( skip.get() ) {
//        return false;
//    } else {
//        skip.set( true );
//        return true;
//    }
//} ).
////                peek( System.out::println ).
//        map( l -> {
//    Matcher matcher = idpat.matcher( l );
//    if( !matcher.matches() ) {
//        throw new IllegalArgumentException( "not an id" );
//    }
//
//    return matcher.group( "id" );
//} );
//
//    }
//
//}
