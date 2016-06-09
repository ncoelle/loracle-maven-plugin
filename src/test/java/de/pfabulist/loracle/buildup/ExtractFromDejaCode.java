package de.pfabulist.loracle.buildup;

import com.esotericsoftware.minlog.Log;
import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import de.pfabulist.kleinod.nio.Filess;
import de.pfabulist.kleinod.nio.Pathss;
import de.pfabulist.kleinod.nio.UnzipToPath;
import de.pfabulist.loracle.license.AliasBuilder;
import de.pfabulist.loracle.license.LOracle;
import de.pfabulist.loracle.license.LicenseID;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static de.pfabulist.kleinod.text.Strings.newString;
import static de.pfabulist.nonnullbydefault.NonnullCheck._nn;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

public class ExtractFromDejaCode {

    AliasBuilder aliasBuilder = new AliasBuilder();

    public void go( LOracle lOracle ) {


        Path tmp = Pathss.getTmpDir( "foo" );
        Filess.createDirectories( tmp );

        UnzipToPath.unzipToPath( _nn( ExtractFromDejaCode.class.getResourceAsStream( "licenses.zip" ) ), tmp );

        Filess.list( _nn( tmp.resolve( "licenses" ) ) ).
                filter( f -> f.toString().endsWith( ".yml" ) ).
//                peek( System.out::println ).
        forEach( f -> {
    try {
        YamlReader reader = new YamlReader( new FileReader( f.toString() ) );
        Object object = reader.read();
        //System.out.println( object );
        Map map = (Map) object;

        Optional<LicenseID> key = lOracle.getByName( (String) map.get( "key" ) );
        Optional<LicenseID> name = lOracle.getByName( (String) map.get( "name" ) );
        Optional<LicenseID> shrt = lOracle.getByName( (String) map.get( "short_name" ) );

        Optional<LicenseID> res = propLicense( lOracle, _nn( (String) map.get( "key" ) ), key, name, shrt );

        if( res.isPresent() ) {
            addStuff( lOracle, res.get(), map );
//                            System.out.println( "\n" );
        } else {
            System.out.println( "hmmmm\n" );
//                            throw new IllegalArgumentException( "oops" );
        }

    } catch( FileNotFoundException | YamlException e ) {
        Log.warn( e.getMessage() );
    }
} );

        Pathss.deleteRecursive( tmp );
    }

    Optional<LicenseID> propLicense( LOracle lOracle, String key, Optional<LicenseID> one, Optional<LicenseID> two, Optional<LicenseID> three ) {

        if( one.isPresent() && two.isPresent() && three.isPresent() ) {
            if( one.get().equals( two.get() ) && two.get().equals( three.get() ) ) {
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
            if( two.get().equals( three.get() ) ) {
//                System.out.println( "2,3 set, old" );
                return two;
            }

//            System.out.println( "2,3 set, not equal" );
//            System.out.println( one + " : " + two + " : " + three );
            return Optional.empty();
        }

        if( one.isPresent() && !two.isPresent() && three.isPresent() ) {
            if( one.get().equals( three.get() ) ) {
//                System.out.println( "1,3 set, old" );
                return one;
            }

//            System.out.println( "1,3 set, not equal" );
//            System.out.println( one + " : " + two + " : " + three );
            return Optional.empty();
        }

        if( one.isPresent() && two.isPresent() && !three.isPresent() ) {
            if( one.get().equals( two.get() ) ) {
//                System.out.println( "1,2 set, old" );
                return two;
            }

//            System.out.println( "1,2 set, not equal" );
//            System.out.println( one + " : " + two + " : " + three );
            return Optional.empty();
        }

//        System.out.println( "1,2,3 not set, new: " + key );

        if( key.equals( "indiana-extreme" ) ) {
            key = "indiana-extreme-1.1.1";
        }

        try {
            return Optional.of( lOracle.newSingle( key, false ) );
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
            Optional<String> uu = aliasBuilder.normalizeUrl( u );
            if ( uu.isPresent()) {
                if( !uu.get().equals( "opensource.org/licenses/bsd-license" ) ) {
                    // bsd is time based
                    lOracle.addUrl( licenseID, uu.get() );
                } else {
                    System.out.printf( licenseID.toString() );
                    int i = 0;
                }
            }
        } );
        //System.out.println( "done" );
    }

}
