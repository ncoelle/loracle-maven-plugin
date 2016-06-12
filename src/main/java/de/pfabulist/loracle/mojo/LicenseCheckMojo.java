package de.pfabulist.loracle.mojo;

import de.pfabulist.loracle.buildup.JSONStartup;
import de.pfabulist.loracle.license.Coordinates;
import de.pfabulist.loracle.license.Decider;
import de.pfabulist.loracle.license.LOracle;
import de.pfabulist.loracle.license.LicenseID;
import de.pfabulist.unchecked.Unchecked;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.apache.maven.artifact.resolver.filter.ScopeArtifactFilter;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.License;
import org.apache.maven.project.MavenProject;
import org.apache.maven.shared.dependency.graph.DependencyGraphBuilder;
import org.apache.maven.shared.dependency.graph.DependencyNode;
import org.apache.maven.shared.dependency.graph.traversal.DependencyNodeVisitor;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static de.pfabulist.nonnullbydefault.NonnullCheck._nn;
import static de.pfabulist.unchecked.NullCheck._orElseGet;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

public class LicenseCheckMojo {

    private final Findings log;
    private final MavenLicenseOracle mlo;
    private final LOracle lOracle;
    private final MavenProject mavenProject;
    private final DependencyGraphBuilder dependencyGraphBuilder;
    private final boolean andIsOr;
    private Optional<Coordinates> self = Optional.empty();

    private Set<Coordinates> necessaries = new HashSet<>();
    private Map<Coordinates, LicenseID> licenses = new HashMap<>();
    private Map<Coordinates, String> scopes = new HashMap<>();

    public LicenseCheckMojo( Findings log, Path localRepo, MavenProject project, DependencyGraphBuilder dependencyGraphBuilder, boolean andIsOr ) {
        this.log = log;
        this.dependencyGraphBuilder = dependencyGraphBuilder;
        this.mlo = new MavenLicenseOracle( log, localRepo );
        this.lOracle = JSONStartup.start().spread();
        this.mavenProject = project;
        this.andIsOr = andIsOr;

        log.info( "---------------------------------------" );
        log.info( "      loracle license check            " );
        log.info( "---------------------------------------" );

    }

    public void config( List<LicenseDeclaration> licenseDeclarations, List<UrlDeclaration> urlDeclarations, int allowUrlsCheckedDaysBefore ) {
        configLicenseDeclarations( licenseDeclarations );
        configUrlDeclarations( urlDeclarations );
        configCheckUrls( allowUrlsCheckedDaysBefore );
    }

    private void configCheckUrls( int allowUrlsCheckedDaysBefore ) {
        if( allowUrlsCheckedDaysBefore > 0 ) {
            lOracle.allowUrlsCheckedDaysBefore( allowUrlsCheckedDaysBefore );
        }

    }

    private void configUrlDeclarations( List<UrlDeclaration> urlDeclarations ) {
        urlDeclarations.forEach( ud -> {
            LicenseID license = lOracle.getOrThrowByName( ud.getLicense() );
            lOracle.addUrlCheckedAt( license, ud.getUrl(), ud.getCheckedAt() );
            log.debug( "setting " + ud.getUrl() + " -> " + license + ", check at " + ud.getCheckedAt() );
        } );
    }

    private void configLicenseDeclarations( List<LicenseDeclaration> licenseDeclarations ) {
        licenseDeclarations.forEach( excl -> {
            Coordinates coo = excl.getCoordinates();
            LicenseID license = lOracle.getOrThrowByName( excl.getLicense() );

            lOracle.addLicenseForArtifact( coo, license );

            log.debug( "setting " + coo + " -> " + license );
        } );
    }

    public void getDependencies() {
        getPluginsAndTheirDependencies();
        getNormalDependencies();
    }

    private void getNormalDependencies() {
        ArtifactFilter artifactFilter = new ScopeArtifactFilter( Artifact.SCOPE_TEST );

        DependencyNode rootNode = Unchecked.u( () -> _nn( dependencyGraphBuilder.buildDependencyGraph( mavenProject, artifactFilter ) ) );

        rootNode.accept( new DependencyNodeVisitor() {
            @Override
            public boolean visit( DependencyNode dependencyNode ) {
                Artifact a = _nn( dependencyNode.getArtifact() );
                Coordinates coo = Coordinates.valueOf( a );
                necessaries.add( coo );
                updateScope( coo, _orElseGet( a.getScope(), "compile" ) );

                if( !self.isPresent() ) {
                    self = Optional.of( coo );
                }
                return true;
            }

            @Override
            public boolean endVisit( DependencyNode dependencyNode ) {
                return true;
            }
        } );
    }

    private void getPluginsAndTheirDependencies() {
        _nn( mavenProject.getBuild() ).getPlugins().forEach( plugin -> {

            Coordinates plugCoo = new Coordinates( _nn( plugin.getGroupId() ), _nn( plugin.getArtifactId() ), _nn( plugin.getVersion() ) );

            necessaries.add( plugCoo );
            scopes.putIfAbsent( plugCoo, "plugin" );

            List<Dependency> deps = _nn( plugin.getDependencies() );
            deps.forEach( d -> {
                Coordinates dCoo = new Coordinates( _nn( d.getGroupId() ), _nn( d.getArtifactId() ), _nn( d.getVersion() ) );
                necessaries.add( dCoo );
                scopes.putIfAbsent( dCoo, "plugin" );
            } );
        } );
    }

    public void determineLicenses() {
        necessaries.stream().
                sorted( ( a, b ) -> getScopeLevel( _nn( scopes.get( a ) ) ) - getScopeLevel( _nn( scopes.get( b ) ) ) ).
                forEach( c -> licenseMapping( c, _nn( scopes.get( c ) ) ) );
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
                log.warn( "unexpected scope " + scope );
                return 100;
        }
    }

    private Optional<LicenseID> licenseMapping( Coordinates coo, String scope ) {
        log.debug( coo.toString() + "    license is ..." );
        List<License> mavenLicenses = mlo.getMavenLicense( coo );

        final Optional<LicenseID> byCoordinates = lOracle.getByCoordinates( coo );

        AtomicReference<Boolean> error = new AtomicReference<>( false );

        Optional<LicenseID> ret = mavenLicenses.stream().
                map( ml -> mavenLicenseToLicense( coo, byCoordinates, error, ml ) ).
                collect( Collectors.reducing( Optional.empty(), this::and ) );

        if( ret.isPresent() && !_nn( error.get() ) ) {
            log.info( String.format( "%-100s %-20s", coo, scope ) + ret.get() );
        }

        ret.ifPresent( l -> licenses.put( coo, l ) );

        return ret;

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

    private Optional<LicenseID> mavenLicenseToLicense( Coordinates coo,
                                                       Optional<LicenseID> byCoordinates,
                                                       AtomicReference<Boolean> error,
                                                       License mavenLicense ) {

        Optional<String> name = Optional.ofNullable( mavenLicense.getName() );
        Optional<LicenseID> byName = name.flatMap( lOracle::getByName );
        Optional<String> url = Optional.ofNullable( mavenLicense.getUrl() );
        Optional<LicenseID> byUrl = url.flatMap( lOracle::getByUrl );

        Optional<LicenseID> licenseID = new Decider( log ).decide( byCoordinates, byName, byUrl );

        if( !licenseID.isPresent() ) {
            log.error( "artifact: " + coo + "   has one unprecise license" );
            log.error( "    by coordinates : " + byCoordinates.map( Object::toString ).orElse( "-" ) );
            log.error( "    by license name: " + name.orElse( "-" ) + " -> " + byName.map( Object::toString ).orElse( "-" ) );
            log.error( "    by license url : " + url.orElse( "-" ) + " ->" + byUrl.map( Object::toString ).orElse( "-" ) );

            log.error( "artifact: " + coo + "   has no or not precise enough license" );
            error.set( true );

            name.ifPresent( n -> log.error( "   it could be (by name) " + lOracle.guessByName( n ) ) );
            url.ifPresent( u -> log.error( "   it could be (by url) " + lOracle.guessByUrl( u ) ) );
        }

        return licenseID;
    }

    private Optional<LicenseID> and( Optional<LicenseID> l, Optional<LicenseID> r ) {
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

        LicenseID ret = lOracle.getAnd( left, right );
        log.warn( "is that really <" + ret + "> or should that be <" + lOracle.getOr( left, right ) + ">" );
        return Optional.of( ret );

    }

    public void checkCompatibility() {
        licenses.forEach( ( c, l ) -> {
            lOracle.getMore( l ).fedoraApproved.ifPresent( fed -> {
                if( !fed ) {
                    log.error( "bad license " + l + " used by " + c + "  (not approved by fedora)" );
                }
            } );
        } );

        if( !self.isPresent() ) {
            return;
        }

        if( !licenses.containsKey( self.get() ) ) {
            return;
        }

        LicenseID mine = _nn( licenses.get( self.get() ) );

        if( mine.equals( lOracle.getOrThrowByName( "gpl-2.0" ) ) || mine.equals( lOracle.getOrThrowByName( "gpl-2.0+" ) ) ) {
            licenses.forEach( ( c, l ) -> {
                lOracle.getMore( l ).gpl2Compatible.ifPresent( ga -> {
                    if( !ga ) {
                        log.error( "not gpl2 compatible: " + l + " used by " + c );
                    }
                } );
            } );
        }

        if( mine.equals( lOracle.getOrThrowByName( "gpl-3.0" ) ) || mine.equals( lOracle.getOrThrowByName( "gpl-3.0+" ) ) ) {
            licenses.forEach( ( c, l ) -> {
                lOracle.getMore( l ).gpl3Compatible.ifPresent( ga -> {
                    if( !ga ) {
                        log.error( "not gpl3 compatible: " + l + " used by " + c );
                    }
                } );
            } );
        }

        if( lOracle.getMore( mine ).copyLeft ) {
            licenses.forEach( ( c, l ) -> {
                if( l.equals( mine ) ) {
                    return;
                }

                lOracle.getMore( l ).gpl2Compatible.ifPresent( gc -> {
                                                                   if( !gc ) {
                                                                       log.error( "not gpl2 compatible: " + l + " used by " + c );
                                                                   }
                                                               }
                );

                lOracle.getMore( l ).gpl3Compatible.ifPresent( gc -> {
                                                                   if( !gc ) {
                                                                       log.error( "not gpl2 compatible: " + l + " used by " + c );
                                                                   }
                                                               }
                );
            } );
        }

        if( !lOracle.getMore( mine ).copyLeft ) {
            licenses.forEach( ( c, l ) -> {
                if( lOracle.getMore( l ).copyLeft ) {
                    log.error( "can't depend on a copy left license: " + l + " used by " + c );
                }
            } );
        }
    }

}
