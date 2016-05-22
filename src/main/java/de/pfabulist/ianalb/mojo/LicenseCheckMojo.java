//package de.pfabulist.ianalb.mojo;
//
//import de.pfabulist.loracle.mojo.Findings;
//import de.pfabulist.ianalb.model.oracle.Aliases;
//import de.pfabulist.ianalb.model.Check;
//import de.pfabulist.loracle.license.Coordinates;
//import de.pfabulist.ianalb.model.oracle.GuessLicense;
//import de.pfabulist.ianalb.model.oracle.KnownLicenses;
//import de.pfabulist.ianalb.model.license.IBLicense;
//import de.pfabulist.ianalb.model.license.Licenses;
//import de.pfabulist.ianalb.model.oracle.UrlToName;
//import de.pfabulist.unchecked.Unchecked;
//import org.apache.maven.artifact.Artifact;
//import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
//import org.apache.maven.artifact.resolver.filter.ScopeArtifactFilter;
//import org.apache.maven.execution.MavenSession;
//import org.apache.maven.plugin.AbstractMojo;
//import org.apache.maven.plugin.MojoExecutionException;
//import org.apache.maven.plugin.MojoFailureException;
//import org.apache.maven.project.MavenProject;
//import org.apache.maven.shared.dependency.graph.DependencyGraphBuilder;
//import org.apache.maven.shared.dependency.graph.DependencyNode;
//import org.apache.maven.shared.dependency.graph.traversal.DependencyNodeVisitor;
//
//import javax.annotation.Nullable;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static de.pfabulist.nonnullbydefault.NonnullCheck._ni;
//
///**
// * Copyright (c) 2006 - 2016, Stephan Pfab
// * SPDX-License-Identifier: BSD-2-Clause
// */
//
////@Mojo( name = "license-check", defaultPhase = LifecyclePhase.INSTALL, requiresDependencyResolution = ResolutionScope.TEST, threadSafe = true )
//public class LicenseCheckMojo extends AbstractMojo {
//
////    @Component
//    @Nullable
//    DependencyGraphBuilder dependencyGraphBuilder;
//
////    @Parameter( defaultValue = "${project}", readonly = true )
//    @Nullable
//    MavenProject project;
//
////    @Parameter( defaultValue = "${session}", required = true, readonly = true )
//    @Nullable private MavenSession session;
//
//    @SuppressWarnings( "PMD.AvoidPrintStackTrace" )
//    public void execute() throws MojoExecutionException, MojoFailureException {
//        getLog().info( "---------------------------------------" );
//        getLog().info( "      license check                    " );
//        getLog().info( "---------------------------------------" );
//
//        MavenProject _project = _ni( project );
//        Path localRepo = Paths.get( _ni(session).getLocalRepository().getBasedir() );
//
//        try {
//
//            if( _project.getLicenses() == null || _project.getLicenses().isEmpty() ) {
//                throw new MojoFailureException( "no license set" );
//            }
//
//            if( _project.getLicenses().size() > 1 ) {
//                getLog().info( "license check can't deal with multiple licenses (yet)" );
//                return;
//            }
//
//            Findings failures = new Findings( getLog() );
//            Licenses licenses = new Licenses();
//            Aliases aliases = new Aliases( licenses );
//            GuessLicense guessLicense = new GuessLicense( failures,
//                                                          localRepo,
//                                                          new KnownLicenses( licenses ),
//                                                          aliases,
//                                                          new UrlToName( licenses ), licenses );
//            //License projLi = _project.getLicenses().get( 0 ).getName();
//            IBLicense projectLicense = guessLicense.guess( Coordinates.valueOf( _project.getArtifact() ),
//                                                           Optional.of( _project.getLicenses().get( 0 ))).
//                    orElseThrow( () -> new MojoFailureException( "project without license" ) );
//            Check check = new Check( licenses );
//
//
//            ArtifactFilter artifactFilter = createResolvingArtifactFilter();
//
//            DependencyNode rootNode = _ni( dependencyGraphBuilder ).buildDependencyGraph( project, artifactFilter );
//
//            final List<Artifact> dependencies = new ArrayList<>();
//
//            rootNode.accept( new DependencyNodeVisitor() {
//                @Override
//                public boolean visit( DependencyNode dependencyNode ) {
//                    dependencies.add( dependencyNode.getArtifact() );
//                    return true;
//                }
//
//                @Override
//                public boolean endVisit( DependencyNode dependencyNode ) {
//                    return true;
//                }
//            } );
//
//            if ( dependencies.size() > 1 ) {
//                dependencies.subList(1, dependencies.size()).stream().forEach(a -> {
//                    getLog().info("" + a);
//
//                    Optional<IBLicense> depLicense = guessLicense.guess(a);
//
//                    if (depLicense.isPresent()) {
//                        getLog().info("   license is: " + depLicense.get());
//                        if (!check.isCompatible(projectLicense, depLicense.get())) {
//                            throw Unchecked.u(new MojoFailureException("licenses not compatible " + projectLicense + " " + depLicense.get()));
//                        }
//                    } else {
//                        getLog().info("   license is: huh!!!!");
//                    }
//                });
//            }
//
//            failures.throwOnError();
//
//        } catch( MojoFailureException e ) {
//            throw e;
//        } catch( Exception e ) {
//            e.printStackTrace();
//            throw new MojoFailureException( e.getMessage() );
//        }
//
//    }
//
//    /**
//     * Gets the artifact filter to use when resolving the dependency tree.
//     *
//     * @return the artifact filter
//     */
//    private ArtifactFilter createResolvingArtifactFilter() {
//        ArtifactFilter filter;
//
//        // filter scope
////        if ( scope != null )
//        {
//            getLog().debug( "+ Resolving dependency tree for scope '" ); //+ scope + "'" );
//
//            filter = new ScopeArtifactFilter( "compile" );
//        }
//        //       else
//        {
//            //   filter = null;
//        }
//
//        return filter;
//    }
//
//
//
//}
