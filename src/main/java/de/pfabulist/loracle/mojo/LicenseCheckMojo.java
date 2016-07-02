package de.pfabulist.loracle.mojo;

import de.pfabulist.loracle.attribution.CopyrightHolder;
import de.pfabulist.loracle.attribution.GetHolder;
import de.pfabulist.loracle.attribution.JarAccess;
import de.pfabulist.loracle.attribution.SrcAccess;
import de.pfabulist.loracle.buildup.JSONStartup;
import de.pfabulist.loracle.license.And;
import de.pfabulist.loracle.license.ContentToLicense;
import de.pfabulist.loracle.license.Coordinates;
import de.pfabulist.loracle.license.Coordinates2License;
import de.pfabulist.loracle.license.Decider;
import de.pfabulist.loracle.license.LOracle;
import de.pfabulist.loracle.license.LicenseFromText;
import de.pfabulist.loracle.license.LicenseID;
import de.pfabulist.loracle.license.MappedLicense;
import de.pfabulist.nonnullbydefault.NonnullCheck;
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
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
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

    private final Coordinates2License coordinates2License;

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

        coordinates2License = JSONStartup.previous( andIsOr );
        coordinates2License.setLog( log );
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
                coordinates2License.add( coo );
                coordinates2License.updateScope( coo, _orElseGet( a.getScope(), "compile" ) );

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

            coordinates2License.add( plugCoo );
            coordinates2License.updateScope( plugCoo, "plugin" );

            List<Dependency> deps = _nn( plugin.getDependencies() );
            deps.forEach( d -> {
                Coordinates dCoo = new Coordinates( _nn( d.getGroupId() ), _nn( d.getArtifactId() ), _nn( d.getVersion() ) );
                coordinates2License.add( dCoo );
                coordinates2License.updateScope( dCoo, "plugin" );
            } );
        } );
    }

    public void determineMavenLicenses() {
        log.debug( "-- determine licenses --" );
//        coordinates2License.update( this::licenseMapping );
        coordinates2License.update( this::getMavenLicenseInfo );
    }

    private void getMavenLicenseInfo( Coordinates coo, Coordinates2License.LiCo lico ) {
        List<License> mavenLicenses = mlo.getMavenLicense( coo );

        lico.setMLicenses(
                mavenLicenses.stream().map(
                        l -> new Coordinates2License.MLicense( _orElseGet( l.getName(), "" ),
                                                               _orElseGet( l.getUrl(), "" ),
                                                               _orElseGet( l.getComments(), "" ) ) ).
                        collect( Collectors.toList() ) );

    }

//    @SuppressWarnings( "PMD.AvoidPrintStackTrace" )
//    private MappedLicense licenseMapping( Coordinates coo ) {
//        log.debug( coo.toString() + "    license is ..." );
//        List<License> mavenLicenses = mlo.getMavenLicense( coo );
//
//        if( mavenLicenses.isEmpty() ) {
//            mavenLicenses = Collections.singletonList( new License() );
//        }
//
//        final MappedLicense byCoordinates = lOracle.getByCoordinates( coo );
//
//        And and = new And( lOracle, log, andIsOr );
//
//        try {
//            return mavenLicenses.stream().
//                    map( ml -> mavenLicenseToLicense( coo, byCoordinates, ml ) ).
//                    collect( Collectors.reducing( MappedLicense.empty(), and::and ) );
//        } catch( Exception ex ) {
//            ex.printStackTrace();
//            return MappedLicense.empty();
//        }
//
//    }

    private MappedLicense mavenLicenseToLicense( //Coordinates coo,
                                                 // MappedLicense byCoordinates,
                                                 Coordinates2License.MLicense mavenLicense ) {

        Optional<String> name = Optional.of( mavenLicense.getName() );
        MappedLicense byName = name.map( lOracle::getByName ).orElse( MappedLicense.empty() );
        Optional<String> url = Optional.of( mavenLicense.getUrl() );
        MappedLicense byUrl = url.map( lOracle::getByUrl ).orElse( MappedLicense.empty() );

        MappedLicense byComments = new ContentToLicense( lOracle, "comments", log, andIsOr ).findLicenses( mavenLicense.getComment() );

        mavenLicense.setByName( byName );
        mavenLicense.setByUrl( byUrl );
        mavenLicense.setByComment( byComments );

        //        if( !licenseID.isPresent() ) {
//            log.warn( "[pom] artifact: " + coo + "   has one unprecise license" );
//            log.warn( "[pom]    by coordinates : " + byCoordinates );
//            log.warn( "[pom]    by license name: " + name.orElse( "-" ) + " -> " + byName );
//            log.warn( "[pom]    by license url : " + url.orElse( "-" ) + " ->" + byUrl );
//
//            log.warn( "[pom] artifact: " + coo + "   has no or not precise enough license" );
//
//            name.ifPresent( n -> log.warn( "[pom]   it could be (by name) " + lOracle.guessByName( n ) ) );
//            url.ifPresent( u -> log.warn( "[pom]   it could be (by url) " + lOracle.guessByUrl( u ) ) );
//        }

        return new Decider().decide( byName, byUrl, byComments );
    }

    public String checkCompatibility( Coordinates coo, String licenseStr ) {

        LicenseID license = lOracle.getOrThrowByName( licenseStr );

        AtomicReference<String> ret = new AtomicReference<>( "" );

        lOracle.getAttributes( license ).isFedoraApproved().ifPresent( fed -> {
            if( !fed ) {
                ret.set( "bad license " + license + " used by " + coo + "  (not approved by fedora)" );
            }
        } );

        if( !self.isPresent() ) {
            return "";
        }

        Optional<Coordinates2License.LiCo> info = coordinates2License.get( _nn( self.get() ) );

        if( !info.isPresent() || !_nn( info.get() ).getLicense().isPresent() ) {
            return "no license on current artifact";
        }

        LicenseID mine = lOracle.getOrThrowByName( _nn( _nn( info.get() ).getLicense().get() ) );

        if( mine.equals( license ) ) {
            return "";
        }

        if( lOracle.getAttributes( mine ).isCopyLeftDef() ) {
            lOracle.getAttributes( license ).isGpl2Compatible().ifPresent(
                    gc -> {
                        if( !gc ) {
                            ret.set( "not gpl2 compatible: " + license + " used by " + coo );
                        }
                    } );

            lOracle.getAttributes( license ).isGpl3Compatible().ifPresent(
                    gc -> {
                        if( !gc ) {
                            ret.set( "not gpl2 compatible: " + license + " used by " + coo );
                        }
                    } );
        }

        if( !lOracle.getAttributes( mine ).isCopyLeftDef() ) {
            if( lOracle.getAttributes( license ).isCopyLeftDef() ) {
                ret.set( "can't depend on a copy left license: " + license + " used by " + coo );
            }
        }

        return _nn( ret.get() );

    }

    public void checkCompatibility() {
        coordinates2License.checkCompatibility( this::checkCompatibility );

//        licenses.forEach( ( c, l ) ->
//                                  lOracle.getAttributes( l ).isFedoraApproved().ifPresent( fed -> {
//                                      if( !fed ) {
//                                          log.error( "bad license " + l + " used by " + c + "  (not approved by fedora)" );
//                                      }
//                                  } ) );
//
//        if( !self.isPresent() ) {
//            return;
//        }
//
//        if( !licenses.containsKey( self.get() ) ) {
//            return;
//        }
//
//        LicenseID mine = _nn( licenses.get( self.get() ) );
//
//        if( lOracle.getAttributes( mine ).isCopyLeftDef() ) {
//            licenses.forEach( ( c, l ) -> {
//                if( l.equals( mine ) ) {
//                    return;
//                }
//
//                lOracle.getAttributes( l ).isGpl2Compatible().ifPresent( gc -> {
//                                                                             if( !gc ) {
//                                                                                 scopeDependingLog( c, "not gpl2 compatible: " + l + " used by " + c );
//                                                                             }
//                                                                         }
//                );
//
//                lOracle.getAttributes( l ).isGpl3Compatible().ifPresent( gc -> {
//                                                                             if( !gc ) {
//                                                                                 scopeDependingLog( c, "not gpl2 compatible: " + l + " used by " + c );
//                                                                             }
//                                                                         }
//                );
//            } );
//        }
//
//        if( !lOracle.getAttributes( mine ).isCopyLeftDef() ) {
//            licenses.forEach( ( c, l ) -> {
//                if( lOracle.getAttributes( l ).isCopyLeftDef() ) {
//                    scopeDependingLog( c, "can't depend on a copy left license: " + l + " used by " + c );
//                }
//            } );
//        }
    }

    public void getHolder() {
        final GetHolder gh = new GetHolder( lOracle, mlo, log );
        coordinates2License.getHolders( gh::getHolder );
    }

    public void summery() {
        coordinates2License.summery();
    }

    public void src() {
        SrcAccess src = new SrcAccess( lOracle, mlo, log, andIsOr );
        coordinates2License.fromSrc( src::check );

    }

    public void store() {
        JSONStartup.previousOut( coordinates2License );
    }

    public void jars() {
        JarAccess src = new JarAccess( lOracle, mlo, log, andIsOr );
        coordinates2License.fromJar( src::check );
    }

    public void computeLicense() {
        coordinates2License.update( this::compute );
    }

    private void compute( Coordinates coordinates, Coordinates2License.LiCo liCo ) {
        MappedLicense byCoo = lOracle.getByCoordinates( coordinates );

        liCo.setByCoordinates( byCoo );

        MappedLicense byPom = liCo.getMavenLicenses().stream().
                map( this::mavenLicenseToLicense ).
                reduce( MappedLicense.empty(), (a,b) -> new And( lOracle, log, andIsOr ).and( a,b ));

        liCo.setPomLicense( byPom );

        LicenseFromText lft = new LicenseFromText( lOracle );
        MappedLicense t1 = lft.getLicense( liCo.getLicenseTxt() );
        if ( t1.isPresent() ) {
            log.debug( "[woo] " + coordinates + " -> " + t1 );
            liCo.setLicenseTxtLicense( t1 );
        }
        MappedLicense t2 = lft.getLicense( liCo.getPomHeader() );
        if ( t2.isPresent() ) {
            log.debug( "[woo] " + coordinates + " -> " + t2 );
            liCo.setPomHeaderLicense( t2 );
        }
        MappedLicense t3 = lft.getLicense( liCo.getHeaderTxt() );
        if ( t3.isPresent() ) {
            log.debug( "[woo] " + coordinates + " -> " + t3 );
            liCo.setHeaderLicense( t3 );
        }
        MappedLicense t4 = lft.getLicense( liCo.getNotice() );
        if ( t4.isPresent() ) {
            log.debug( "[woo] " + coordinates + " -> " + t4 );
            liCo.setNoticeLicense( t4 );
        }

        MappedLicense sum = new Decider().decide( byCoo, byPom, t1, t2, t3, t4 );


        if ( sum.isPresent() ) {
            liCo.setLicense( sum );
            //log.debug( "coordinates + pom: " + coordinates + " -> " +sum.toString()  );
            // todo overridable by flag
            return;
        }

        log.debug( "searching in in content for " + coordinates );

        MappedLicense byPomHeader = new ContentToLicense( lOracle, "by pom header", log, andIsOr ).findLicenses( liCo.getPomHeader() );
        liCo.setPomHeaderLicense( byPomHeader );


        MappedLicense byLicenseTxt = new ContentToLicense( lOracle, "by license file", log, andIsOr ).findLicenses( liCo.getLicenseTxt() );
        liCo.setLicenseTxtLicense( byLicenseTxt );

        MappedLicense byHeader = new ContentToLicense( lOracle, "by file header", log, andIsOr ).findLicenses( liCo.getHeaderTxt() );
        liCo.setHeaderLicense( byHeader );

        MappedLicense byNotice = new ContentToLicense( lOracle, "by notice", log, andIsOr ).findLicenses( liCo.getNotice() );
        liCo.setNoticeLicense( byNotice );


        sum = new Decider().decide( byCoo, byPom, byPomHeader, byLicenseTxt, byHeader, byNotice );
        liCo.setLicense( sum );

        log.debug( "done found?: " + sum.toString()  );

    }

    public void computeHolder() {
        coordinates2License.update( this::computeHolder );
    }

    @SuppressWarnings( "PMD.UnusedFormalParameter" ) // so it can be called via update
    private void computeHolder( Coordinates coordinates, Coordinates2License.LiCo liCo ) {

        Optional<CopyrightHolder> ret =
                liCo.getMavenLicenses().stream().
                        map( ml -> ContentToLicense.copyRightPattern.matcher( NonnullCheck._orElseGet( ml.getComment(), "" ) ) ).
                        filter( Matcher::matches ).
                        map( m -> new CopyrightHolder( _nn( m.group( "year" ) ), _nn( m.group( "holder" ) ) ) ).
                        findAny();

        if ( ret.isPresent() ) {
            liCo.setHolder( ret );
            return;
        }

        ret = new ContentToLicense( lOracle, "by license file", log, andIsOr ).getHolder( liCo.getLicenseTxt() );
        if ( ret.isPresent() ) {
            liCo.setHolder( ret );
            return;
        }

        ret = new ContentToLicense( lOracle, "by header file", log, andIsOr ).getHolder( liCo.getHeaderTxt() );
        if ( ret.isPresent() ) {
            liCo.setHolder( ret );
            return;
        }

        ret = new ContentToLicense( lOracle, "by notice file", log, andIsOr ).getHolder( liCo.getNotice() );
        if ( ret.isPresent() ) {
            liCo.setHolder( ret );
        }

    }

    public void getNotice() {
        final GetHolder gh = new GetHolder( lOracle, mlo, log );
        coordinates2License.update( gh::getNotice );
    }

    public void getPomHeader() {
        SrcAccess src = new SrcAccess( lOracle, mlo, log, andIsOr );
        coordinates2License.fromSrc( src::getPomHeader );

    }
}
