package de.pfabulist.loracle.mojo;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.apache.maven.execution.MavenSession;
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

import javax.annotation.Nullable;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import static de.pfabulist.roast.NonnullCheck._nn;

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

    @Parameter( property = "license-check.andIsOr", defaultValue = "true" )
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

    public DependencyGraphBuilder getDependencyGraphBuilder() {
        return _nn( dependencyGraphBuilder );
    }

    public MavenProject getProject() {
        return _nn( project );
    }

    public MavenSession getSession() {
        return _nn( session );
    }

    public List<LicenseDeclaration> getLicenseDeclarations() {
        return licenseDeclarations == null ? Collections.emptyList() : licenseDeclarations;
    }

    public List<UrlDeclaration> getUrlDeclarations() {
        return urlDeclarations == null ? Collections.emptyList() : urlDeclarations;
    }

    @SuppressWarnings( "PMD.AvoidPrintStackTrace" )
    public void execute() throws MojoExecutionException, MojoFailureException {


        try {

            LicenseCheckMojo mojo =
                    new LicenseCheckMojo( getLog(),
                                          Paths.get( _nn( getSession().getLocalRepository() ).getBasedir() ),
                                          getProject(),
                                          getDependencyGraphBuilder());

            mojo.config( getLicenseDeclarations(), getUrlDeclarations(), allowUrlsCheckedDaysBefore );
            mojo.getDependencies();
            mojo.determineMavenLicenses();
            mojo.jars();
            mojo.src();
            mojo.getNotice();
            mojo.getPomHeader();
            mojo.downloadPages();

            mojo.computeLicense();
            mojo.computeHolder();
            mojo.checkCompatibility();

            mojo.summery();

            mojo.generateLicenseTxts();
            mojo.generateNotice();
            mojo.store();



        } catch( Exception e ) {
            e.printStackTrace();
            if( stopOnError ) {
                throw new MojoFailureException( e.getMessage() );
            }
        }

        if( stopOnError ) {
            getLog().throwOnError();
        }

    }


    @Override
    public Findings getLog() {
        if( failures == null ) {
            failures = new Findings( super.getLog() );
        }
        return failures;
    }
}
