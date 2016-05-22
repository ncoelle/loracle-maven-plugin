package de.pfabulist.loracle.buildup;

import com.google.gson.GsonBuilder;
import de.pfabulist.loracle.license.Coordinates;
import de.pfabulist.loracle.license.LOracle;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.handler.ArtifactHandler;
import org.apache.maven.artifact.metadata.ArtifactMetadata;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.OverConstrainedVersionException;
import org.apache.maven.artifact.versioning.VersionRange;
import org.junit.Test;

import java.io.File;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

@SuppressFBWarnings( {"EQ_COMPARETO_USE_OBJECT_EQUALS"} )
public class BuildupTest {

    @Test
    public void spdx() {
        LOracle lOracle = new LOracle();
        new ExtractSpdxLicensesFromHTML().getLicenses( lOracle );

        assertThat( lOracle.getSingle( "ZPl-1.1" ) ).isPresent();
    }

    @Test
    public void lesserOrLater() {
        LOracle lOracle = new LOracle();
        new ExtractSpdxLicensesFromHTML().getLicenses( lOracle );

        lOracle.addLongName( lOracle.getByName( "LGPL-3.0+" ).get(), "GNU Lesser Public License" );

        System.out.println( lOracle.getByName( "GNU Lesser Public License" ));
    }

    @Test
    public void testApache() {
        LOracle lOracle = new LOracle();
        new ExtractSpdxLicensesFromHTML().getLicenses( lOracle );


        assertThat( lOracle.getByName( "Apache Software License - Version 2.0" )).
                isEqualTo( lOracle.getByName( "apache-2.0" ) );



//        javax.servlet:javax.servlet-api:jar:3.1.0:compile
//                [INFO] name:      CDDL + GPLv2 with classpath exception
//                [INFO] long name: Optional.empty
//                [INFO] arti:      Optional.empty
    }

    @Test
    public void testClasspathException() {
        LOracle lOracle = new LOracle();
        new ExtractSpdxLicensesFromHTML().getLicenses( lOracle );
        new ExtractSPDXExceptionsFromHTML( lOracle );

        assertThat( lOracle.getByName( "apache-2.0 with Classpath-exception-2.0" ) ).isPresent();

    }

    @Test
    public void testJavaxServlet() {
        LOracle lOracle = new LOracle();
        new ExtractSpdxLicensesFromHTML().getLicenses( lOracle );
        new ExtractSPDXExceptionsFromHTML( lOracle );

        lOracle.addLicenseForArtifact( new Coordinates( "javax.servlet", "javax.servlet-api", "3.1.0" ),
                                       lOracle.getOrThrowByName( "cddl-1.1 with Classpath-exception-2.0" ));


        assertThat( lOracle.getByCoordinates( new Coordinates( "javax.servlet", "javax.servlet-api", "3.1.0" ) )).isPresent();
    }

    @Test
    public void plus() {
        LOracle lOracle = new LOracle();
        new ExtractSpdxLicensesFromHTML().getLicenses( lOracle );

        assertThat( lOracle.getByName( "GPL-2.0+" ) ).isPresent();

        System.out.println( lOracle.getByName( "GPL-2.0+" ) );
    }

    @Test
    public void testApacheUrl() {
//        LOracle lOracle = new LOracle();
//        new ExtractSpdxLicensesFromHTML().getLicenses( lOracle );
//        new ExtractSPDXExceptionsFromHTML( lOracle );
//        lOracle.addLongName( lOracle.getOrThrowByName(  "LGPL-3.0+" ), "GNU Lesser Public License" );
//        lOracle.addLongName( lOracle.getOrThrowByName(  "BSD-3-Clause" ), "New BSD" );
//        lOracle.addLicenseForArtifact( new Coordinates( "net.jcip", "jcip-annotations", "1.0" ), lOracle.getOrThrowByName( "CC-BY-2.5" ));;
//        lOracle.addLicenseForArtifact( new Coordinates( "javax.servlet", "javax.servlet-api", "3.1.0" ),
//                                       lOracle.getOrThrowByName( "cddl-1.1 with Classpath-exception-2.0" ));
//        lOracle.addLicenseForArtifact( new Coordinates( "org.apache.httpcomponents","httpclient","4.0.1" ),
//                                       lOracle.getOrThrowByName( "Apache-2.0" ));
//        lOracle.addLicenseForArtifact( new Coordinates( "commons-httpclient","commons-httpclient","3.1" ),
//                                       lOracle.getOrThrowByName( "Apache-2.0" ));
//
//        lOracle.addUrl( lOracle.getOrThrowByName( "Apache-2.0" ), "http://www.apache.org/licenses/LICENSE-2.0" );

        LOracle lOracle = new LOracle();
        new ExtractSpdxLicensesFromJSON().go( lOracle );
        //new ExtractSpdxLicensesFromHTML().getLicenses( lOracle );
        new ExtractSPDXExceptionsFromHTML( lOracle );
        lOracle.addLongName( lOracle.getOrThrowByName(  "LGPL-3.0+" ), "GNU Lesser Public License" );
        //lOracle.addLongName( lOracle.getOrThrowByName(  "BSD-3-Clause" ), "New BSD" );
        lOracle.addLicenseForArtifact( new Coordinates( "net.jcip", "jcip-annotations", "1.0" ), lOracle.getOrThrowByName( "CC-BY-2.5" ));;
        lOracle.addLicenseForArtifact( new Coordinates( "javax.servlet", "javax.servlet-api", "3.1.0" ),
                                       lOracle.getOrThrowByName( "CDDl-1.1 or GPL-2.0 with Classpath-exception-2.0" ));
        lOracle.addLicenseForArtifact( new Coordinates( "org.apache.httpcomponents","httpclient","4.0.1" ),
                                       lOracle.getOrThrowByName( "Apache-2.0" ));

        lOracle.addUrl( lOracle.getOrThrowByName( "mit" ), "http://www.opensource.org/licenses/mit-license.php" );

        lOracle.newSingle( "closed", false );

        assertThat( lOracle.getByUrl( "http://www.apache.org/licenses/LICENSE-2.0" )).isPresent();
        assertThat( lOracle.getByUrl( "http://www.apache.org/licenses/LICENSE-2.0.TXT" )).isPresent();
        assertThat( lOracle.getByUrl( "http://www.opensource.org/licenses/mit-license.php" )).isPresent();


        Artifact arti = new Artifact() {
            @Override
            public String getGroupId() {
                return "javax.servlet";
            }

            @Override
            public String getArtifactId() {
                return "javax.servlet-api";
            }

            @Override
            public String getVersion() {
                return "3.1.0";
            }

            @Override
            public void setVersion( String version ) {

            }

            @Override
            public String getScope() {
                return null;
            }

            @Override
            public String getType() {
                return null;
            }

            @Override
            public String getClassifier() {
                return null;
            }

            @Override
            public boolean hasClassifier() {
                return false;
            }

            @Override
            public File getFile() {
                return null;
            }

            @Override
            public void setFile( File destination ) {

            }

            @Override
            public String getBaseVersion() {
                return null;
            }

            @Override
            public void setBaseVersion( String baseVersion ) {

            }

            @Override
            public String getId() {
                return null;
            }

            @Override
            public String getDependencyConflictId() {
                return null;
            }

            @Override
            public void addMetadata( ArtifactMetadata metadata ) {

            }

            @Override
            public Collection<ArtifactMetadata> getMetadataList() {
                return null;
            }

            @Override
            public void setRepository( ArtifactRepository remoteRepository ) {

            }

            @Override
            public ArtifactRepository getRepository() {
                return null;
            }

            @Override
            public void updateVersion( String version, ArtifactRepository localRepository ) {

            }

            @Override
            public String getDownloadUrl() {
                return null;
            }

            @Override
            public void setDownloadUrl( String downloadUrl ) {

            }

            @Override
            public ArtifactFilter getDependencyFilter() {
                return null;
            }

            @Override
            public void setDependencyFilter( ArtifactFilter artifactFilter ) {

            }

            @Override
            public ArtifactHandler getArtifactHandler() {
                return null;
            }

            @Override
            public List<String> getDependencyTrail() {
                return null;
            }

            @Override
            public void setDependencyTrail( List<String> dependencyTrail ) {

            }

            @Override
            public void setScope( String scope ) {

            }

            @Override
            public VersionRange getVersionRange() {
                return null;
            }

            @Override
            public void setVersionRange( VersionRange newRange ) {

            }

            @Override
            public void selectVersion( String version ) {

            }

            @Override
            public void setGroupId( String groupId ) {

            }

            @Override
            public void setArtifactId( String artifactId ) {

            }

            @Override
            public boolean isSnapshot() {
                return false;
            }

            @Override
            public void setResolved( boolean resolved ) {

            }

            @Override
            public boolean isResolved() {
                return false;
            }

            @Override
            public void setResolvedVersion( String version ) {

            }

            @Override
            public void setArtifactHandler( ArtifactHandler handler ) {

            }

            @Override
            public boolean isRelease() {
                return false;
            }

            @Override
            public void setRelease( boolean release ) {

            }

            @Override
            public List<ArtifactVersion> getAvailableVersions() {
                return null;
            }

            @Override
            public void setAvailableVersions( List<ArtifactVersion> versions ) {

            }

            @Override
            public boolean isOptional() {
                return false;
            }

            @Override
            public void setOptional( boolean optional ) {

            }

            @Override
            public ArtifactVersion getSelectedVersion() throws OverConstrainedVersionException {
                return null;
            }

            @Override
            public boolean isSelectedVersionKnown() throws OverConstrainedVersionException {
                return false;
            }

            @Override
            public int compareTo( Artifact o ) {
                return 0;
            }
        };

        assertThat( lOracle.getByCoordinates( Coordinates.valueOf( arti ) )).isEqualTo(
                lOracle.getByName( "CDDl-1.1 or GPL-2.0 with Classpath-exception-2.0" ));


        assertThat( lOracle.getByName( "GNU Lesser Public License" )).isEqualTo( lOracle.getByName( "LGPL-3.0+" ) );

        System.out.println( new GsonBuilder().setPrettyPrinting().create().toJson( lOracle ));

        //http://www.opensource.org/licenses/mit-license.php

//        LOracle lOracle1 = new Gson().fromJson( new Gson().toJson( lOracle ), LOracle.class);

        //System.out.println( new Gson().toJson( lOracle ));
        // http://www.apache.org/licenses/LICENSE-2.0
    }

    @Test
    public void startup() {
        LOracle lOracle = JSONStartup.start().spread();

        assertThat( lOracle.getByUrl( "http://www.apache.org/licenses/LICENSE-2.0" )).isPresent();
        assertThat( lOracle.getByUrl( "http://www.apache.org/licenses/LICENSE-2.0.TXT" )).isPresent();


        Artifact arti = new Artifact() {
            @Override
            public String getGroupId() {
                return "javax.servlet";
            }

            @Override
            public String getArtifactId() {
                return "javax.servlet-api";
            }

            @Override
            public String getVersion() {
                return "3.1.0";
            }

            @Override
            public void setVersion( String version ) {

            }

            @Override
            public String getScope() {
                return null;
            }

            @Override
            public String getType() {
                return null;
            }

            @Override
            public String getClassifier() {
                return null;
            }

            @Override
            public boolean hasClassifier() {
                return false;
            }

            @Override
            public File getFile() {
                return null;
            }

            @Override
            public void setFile( File destination ) {

            }

            @Override
            public String getBaseVersion() {
                return null;
            }

            @Override
            public void setBaseVersion( String baseVersion ) {

            }

            @Override
            public String getId() {
                return null;
            }

            @Override
            public String getDependencyConflictId() {
                return null;
            }

            @Override
            public void addMetadata( ArtifactMetadata metadata ) {

            }

            @Override
            public Collection<ArtifactMetadata> getMetadataList() {
                return null;
            }

            @Override
            public void setRepository( ArtifactRepository remoteRepository ) {

            }

            @Override
            public ArtifactRepository getRepository() {
                return null;
            }

            @Override
            public void updateVersion( String version, ArtifactRepository localRepository ) {

            }

            @Override
            public String getDownloadUrl() {
                return null;
            }

            @Override
            public void setDownloadUrl( String downloadUrl ) {

            }

            @Override
            public ArtifactFilter getDependencyFilter() {
                return null;
            }

            @Override
            public void setDependencyFilter( ArtifactFilter artifactFilter ) {

            }

            @Override
            public ArtifactHandler getArtifactHandler() {
                return null;
            }

            @Override
            public List<String> getDependencyTrail() {
                return null;
            }

            @Override
            public void setDependencyTrail( List<String> dependencyTrail ) {

            }

            @Override
            public void setScope( String scope ) {

            }

            @Override
            public VersionRange getVersionRange() {
                return null;
            }

            @Override
            public void setVersionRange( VersionRange newRange ) {

            }

            @Override
            public void selectVersion( String version ) {

            }

            @Override
            public void setGroupId( String groupId ) {

            }

            @Override
            public void setArtifactId( String artifactId ) {

            }

            @Override
            public boolean isSnapshot() {
                return false;
            }

            @Override
            public void setResolved( boolean resolved ) {

            }

            @Override
            public boolean isResolved() {
                return false;
            }

            @Override
            public void setResolvedVersion( String version ) {

            }

            @Override
            public void setArtifactHandler( ArtifactHandler handler ) {

            }

            @Override
            public boolean isRelease() {
                return false;
            }

            @Override
            public void setRelease( boolean release ) {

            }

            @Override
            public List<ArtifactVersion> getAvailableVersions() {
                return null;
            }

            @Override
            public void setAvailableVersions( List<ArtifactVersion> versions ) {

            }

            @Override
            public boolean isOptional() {
                return false;
            }

            @Override
            public void setOptional( boolean optional ) {

            }

            @Override
            public ArtifactVersion getSelectedVersion() throws OverConstrainedVersionException {
                return null;
            }

            @Override
            public boolean isSelectedVersionKnown() throws OverConstrainedVersionException {
                return false;
            }

            @Override
            public int compareTo( Artifact o ) {
                return 0;
            }
        };

        assertThat( lOracle.getByCoordinates( Coordinates.valueOf( arti ) )).isEqualTo(
                lOracle.getByName( "CDDl-1.1 or GPL-2.0 with Classpath-exception-2.0" ));


        assertThat( lOracle.getByName( "GNU Lesser Public License" )).isEqualTo( lOracle.getByName( "LGPL-3.0+" ) );
        assertThat( lOracle.getByUrl( "http://www.opensource.org/licenses/mit-license.php" )).isPresent();

        assertThat( lOracle.getByUrl( "http://www.opensource.org/licenses/mit-license.php" )).
                isEqualTo( lOracle.getByName( "mit" ) );
    }

    @Test
    public void testJson() {
        new ExtractSpdxLicensesFromJSON().go( new LOracle() );
    }

//    @Test
//    public void fedora() {
//        LOracle lOracle = new LOracle( new ExtractSpdxLicensesFromHTML().getLicenses() );
//
//        new ExtractGoodFedoraLicensesFromHTML().getFedoraInfo().peek( System.out::println ).
//                map( f -> lOracle.getByName( f.name ) ).
//                forEach( System.out::println );
//    }
//
//    @Test
//    public void gson() {
//        List<IANALicense> ll = new ExtractSpdxLicensesFromHTML().getLicenses();
//
//        System.out.println( new GsonBuilder().setPrettyPrinting().create().toJson( ll ));
//    }
//
//    @Test
//    public void upperCaseTest() {
//        LOracle lOracle = new LOracle( new ExtractSpdxLicensesFromHTML().getLicenses() );
//
//        assertThat( lOracle.getByName( "BSD-2-Clause" )).isPresent();
//    }
}
