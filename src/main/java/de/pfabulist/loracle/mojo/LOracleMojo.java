package de.pfabulist.loracle.mojo;

import de.pfabulist.loracle.license.Coordinates;
import de.pfabulist.loracle.buildup.JSONStartup;
import de.pfabulist.loracle.license.LOracle;
import de.pfabulist.loracle.license.LicenseID;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.apache.maven.artifact.resolver.filter.ScopeArtifactFilter;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.License;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.apache.maven.shared.dependency.graph.DependencyGraphBuilder;
import org.apache.maven.shared.dependency.graph.DependencyNode;
import org.apache.maven.shared.dependency.graph.traversal.DependencyNodeVisitor;

import javax.annotation.Nullable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static de.pfabulist.nonnullbydefault.NonnullCheck._ni;
import static de.pfabulist.nonnullbydefault.NonnullCheck._nn;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

@Mojo( name = "license-check", defaultPhase = LifecyclePhase.INSTALL, requiresDependencyResolution = ResolutionScope.TEST, threadSafe = true )
public class LOracleMojo extends AbstractMojo {

    @Component
    @Nullable
    DependencyGraphBuilder dependencyGraphBuilder;

    @Parameter( defaultValue = "${project}", readonly = true )
    @Nullable
    MavenProject project;

    @Parameter( defaultValue = "${session}", required = true, readonly = true )
    @Nullable
    private MavenSession session;

    @Parameter( property = "license-check.ikwid" )
    @Nullable
    List<LicenseDeclaration> licenseDeclarations;

    public DependencyGraphBuilder getDependencyGraphBuilder() {
        return _ni( dependencyGraphBuilder );
    }

    public MavenProject getProject() {
        return _ni( project );
    }

    public MavenSession getSession() {
        return _ni( session );
    }

    public List<LicenseDeclaration> getLicenseDeclarations() {
        return licenseDeclarations == null ? Collections.emptyList() : licenseDeclarations;
    }

    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info( "---------------------------------------" );
        getLog().info( "      loracle license check                    " );
        getLog().info( "---------------------------------------" );

        Path localRepo = Paths.get( _nn( getSession().getLocalRepository() ).getBasedir() );

        try {

            if( getProject().getLicenses() == null || getProject().getLicenses().isEmpty() ) {
                throw new MojoFailureException( "no license set" );
            }

            if( getProject().getLicenses().size() > 1 ) {
                getLog().info( "license check can't deal with multiple licenses (yet)" );
                return;
            }

            Findings failures = new Findings( getLog() );
            LOracle lOracle = JSONStartup.start().spread();

            for( LicenseDeclaration excl : getLicenseDeclarations() ) {
                Coordinates coo = Coordinates.valueOf( excl.getCoordinates().orElseThrow(
                        () -> new MojoFailureException( "no coordinates set in configuration of LicenseDeclarations in LOracle Plugin" ) ) );
                LicenseID license =
                        lOracle.getByName(
                                excl.getLicense().orElseThrow(
                                        () -> new MojoFailureException( "no name set in configuration of LicenseDeclarations in LOracle Plugin" ) ) ).
                                orElseThrow( () -> new MojoFailureException( "illegal license name in configuration of LicenseDeclarations in LOracle Plugin" ) );

                lOracle.addLicenseForArtifact( coo, license );
            }

            MavenLicenseOracle mlo = new MavenLicenseOracle( failures, localRepo );

            ArtifactFilter artifactFilter = createResolvingArtifactFilter();

            DependencyNode rootNode = _nn( getDependencyGraphBuilder().buildDependencyGraph( project, artifactFilter ) );

            final List<Artifact> dependencies = new ArrayList<>();

            rootNode.accept( new DependencyNodeVisitor() {
                @Override
                public boolean visit( DependencyNode dependencyNode ) {
                    dependencies.add( dependencyNode.getArtifact() );
                    return true;
                }

                @Override
                public boolean endVisit( DependencyNode dependencyNode ) {
                    return true;
                }
            } );

            dependencies.

//            if( dependencies.size() > 1 ) {
//                dependencies.subList( 1, dependencies.size() ).
        stream().forEach( a -> {
//                    getLog().info( "" + a );

                licenseMapping( failures, lOracle, mlo, a );
            } );

            failures.throwOnError();

        } catch( MojoFailureException e ) {
            throw e;
        } catch( Exception e ) {
            e.printStackTrace();
            throw new MojoFailureException( e.getMessage() );
        }

    }

    private Optional<LicenseID> licenseMapping( Findings findings, LOracle lOracle, MavenLicenseOracle mlo, Artifact a ) {
        Optional<License> mavenLicense = mlo.getMavenLicense( a );
        Coordinates coo = Coordinates.valueOf( a );

//        getLog().info( "artifact:      " + coo );
        final Optional<LicenseID> byCoordinates = lOracle.getByCoordinates( Coordinates.valueOf( a ) );
//        getLog().info( "    license:   " + byCoordinates.map( Object::toString ).orElse( "-" ) );

        Optional<String> name = mavenLicense.flatMap( l -> Optional.ofNullable( l.getName() ) );
//        getLog().info( "  name:        " + name.orElse( "-" ) );
        Optional<LicenseID> byName = name.flatMap( lOracle::getByName );
//        getLog().info( "    license:   " + byName.map( Object::toString ).orElse( "-" ) );

        Optional<String> url = mavenLicense.flatMap( l -> Optional.ofNullable( l.getUrl() ) );
//        getLog().info( "  url:        " + url.orElse( "-" ) );
        Optional<LicenseID> byUrl = url.flatMap( lOracle::getByUrl );
//        getLog().info( "    license:   " + byUrl.map( Object::toString ).orElse( "-" ) );

        try {
            LicenseID sum =
                    byCoordinates.orElseGet(
                            () -> byName.orElseGet(
                                    () -> byUrl.orElseThrow(
                                            () -> new IllegalArgumentException( "no license for:" + coo ) ) ) );
            //          getLog().info( "  license:  " + sum );
            getLog().info( artiPlus( a ) + sum );
            return Optional.of( sum );
        } catch( IllegalArgumentException e ) {
            getLog().error( "artifact: " + coo + "   has no or not precise enough license" );
            getLog().error( "    by coordinates : " + byCoordinates.map( Object::toString ).orElse( "-" ) );
            getLog().error( "    by license name: " + name.orElse( "-" ) + " -> " + byName.map( Object::toString ).orElse( "-" ) );
            getLog().error( "    by license url : " + url.orElse( "-" ) + " ->" + byUrl.map( Object::toString ).orElse( "-" ) );

            findings.error( "artifact: " + coo + "   has no or not precise enough license" );
            return Optional.empty();
        }
    }

    String artiPlus( Artifact coo ) {

        StringBuilder sb = new StringBuilder().append( coo.toString() );

        for( int i = coo.toString().length(); i < 80; i++ ) {
            sb.append( " " );
        }

        return sb.toString();

    }

    /**
     * Gets the artifact filter to use when resolving the dependency tree.
     *
     * @return the artifact filter
     */
    private ArtifactFilter createResolvingArtifactFilter() {
        return new ScopeArtifactFilter( Artifact.SCOPE_TEST );
    }

}
