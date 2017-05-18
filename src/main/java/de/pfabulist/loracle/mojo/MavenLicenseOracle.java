package de.pfabulist.loracle.mojo;

import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;
import de.pfabulist.frex.Frex;
import de.pfabulist.loracle.license.Coordinates;
import de.pfabulist.loracle.license.Findings;
import de.pfabulist.roast.functiontypes.Function_;
import de.pfabulist.roast.nio.Files_;
import org.apache.maven.model.License;
import org.apache.maven.model.Model;
import org.apache.maven.model.Parent;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import javax.annotation.Nullable;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static de.pfabulist.roast.NonnullCheck._nn;
import static de.pfabulist.roast.NonnullCheck.n_or;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

public class MavenLicenseOracle {

    private final Findings log;
    private final Path localRepo;

    public MavenLicenseOracle( Findings log, Path localRepo ) {
        this.log = log;
        this.localRepo = localRepo;
    }

    public List<License> getMavenLicense( Coordinates coo ) {

        while( true ) {
            Path pom = getPom( coo );
            List<License> licenses = extractLicense( pom );

            if( !licenses.isEmpty() ) {
                log.debug( "      licenses found " );
                licenses.forEach( l -> log.debug( "         " + n_or( l.getName(), "-" ) + " : " + n_or( l.getUrl(), "-" ) ) );
                return licenses;
            }

            log.debug( "    no licenses found in " + coo );

            Optional<Coordinates> parentCoo = extractParent( pom );

            if( parentCoo.isPresent() ) {
                coo = _nn( parentCoo.get() );
                log.debug( "          going to parent: " + coo );
            } else {
                log.debug( "          no parent, i,e. no license found" );
                return Collections.emptyList();
            }
        }
    }

    public Path getPom( Coordinates coords ) {
        return _nn( coords.getSnapshotTolerantDir( localRepo ).resolve( coords.getArtifactId() + "-" + coords.getVersion() + ".pom" ) );
    }

    public Path getArtifactOld( Coordinates coords ) {
        Path ret = localRepo;
        ret = _nn( ret.resolve( coords.getGroupId().replace( '.', '/' ) ) );
        ret = _nn( ret.resolve( coords.getArtifactId() ) );
        ret = _nn( ret.resolve( coords.getVersion() ) );
        ret = _nn( ret.resolve( coords.getArtifactId() + "-" + coords.getVersion() + ".jar" ) );  // todo war ...
        return ret;
    }

    enum ArtifactNameVariables {
        suffix
    }

    public Path getArtifact( Coordinates coords ) {
//        Path path = localRepo;
//        path = _nn( path.resolve( coords.getGroupId().replace( '.', '/' ) ) );
//        path = _nn( path.resolve( coords.getArtifactId() ) );
//        path = _nn( path.resolve( coords.getVersion() ) );
        Path dir = coords.getSnapshotTolerantDir( localRepo );

        Pattern pattern =
                Frex.txt( _nn( dir.resolve( coords.getArtifactId() + "-" + coords.getVersion() ) ).toString() ).
                        then( Frex.or( Frex.alphaNum(), Frex.txt( '-' ) ).zeroOrMore().var( ArtifactNameVariables.suffix ) ).
                        then( Frex.txt( ".jar" ) ).buildCaseInsensitivePattern();

        Path classic = _nn( dir.resolve( coords.getArtifactId() + "-" + coords.getVersion() + ".jar" ) );
        if( !Files.exists( dir ) ) {
            return classic;
        }

        return Files_.list( dir ).
                filter( p -> {
                    Matcher matcher = pattern.matcher( p.toString() );
                    if( !matcher.find() ) {
                        return false;
                    }

                    switch( _nn( matcher.group( "suffix" ) ) ) {
                        case "-sources":
                        case "-javadoc":
                            return false;
                        default:
                            return true;
                    }
                } ).
                findFirst().
                orElse( classic );
    }

    public Path getSrc( Coordinates coords ) {
        return _nn( coords.getSnapshotTolerantDir( localRepo ).resolve( coords.getArtifactId() + "-" + coords.getVersion() + "-sources.jar" ) );
    }

    List<License> extractLicense( final Path pom ) {
        return fromPom( pom, m -> Optional.ofNullable( m.getLicenses() ) ).orElse( Collections.emptyList() );
//        if( !Files.exists( pom ) ) {
//            log.warn( "no such pom file: " + pom );
//            return Collections.emptyList();
//        }
//        MavenXpp3Reader reader = new MavenXpp3Reader();
//        CharsetDetector cd = new CharsetDetector();
//        try( InputStream iss = Files.newInputStream( pom );
//             BufferedInputStream is = new BufferedInputStream( iss ) ) {
//            cd.setText( is );
//            @Nullable CharsetMatch[] cm = cd.detectAll();
//            if( cm == null || cm.length == 0 ) {
//                log.warn( "can't detect encoding: " + pom );
//                return Collections.emptyList();
//            }
//            Reader pathReader = _nn( _nn( cm[ 0 ] ).getReader() );
//            Model pomModel = _nn( reader.read( pathReader ) );
//
//            return _nn( pomModel.getLicenses() );
//
//        } catch( IOException | XmlPullParserException e ) {
//            log.warn( "error extracting license from pom " + pom.toString() + ": " + e );
//        }
//
//        return Collections.emptyList();
    }

    Optional<Coordinates> extractParent( Path pom ) {
        return fromPom( pom, m -> {
            @Nullable Parent parent = m.getParent();
            if( parent == null ) {
                return Optional.empty();
            }

            return Optional.of( new Coordinates( _nn( parent.getGroupId() ), _nn( parent.getArtifactId() ), _nn( parent.getVersion() ) ) );

        } );
//        MavenXpp3Reader reader = new MavenXpp3Reader();
//        try( Reader pathReader = Files.newBufferedReader( pom ) ) {
//            Model pomModel = _nn( reader.read( pathReader ) );
//
//            @Nullable Parent parent = pomModel.getParent();
//            if( parent == null ) {
//                return Optional.empty();
//            }
//
//            return Optional.of( new Coordinates( _nn( parent.getGroupId() ), _nn( parent.getArtifactId() ), _nn( parent.getVersion() ) ) );
//
//        } catch( IOException | XmlPullParserException e ) {
//            log.warn( "error extracting parent from pom " + e );
//        }
//
//        return Optional.empty();
    }

    <T> Optional<T> fromPom( Path pom, Function_<Model, Optional<T>> func ) {
        if( !Files.exists( pom ) ) {
            log.warn( "no such pom file: " + pom );
            return Optional.empty();
        }

        MavenXpp3Reader reader = new MavenXpp3Reader();
        CharsetDetector cd = new CharsetDetector();
        try( InputStream iss = Files.newInputStream( pom );
             BufferedInputStream is = new BufferedInputStream( iss ) ) {
            cd.setText( is );
            @Nullable CharsetMatch[] cm = cd.detectAll();
            if( cm == null || cm.length == 0 ) {
                log.warn( "can't detect encoding of pom: " + pom );
                return Optional.empty();
            }
            Reader pathReader = _nn( _nn( cm[ 0 ] ).getReader() );
            Model pomModel = _nn( reader.read( pathReader ) );

            return _nn( func.apply_( pomModel ) );

        } catch( IOException | XmlPullParserException e ) {
            log.warn( "error extracting license from pom " + pom.toString() + ": " + e );
        }

        return Optional.empty();

    }

}
