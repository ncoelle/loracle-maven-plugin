package de.pfabulist.loracle.buildup;

import com.google.gson.GsonBuilder;
import de.pfabulist.loracle.license.AliasBuilder;
import de.pfabulist.loracle.license.Coordinates;
import de.pfabulist.loracle.license.LOracle;
import de.pfabulist.loracle.license.LicenseID;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.handler.ArtifactHandler;
import org.apache.maven.artifact.metadata.ArtifactMetadata;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.OverConstrainedVersionException;
import org.apache.maven.artifact.versioning.VersionRange;
import org.apache.maven.model.profile.activation.OperatingSystemProfileActivator;
import org.junit.Test;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

@SuppressFBWarnings( { "EQ_COMPARETO_USE_OBJECT_EQUALS" } )
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

        lOracle.addLongName( lOracle.getOrThrowByName( "LGPL-3.0+" ), "GNU Lesser Public License" );

        System.out.println( lOracle.getByName( "GNU Lesser Public License" ) );
    }

    @Test
    public void testApache() {
        LOracle lOracle = new LOracle();
        new ExtractSpdxLicensesFromHTML().getLicenses( lOracle );

        assertThat( lOracle.getByName( "Apache Software License - Version 2.0" ) ).
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
                                       lOracle.getOrThrowByName( "cddl-1.1 with Classpath-exception-2.0" ) );

        assertThat( lOracle.getByCoordinates( new Coordinates( "javax.servlet", "javax.servlet-api", "3.1.0" ) ) ).isPresent();
    }

    @Test
    public void plus() {
        LOracle lOracle = new LOracle();
        new ExtractSpdxLicensesFromHTML().getLicenses( lOracle );

        assertThat( lOracle.getByName( "GPL-2.0+" ) ).isPresent();

        System.out.println( lOracle.getByName( "GPL-2.0+" ) );
    }

    @Test
    public void testBuildup() {

        LOracle lOracle = new LOracle();
        new ExtractSpdxLicensesFromJSON().go( lOracle );
        new ExtractSPDXExceptionsFromHTML( lOracle );

        lOracle.addLicenseForArtifact( new Coordinates( "net.jcip", "jcip-annotations", "1.0" ), lOracle.getOrThrowByName( "CC-BY-2.5" ) );

        lOracle.addLicenseForArtifact( new Coordinates( "javax.servlet", "javax.servlet-api", "3.1.0" ),
                                       lOracle.getOrThrowByName( "CDDl-1.1 or GPL-2.0 with Classpath-exception-2.0" ) );
        lOracle.addLicenseForArtifact( new Coordinates( "org.apache.httpcomponents", "httpclient", "4.0.1" ),
                                       lOracle.getOrThrowByName( "Apache-2.0" ) );

        lOracle.addUrl( lOracle.getOrThrowByName( "CDDl-1.1 or GPL-2.0 with Classpath-exception-2.0" ), "glassfish.java.net/public/cddl+gpl_1_1" );

        lOracle.addUrl( lOracle.getOrThrowByName( "cc0-1.0" ), "repository.jboss.org/licenses/cc0-1.0" );

        // osi license urls
        lOracle.addUrl( lOracle.getOrThrowByName( "mit" ), "http://www.opensource.org/licenses/mit-license.php" );
        lOracle.addUrl( lOracle.getOrThrowByName( "cpl-1.0" ), "http://www.opensource.org/licenses/cpl1.0" );

        // gnu license urls
        lOracle.addUrl( lOracle.getOrThrowByName( "LGPL-2.1" ), "gnu.org/licenses/lgpl-2.1" );

        // fsf license urls
        lOracle.addUrl( lOracle.getOrThrowByName( "agpl-3.0" ), "www.fsf.org/licensing/licenses/agpl-3.0.html"  );

        // new licenses
        lOracle.newSingle( "closed", false );
        lOracle.addLicenseForArtifact( Coordinates.valueOf( "dom4j:dom4j:1.6.1" ), lOracle.newSingle( "dom4j", false ));
        lOracle.addLicenseForArtifact( Coordinates.valueOf( "aopalliance:aopalliance:1.0" ), lOracle.newSingle( "aop-pd", false ) );

        new ExtractGoodFedoraLicensesFromHTML().addFedoraInfo( lOracle );
        new ExtractBadFedoraLicensesFromHTML().addFedoraInfo( lOracle );

        testAll( lOracle );


        System.out.println( new GsonBuilder().setPrettyPrinting().create().toJson( lOracle ) );

//                f -> System.out.println( f.name + " -> " + lOracle.getByName( f.name ).map( Object::toString ).orElse( "-" ) +
//                                                 " | " + f.shortName + " -> " + lOracle.getByName( f.shortName ).map( Object::toString ).orElse( "-" )));
//        f -> System.out.println( f.name + " -> " + lOracle.getByName( f.name ).map( Object::toString ).orElse( "-" ) ));

        //http://www.opensource.org/licenses/mit-license.php

//        LOracle lOracle1 = new Gson().fromJson( new Gson().toJson( lOracle ), LOracle.class);

        //System.out.println( new Gson().toJson( lOracle ));
        // http://www.apache.org/licenses/LICENSE-2.0
    }

    public void testAll( LOracle lOracle ) {
        assertThat( lOracle.getByUrl( "http://www.apache.org/licenses/LICENSE-2.0" ) ).isPresent();
        assertThat( lOracle.getByUrl( "http://www.apache.org/licenses/LICENSE-2.0.TXT" ) ).isPresent();
        assertThat( lOracle.getByUrl( "http://www.opensource.org/licenses/mit-license.php" ) ).isPresent();
        assertThat( lOracle.getByUrl( "glassfish.java.net/public/cddl+gpl_1_1" ) ).isPresent();
        assertThat( lOracle.getByCoordinates( Coordinates.valueOf( "aopalliance:aopalliance:1.0" ) ) ).isPresent();


        assertThat( lOracle.getByCoordinates( Coordinates.valueOf( "javax.servlet:javax.servlet-api:3.1.0" ) ) ).isEqualTo(
                lOracle.getByName( "CDDl-1.1 or GPL-2.0 with Classpath-exception-2.0" ) );

        assertThat( lOracle.getOrThrowByName( "MPL 2.0" ) ).isEqualTo( lOracle.getOrThrowByName( "mpl-2.0" ) );
        assertThat( lOracle.getByName( "MPL 2.0, and EPL 1.0" ) ).isPresent();

//        assertThat( lOracle.getOrThrowByName( "ASL-1.1" )).isEqualTo( lOracle.getOrThrowByName( "apache-1.1" ) );

    }

    @Test
    public void startup() {
        LOracle lOracle = JSONStartup.start().spread();

        testAll( lOracle );
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
