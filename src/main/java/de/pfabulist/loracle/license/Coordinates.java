package de.pfabulist.loracle.license;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.apache.maven.artifact.Artifact;

import javax.annotation.Nullable;

import static de.pfabulist.nonnullbydefault.NonnullCheck._nn;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

public class Coordinates {

    private final String groupId;
    private final String artifactId;
    private final String version;

    public Coordinates( String groupId, String artifactId, String version ) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;

        if( groupId.isEmpty() || artifactId.isEmpty() || version.isEmpty() ) {
            throw new IllegalArgumentException( "not a legal coordinates, one of group, artifact, version is empty" );
        }
    }

    public static Coordinates valueOf( String str ) {
        String[] parts = str.split( ":" );

        if( parts.length < 3 ) {
            throw new IllegalArgumentException( "not legal coordinates group:arti:version, got: " + str );
        }

        return new Coordinates( _nn( parts[ 0 ] ), _nn( parts[ 1 ] ), _nn( parts[ 2 ] ) );
    }

    public static Coordinates valueOf( Artifact arti ) {
        return new Coordinates( _nn( arti.getGroupId() ), _nn( arti.getArtifactId() ), _nn( arti.getVersion() ) );
    }

    public String getGroupId() {
        return groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public String getVersion() {
        return version;
    }

    @Override
    @SuppressFBWarnings( "NP_METHOD_PARAMETER_TIGHTENS_ANNOTATION" )
    public boolean equals( @Nullable Object o ) {
        if( this == o ) {
            return true;
        }
        if( o == null ) {
            return false;
        }
        if( !( o instanceof Coordinates ) ) {
            return false;
        }

        Coordinates that = (Coordinates) o;

        if( !groupId.equals( that.groupId ) ) {
            return false;
        }
        if( !artifactId.equals( that.artifactId ) ) {
            return false;
        }
        return version.equals( that.version );

    }

    @Override
    public int hashCode() {
        int result = groupId.hashCode();
        result = 31 * result + artifactId.hashCode();
        result = 31 * result + version.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return groupId + ':' + artifactId + ':' + version;
    }
}


