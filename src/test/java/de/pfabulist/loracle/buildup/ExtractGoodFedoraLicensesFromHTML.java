package de.pfabulist.loracle.buildup;

import com.esotericsoftware.minlog.Log;
import de.pfabulist.frex.Frex;
import de.pfabulist.kleinod.nio.Filess;
import de.pfabulist.loracle.license.LOracle;
import de.pfabulist.loracle.license.LicenseID;

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

public class ExtractGoodFedoraLicensesFromHTML {

    public static class FedoraInfo {
        public int count = 0;
        public String name = "";
        public String shortName = "";
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
                    then( Frex.anyBut( Frex.txt( '"' )).oneOrMore().var( "url" )).
                    then( Frex.any().zeroOrMore()).
                    buildCaseInsensitivePattern();

    Pattern namepat = txt( "<td>" ).
            then( Frex.anyBut( txt( '<' ) ).oneOrMore().var( "id" ) ).
            then( Frex.whitespace().zeroOrMore() ).
            then( txt( "</td>" ) ).buildPattern();


    public Stream<FedoraInfo> getFedoraInfo() {
        AtomicReference<FedoraInfo> fi = new AtomicReference<>( new FedoraInfo() );

        return Filess.lines( getClass().getResourceAsStream( "/de/pfabulist/loracle/fedora-goodlicenses-html-fragment.txt" ), Charset.forName( "UTF-8" ) ).
                map( l -> {
                    Matcher nameMather;

                    switch( fi.get().count ) {
                        case 0:
                            break;
                        case 1:
                            nameMather = namepat.matcher( l );
                            if ( nameMather.matches() ) {
                                fi.get().name = nameMather.group( "id" ).trim();
                            }
                            break;
                        case 2:
                            nameMather = namepat.matcher( l );
                            if ( nameMather.matches() ) {
                                fi.get().shortName = nameMather.group( "id" ).trim();
                                //System.out.println("---" + fi.get().longName );
                            }
                            break;
                        case 3:
                            fi.get().fsf = l.contains( "YES" ); // todo comments
                            break;
                        case 4:
                            fi.get().gpl2Compatible = l.contains( "YES" );
                            break;
                        case 5:
                            fi.get().gpl3Compatible = l.contains( "YES" );
                            break;
                        case 6:
                            Matcher matcher = pat.matcher( l );
                            if ( matcher.matches()) {
                                fi.get().url = matcher.group( "url" );
                            }
                            break;
                        case 7:
                            break;
                        default:
                            throw new IllegalArgumentException( "huh" );
                    }

                    FedoraInfo ret = fi.get();
                    ret.count++;

                    if( ret.count == 8 ) {
                        fi.set( new FedoraInfo() );
                    }
                    return ret;
                } ).
                filter( f -> f.count == 8 );
//                forEach( f -> System.out.println( f.name ) );

    }


    public void addFedoraInfo( LOracle lOracle ) {
        getFedoraInfo().forEach(

                f -> {

                    StringBuilder sb = new StringBuilder();

                    Optional<LicenseID> shrt = lOracle.getByName( f.shortName );
                    Optional<LicenseID> lng = lOracle.getByName( f.name );
                    Optional<LicenseID> url = lOracle.getByUrl( f.url );

                    String str = shrt.map( Object::toString ).orElse( "<" + f.shortName + ">" ) + " : " +
                            lng.map( Object::toString ).orElse( "<" + f.name + ">" ) + " : " +
                            url.map( Object::toString ).orElse( "<" + f.url + ">" );

                    sb.append(  f.shortName + " : " + f.name + " : " + f.url + "\n");
                    sb.append( str + "\n" );

                    if ( f.name.equals( "4Suite Copyright License" ) ) {
                        // this is just apache 1.1
//                        System.out.println("4suite is apache-1.1");
                        LicenseID apache11 = lOracle.getOrThrowByName( "apache-1.1" );
                        lOracle.addLongName( apache11, "4Suite Copyright License" );
                        lOracle.addUrl( apache11, f.url );
                        addFedoraInfo( lOracle, apache11, f );

                        return;
                    }

//                    if ( f.name.equals( "Adobe Glyph List License" ) ) {
//                        // this is variant of mit, found in spdx
//                        // but mit is extra, don't add short
//                        System.out.println("4suite is apache-1.1");
//                        addFedoraInfo( lOracle, lng.get(), f );
//
//                        return;
//                    }

                    if ( shrt.isPresent() && lng.isPresent() && shrt.get().equals( lng.get() )) {
                        sb.append( "   adding : " + f.url + "\n");
                        lOracle.addUrl( shrt.get(), f.url );

                        addFedoraInfo( lOracle, lng.get(), f );


                        return;
                    }

                    if ( lng.isPresent() && shrt.isPresent() ) {
                        // but not equal

                        if ( url.isPresent() && lng.get().equals( url.get() )) {
                            // this is variant of mit, found in spdx
                            // but mit is extra, don't add short
                            sb.append( "strange1 short\n");
                            addFedoraInfo( lOracle, lng.get(), f );

                            return;
                        }

                        sb.append( "---- " + lng.get() + " :::: " + shrt.get() + "\n");

                        // todo check on net
                        sb.append( "todo\n\n");
                        System.out.println( sb.toString());

                        return;

//                        if ( !guess.isEmpty()) {
//                            throw new IllegalArgumentException( "hah" );
//                        }
                    }

                    if ( lng.isPresent() && !shrt.isPresent()) {


                        Set<LicenseID> guess = lOracle.guessByName( f.shortName );
                        sb.append( "   short guess: " + guess + "\n");

                        if ( guess.isEmpty() ) {
                            // => new long name
                            sb.append(  "   new long name: " + lng.get() + " " + f.shortName + "\n" );
                            lOracle.addLongName( lng.get(), f.shortName );
                            addFedoraInfo( lOracle, lng.get(), f );
                            return;
                        }

                        if ( guess.contains( lng.get() )) {
                            sb.append( "   weak short, no changre\n");
                            addFedoraInfo( lOracle, lng.get(), f );
                            return;
                        }

                        if ( !guess.contains( lng.get()  )) {
                            sb.append(  "+++ " + lng.get() + " :::: " + f.shortName + "\n");
                        }

                        // todo check on net

                        sb.append( "todo\n\n");
                        System.out.println(sb.toString());

                        return;


                    }

                    if ( !lng.isPresent() && !shrt.isPresent() ) {
                        Set<LicenseID> guessLng = lOracle.guessByName( f.name );
                        Set<LicenseID> guessShrt = lOracle.guessByName( f.shortName );
                        sb.append(  "   guessing " + guessLng + " " + guessShrt + "\n");

                        LicenseID afl30 = lOracle.getOrThrowByName( "afl-3.0" );
                        if ( url.isPresent() && url.get().equals( afl30 )) {
                        //    System.out.println( "afl 3 with week long and shortname" );
                            addFedoraInfo( lOracle, afl30, f );
                            return;
                        }

//                        LicenseID afl30 = lOracle.getOrThrowByName( "afl-3.0" );
//                        if ( url.isPresent() && url.get().equals( afl30 )) {
//                            System.out.println( "afl 3 with week long and shortname" );
//                            addFedoraInfo( lOracle, afl30, f );
//                            return;
//                        }

                        if ( guessLng.isEmpty() && guessShrt.isEmpty() ) {
                            if ( !url.isPresent() ) {

                                if ( f.name.equals( "Affero General Public License 3.0 with Zarafa trademark exceptions" )) {
                          //          System.out.println( "   found new license: " );
                                    LicenseID newL = lOracle.newSingle( "AGPL-3.0 with Zarafa-trademark-exception", false );
                                    lOracle.addLongName( newL, "Affero General Public License 3.0 with Zarafa trademark exceptions" );
                                    addFedoraInfo( lOracle, newL, f );
                                    return;
                                }

                                if ( !f.shortName.contains( "GPL" ) &&
                                        !f.shortName.contains( "BSD" )  &&
                                        !f.shortName.equals( "Public Domain" ) )
                                {
                                    LicenseID newL = lOracle.newSingle( f.shortName, false );
                                    lOracle.addLongName( newL, f.name );
                                    addFedoraInfo( lOracle, newL, f );
                                    return;
                                }
//                                switch( f.shortName ) {
//                                    case "App-s2p" :
//                                    case "ARL" :
//                                    case "Bitbtex" :
//                                    case "CNRI" :
//                                    case "CRC32" :
//                                    case "Crystal Stacker" :
//                                    case "DMIT" :
//                                    case "EPICS" :
//                                    case "Jabber" :
//                                    case "Julius" :
//                                    case "Knuth" :
//                                    case "LOSLA" :
//                                    case "Lhcyr" :
//                                    case "LLGPL" :
//                                    case "Logica" :
//                                    case "mecab-ipadic" :
//                                    case "midnight" :
//                                    case "mod_macro" :
//                                    case "Logica" :
//                                        LicenseID newL = lOracle.newSingle( f.shortName, false );
//                                        lOracle.addLongName( newL, f.name );
//                                        addFedoraInfo( lOracle, newL, f );
//                                        break;
//                                    default:
//                                        // nix
//                                }

                                sb.append(  "   found new license: " + f.shortName + "\n");
                                sb.append(  "    not doing anything yet\n" );
                                sb.append(  "todo\n\n" );
                                System.out.println(sb.toString());
//                                LicenseID newL = lOracle.newSingle( f.shortName, false );
//                                lOracle.addLongName( newL, f.name );
//                                addFedoraInfo( lOracle, newL, f );
                                return;
                            } else {

                            }
                        }

                        if ( guessLng.size() == guessShrt.size() && guessLng.stream().filter( ll -> !guessShrt.contains( ll ) ).findAny().isPresent() ) {
                            sb.append(  "   known guessing now what" );
                            sb.append(  "todo\n\n" );
                            System.out.println(sb.toString());
                            return;
                        }


                    }

                    sb.append(  "todo\n\n" );
                    System.out.println(sb.toString());

                });

        //                    if ( shrt.isPresent() && !lng.isPresent()) {
//                        System.out.println( "new long " + shrt.get() + " : " + f.name );
//                    }
//
//                    AliasBuilder aliasBuilder = new AliasBuilder();
//                    if ( !shrt.isPresent() && lng.isPresent()) {
//                        System.out.println( "new short " + lng.get() + " : " + aliasBuilder.reduce( f.shortName ))   ;
//
////                        if ( lng.get().toString().startsWith( "apache" )) {
////                            lOracle.addLongName( lng.get(), f.shortName );
////                        }
////                        if ( lng.get().toString().startsWith( "gpl" )) {
////                            lOracle.addLongName( lng.get(), f.shortName );
////                        }
////
////                        if ( lng.get().toString().startsWith( "mpl-" )) {
////                            lOracle.addLongName( lng.get(), f.shortName );
////                        }
////
////                        if ( lng.get().toString().startsWith( "zpl-" )) {
////                            lOracle.addLongName( lng.get(), f.shortName );
////                        }
//                    }

    }

    private void addFedoraInfo( LOracle lOracle, LicenseID licenseID, FedoraInfo fedoraInfo ) {
        lOracle.getMore( licenseID ).fedoraApproved = Optional.of( Boolean.TRUE );
        try {
            lOracle.addUrl( licenseID, fedoraInfo.url );
        } catch( Exception e ) {
            System.out.println( fedoraInfo.url + " not an url");
        }

        lOracle.getMore( licenseID ).gpl2Compatible = Optional.of( fedoraInfo.gpl2Compatible );
        lOracle.getMore( licenseID ).gpl3Compatible = Optional.of( fedoraInfo.gpl3Compatible );

//        System.out.println("done\n");

    }
}
