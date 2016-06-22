package de.pfabulist.loracle.mojo;

import de.pfabulist.loracle.license.Coordinates;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.model.License;
import org.apache.maven.model.Model;
import org.apache.maven.model.Parent;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.plugin.logging.Log;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static de.pfabulist.frex.Frex.txt;
import static de.pfabulist.kleinod.text.Strings.newString;
import static de.pfabulist.nonnullbydefault.NonnullCheck._nn;
import static de.pfabulist.unchecked.NullCheck._orElseGet;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

public class MavenLicenseOracle {

    private final Log log;
    private final Path localRepo;

    public MavenLicenseOracle( Log log, Path localRepo ) {
        this.log = log;
        this.localRepo = localRepo;
    }

    public List<License> getMavenLicense( Coordinates coo ) {

        while( true ) {
            Path pom = getPom( coo );
            List<License> licenses = extractLicense( pom );

            if( !licenses.isEmpty() ) {
                log.debug( "      licenses found " );
                licenses.forEach( l -> log.debug( "         " + _orElseGet( l.getName(), "-" ) + " : " + _orElseGet( l.getUrl(), "-" ) ) );
                return licenses;
            }

            log.debug( "    no licenses found in " + coo  );

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

    private Path getPom( Coordinates coords ) {
        Path ret = localRepo;
        ret = _nn( ret.resolve( coords.getGroupId().replace( '.', '/' ) ) );
        ret = _nn( ret.resolve( coords.getArtifactId() ) );
        ret = _nn( ret.resolve( coords.getVersion() ) );
        ret = _nn( ret.resolve( coords.getArtifactId() + "-" + coords.getVersion() + ".pom" ) );
        return ret;
    }

    public Path getArtifact( Coordinates coords ) {
        Path ret = localRepo;
        ret = _nn( ret.resolve( coords.getGroupId().replace( '.', '/' ) ) );
        ret = _nn( ret.resolve( coords.getArtifactId() ) );
        ret = _nn( ret.resolve( coords.getVersion() ) );
        ret = _nn( ret.resolve( coords.getArtifactId() + "-" + coords.getVersion() + ".jar" ) );  // todo war ...
        return ret;
    }

    public Path getSrc( Coordinates coords ) {
        Path ret = localRepo;
        ret = _nn( ret.resolve( coords.getGroupId().replace( '.', '/' ) ) );
        ret = _nn( ret.resolve( coords.getArtifactId() ) );
        ret = _nn( ret.resolve( coords.getVersion() ) );
        ret = _nn( ret.resolve( coords.getArtifactId() + "-" + coords.getVersion() + "-sources.jar" ) );  // todo war ...
        return ret;
    }


    List<License> extractLicense( final Path pom ) {
        MavenXpp3Reader reader = new MavenXpp3Reader();
        try( Reader pathReader = Files.newBufferedReader( pom ) ) {
            Model pomModel = _nn( reader.read( pathReader ) );

            return _nn( pomModel.getLicenses() );

        } catch( IOException | XmlPullParserException e ) {
            log.warn( "\n++++++++++++++++++++++++++++++++++++++ " );
            log.warn( "error extracting license from pom " + e );
            log.warn( "++++++++++++++++++++++++++++++++++++++ \n" );
        }

        return Collections.emptyList();
    }


    Optional<Coordinates> extractParent( Path pom ) {
        MavenXpp3Reader reader = new MavenXpp3Reader();
        try( Reader pathReader = Files.newBufferedReader( pom ) ) {
            Model pomModel = _nn( reader.read( pathReader ) );

            @Nullable Parent parent = pomModel.getParent();
            if( parent == null ) {
                return Optional.empty();
            }

            return Optional.of( new Coordinates( _nn( parent.getGroupId() ), _nn( parent.getArtifactId() ), _nn( parent.getVersion() ) ) );

        } catch( IOException | XmlPullParserException e ) {
            log.warn( "error extracting parent from pom " + e );
        }

        return Optional.empty();

    }

}
