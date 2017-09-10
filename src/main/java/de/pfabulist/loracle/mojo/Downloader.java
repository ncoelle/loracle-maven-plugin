package de.pfabulist.loracle.mojo;

import com.google.gson.Gson;
import de.pfabulist.loracle.buildup.JSONStartup;
import de.pfabulist.loracle.license.*;
import de.pfabulist.loracle.maven.Coordinates;
import de.pfabulist.loracle.text.Normalizer;
import de.pfabulist.roast.nio.Files_;
import de.pfabulist.roast.nio.Paths_;
import de.pfabulist.roast.unchecked.Unchecked;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

import static de.pfabulist.frex.Frex.txt;
import static de.pfabulist.kleinod.text.Strings.newString;
import static de.pfabulist.roast.NonnullCheck._nn;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

@SuppressWarnings( { "PMD.UnusedPrivateField", "PMD.AvoidPrintStackTrace" } )
public class Downloader {

    private final Findings log;
    Normalizer normalizer = new Normalizer();
    //private boolean noInternet = true;
    private final LOracle lOracle;

    public Downloader( Findings log, LOracle lOracle ) {
        this.log = log;
        this.lOracle = lOracle;
    }

    public void get( Coordinates coordinates, Coordinates2License.LiCo liCo ) {
        if( !liCo.getLicenseTxt().isEmpty() ) {
            return;
        }

        if( liCo.getMavenLicenses().isEmpty() ) {
            getLico( coordinates ).ifPresent( stored -> {
                if( stored.getMavenLicenses().isEmpty() || _nn( stored.getMavenLicenses().get( 0 ) ).getUrl().isEmpty() ) {
                    log.warn( "[huh]" );
                    return;
                }

                //String u = _nn(stored.getMavenLicenses().get(0)).getUrl();

                liCo.setMavenLicenses( stored.getMavenLicenses() );

                log.debug( "found stored information for " + coordinates );
            } );
        }

        liCo.getMavenLicenses().stream().
                map( Coordinates2License.MLicense::getUrl ).
                filter( u -> !u.isEmpty() ).
                forEach( this::download );

    }

    public void getExtension( Coordinates coordinates, Coordinates2License.LiCo liCo ) {
        if( !liCo.getLicenseTxt().isEmpty() ) {
            return;
        }

        getLico( coordinates ).ifPresent( stored -> {
//            if ( stored.getMavenLicenses().isEmpty() || _nn(stored.getMavenLicenses().get(0)).getUrl().isEmpty() ) {
//                log.warn( "[huh]" );
//                return;
//            }

            //String u = _nn(stored.getMavenLicenses().get(0)).getUrl();

            liCo.setMavenLicenses( stored.getMavenLicenses() );

            log.debug( "found stored information for " + coordinates );
        } );
    }

    public void download( String url ) {

        log.debug( "[download?] " + url );

        Path path = Paths_.get__( "" ).resolve_( "target/generated-sources/loracle/url/" + getUrlPath( url ) );
        if( Files_.exists( path ) ) {
            return;
        }
        Files_.createDirectories( path.getParent() );

        Optional<String> res = lOracle.getUrlContent( url );
        if( !res.isPresent() ) {
            log.debug( "    url not stored: " + url );
            return;
        }

        try( @Nullable InputStream is = getClass().getResourceAsStream( res.get() ) ) {
            if( is == null ) {
                log.debug( "    not found: " + getUrlPath( url ) );
                return;
            }
            Files_.copy( is, path );
            log.debug( "    found stored in loracle" );
            return;
        } catch( IOException e ) {
            // not stored
        }

        log.warn( "       not found " + url );
        log.warn( "[try]  add page src as resource see loracle-custom" );
        return;

//        if( noInternet ) {
//            log.warn( "       not found " + url );
//            log.warn( "[try]  add page src as resource: /de/pfabulist/loracle/urls/" + getUrlPath( url ) );
//            log.warn( "       see loracle-maven-plugin docu for more details" );
//            return;
//        }
//
//        log.debug( "    trying " );
//        try {
//            Files.write( path, getBytes( _nn( _nn( _nn( Jsoup.connect( url ).get() ).body() ).text() ) ) );
//            log.debug( "    success " );
//        } catch( Exception e ) {
//            e.printStackTrace();
//            log.warn( "no internet or 404: " + url );
//            return;
//        }
    }

    private static final String urlspecial = txt( ':' ).or( txt( '/' ) ).or( txt( '*' ) ).or( txt( '"' ) ).or( txt( '<' ) ).or( txt( '>' ) ).or( txt( '?' ) ).or( txt( '\\' ) ).buildPattern().toString();

    private String getUrlPath( String url ) {
        String u = LoUrl.getReleventUrlPart( url ).orElse( url );
        return u.replaceAll( urlspecial, "_" );
    }

    public String get( String u ) {
        Path path = Paths_.get__( "" ).resolve_( "target/generated-sources/loracle/url/" + getUrlPath( u ) );
        if( !Files.exists( path ) ) {
            return "";
        }

        if( Files.isDirectory( path ) ) {
            log.debug( "[huh url?] " + path );
            return "";
        }

        return newString( Files_.readAllBytes( path ) );

    }

    public void setNoInternet() {
        // noInternet = true;
    }

    public static Optional<Coordinates2License.LiCo> getLico( Coordinates coo ) {
        byte[] buf = new byte[ 3000000 ];

        int got = 0;
        try( @Nullable InputStream in = JSONStartup.class.getResourceAsStream( "/de/pfabulist/loracle/coordinates/" + coo.toFilename() + ".json" ) ) {
            if( in == null ) {
                return Optional.empty();
            }
            while( true ) {
                int once = in.read( buf, got, 3000000 - got );
                if( once < 0 ) {
                    break;
                }
                got += once;
            }
        } catch( IOException e ) {
            throw Unchecked.u( e );
        }

        String jsonstr = new String( buf, 0, got, StandardCharsets.UTF_8 );

        return Optional.of( new Gson().fromJson( jsonstr, Coordinates2License.LiCo.class ) );
    }

    public void generateLicensesTxt( String prefix, Coordinates coordinates, Coordinates2License.LiCo liCo ) {
        Path src = Paths_.get__( "target/generated-sources/loracle/coordinates/" + coordinates.toFilename() + "/license-0.txt" ).toAbsolutePath_();

        if( Files.exists( src ) && Files_.size( src ) > 0 ) {

            String fname = coordinates.toFilename() + "-license.txt";

            getEmptyNoticeLicenseTarget( prefix, coordinates.toFilename() + "-license.txt" ).ifPresent( tgt -> Files_.copy( src, tgt ) );
            liCo.setLicenseFilenames( Collections.singletonList( fname ));
            return;
        }

        List<String> fnames = new ArrayList<>();
        liCo.getLicense().ifPresent( li -> lOracle.getByName( li ).ifPresent(
                license -> LicenseIDs.flattenToStrings( license ).
                        forEach( lstr -> {
                            String fname = onPredefLicense( lstr,
                                                            ( filename, is ) -> getEmptyNoticeLicenseTarget( prefix, filename ).ifPresent( tgt -> Files_.copy( is, tgt ) ) ).
                                    orElseGet( () -> onUrldefLicense( lstr,
                                                                      ( filename, is ) -> getEmptyNoticeLicenseTarget( prefix, filename ).ifPresent( tgt -> Files_.copy( is, tgt ) ) ).
                                            orElse( "" ) );
                            if( fname.isEmpty() ) {
                                log.warn( "can not find text for " + lstr );
                            } else {
                                fnames.add( fname );
                            }

                        } ) ) );

        liCo.setLicenseFilenames( fnames );
    }

    private Optional<Path> getEmptyNoticeLicenseTarget( String prefix, String fname ) {
        final String filename = prefix + "/" + fname;
        Path tgt = Paths_.get__( "target/generated-sources/loracle/licenses/" + filename ).toAbsolutePath_();
        if( Files.exists( tgt ) ) {
            return Optional.empty();
        }

        Files_.createDirectories( tgt.getParent() );

        return Optional.of( tgt );
    }

    @SuppressFBWarnings( "REC_CATCH_EXCEPTION" )
    public Optional<String> onPredefLicense( String lstr, BiConsumer<String, InputStream> isConsumer ) {

        String fname = Normalizer.toFilename( lstr ) + ".txt";

        try( @Nullable InputStream is = Downloader.class.getResourceAsStream( "/de/pfabulist/loracle/urls/" + fname ) ) {
            if( is == null ) {
                log.debug( "no prefdef license text found for: " + lstr );
                return Optional.empty();
            }
            isConsumer.accept( fname, is );

            return Optional.of( fname );
        } catch( Exception e ) {
            log.debug( "no prefdef license text found for: " + lstr );
        }

        return Optional.empty();
    }

    @SuppressFBWarnings( "REC_CATCH_EXCEPTION" )
    public Optional<String> onUrldefLicense( String str, BiConsumer<String, InputStream> isConsumer ) {

        Optional<LicenseID> olicense = lOracle.getByName( str ).noReason();

        if( !olicense.isPresent() ) {
            return Optional.empty();
        }

        LicenseID license = _nn( olicense.get() );

        Optional<String> ourl = lOracle.getMore( license ).urls.stream().filter( u -> lOracle.getUrlContent( u ).isPresent() ).findFirst();

        if( !ourl.isPresent() ) {
            return Optional.empty();
        }

        String url = _nn( ourl.get() );
        String fname = Normalizer.toFilename( url ) + ".txt";
        String res = _nn( lOracle.getUrlContent( url ).get() ); // good by first stream

        try( @Nullable InputStream is = Downloader.class.getResourceAsStream( res ) ) {
            if( is == null ) {
                log.warn( "urlcontent but no resource " + url );
                return Optional.empty();
            }
            isConsumer.accept( fname, is );

            return Optional.of( fname );
        } catch( Exception e ) {
            log.warn( "urlcontent but no resource " + url );
        }

        return Optional.empty();
    }

}
