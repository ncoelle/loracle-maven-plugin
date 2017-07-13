package de.pfabulist.loracle.license;

import de.pfabulist.loracle.fulltext.TextToLicense;
import de.pfabulist.loracle.text.Normalizer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Copyright (c) 2006 - 2017, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

public class LicenseFromText {

    private final LOracle lOracle;
    private final Findings log;

    public LicenseFromText( LOracle lOracle, Findings log ) {
        this.lOracle = lOracle;
        this.log = log;
    }

//    public MappedLicense getLicense( String txt ) {
//        String norm = new Normalizer().norm( txt );
//
//        return licenseTextsToLicenses.stream().
//                map( f -> match( f, norm ) ).
//                filter( Optional::isPresent ).
//                findFirst().map( l -> MappedLicense.of( l, "by full text" ) ).
//                orElse( MappedLicense.empty() );
//
//    }

    // todo exceptions

    public MappedLicense getLicense( String txt ) {

    //    System.out.println( "  +++ frsg. txt " + txt.substring( 0, Math.min( txt.length(), 100 ) ));
      //  licenseTextsToLicenses.forEach( f -> System.out.println( "        ++ " + f.getLicense()) );
        final String txtn = Normalizer.norm( txt );

        Optional<String> lic = licenseTextsToLicenses.stream().
                filter( f -> f.matches( txtn ) ).
                map( TextToLicense::getLicense ).
                findFirst();


        //System.out.println( "     ++ "+ lic.isPresent());

       if ( !lic.isPresent() ) {
           return MappedLicense.empty();
       }

       LicenseID id = new FuzzyParser( lOracle ).getExtended( lic.get() );

        return MappedLicense.of( id, "by full license text" );
    }

//                for ( int i = 0; i < licenseTextsToLicenses.size(); i++ ) {
//                    if ( licenseTextsToLicenses[i].)
//                }
//
//                List<P<String, MappedLicense>> textPieceToLicense = new ArrayList<>();
//                textPieceToLicense.add( P.of( Normalizer.norm( txt ), MappedLicense.empty() ));
//
//                And and = new And( lOracle, log, true );
//
//                return licenseTextsToLicenses.stream().
//                        map( f -> match3( f, textPieceToLicense ) ).
//                        filter( Optional::isPresent ).
//                        map( l -> MappedLicense.of( l, "by full text" ) ).
//                        reduce( and::and ).
//                        orElseGet( MappedLicense::empty );




    static List<TextToLicense> licenseTextsToLicenses = new ArrayList<>();


    // todo used ?
//    private Optional<LicenseID> match( SimpleMatch2 simpl, String in ) {
//        int pos = 0;
//        for( int i = 0; i < simpl.getFragments().size(); i++ ) {
//            String frag = _nn( simpl.getFragments().get( i ) );
//            int nextPos = in.indexOf( frag, pos );
//            if( nextPos < 0 || nextPos > pos + _nn( simpl.getMaxAny().get( i ) ) ) {
////                if ( nextPos > 0 ) {
////                    String duh = in.substring( pos );
////                    String foo = in.substring( nextPos );
////                    int g = 0;
////                }
//                return Optional.empty();
//            }
//
//            pos = nextPos + frag.length();
//        }
//
//        return Optional.of( lOracle.getOrThrowByName( simpl.getLicense() ) );
//    }

//    private  Optional<LicenseID> match3( SimpleMatch2 frag, List<P<String, MappedLicense>> txts ) {
//
//        WorkOnOne<P<String, MappedLicense>> woo = new WorkOnOne<>( txts.stream() );
//        List<P<String, MappedLicense>> ll = woo.work( t -> match3( frag, t ),
//                                                      ( rr, t ) -> Splitter.cutMiddle( t.i0, rr.i0, rr.i1, lOracle.getByName( frag.getLicense() ) ) );
//
//        System.out.println("+++++ match 3" + frag.getLicense() + " tried ");
//        txts.forEach( e -> System.out.println( "    =" + e.i0.substring( 0, Math.max( e.i0.length(), 100 ) ) ));
//        if( woo.foundp() ) {
//            txts.clear();
//            txts.addAll( ll );
//            return Optional.of( lOracle.getOrThrowByName( frag.getLicense() ) );
//        }
//
//        System.out.println("+++++ failed ");
//        return Optional.empty();
//
//    }
//
//    private Optional<P<Integer, Integer>> match3( SimpleMatch2 simpl, P<String, MappedLicense> pair ) {
//
//        pair.with( ( t, l ) -> true );
//
//        if( pair.i1.isPresent() ) {
//            return Optional.empty();
//        }
//
//        String txt = pair.i0;
//
//        int startPos = Integer.MAX_VALUE;
//        int pos = 0;
//        for( int i = 0; i < simpl.getFragments().size(); i++ ) {
//            String frag = _nn( simpl.getFragments().get( i ) );
//            int nextPos = txt.indexOf( frag, pos );
//            if( nextPos < 0 || nextPos > pos + _nn( simpl.getMaxAny().get( i ) ) ) {
////                if ( nextPos > 0 ) {
////                    String duh = in.substring( pos );
////                    String foo = in.substring( nextPos );
////                    int g = 0;
////                }
//                return Optional.empty();
//            }
//
//            if( nextPos < startPos ) {
//                startPos = nextPos;
//            }
//
//            pos = nextPos + frag.length();
//        }
//
//        return Optional.of( P.of( startPos, pos ) );
//    }
//
//    private boolean findBSD3( String norm ) {
//        int pos = norm.indexOf( BSD3 );
//        if( pos >= 0 ) {
//            pos = norm.indexOf( BSD3_part2, pos + BSD3.length() );
//            if( pos > -1 ) {
//                return true;
//            }
//        }
//
//        pos = norm.indexOf( BSD3_nonums );
//        if( pos >= 0 ) {
//            pos = norm.indexOf( BSD3_part2, pos + BSD3_nonums.length() );
//            if( pos > -1 ) {
//                return true;
//            }
//        }
//        return false;
//    }



//    private static class Simple {
//        private final String license;
//        private final Pattern pattern;
//
//        private Simple( String license, Pattern pattern ) {
//            this.license = license;
//            this.pattern = pattern;
//        }
//
//        public String getLicense() {
//            return license;
//        }
//
//        public Pattern getPattern() {
//            return pattern;
//        }
//    }


}
