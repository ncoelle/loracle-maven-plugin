package de.pfabulist.loracle.buildup;

import com.esotericsoftware.minlog.Log;
import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import de.pfabulist.kleinod.nio.UnzipToPath;
import de.pfabulist.loracle.license.LOracle;
import de.pfabulist.loracle.license.LicenseID;
import de.pfabulist.loracle.license.SingleLicense;
import de.pfabulist.loracle.text.Normalizer;
import de.pfabulist.roast.nio.Files_;
import de.pfabulist.roast.nio.Paths_;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static de.pfabulist.roast.NonnullCheck._nn;
import static de.pfabulist.roast.lang.Class_.getClass__;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

@SuppressFBWarnings("REC_CATCH_EXCEPTION")
public class ExtractFromDejaCode {

    Normalizer normalizer = new Normalizer();

    public void go( LOracle lOracle ) {

        Path tmp = Paths_.getTmpDir( "foo" );
        Files_.createDirectories( tmp );

        UnzipToPath.unzipToPath( getClass__(this).getResourceAsStream_ot( "/de/pfabulist/loracle/buildup/licenses.zip" ), tmp );

                Files_.list( _nn( tmp.resolve( "licenses" ) ) ).
                filter( f -> f.toString().endsWith( ".yml" ) ).
                forEach( f -> {
                    try {

                        // System.out.printf( "YML ++++ " + f.toString() );

                        YamlReader reader = new YamlReader( new FileReader( f.toString() ) );
                        Object object = _nn( reader.read() );
                        //System.out.println( object );
                        Map map = (Map) object;

                        Optional<LicenseID> key = lOracle.getByName( _nn( (String) map.get( "key" ) ) ).noReason();
                        Optional<LicenseID> name = lOracle.getByName( _nn( (String) map.get( "name" ) ) ).noReason();
                        Optional<LicenseID> shrt = lOracle.getByName( _nn( (String) map.get( "short_name" ) ) ).noReason();

                        Optional<LicenseID> res = propLicense( lOracle, _nn( (String) map.get( "key" ) ), key, name, shrt );

                        if( res.isPresent() ) {
                            addStuff( lOracle, _nn( res.get() ), map );
//                            System.out.println( "\n" );
                        } else {
                            switch( (String) _nn( map.get( "name" ) ) ) {
                                case "Apache-licensed software (unknown license version)":
                                case "Apple Attribution License":
                                case "BSD-Original-UC": // actually spdx: bsd-4-clause-uc but not much extra info
                                case "Commercial Option": // a closed version ?
                                case "Commercial Contract": // a closed version ?
                                case "GNU Free Documentation License, any version":
                                case "GPL, no version":
                                case "LGPL, no version":
                                case "MPICH License":
                                case "Mozilla Public License":
                                case "Proprietary": // a closed version ?
                                case "Proprietary license": // a closed version ?
                                    // not precise: couldbe
                                    return;
                                default:
                                    System.out.println( "hmmmm\n" );
                                    propLicense( lOracle, _nn( (String) map.get( "key" ) ), key, name, shrt );
                            }
                        }

                    } catch( FileNotFoundException | YamlException e ) {
                        Log.warn( e.getMessage() );
                    }
                } );

        Files_.deleteRecursive_( tmp );
    }

    Optional<LicenseID> propLicense( LOracle lOracle, String key, Optional<LicenseID> one, Optional<LicenseID> two, Optional<LicenseID> three ) {

        if( one.isPresent() && two.isPresent() && three.isPresent() ) {
            if( _nn(one.get()).equals( two.get() ) && _nn(two.get()).equals( three.get() ) ) {
//                System.out.println( "3 set, old" );
//                System.out.println( one + " : " + two + " : " + three );
                return one;
            }

//            System.out.println( "3 set, not equal" );
//            System.out.println( one + " : " + two + " : " + three );
            return Optional.empty();
        }

        if( one.isPresent() && !two.isPresent() && !three.isPresent() ) {
//            System.out.println( "1 set, old" );
//            System.out.println( one + " : " + two + " : " + three );
            return one;
        }
        if( !one.isPresent() && two.isPresent() && !three.isPresent() ) {
//            System.out.println( "2 set, old" );
//            System.out.println( one + " : " + two + " : " + three );
            return two;
        }
        if( !one.isPresent() && !two.isPresent() && three.isPresent() ) {
//            System.out.println( "3 set, old" );
//            System.out.println( one + " : " + two + " : " + three );
            return three;
        }

        if( !one.isPresent() && two.isPresent() && three.isPresent() ) {
            if( _nn(two.get()).equals( three.get() ) ) {
//                System.out.println( "2,3 set, old" );
                return two;
            }

//            System.out.println( "2,3 set, not equal" );
//            System.out.println( one + " : " + two + " : " + three );
            return Optional.empty();
        }

        if( one.isPresent() && !two.isPresent() && three.isPresent() ) {
            if( _nn(one.get()).equals( three.get() ) ) {
//                System.out.println( "1,3 set, old" );
                return one;
            }

//            System.out.println( "1,3 set, not equal" );
//            System.out.println( one + " : " + two + " : " + three );
            return Optional.empty();
        }

        if( one.isPresent() && two.isPresent() && !three.isPresent() ) {
            if( _nn(one.get()).equals( two.get() ) ) {
//                System.out.println( "1,2 set, old" );
                return two;
            }

//            System.out.println( "1,2 set, not equal" );
//            System.out.println( one + " : " + two + " : " + three );
            return Optional.empty();
        }

//        System.out.println( "1,2,3 not set, new: " + key );

        String oldKey = key;
        if( key.equals( "indiana-extreme" ) ) {
            key = "indiana-extreme-1.1.1";
        }

        if( key.equals( "commercial-option" )
                || key.equals( "proprietary" )
                || key.equals( "commercial" ) ) {
            // what is this ?
            return Optional.empty();
        }

        // change spdx 1.0 ids to 2.0
        switch( key ) {
            case "gpl-2.0-classpath":
                return lOracle.getByName( "gpl-2.0 with classpath-exception-2.0" ).noReason();
            case "gpl-2.0-autoconf":
                return lOracle.getByName( "gpl-2.0 with Autoconf-exception-2.0" ).noReason();
            case "gpl-2.0-bison":
                return lOracle.getByName( "gpl-2.0 with Bison-exception-2.2" ).noReason();
            case "gpl-2.0-clisp":
                return lOracle.getByName( "gpl-2.0 with CLISP-exception-2.0" ).noReason();
            case "gpl-2.0-font":
                return lOracle.getByName( "gpl-2.0 with Font-exception-2.0" ).noReason();
            case "gpl-2.0-freertos":
                return lOracle.getByName( "gpl-2.0 with freertos-exception-2.0" ).noReason();
            case "gpl-2.0-gcc":
                return lOracle.getByName( "gpl-2.0 with GCC-exception-2.0" ).noReason();
            case "gpl-2.0-libtool":
                return lOracle.getByName( "gpl-2.0 with Libtool-exception" ).noReason();
            case "gpl-2.0-openssl":
                return lOracle.getByName( "gpl-2.0 with openvpn-openssl-exception" ).noReason(); // todo check
            case "gpl-2.0-uboot":
                return lOracle.getByName( "gpl-2.0 with u-boot-exception-2.0" ).noReason();

            case "gpl-2.0-plus":
                return lOracle.getByName( "gpl-2.0+" ).noReason();
            case "gpl-2.0-plus-gcc":
                return lOracle.getByName( "gpl-2.0+ with GCC-exception-2.0" ).noReason();

            case "gpl-3.0-autoconf":
                return lOracle.getByName( "gpl-3.0 with Autoconf-exception-3.0" ).noReason();
            case "gpl-3.0-bison":
                return lOracle.getByName( "gpl-3.0 with Bison-exception-2.2" ).noReason();
            case "gpl-3.0-font":
                return lOracle.getByName( "gpl-3.0 with Font-exception-2.0" ).noReason();
            case "gpl-3.0-gcc":
                return lOracle.getByName( "gpl-3.0 with GCC-exception-3.1" ).noReason();

            case "gpl-3.0-plus":
                return lOracle.getByName( "gpl-3.0+" ).noReason();

            case "lgpl-2.0-plus":
                return lOracle.getByName( "lgpl-2.0+" ).noReason();
            case "lgpl-2.0-plus-gcc":
                return lOracle.getByName( "lgpl2.0 with GCC-exception-2.0" ).noReason();

            case "lgpl-2.1-fltk":
                return lOracle.getByName( "lgpl-2.1 with FLTK-exception" ).noReason();

            case "lgpl-2.1-plus":
                return lOracle.getByName( "lgpl-2.1+" ).noReason();

            case "lgpl-3.0-plus":
                return lOracle.getByName( "lgpl-3.0" ).noReason();
            case "lgpl-3.0-plus-openssl":
                return lOracle.getByName( "lgpl-3.0+ with openvpn-openssl-exception" ).noReason(); // todo check

            default:
        }

        try {

            System.out.println( "+++ deja ++ " + key + "    " + "/de/pfabulist/loracle/deja/" + oldKey + ".LICENSE");
            try( InputStream is = _nn(ExtractFromDejaCode.class.getResourceAsStream( "/de/pfabulist/loracle/deja/" + oldKey + ".LICENSE" ))) {
                Files_.copy( is, Paths_.get__( "src/main/resources/de/pfabulist/loracle/urls/" + key + ".txt" ).toAbsolutePath_(), StandardCopyOption.REPLACE_EXISTING );
            }
            LicenseID ret = lOracle.newSingle( key, false );
            lOracle.getMore( ret ).attributes.setFromDeja();    //lOracle.addUrlContent(  );
            return Optional.of( ret );
        } catch( Exception e ) {
            System.out.println( "known (probably guess) " + key );
            return Optional.empty();
        }
    }

    private void addStuff( LOracle lOracle, LicenseID licenseID, Map map ) {
        Optional.ofNullable( (String) map.get( "key" ) ).ifPresent( s -> lOracle.addLongName( licenseID, s ) );
        Optional.ofNullable( (String) map.get( "name" ) ).ifPresent( s -> lOracle.addLongName( licenseID, s ) );
        Optional.ofNullable( (String) map.get( "short_name" ) ).ifPresent( s -> lOracle.addLongName( licenseID, s ) );

        if( !licenseID.equals( lOracle.getOrThrowByName( "cddl-1.1" ) ) ) {
            Optional.ofNullable( (List<String>) map.get( "text_urls" ) ).ifPresent( l -> l.forEach( u -> lOracle.addUrl( licenseID, u ) ) );
        }
        Optional.ofNullable( (String) map.get( "osi_url" ) ).ifPresent( u -> {
            Optional<String> uu = normalizer.normalizeUrl( u );
            if( uu.isPresent() ) {
                if( !_nn( uu.get() ).equals( "opensource.org/licenses/bsd-license" ) ) {
                    // bsd is time based
                    lOracle.addUrl( licenseID, _nn(uu.get()) );
                } else {
                    System.out.printf( licenseID.toString() );
                    int i = 0;
                }
            }
        } );
        //System.out.println( "done" );

        if ( lOracle.getMore( licenseID ).urls.isEmpty() ) {
            if ( licenseID instanceof SingleLicense ) {
                int i = 0;
            }
        }

    }

}
