package de.pfabulist.loracle.buildup;

import de.pfabulist.frex.Frex;
import de.pfabulist.loracle.license.LOracle;
import de.pfabulist.loracle.license.LicenseID;
import de.pfabulist.roast.nio.Filess;

import java.nio.charset.Charset;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static de.pfabulist.frex.Frex.txt;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

public class ExtractBadFedoraLicensesFromHTML {

    public static class FedoraInfo {
        public int count = 0;
        public String name = "";
        public boolean fsf;
        public boolean gpl2Compatible;
        public boolean gpl3Compatible;
        public String url = "";

        @Override
        public String toString() {
            return "FedoraInfo{" +
                    "name='" + name + '\'' +
                    ", fsf=" + fsf +
                    ", gpl2Compatible=" + gpl2Compatible +
                    ", gpl3Compatible=" + gpl3Compatible +
                    ", url='" + url + '\'' +
                    '}';
        }
    }

    //<td><a class="external free" href="http://www.openoffice.org/licenses/sissl_license.html">http://www.openoffice.org/licenses/sissl_license.html</a>
    private Pattern pat =
            Frex.any().zeroOrMore().
                    then( Frex.txt( "href=\"" ) ).
                    then( Frex.anyBut( Frex.txt( '"' ) ).oneOrMore().var( "url" ) ).
                    then( Frex.any().zeroOrMore() ).
                    buildCaseInsensitivePattern();

    Pattern namepat = txt( "<td>" ).
            then( Frex.anyBut( txt( '<' ) ).oneOrMore().var( "id" ) ).
            then( Frex.whitespace().zeroOrMore() ).
            then( txt( "</td>" ) ).buildPattern();

    public Stream<FedoraInfo> getFedoraInfo() {
        AtomicReference<FedoraInfo> fi = new AtomicReference<>( new FedoraInfo() );

        return Filess.lines( getClass().getResourceAsStream( "/de/pfabulist/loracle/fedora-bad-html-fragment.txt" ), Charset.forName( "UTF-8" ) ).
                map( l -> {
                    Matcher nameMather;

                    switch( fi.get().count ) {
                        case 0:
                            break;
                        case 1:
                            nameMather = namepat.matcher( l );
                            if( nameMather.matches() ) {
                                fi.get().name = nameMather.group( "id" ).trim();
                            }
                            break;
                        case 2:
                            break;
                        case 3:
                            Matcher matcher = pat.matcher( l );
                            if( matcher.matches() ) {
                                fi.get().url = matcher.group( "url" );
                            }
                            break;
                        case 4:
                        case 5:
                            break;
                        default:
                            throw new IllegalArgumentException( "huh" );
                    }

                    FedoraInfo ret = fi.get();
                    ret.count++;

                    if( ret.count == 6 ) {
                        fi.set( new FedoraInfo() );
                    }
                    return ret;
                } ).
                filter( f -> f.count == 6 );

    }

    public void addFedoraInfo( LOracle lOracle ) {
        getFedoraInfo().forEach(

                f -> {

                    StringBuilder sb = new StringBuilder();

//                    Optional<LicenseID> shrt = lOracle.getByName( f.shortName );
                    Optional<LicenseID> lng = lOracle.getByName( f.name ).noReason();
                    Optional<LicenseID> url = lOracle.getByUrl( f.url ).noReason();

                    String str =
                            lng.map( Object::toString ).orElse( "<" + f.name + ">" ) + " : " +
                                    url.map( Object::toString ).orElse( "<" + f.url + ">" );

                    sb.append( " bad: " + f.name + " : " + f.url + "\n" );
                    sb.append( str + "\n" );

                    System.out.println( sb.toString() + "\n" );

                    if( lng.isPresent() && url.isPresent() && lng.get().equals( url.get() ) ) {
                        lOracle.getAttributes( lng.get() ).setFedoraApproved( false );
                        return;
                    }

                    if ( !lng.isPresent() && !url.isPresent()) {
                        Set<LicenseID> gn = lOracle.guessByName( f.name );
                        Set<LicenseID> gu = lOracle.guessByUrl( f.url );
                        System.out.println( "name  " + lOracle.guessByName( f.name ));
                        System.out.println( "url   " + lOracle.guessByUrl( f.url ));

                        if ( gn.isEmpty() && gu.isEmpty() ) {
                            System.out.println("new bad");
                            LicenseID nn = lOracle.newSingle( f.name, false );
                            lOracle.getMore( nn ).attributes.setFromFedora();
                            if ( !f.url.isEmpty() ) {
                                lOracle.addUrl( nn, f.url );
                            }
                            lOracle.getAttributes( nn  ).setFedoraApproved( false );
                        }
                    }

                    return;

                });

    }
}