package de.pfabulist.loracle.license;

import de.pfabulist.frex.Frex;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.apache.maven.artifact.Artifact;

import javax.annotation.Nullable;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static de.pfabulist.nonnullbydefault.NonnullCheck._nn;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

public class Coordinates {

    public static final Pattern snapshot =
            Frex.any().oneOrMore().lazy().var( "base" ).
                    then( Frex.number().times( 8 ) ).
                    then( Frex.txt( '.' ) ).
                    then( Frex.number().times( 6 ) ).
                    then( Frex.txt( '-' ) ).
                    then( Frex.number().oneOrMore() ).
                    buildCaseInsensitivePattern();

    private final String coo;

    public Coordinates( String groupId, String artifactId, String version ) {
        coo = groupId + ":" + artifactId + ":" + version;

        if( groupId.isEmpty() || artifactId.isEmpty() || version.isEmpty() ) {
            throw new IllegalArgumentException( "not a legal coordinates, one of group, artifact, version is empty" );
        }
        if( groupId.contains( ":" ) || artifactId.contains( ":" ) || version.contains( ":" ) ) {
            throw new IllegalArgumentException( "not a legal coordinates, one of group, artifact, contains a ':' " );
        }
    }

    public static Coordinates valueOf( String str ) {
        String[] parts = str.split( ":" );

        if( parts.length < 3 ) {
            throw new IllegalArgumentException( "not legal coordinates group:arti:version, got: " + str );
        }

        if( parts.length == 3 ) {
            return new Coordinates( _nn( parts[ 0 ] ), _nn( parts[ 1 ] ), _nn( parts[ 2 ] ) );
        }

        if( parts.length == 4 ) {
            // with packaging or classifier
            return new Coordinates( _nn( parts[ 0 ] ), _nn( parts[ 1 ] ), _nn( parts[ 3 ] ) );
        }

        if( parts.length == 5 ) {
            // with packaging and classifier
            return new Coordinates( _nn( parts[ 0 ] ), _nn( parts[ 1 ] ), _nn( parts[ 4 ] ) );
        }

        throw new IllegalArgumentException( "not legal coordinates, too many ':' " + str );
    }

    public static Coordinates valueOf( Artifact arti ) {
        return new Coordinates( _nn( arti.getGroupId() ), _nn( arti.getArtifactId() ), _nn( arti.getVersion() ) );
    }

    public String getGroupId() {
        String[] parts = coo.split( ":" );
        return _nn( parts[ 0 ] );
    }

    public String getArtifactId() {
        String[] parts = coo.split( ":" );
        return _nn( parts[ 1 ] );

    }

    public String getVersion() {
        String[] parts = coo.split( ":" );
        return _nn( parts[ 2 ] );
    }

    @Override
    @SuppressFBWarnings( "NP_METHOD_PARAMETER_TIGHTENS_ANNOTATION" )
    public boolean equals( @Nullable Object o ) {
        if( this == o ) {
            return true;
        }
        if( o == null || getClass() != o.getClass() ) {
            return false;
        }

        Coordinates that = (Coordinates) o;

        return coo.equals( that.coo );

    }

    @Override
    public int hashCode() {
        return coo.hashCode();
    }

    @Override
    public String toString() {
        return coo;
    }

    public boolean matches( Coordinates other ) {
        if( !coo.contains( "*" ) ) {
            return equals( other );
        }

        // todo * at end

        List<String> txt = new ArrayList<>( Arrays.asList( coo.split( "\\*" ) ) );

        if( coo.endsWith( "*" ) ) {
            txt.add( "" );
        }

        if( _nn( txt.get( 0 ) ).isEmpty() ) {
            throw new IllegalArgumentException( "group must not start with *" );
        }

        Pattern pat =
                txt.subList( 1, txt.size() ).stream().
                        map( Frex::txt ).
                        collect( Collectors.reducing( Frex.txt( _nn( txt.get( 0 ) ) ), ( f, g ) -> f.then( Frex.anyBut( Frex.txt( ':' ) ).zeroOrMore() ).then( g ) ) ).
                        buildCaseInsensitivePattern();

        return pat.matcher( other.coo ).matches();
    }

    public boolean isSnapshot() {
        return snapshot.matcher( getVersion() ).matches();
    }

    public Path getSnapshotTolerantDir( Path base ) {
        Matcher matcher = snapshot.matcher( getVersion() );
        if( matcher.matches() ) {
            return _nn( _nn( _nn( base.resolve( getGroupId().replace( '.', '/' ) ) ).resolve( getArtifactId() ) ).resolve( matcher.group( "base" ) + "-SNAPSHOT" ) );
        }

        return _nn( _nn( _nn( base.resolve( getGroupId().replace( '.', '/' ) ) ).resolve( getArtifactId() ) ).resolve( getVersion() ) );
    }

}


