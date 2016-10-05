package de.pfabulist.loracle.mojo;

import de.pfabulist.loracle.attribution.CopyrightHolder;
import de.pfabulist.loracle.attribution.GetHolder;
import de.pfabulist.loracle.attribution.JarAccess;
import de.pfabulist.loracle.attribution.SrcAccess;
import de.pfabulist.loracle.buildup.JSONStartup;
import de.pfabulist.loracle.license.ContentToLicense;
import de.pfabulist.loracle.license.Coordinates;
import de.pfabulist.loracle.license.Coordinates2License;
import de.pfabulist.loracle.license.LOracle;
import de.pfabulist.loracle.license.LicenseID;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.apache.maven.artifact.resolver.filter.ScopeArtifactFilter;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.License;
import org.apache.maven.project.MavenProject;
import org.apache.maven.shared.dependency.graph.DependencyGraphBuilder;
import org.apache.maven.shared.dependency.graph.DependencyNode;
import org.apache.maven.shared.dependency.graph.traversal.DependencyNodeVisitor;

import javax.annotation.Nullable;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

import static de.pfabulist.loracle.license.Coordinates2License.getScopeLevel;
import static de.pfabulist.roast.NonnullCheck._nn;
import static de.pfabulist.roast.NonnullCheck._orElseGet;
import static de.pfabulist.roast.functiontypes.Supplierr.v;

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
    private final LicenseIntelligence licenseIntelligence;
    private final Downloader downloader;
    private final ContentToLicense contentToLicense;
    private Optional<Coordinates> self = Optional.empty();

    private final Coordinates2License coordinates2License;
    private final SrcAccess src;
    private final JarAccess jarAccess;
    private final SrcAccess srcAccess;

    public LicenseCheckMojo( Findings log, Path localRepo, MavenProject project, DependencyGraphBuilder dependencyGraphBuilder ) {
        this.log = log;
        this.dependencyGraphBuilder = dependencyGraphBuilder;
        this.mlo = new MavenLicenseOracle( log, localRepo );
        this.lOracle = JSONStartup.start().spread();
        this.mavenProject = project;

        log.info( "---------------------------------------" );
        log.info( "      loracle license check            " );
        log.info( "---------------------------------------" );

        coordinates2License = JSONStartup.previous();
        coordinates2License.setLog( log );
        licenseIntelligence = new LicenseIntelligence( lOracle, log );
        downloader = new Downloader( log, lOracle );
        contentToLicense = new ContentToLicense( lOracle, log );
        src = new SrcAccess( lOracle, mlo, log );
        jarAccess = new JarAccess( lOracle, mlo, log );
        srcAccess = new SrcAccess( lOracle, mlo, log );
    }

    public void config( List<LicenseDeclaration> licenseDeclarations, List<UrlDeclaration> urlDeclarations, int allowUrlsCheckedDaysBefore ) {

        configLicenseDeclarations( licenseDeclarations );
        configUrlDeclarations( urlDeclarations );
//        configCheckUrls( allowUrlsCheckedDaysBefore );
    }

//    private void configCheckUrls( int allowUrlsCheckedDaysBefore ) {
//        if( allowUrlsCheckedDaysBefore > 0 ){
//            lOracle.allowUrlsCheckedDaysBefore( allowUrlsCheckedDaysBefore );
//        }
//
//    }

    private void configUrlDeclarations( List<UrlDeclaration> urlDeclarations ) {
        urlDeclarations.forEach( ud -> {
            LicenseID license = lOracle.getOrThrowByName( ud.getLicense() );
  //          lOracle.addUrlCheckedAt( license, ud.getUrl(), ud.getCheckedAt() );
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

    private String getUse( DependencyNode node ) {
        Artifact a = _nn(node.getArtifact());
        String ret = _orElseGet( a.getScope(), "" );
        @Nullable DependencyNode it = node.getParent();
        while( it != null ) {
            Coordinates coo = Coordinates.valueOf( _nn(it.getArtifact()));
            ret = coo.toString() + " -> " + ret;
            it = it.getParent();
        }

        return ret;
    }

    private void getNormalDependencies() {
        ArtifactFilter artifactFilter = new ScopeArtifactFilter( Artifact.SCOPE_TEST );

        DependencyNode rootNode = v( () -> _nn( dependencyGraphBuilder.buildDependencyGraph( mavenProject, artifactFilter )));

        rootNode.accept( new DependencyNodeVisitor() {
            @Override
            public boolean visit( DependencyNode dependencyNode ) {
                Artifact a = _nn( _nn(dependencyNode).getArtifact() );
                Coordinates coo = Coordinates.valueOf( a );
                coordinates2License.add( coo );
                coordinates2License.updateScope( coo, _orElseGet( a.getScope(), "compile" ) );
                coordinates2License.addUse( coo, getUse( _nn(dependencyNode )) );

                if( !self.isPresent() ) {
                    self = Optional.of( coo );
                    coordinates2License.setSelf( coo );
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

    public String checkCompatibility( Coordinates coo, String licenseStr ) {

        LicenseID license = lOracle.getOrThrowByName( licenseStr );

        AtomicReference<String> ret = new AtomicReference<>( "" );

        lOracle.getAttributes( license ).isFedoraApproved().ifPresent( fed -> {
            if( !fed ) {
                ret.set( "bad license " + license + " used by: " + coo + "  (not approved by fedora)" );
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
    }

    public void getHolder() {
        final GetHolder gh = new GetHolder( lOracle, mlo, log );
        coordinates2License.getHolders( gh::getHolder );
    }

    public void summery() {
        coordinates2License.summery();
    }

    public void src() {
        coordinates2License.fromSrc( srcAccess::check );

    }

    public void store() {
        JSONStartup.previousOut( coordinates2License );
    }

    public void jars() {
        coordinates2License.fromJar( jarAccess::check );
    }

    public void computeHolder() {
        coordinates2License.update( this::computeHolder );
    }

    @SuppressWarnings( "PMD.UnusedFormalParameter" ) // so it can be called via update
    private void computeHolder( Coordinates coordinates, Coordinates2License.LiCo liCo ) {

        Optional<CopyrightHolder> ret =
                liCo.getMavenLicenses().stream().
                        map( ml -> ContentToLicense.copyRightPattern.matcher( _orElseGet( ml.getComment(), "" ) ) ).
                        filter( Matcher::matches ).
                        map( m -> new CopyrightHolder( _nn( m.group( "year" ) ), _nn( m.group( "holder" ) ) ) ).
                        findAny();

        if( ret.isPresent() ) {
            liCo.setHolder( ret );
            return;
        }

        ret = contentToLicense.getHolder( liCo.getLicenseTxt() ); // , "by license file"
        if( ret.isPresent() ) {
            liCo.setHolder( ret );
            return;
        }

        ret = contentToLicense.getHolder( liCo.getHeaderTxt() ); // , "by header file"
        if( ret.isPresent() ) {
            liCo.setHolder( ret );
            return;
        }

        ret = contentToLicense.getHolder( liCo.getNotice() ); // , "by notice file"
        if( ret.isPresent() ) {
            liCo.setHolder( ret );
        }

    }

    public void getNotice() {
        final GetHolder gh = new GetHolder( lOracle, mlo, log );
        coordinates2License.update( gh::getNotice );
    }

    public void getPomHeader() {
        coordinates2License.fromSrc( src::getPomHeader );

    }

    public void generateNotice() {
        coordinates2License.generateNotice();

    }

    public void computeLicense() {
        coordinates2License.update( licenseIntelligence::compute );
    }

    public void downloadPages() {
        coordinates2License.update( downloader::getExtension );
    }

    public void generateLicenseTxts() {
        coordinates2License.update(
                lico -> getScopeLevel( lico.getScope() ) < getScopeLevel( "test" ) ,
                (coo, lico) -> downloader.generateLicensesTxt( self.map( Coordinates::getArtifactId).orElse( "notice" ), coo, lico ) );
    }
}
