package de.pfabulist.loracle.mojo;

import com.google.gson.Gson;
import de.pfabulist.frex.Frex;
import de.pfabulist.kleinod.nio.Filess;
import de.pfabulist.loracle.buildup.JSONStartup;
import de.pfabulist.loracle.license.Coordinates;
import de.pfabulist.loracle.license.Coordinates2License;
import de.pfabulist.loracle.license.LOracle;
import de.pfabulist.loracle.license.Normalizer;
import de.pfabulist.unchecked.Unchecked;
import org.jsoup.Jsoup;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static de.pfabulist.kleinod.text.Strings.getBytes;
import static de.pfabulist.kleinod.text.Strings.newString;
import static de.pfabulist.nonnullbydefault.NonnullCheck._nn;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

@SuppressWarnings( { "PMD.UnusedPrivateField", "PMD.AvoidPrintStackTrace" } )
public class Downloader {

    private final Findings log;
    private final Url2License url2License;
    Normalizer normalizer = new Normalizer();
    private boolean noInternet = true;
    private final LOracle lOracle;

    public Downloader( Findings log, Url2License url2License, LOracle lOracle ) {
        this.log = log;
        this.url2License = url2License;
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

        Path path = _nn( Paths.get( "" ).resolve( "target/generated-sources/loracle/url/" + getUrlPath( url ) ) );
        if( Files.exists( path ) ) {
            return;
        }
        Filess.createDirectories( _nn( path.getParent() ) );

        Optional<String> res = lOracle.getUrlContent( url );
        if ( !res.isPresent()) {
            log.debug( "    url not known: " + url );
            return;
        }

        try( @Nullable InputStream is = getClass().getResourceAsStream( res.get() )) {
            if( is == null ) {
                log.debug( "    not found: " + getUrlPath( url ) );
                return;
            }
            Files.copy( is, path );
            log.debug( "    found stored in loracle" );
            return;
        } catch( IOException e ) {
            // not stored
        }

        if( noInternet ) {
            log.warn( "       not found " + url );
            log.warn( "[try]  add page src as resource: /de/pfabulist/loracle/urls/" + getUrlPath( url ) );
            log.warn( "       see loracle-maven-plugin docu for more details" );
            return;
        }

        log.debug( "    trying " );
        try {
            Files.write( path, getBytes( _nn( _nn( _nn( Jsoup.connect( url ).get() ).body() ).text() ) ) );
            log.debug( "    success " );
        } catch( Exception e ) {
            e.printStackTrace();
            log.warn( "no internet or 404: " + url );
            return;
        }
    }

    private static final String urlspecial = Frex.or( Frex.txt( ':' ), Frex.txt( '/' ) ).buildPattern().toString();

    private String getUrlPath( String url ) {
        String u = normalizer.normalizeUrl( url ).orElse( url );
        return u.replaceAll( urlspecial, "_" );
    }

    public String get( String u ) {
        Path path = _nn( Paths.get( "" ).resolve( "target/generated-sources/loracle/url/" + getUrlPath( u ) ) );
        if( !Files.exists( path ) ) {
            return "";
        }

        if( Files.isDirectory( path ) ) {
            log.debug( "[huh url?] " + path );
            return "";
        }

        return newString( Filess.readAllBytes( path ) );

    }

    public void setNoInternet() {
        noInternet = true;
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

}
