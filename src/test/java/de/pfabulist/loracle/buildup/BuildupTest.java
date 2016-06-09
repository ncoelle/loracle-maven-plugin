package de.pfabulist.loracle.buildup;

import com.google.gson.GsonBuilder;
import de.pfabulist.kleinod.nio.Filess;
import de.pfabulist.loracle.license.Coordinates;
import de.pfabulist.loracle.license.LOracle;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Paths;

import static de.pfabulist.kleinod.text.Strings.getBytes;
import static org.assertj.core.api.Assertions.assertThat;

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

    @SuppressFBWarnings({"DMI_HARDCODED_ABSOLUTE_FILENAME"})
    @Test
    public void testBuildup() {

        LOracle lOracle = new LOracle();
        new ExtractSpdxLicensesFromJSON().go( lOracle );
        new ExtractSPDXExceptionsFromHTML( lOracle );

        lOracle.addUrl( lOracle.getOrThrowByName( "bsd-2-clause" ), "opensource.org/licenses/bsd-2-clause" );
        lOracle.addLongName( lOracle.getOrThrowByName( "agpl-1.0" ), "affero 1.0" );
        lOracle.addLongName( lOracle.getOrThrowByName( "agpl-3.0" ), "affero 3.0" );
        lOracle.addLongName( lOracle.getOrThrowByName( "lgpl-2.1" ), "GNU Lesser General Public License (LGPL), Version 2.1" ); // extra (LGPL)
        lOracle.addLongName( lOracle.getOrThrowByName( "GPL-2.0 with Classpath-exception-2.0" ), "GPL2 w/ CPE" );
        lOracle.addLongName( lOracle.getOrThrowByName( "isc" ), "isc bsd" );

        new ExtractFromDejaCode().go( lOracle );


        lOracle.addUrl( lOracle.getOrThrowByName( "CDDl-1.1 or GPL-2.0 with Classpath-exception-2.0" ), "https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html" );
        lOracle.addUrl( lOracle.getOrThrowByName( "CDDl-1.1 or GPL-2.0 with Classpath-exception-2.0" ), "glassfish.java.net/public/cddl+gpl_1_1" );
        lOracle.addUrl( lOracle.getOrThrowByName( "CDDl-1.0 or GPL-2.0 with Classpath-exception-2.0" ), "https://glassfish.dev.java.net/public/CDDL+GPL.html" );
        lOracle.addUrl( lOracle.getOrThrowByName( "CDDl-1.0 or GPL-2.0 with Classpath-exception-2.0" ), "http://glassfish.java.net/public/CDDL+GPL.html" );
        lOracle.addUrl( lOracle.getOrThrowByName( "CDDl-1.0 or GPL-2.0 with Classpath-exception-2.0" ), "https://glassfish.dev.java.net/nonav/public/CDDL+GPL.html" );

//        lOracle.addUrl( lOracle.getOrThrowByName( "cc0-1.0" ), "repository.jboss.org/licenses/cc0-1.0" );

        // osi license urls
        lOracle.addUrl( lOracle.getOrThrowByName( "mit" ), "http://www.opensource.org/licenses/mit-license.php" );
  //      lOracle.addUrl( lOracle.getOrThrowByName( "cpl-1.0" ), "http://www.opensource.org/licenses/cpl1.0" );

        // gnu license urls
    //    lOracle.addUrl( lOracle.getOrThrowByName( "LGPL-2.1" ), "gnu.org/licenses/lgpl-2.1" );
        lOracle.addUrl( lOracle.getOrThrowByName( "LGPL-3.0+" ), "gnu.org/licenses/lgpl" );

        // fsf license urls
//        lOracle.addUrl( lOracle.getOrThrowByName( "agpl-3.0" ), "www.fsf.org/licensing/licenses/agpl-3.0.html"  );

        // creative commons urls
        lOracle.addUrl( lOracle.getOrThrowByName( "cc0-1.0" ), "http://creativecommons.org/publicdomain/zero/1.0/"  );

        // new licenses
        lOracle.newSingle( "closed", false );
        lOracle.addLicenseForArtifact( Coordinates.valueOf( "aopalliance:aopalliance:1.0" ), lOracle.newSingle( "aop-pd", false ) );


        // couldbe
        lOracle.addCouldBeUrl( lOracle.getOrThrowByName( "bsd-3-clause" ), "http://asm.ow2.org/license.html");


        // by artifact
        lOracle.addLicenseForArtifact( Coordinates.valueOf( "dom4j:dom4j:1.6.1" ), lOracle.getOrThrowByName(  "dom4j" ));

        lOracle.addLicenseForArtifact( new Coordinates( "net.jcip", "jcip-annotations", "1.0" ), lOracle.getOrThrowByName( "CC-BY-2.5" ) );

        lOracle.addLicenseForArtifact( new Coordinates( "javax.servlet", "javax.servlet-api", "3.1.0" ),
                                       lOracle.getOrThrowByName( "CDDl-1.1 or GPL-2.0 with Classpath-exception-2.0" ) );
        lOracle.addLicenseForArtifact( new Coordinates( "org.apache.httpcomponents", "httpclient", "4.0.1" ),
                                       lOracle.getOrThrowByName( "Apache-2.0" ) );
        lOracle.addLicenseForArtifact( Coordinates.valueOf( "net.sourceforge.pmd:pmd-*:5.4.1" ),
                                       lOracle.getOrThrowByName( "bsd-4-clause" ) );
        lOracle.addLicenseForArtifact( Coordinates.valueOf( "org.scala-lang:scala*:2.10.0" ), // TODO verify  or via timed url ?
                                       lOracle.getOrThrowByName( "bsd-2-clause" ) );
        lOracle.addLicenseForArtifact( Coordinates.valueOf( "org.scala-lang:scala*:2.10.5" ), // TODO verify
                                       lOracle.getOrThrowByName( "bsd-2-clause" ) );
        lOracle.addLicenseForArtifact( Coordinates.valueOf( "antlr:antlr:2.7.7" ), // TODO verify, before antlr has antlr-pd
                                       lOracle.getOrThrowByName( "bsd-2-clause" ) );
        lOracle.addLicenseForArtifact( Coordinates.valueOf( "antlr:antlr:3.5.2" ), // TODO verify, before antlr has antlr-pd
                                       lOracle.getOrThrowByName( "bsd-2-clause" ) );
        lOracle.addLicenseForArtifact( Coordinates.valueOf( "org.antlr:ST4:4.0.4" ), // TODO verify, before antlr has antlr-pd
                                       lOracle.getOrThrowByName( "bsd-2-clause" ) );
        lOracle.addLicenseForArtifact( Coordinates.valueOf( "jline:jline:1.0" ),
                                       lOracle.getOrThrowByName( "bsd-2-clause" ) );
        lOracle.addLicenseForArtifact( Coordinates.valueOf( "org.tachyonproject:tachyon-*:0.8.2" ),
                                       lOracle.getOrThrowByName( "apache-2" ) );
        lOracle.addLicenseForArtifact( Coordinates.valueOf( "com.thoughtworks.paranamer:paranamer:2.3" ),
                                       lOracle.getOrThrowByName( "bsd-2-clause" ));


        lOracle.addLicenseForArtifact( Coordinates.valueOf( "org.json4s:json4s-*:3.2.10" ),
                                       lOracle.getOrThrowByName( "apache-2" ) );

        // bsd-2-clause hmm
        //http://www.opensource.org/licenses/bsd-license.php
        //http://asm.objectweb.org/license.html

        // aduna bsd ? 3-clause

        // com.jcabi:jcabi-log:0.14       :  http://www.jcabi.com/LICENSE.txt
        // com.jcabi:jcabi-manifests:1.1

        // scala 2.10.5




        new ExtractGoodFedoraLicensesFromHTML().addFedoraInfo( lOracle );
        new ExtractBadFedoraLicensesFromHTML().addFedoraInfo( lOracle );

        // checkat

        lOracle.addUrlCheckedAt( lOracle.getOrThrowByName( "bsd-2-clause" ), "http://asm.objectweb.org/license.html", "2016-06-08" );
        lOracle.addUrlCheckedAt( lOracle.getOrThrowByName( "bsd-2-clause" ), "http://asm.ow2.org/license.html", "2016-06-08" ); // todo
        lOracle.addUrlCheckedAt( lOracle.getOrThrowByName( "bsd-2-clause" ), "http://antlr.org/license.html", "2016-06-08" ); // todo
        lOracle.addUrlCheckedAt( lOracle.getOrThrowByName( "bsd-2-clause" ), "http://www.opensource.org/licenses/bsd-license.php", "2016-06-08" );
        lOracle.addUrlCheckedAt( lOracle.getOrThrowByName( "bsd-4-clause" ), "http://hsqldb.org/web/hsqlLicense.html", "2016-06-08" );
        lOracle.addUrlCheckedAt( lOracle.getOrThrowByName( "bsd-3-clause" ), "http://repo.aduna-software.org/legal/aduna-bsd.txt", "2016-06-08" );
        lOracle.addUrlCheckedAt( lOracle.getOrThrowByName( "bsd-4-clause" ), "http://www.jcabi.com/LICENSE.txt", "2016-06-09" );  // todo
        lOracle.addUrlCheckedAt( lOracle.getOrThrowByName( "bsd-2-clause" ), " http://antlr.org/license.html", "2016-06-09" );  // todo


        //System.out.println( new GsonBuilder().setPrettyPrinting().create().toJson( lOracle ) );
        Filess.write( Paths.get( "/Users/openCage/current/java-projects/loracle-maven-plugin/src/main/resources/de/pfabulist/loracle/loracle.json" ),
                      getBytes( new GsonBuilder().setPrettyPrinting().create().toJson( lOracle ) ));

        testAll( lOracle );



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

        assertThat( lOracle.getOrThrowByName( "ASL-1.1" )).isEqualTo( lOracle.getOrThrowByName( "apache-1.1" ) );

        assertThat( lOracle.getByUrl( "gnu.org/licenses/lgpl.html"  )).isEqualTo( lOracle.getByName( "lgpl-3.0+" ) );

        assertThat( lOracle.getOrThrowByName( "GPL-2.0 or later" )).isEqualTo( lOracle.getOrThrowByName( "GPL-2.0+" ) );

        assertThat( lOracle.getByUrl( "http://opensource.org/licenses/BSD-2-Clause" )).
                isEqualTo( lOracle.getByName( "bsd-2-clause" ) );

        assertThat( lOracle.getByName( "Indiana University Extreme! Lab Software License, vesion 1.1.1" )).
                isEqualTo( lOracle.getByName( "indiana-extreme-1.1.1" ));

        assertThat( lOracle.getByUrl( "http://freemarker.org/LICENSE.txt" )).isPresent();

        assertThat( lOracle.getByUrl( "http://htmlparser.sourceforge.net/cpl1.0.txt" )).isPresent();
        assertThat( lOracle.getByUrl( "http://foo.bar/baz/lgpl-2.1.html" )).isPresent();
        assertThat( lOracle.getByUrl( "gnu.org/licenses/lgpl-2.1" )).isPresent();
        assertThat( lOracle.getByUrl( "repository.jboss.org/licenses/cc0-1.0" )).isPresent();
        assertThat( lOracle.getByUrl( "www.fsf.org/licensing/licenses/agpl-3.0.html"  )).isPresent();

        assertThat( lOracle.getByUrl( "http://creativecommons.org/publicdomain/zero/1.0/" )).
                isEqualTo( lOracle.getByName( "cc0-1.0" ) );

        assertThat( lOracle.getByUrl(  "http://www.opensource.org/licenses/cpl1.0" )).
                isEqualTo( lOracle.getByName( "cpl-1.0" ) );

        assertThat( lOracle.getByCoordinates( Coordinates.valueOf( "org.scala-lang:scalap:2.10.0" ))).isPresent();


        lOracle.allowUrlsCheckedDaysBefore( 100 );
        System.out.println( lOracle.guessByUrl( "http://www.opensource.org/licenses/bsd-license.php" ));
        assertThat( lOracle.getByUrl( "http://www.opensource.org/licenses/bsd-license.php"  )).isPresent();


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
