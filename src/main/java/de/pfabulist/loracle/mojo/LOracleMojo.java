package de.pfabulist.loracle.mojo;

import de.pfabulist.loracle.license.Coordinates;
import de.pfabulist.loracle.buildup.JSONStartup;
import de.pfabulist.loracle.license.Decider;
import de.pfabulist.loracle.license.LOracle;
import de.pfabulist.loracle.license.LicenseID;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.apache.maven.artifact.resolver.filter.ScopeArtifactFilter;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.License;
import org.apache.maven.model.Plugin;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

import static de.pfabulist.nonnullbydefault.NonnullCheck._ni;
import static de.pfabulist.nonnullbydefault.NonnullCheck._nn;
import static de.pfabulist.unchecked.NullCheck._orElseGet;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

@SuppressFBWarnings( { "UPM_UNCALLED_PRIVATE_METHOD" } )
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

    @Parameter( property = "license-check.urldeclarations" )
    @Nullable
    List<UrlDeclaration> urlDeclarations;

    @Parameter( property = "license-check.stopOnError", defaultValue = "true" )
    boolean stopOnError;

    @Parameter( property = "license-check.andIsOr", defaultValue = "false" )
    boolean andIsOr;

    @Parameter( property = "license-check.allowUrlsCheckedDaysBefore", defaultValue = "-1000" )
    int allowUrlsCheckedDaysBefore;

//    @Component
//    @Nullable
//    private PluginDescriptor pluginDescriptor;

//    private PluginDescriptor getPD() {
//        return _nn( pluginDescriptor );
//    }

    @Nullable
    private Findings failures;


    private Set<Coordinates> necessaries = new HashSet<>();
    private Map<Coordinates, LicenseID> licenses = new HashMap<>();
    private Map<Coordinates, String> scopes = new HashMap<>();

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

    public List<UrlDeclaration> getUrlDeclarations() {
        return urlDeclarations == null ? Collections.emptyList() : urlDeclarations;
    }

    @SuppressWarnings( "PMD.AvoidPrintStackTrace" )
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info( "---------------------------------------" );
        getLog().info( "      loracle license check            " );
        getLog().info( "---------------------------------------" );

        Path localRepo = Paths.get( _nn( getSession().getLocalRepository() ).getBasedir() );

        try {

            if( getProject().getLicenses() == null || getProject().getLicenses().isEmpty() ) {
                throw new MojoFailureException( "no license set" );
            }

            if( getProject().getLicenses().size() > 1 ) {
                MavenLicenseOracle.getAndLicense( getProject().getLicenses() );
//                License and = new License();
//                and.setName( getProject().g );
//                getLog().info( "license check can't deal with multiple licenses (yet)" );
//                return;
            }

            LOracle lOracle = JSONStartup.start().spread();

            if( allowUrlsCheckedDaysBefore > 0 ) {
                lOracle.allowUrlsCheckedDaysBefore( allowUrlsCheckedDaysBefore );
            }

            MavenLicenseOracle mlo = new MavenLicenseOracle( getLog(), localRepo );

            List<Plugin> plugins = _nn( getProject().getBuild() ).getPlugins();
            for( Plugin p : plugins ) {
                Coordinates plugCoo = new Coordinates( _nn( p.getGroupId() ), _nn( p.getArtifactId() ), _nn( p.getVersion() ) );

                necessaries.add( plugCoo );
                scopes.putIfAbsent( plugCoo, "plugin" );

                List<Dependency> deps = _nn( p.getDependencies() );
                deps.forEach( d -> {
                    Coordinates dCoo = new Coordinates( _nn( d.getGroupId() ), _nn( d.getArtifactId() ), _nn( d.getVersion() ) );
                    necessaries.add( dCoo );
                    scopes.putIfAbsent( dCoo, "plugin" );
                } );
            }

            for( LicenseDeclaration excl : getLicenseDeclarations() ) {
                Coordinates coo = Coordinates.valueOf( excl.getCoordinates().orElseThrow(
                        () -> new MojoFailureException( "no coordinates set in configuration of LicenseDeclarations in LOracle Plugin" ) ) );
                LicenseID license =
                        lOracle.getByName(
                                excl.getLicense().orElseThrow(
                                        () -> new MojoFailureException( "no name set in configuration of LicenseDeclarations in LOracle Plugin" ) ) ).
                                orElseThrow( () -> new MojoFailureException( "illegal license name in configuration of LicenseDeclarations in LOracle Plugin" ) );

                lOracle.addLicenseForArtifact( coo, license );

                getLog().info( "setting " + coo + " -> " + license );
            }

            getUrlDeclarations().forEach( ud -> {
                lOracle.addUrlCheckedAt( lOracle.getOrThrowByName( ud.getLicense() ), ud.getUrl(), ud.getCheckedAt() );
            } );

            ArtifactFilter artifactFilter = createResolvingArtifactFilter();

            DependencyNode rootNode = _nn( getDependencyGraphBuilder().buildDependencyGraph( project, artifactFilter ) );

//            final List<Artifact> dependencies = new ArrayList<>();

            rootNode.accept( new DependencyNodeVisitor() {
                @Override
                public boolean visit( DependencyNode dependencyNode ) {
  //                  dependencies.add( dependencyNode.getArtifact() );

                    Artifact a = _nn( dependencyNode.getArtifact() );
                    Coordinates coo = Coordinates.valueOf( a );
                    necessaries.add( coo );
                    updateScope( coo, _orElseGet( a.getScope(), "compile" ) );

                    return true;
                }

                @Override
                public boolean endVisit( DependencyNode dependencyNode ) {
                    return true;
                }
            } );


            necessaries.stream().
                    sorted( ( a, b ) -> getScopeLevel( _nn( scopes.get( a ) ) ) - getScopeLevel( _nn( scopes.get( b ) ) ) ).
                    forEach( c -> licenseMapping( lOracle, mlo, c, _nn( scopes.get( c ) ) ) );

            licenses.forEach( ( a, c ) -> getLog().info( c.toString() ) );

            if( stopOnError ) {
                getLog().throwOnError();
            }

        } catch( MojoFailureException e ) {
            if( stopOnError ) {
                throw e;
            }
        } catch( Exception e ) {
            e.printStackTrace();
            if( stopOnError ) {
                throw new MojoFailureException( e.getMessage() );
            }
        }

    }

    private void updateScope( Coordinates coo, String scope ) {
        if( !scopes.containsKey( coo ) ) {
            scopes.put( coo, scope );
            return;
        }

        String oldScope = _nn( scopes.get( coo ) );

        if( getScopeLevel( scope ) < getScopeLevel( oldScope ) ) {
            scopes.put( coo, scope );
        }

    }

    int getScopeLevel( String scope ) {
        switch( scope ) {
            case "plugin":
                return 8;
            case "import":
                return 7;
            case "system":
                return 6;
            case "test":
                return 5;
            case "runtime":
                return 4;
            case "provided":
                return 3;
            case "optional":
                return 2;
            case "compile":
                return 1;
            default:
                getLog().warn( "unexpected scope " + scope );
                return 100;
        }
    }

    private Optional<LicenseID> licenseMapping( LOracle lOracle, MavenLicenseOracle mlo, Coordinates coo, String scope ) {
        getLog().debug( coo.toString() + "    license is ..." );
        List<License> mavenLicenses = mlo.getMavenLicense( coo );

        final Optional<LicenseID> byCoordinates = lOracle.getByCoordinates( coo );

        AtomicReference<Boolean> error = new AtomicReference<>( false );

        Optional<LicenseID> ret = mavenLicenses.stream().
                map( ml -> mavenLicenseToLicense( lOracle, coo, byCoordinates, error, ml ) ).
                collect( Collectors.reducing( Optional.empty(), and( lOracle ) ) );

        if( ret.isPresent() && !_nn( error.get() ) ) {
            getLog().info( artiPlus( coo, scope ) + ret.get() );
        }

        return ret;

    }

    private BinaryOperator<Optional<LicenseID>> and( LOracle lOracle ) {
        return ( Optional<LicenseID> l, Optional<LicenseID> r ) -> {
            if( !l.isPresent() ) {
                return r;
            }

            if( !r.isPresent() ) {
                return l;
            }

            LicenseID left = _nn( l.get() );
            LicenseID right = _nn( r.get() );

            if( left.equals( right ) ) {
                return l;
            }

            if( andIsOr ) {
                return Optional.of( lOracle.getOr( left, right ) );
            }

            LicenseID ret =lOracle.getAnd( left, right );
            getLog().warn( "is that really <" + ret + "> or should that be <" + lOracle.getOr( left, right ) + ">");
            return Optional.of( ret );

        };
    }

    private Optional<LicenseID> mavenLicenseToLicense( LOracle lOracle,
                                                       Coordinates coo,
                                                       Optional<LicenseID> byCoordinates,
                                                       AtomicReference<Boolean> error,
                                                       License mavenLicense ) {

        Optional<String> name = Optional.ofNullable( mavenLicense.getName() );
        Optional<LicenseID> byName = name.flatMap( lOracle::getByName );
        Optional<String> url = Optional.ofNullable( mavenLicense.getUrl() );
        Optional<LicenseID> byUrl = url.flatMap( lOracle::getByUrl );

        Optional<LicenseID> licenseID = new Decider( getLog() ).decide( byCoordinates, byName, byUrl );

        if( !licenseID.isPresent() ) {
            getLog().error( "artifact: " + coo + "   has one unprecise license" );
            getLog().error( "    by coordinates : " + byCoordinates.map( Object::toString ).orElse( "-" ) );
            getLog().error( "    by license name: " + name.orElse( "-" ) + " -> " + byName.map( Object::toString ).orElse( "-" ) );
            getLog().error( "    by license url : " + url.orElse( "-" ) + " ->" + byUrl.map( Object::toString ).orElse( "-" ) );

            getLog().error( "artifact: " + coo + "   has no or not precise enough license" );
            error.set( true );

            name.ifPresent( n -> getLog().error( "   it could be (by name) " + lOracle.guessByName( n ) ) );
            url.ifPresent( u -> getLog().error( "   it could be (by url) " + lOracle.guessByUrl( u ) ) );
        }

        return licenseID;
    }

    /**
     * Gets the artifact filter to use when resolving the dependency tree.
     *
     * @return the artifact filter
     */
    private ArtifactFilter createResolvingArtifactFilter() {
        return new ScopeArtifactFilter( Artifact.SCOPE_TEST );
    }

    String artiPlus( Coordinates coo, String scope ) {

        return String.format( "%-100s %-20s", coo, scope );
    }

    @Override
    public Findings getLog() {
        if ( failures == null ) {
            failures = new Findings( super.getLog() );
        }
        return failures;
    }
}
