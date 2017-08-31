package de.pfabulist.loracle.buildup;

import com.google.gson.GsonBuilder;
import de.pfabulist.loracle.license.MappedLicense;
import de.pfabulist.loracle.maven.Coordinates;
import de.pfabulist.loracle.license.LOracle;
import de.pfabulist.loracle.license.LicenseID;
import de.pfabulist.loracle.license.MappedSomeLicense;
import de.pfabulist.roast.nio.Files_;
import de.pfabulist.roast.nio.Paths_;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.junit.Ignore;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Locale;

import static de.pfabulist.kleinod.text.Strings.getBytes;
import static de.pfabulist.roast.NonnullCheck.n_;
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

        assertThat( lOracle.getSingle( "ZPl-1.1" ).isPresent() ).isTrue();
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

        assertThat( lOracle.getByName( "apache-2.0 with Classpath-exception-2.0" ).isPresent() ).isTrue();

    }

    @Test
    public void testJavaxServlet() {
        LOracle lOracle = new LOracle();
        new ExtractSpdxLicensesFromHTML().getLicenses( lOracle );
        new ExtractSPDXExceptionsFromHTML( lOracle );

        lOracle.addLicenseForArtifact( new Coordinates( "javax.servlet", "javax.servlet-api", "3.1.0" ),
                                       lOracle.getOrThrowByName( "cddl-1.1 with Classpath-exception-2.0" ) );

        assertThat( lOracle.getByCoordinates( new Coordinates( "javax.servlet", "javax.servlet-api", "3.1.0" ) ).isPresent() ).isTrue();
    }

    // org.apache.parquet:parquet-format:2.3.0-incubating todo (mit because attribution)

    @Test
    public void plus() {
        LOracle lOracle = new LOracle();
        new ExtractSpdxLicensesFromHTML().getLicenses( lOracle );

        assertThat( lOracle.getByName( "GPL-2.0+" ).isPresent() ).isTrue();

        System.out.println( lOracle.getByName( "GPL-2.0+" ) );
    }

    @SuppressFBWarnings( { "DMI_HARDCODED_ABSOLUTE_FILENAME" } )
    @Test
    public void testBuildup() {

        buildAfterExternal();
        LOracle lOracle =
                //new LOracle();
                JSONStartup.start().spread();

        testAll( lOracle );

    }

    public void buildAfterExternal() {
    }
        public void buildAfterExternalo() {
        Path dir = Paths_.get__( "" ).toAbsolutePath__().resolve_( "src/main/resources/de/pfabulist/loracle/" );

//        if ( Files_.exists_( dir.resolve( "local-json-done" ))) {
//
//            return;
//        }

        System.out.println( "----------------------- " );
        System.out.println( "------ spdx ----------- " );
        System.out.println( "----------------------- " );

        LOracle lOracle =
                //new LOracle();
                JSONStartup.startSpdx().spreadNoexternals();
        // add too simple

//        lOracle.addTooSimple( "map", "par", "free", "foundation", "fsf", "initial developer", "wide", "attribution" );
//        lOracle.addTooSimple( "only", "attribution only", "hp", "microsoft", "json", "closed", "government", "doc" );
//        lOracle.addTooSimple( "directory", "jetty", "sequence", "fork", "open", "regexp", "berkeley" );
//        // todo via dictionary ?



//        new ExtractSpdxLicensesFromJSON().go( lOracle );
//        new ExtractSPDXExceptionsFromHTML( lOracle );

        System.out.println( "\n\n#licenses " + lOracle.getSingleLicenseCount() + "\n\n" );

        //lOracle.addUrl( lOracle.getOrThrowByName( "bsd-2-clause" ), "opensource.org/licenses/bsd-2-clause" );
        lOracle.addUrl( lOracle.getOrThrowByName( "epl-1.0" ), "http://www.eclipse.org/org/documents/epl-v10.php" );

        lOracle.addLongName( lOracle.getOrThrowByName( "agpl-1.0" ), "affero 1.0" );
        lOracle.addLongName( lOracle.getOrThrowByName( "agpl-3.0" ), "affero 3.0" );
        lOracle.addLongName( lOracle.getOrThrowByName( "GPL-2.0 with Classpath-exception-2.0" ), "GPL2 w/ CPE" );
        lOracle.addLongName( lOracle.getOrThrowByName( "isc" ), "isc bsd" );
        lOracle.addLongName( lOracle.getOrThrowByName( "bsd-3-clause" ), "3-clause bsd" );

        // extra short name
        lOracle.addLongName( lOracle.getOrThrowByName( "lgpl-2.1" ), "GNU Lesser General Public License (LGPL), Version 2.1" ); // extra (LGPL)
        lOracle.addLongName( lOracle.getOrThrowByName( "gpl-3.0" ), "gnu (gpl) 3.0" );
        lOracle.addLongName( lOracle.getOrThrowByName( "gpl-2.0" ), "gnu (gpl) 2.0" );
        lOracle.addLongName( lOracle.getOrThrowByName( "gpl-1.0" ), "gnu (gpl) 1.0" );
        lOracle.addLongName( lOracle.getOrThrowByName( "cddl-1.0" ), "Common Development and Distribution License (CDDL) v1.0" );

        lOracle.addLongName( lOracle.getOrThrowByName( "CDDl-1.1 or GPL-2.0 with Classpath-exception-2.0" ), "CDDL+GPL_1_1" );
        lOracle.addLongName( lOracle.getOrThrowByName( "CDDl-1.0 or GPL-2.0 with Classpath-exception-2.0" ), "CDDL+GPL" );

        // gnu license urls
        lOracle.addUrl( lOracle.getOrThrowByName( "LGPL-3.0+" ), "gnu.org/licenses/lgpl" );
        lOracle.addUrl( lOracle.getOrThrowByName( "GPL-3.0+" ), "gnu.org/licenses/gpl" );

        System.out.println( "----------------------- " );
        System.out.println( "------ dejacode ------- " );
        System.out.println( "----------------------- " );

        new ExtractFromDejaCode().go( lOracle );

        System.out.println( "\n\n#licenses " + lOracle.getSingleLicenseCount() + "\n\n" );

        // creative commons urls
        lOracle.addUrl( lOracle.getOrThrowByName( "cc0-1.0" ), "http://creativecommons.org/publicdomain/zero/1.0/" );

        // new licenses
        try {
            LicenseID closed = lOracle.newSingle( "closed", false );
            lOracle.getMore( closed ).attributes.setGpl2Compatible( false );
            lOracle.getMore( closed ).attributes.setGpl3Compatible( false );

            LicenseID hs = lOracle.newSingle( "HSQLDB", false ); // ~ BSD-4-clause , copyright ?
            lOracle.addUrl( hs, " http://hsqldb.org/web/hsqlLicense.html" );

            LicenseID gs = lOracle.newSingle( "gsbase-1.0", false );
            lOracle.addUrl( gs, "gsbase.sourceforge.net/license.html" );
        } catch( IllegalArgumentException e  ) {
            // gibts schp
        }



//        LicenseID bouncy = lOracle.newSingle( "bouncycastle", false ); // is really mit
//        lOracle.addUrl( bouncy, "bouncyastle.org/license.html" ); // todo

        // by artifact
        // lOracle.addLicenseForArtifact( Coordinates.valueOf( "dom4j:dom4j:1.6.1" ), lOracle.getOrThrowByName( "dom4j" ) ); found in license
        // Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
        // http://dom4j.sourceforge.net/dom4j-1.6.1/license.html


//        lOracle.addLicenseForArtifact( new Coordinates( "javax.servlet", "javax.servlet-api", "3.1.0" ),
//                                       lOracle.getOrThrowByName( "CDDl-1.1 or GPL-2.0 with Classpath-exception-2.0" ) );
        // better with pom

        // https://github.com/highsource/maven-jaxb2-plugin,
        // https://github.com/highsource/maven-jaxb2-plugin/blob/master/LICENSE
        // Copyright (c) 2006-2014, Alexey Valikov.

//        lOracle.addLicenseForArtifact( Coordinates.valueOf( "org.hibernate.javax.persistence:hibernate-jpa-2.0-api:1.0.1.Final" ),
//                                       lOracle.getOrThrowByName( "Eclipse Distribution License - v 1.0" )); // or epl1-0
        // found in header

        // Copyright (c) 2007, Eclipse Foundation, Inc. and its licensors.
        // Copyright (c) 2008, 2009 Sun Microsystems, Oracle Corporation. All rights reserved.
        // http://grepcode.com/file/repo1.maven.org/maven2/org.hibernate.javax.persistence/hibernate-jpa-2.0-api/1.0.1.Final/readme.txt?av=f

        //Copyright (c) 2008, 2009 Sun Microsystems, Oracle Corporation. All rights reserved.
//
//        This program and the accompanying materials are made available under the
//        terms of the Eclipse Public License v1.0 and Eclipse Distribution License v. 1.0
//        which accompanies this distribution.
//                The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
//        and the Eclipse Distribution License is available at
//        http://www.eclipse.org/org/documents/edl-v10.php.
//
//        Contributors:
//        Linda DeMichiel -Java Persistence 2.0 - Proposed Final Draft, Version 2.0 (August 31, 2009)
//        Specification available from http://jcp.org/en/jsr/detail?id=317
//        Oracle Committers - EclipseLink specific implementations and OSGi support

        // postgres
        // Copyright (c) 1997-2011, PostgreSQL Global Development Group

        // bsd-2-clause hmm
        //http://www.opensource.org/licenses/bsd-license.php
        //http://asm.objectweb.org/license.html

        // aduna bsd ? 3-clause

        // com.jcabi:jcabi-log:0.14       :  http://www.jcabi.com/LICENSE.txt
        // com.jcabi:jcabi-manifests:1.1

        // scala 2.10.5

        System.out.println( "----------------------- " );
        System.out.println( "------ fedora --------- " );
        System.out.println( "----------------------- " );

        new ExtractGoodFedoraLicensesFromHTML().addFedoraInfo( lOracle );
        new ExtractBadFedoraLicensesFromHTML().addFedoraInfo( lOracle );

        System.out.println( "\n\n#licenses " + lOracle.getSingleLicenseCount() + "\n\n" );

        // checkat

//        lOracle.addUrlCheckedAt( lOracle.getOrThrowByName( "bsd-3-clause" ), "http://asm.objectweb.org/license.html", "2016-06-08" );
////        lOracle.addUrlCheckedAt( lOracle.getOrThrowByName( "bsd-3-clause" ), "http://asm.ow2.org/license.html", "2016-06-08" ); // todo
//        lOracle.addUrlCheckedAt( lOracle.getOrThrowByName( "bsd-2-clause" ), "http://www.opensource.org/licenses/bsd-license.php", "2016-06-08" );
////        lOracle.addUrlCheckedAt( lOracle.getOrThrowByName( "bsd-4-clause" ), "http://hsqldb.org/web/hsqlLicense.html", "2016-06-08" ); // now new license
//        lOracle.addUrlCheckedAt( lOracle.getOrThrowByName( "bsd-3-clause" ), "https://jdbc.postgresql.org/about/license.html", "2016-06-20" );
//        lOracle.addUrlCheckedAt( lOracle.getOrThrowByName( "bsd-3-clause" ), "https://jdbc.postgresql.org/license.html", "2016-06-20" );

        lOracle.addUrl( lOracle.getOrThrowByName( "wtfpl" ), "http://www.wtfpl.net" );

        System.out.println( "----------------------- " );
        System.out.println( "------ ifross.org ----- " );
        System.out.println( "----------------------- " );

        // strong copy left
        strongCL( lOracle );

        System.out.println( "\n\n#licenses " + lOracle.getSingleLicenseCount() + "\n\n" );

        // ---

        lOracle.addUrlContent( "http://www.apache.org/licenses/LICENSE-2.0", "/de/pfabulist/loracle/urls/apache-2.txt" );
        lOracle.addUrlContent( "http://glassfish.java.net/public/CDDL+GPL_1_1.html", "/de/pfabulist/loracle/urls/glassfish_cddl+gpl_1_1" );
        lOracle.addUrlContent( "http://glassfish.java.net/public/CDDL+GPL.html", "/de/pfabulist/loracle/urls/glassfish_cddl+gpl_1.0.txt" );
        lOracle.addUrlContent( "http://www.eclipse.org/org/documents/epl-v10.php", "/de/pfabulist/loracle/urls/eclipse-v10.txt" );

        Files_.write( Paths_.get__( "" ).toAbsolutePath__().resolve_( "src/main/resources/de/pfabulist/loracle/loracle.json" ),
                      getBytes( new GsonBuilder().setPrettyPrinting().create().toJson( lOracle ) ) );
        Files_.write( dir.resolve( "local-json-done" ),
                      getBytes( new GsonBuilder().setPrettyPrinting().create().toJson( lOracle ) ) );


    }

    public void testAll( LOracle lOracle ) {

        assertThat( lOracle.getByName( "apache" ).isPresent() ).isFalse();

        assertThat( lOracle.getByUrl( "http://www.apache.org/licenses/LICENSE-2.0" ).isPresent() ).isTrue();

        assertThat( lOracle.getByUrl( "http://www.apache.org/licenses/LICENSE-2.0.TXT" ).isPresent() ).isTrue();
        assertThat( lOracle.getByUrl( "http://www.opensource.org/licenses/mit-license.php" ).isPresent() ).isTrue();
        assertThat( lOracle.getByUrl( "glassfish.java.net/public/cddl+gpl_1_1" ).isPresent() ).isTrue();
// in found        assertThat( lOracle.getByCoordinates( Coordinates.valueOf( "aopalliance:aopalliance:1.0" ) ).isPresent() ).isTrue();

        // done via license o.ae.
//        assertThat( lOracle.getByCoordinates( Coordinates.valueOf( "javax.servlet:javax.servlet-api:3.1.0" ) ) ).isEqualTo(
//                lOracle.getByName( "CDDl-1.1 or GPL-2.0 with Classpath-exception-2.0" ) );

        assertThat( lOracle.getOrThrowByName( "MPL 2.0" ) ).isEqualTo( lOracle.getOrThrowByName( "mpl-2.0" ) );
        assertThat( lOracle.getByName( "MPL 2.0, and EPL 1.0" ).isPresent() ).isTrue();

        // todo: fedora
//        System.out.println( lOracle.longNameMapper.get( "asl 1.1" ));
//        assertThat( lOracle.getOrThrowByName( "ASL-1.1" ) ).isEqualTo( lOracle.getOrThrowByName( "apache-1.1" ) );

        // todo url
        // assertThat( lOracle.getByUrl( "gnu.org/licenses/lgpl.html" ) ).isEqualTo( lOracle.getByName( "lgpl-3.0+" ) );

        assertThat( lOracle.getOrThrowByName( "GPL-2.0 or later" ) ).isEqualTo( lOracle.getOrThrowByName( "GPL-2.0+" ) );

        assertThat( lOracle.getByUrl( "http://opensource.org/licenses/BSD-2-Clause" ) ).
                isEqualTo( lOracle.getByName( "bsd-2-clause" ) );

        assertThat( lOracle.getByName( "Indiana University Extreme! Lab Software License, vesion 1.1.1" ) ).
                isEqualTo( lOracle.getByName( "indiana-extreme-1.1.1" ) );

        // todo
        // assertThat( lOracle.getByUrl( "http://freemarker.org/LICENSE.txt" ).isPresent() ).isTrue();

        assertThat( lOracle.getByUrl( "http://htmlparser.sourceforge.net/cpl1.0.txt" ).isPresent() ).isTrue();
        assertThat( lOracle.getByUrl( "http://foo.bar/baz/lgpl-2.1.html" ).isPresent() ).isTrue();
        assertThat( lOracle.getByUrl( "gnu.org/licenses/lgpl-2.1" ).isPresent() ).isTrue();
        assertThat( lOracle.getByUrl( "repository.jboss.org/licenses/cc0-1.0" ).isPresent() ).isTrue();
        assertThat( lOracle.getByUrl( "www.fsf.org/licensing/licenses/agpl-3.0.html" ).isPresent() ).isTrue();

        // todo
    //        assertThat( lOracle.getByUrl( "http://creativecommons.org/publicdomain/zero/1.0/" ) ).
    //                isEqualTo( lOracle.getByName( "cc0-1.0" ) );

        assertThat( lOracle.getByUrl( "http://www.opensource.org/licenses/cpl1.0" ) ).
                isEqualTo( lOracle.getByName( "cpl-1.0" ) );

        assertThat( lOracle.getByCoordinates( Coordinates.valueOf( "org.scala-lang:scalap:2.10.0" ) ).isPresent() ).isTrue();

//        lOracle.allowUrlsCheckedDaysBefore( 100 );
//        System.out.println( lOracle.guessByUrl( "http://www.opensource.org/licenses/bsd-license.php" ) );
        assertThat( lOracle.getByUrl( "http://www.opensource.org/licenses/bsd-license.php" ).isPresent() ).isTrue();

        assertThat( lOracle.getByCoordinates( Coordinates.valueOf( "net.jcip:jcip-annotations:1.0" ) ).isPresent() ).isTrue();

        // todo
        // assertThat( lOracle.getByUrl( "http://foo/cddl+gpl_1_1" ).isPresent() ).isTrue();

        assertThat( lOracle.getByUrl( "http://www.opensource.org/licenses/mit-license.php" ) ).isEqualTo( MappedLicense.of( lOracle.getOrThrowByName( "mit" ), "testing" ) );

    }

    public void strongCL( LOracle lOracle ) {
        // http://www.ifross.org/ifross_html/lizenzcenter-en.html

//        LOracle lOracle = JSONStartup.start().spread();

        lOracle.getAttributes( lOracle.getOrThrowByName( "gpl-2.0" ) ).setCopyLeft( true );
        lOracle.getAttributes( lOracle.getOrThrowByName( "gpl-2.0+" ) ).setCopyLeft( true );
        lOracle.getAttributes( lOracle.getOrThrowByName( "gpl-3.0" ) ).setCopyLeft( true );
        lOracle.getAttributes( lOracle.getOrThrowByName( "gpl-3.0+" ) ).setCopyLeft( true );
        lOracle.getAttributes( lOracle.getOrThrowByName( "agpl-1.0" ) ).setCopyLeft( true );
        lOracle.getAttributes( lOracle.getOrThrowByName( "agpl-1.0+" ) ).setCopyLeft( true );
        lOracle.getAttributes( lOracle.getOrThrowByName( "agpl-3.0" ) ).setCopyLeft( true );
        lOracle.getAttributes( lOracle.getOrThrowByName( "agpl-3.0+" ) ).setCopyLeft( true );

        lOracle.getAttributes( lOracle.getOrThrowByName( "agpl-1.0" ) ).setCopyLeft( true );
        //System.out.println( lOracle.getOrThrowByName( "Affero General Public License" ));
        //System.out.println( lOracle.getByUrl( "http://www.affero.org/oagpl.html" ));

        {
            LicenseID alt = lOracle.newSingle( "alternate-1.1", false );
            lOracle.getMore( alt ).attributes.setFromIFross();
            lOracle.getAttributes( alt ).setCopyLeft( true );
            lOracle.addLongName( alt, "Alternate Route Open Source License (v. 1.1)" );
            lOracle.addUrl( alt, "http://www.wsdot.wa.gov/eesc/bridge/alternateroute/arosl.htm" );
        }
        System.out.println( lOracle.getByName( "Alternate Route Open Source License (v. 1.1)" ) );
        System.out.println( lOracle.getByUrl( "http://www.wsdot.wa.gov/eesc/bridge/alternateroute/arosl.htm" ) );
        System.out.println( "" );

        lOracle.getAttributes( lOracle.getOrThrowByName( "cecill-2.0" ) ).setCopyLeft( true );
        System.out.println( lOracle.getByName( "CeCILL License (v. 2)" ) );
        System.out.println( lOracle.getByUrl( "http://www.cecill.info/licences/Licence_CeCILL_V2-en.txt" ) );
        System.out.println( "" );

        {
            LicenseID cp = lOracle.newSingle( "crosspoint-1.1", false );
            lOracle.getMore( cp ).attributes.setFromIFross();
            lOracle.getAttributes( cp ).setCopyLeft( true );
            lOracle.addLongName( cp, "CrossPoint Source Code License, (only in German)" );
            lOracle.addLongName( cp, "CrossPoint Source Code License" );
            lOracle.addUrl( cp, "http://www.crosspoint.de/srclicense.html" );
        }
        System.out.println( lOracle.getByName( "CrossPoint Source Code License, (only in German)" ) );
        System.out.println( lOracle.getByUrl( "http://www.crosspoint.de/srclicense.html" ) );
        System.out.println( "" );
//
        System.out.println( lOracle.getByName( "eCos License (v. 2.0)" ) );
        System.out.println( lOracle.getByUrl( "http://www.gnu.org/licenses/ecos-license.html" ) );
        System.out.println( "" );
//
        System.out.println( lOracle.getByName( "FreeCard License" ) );
        System.out.println( lOracle.getByUrl( "http://freecard.sourceforge.net/website/licence/license.php" ) );
        System.out.println( "" );
//
        System.out.println( lOracle.getByName( "GNU Classpath -GPL with special exception " ) );
        System.out.println( lOracle.getByUrl( "http://www.gnu.org/software/classpath/license.html" ) );
        System.out.println( "" );
//
        System.out.println( lOracle.getByName( "GNU Emacs General Public License" ) );
        System.out.println( lOracle.getByUrl( "http://www.free-soft.org/gpl_history/emacs_gpl.html" ) );
        System.out.println( "" );
//
        System.out.println( lOracle.getByName( "GNU General Public License (GPL) (v. 1.0)" ) );
        System.out.println( lOracle.getByUrl( "http://www.gnu.org/copyleft/copying-1.0.html" ) );
        System.out.println( "" );
//
        System.out.println( lOracle.getByName( "GNU General Public License (GPL) (v. 2.0)" ) );
        System.out.println( lOracle.getByUrl( "http://www.gnu.org/licenses/old-licenses/gpl-2.0.html" ) );
        System.out.println( "" );
//
        System.out.println( lOracle.getByName( "GNU General Public License (GPL) (v. 3.0)" ) );
        System.out.println( lOracle.getByUrl( "http://www.fsf.org/licensing/licenses/gpl.html" ) );
        System.out.println( "" );
//
        System.out.println( lOracle.getByName( "GNU General Public License (GPL) (v. 3.0) - inofficial German translation" ) );
        System.out.println( lOracle.getByUrl( "http://www.gnu.de/gpl-ger.html" ) );
        System.out.println( "" );
//
        System.out.println( lOracle.getByName( "GNU Affero General Public License (GPL) (v. 3.0)" ) );
        System.out.println( lOracle.getByUrl( "http://www.fsf.org/licensing/licenses/agpl-3.0.html" ) );
        System.out.println( "" );
//
        {
            LicenseID li = lOracle.newSingle( "hpl-1.0", false );
            lOracle.getMore( li ).attributes.setFromIFross();
            lOracle.getAttributes( li ).setCopyLeft( true );
            lOracle.addLongName( li, "Honest Public License (HPL) (1.0)" );
            lOracle.addLongName( li, "Honest Public License (1.0)" );
            lOracle.addUrl( li, "http://www.projectpier.org/manual/tour/licence" );

        }
        System.out.println( lOracle.getByName( "Honest Public License (HPL) (1.0)" ) );
        System.out.println( lOracle.getByUrl( "http://www.projectpier.org/manual/tour/licence" ) );
        System.out.println( "" );
//
        {
            LicenseID li = lOracle.newSingle( "hpl-1.1", false );
            lOracle.getMore( li ).attributes.setFromIFross();
            lOracle.getAttributes( li ).setCopyLeft( true );
            lOracle.addLongName( li, "Honest Public License (HPL) (1.1)" );
            lOracle.addLongName( li, "Honest Public License (1.1)" );
            lOracle.addUrl( li, "http://www.opensourcestrategies.com/HPLv1.1.txt" );

        }
        System.out.println( lOracle.getByName( "Honest Public License (HPL) (1.1)" ) );
        System.out.println( lOracle.getByUrl( "http://www.opensourcestrategies.com/HPLv1.1.txt" ) );
        System.out.println( "" );
//
        System.out.println( lOracle.getByName( "Open RTLinux Patent License" ) );
        System.out.println( lOracle.getByUrl( "http://www.rtlinuxfree.com/openpatentlicense.html" ) );
        System.out.println( "" );
//
//
        lOracle.getAttributes( lOracle.getOrThrowByName( "apsl-2.0" ) ).setCopyLeft( true );
        System.out.println( lOracle.getByName( "Apple Public Source License (v. 2.0)" ) );
        System.out.println( lOracle.getByUrl( "http://www.opensource.apple.com/apsl/2.0.txt" ) );
        System.out.println( "" );
//
        System.out.println( lOracle.getByName( "Arphic Public License" ) );
        System.out.println( lOracle.getByUrl( "http://ftp.gnu.org/non-gnu/chinese-fonts-truetype/LICENSE" ) );
        System.out.println( "" );
//
        System.out.println( lOracle.getByName( "Boost Software License" ) );
        System.out.println( lOracle.getByUrl( "http://www.boost.org/LICENSE_1_0.txt" ) );
        System.out.println( "" );
//
        {
            lOracle.getAttributes( lOracle.getOrThrowByName( "cpl-1.0" ) ).setWeakCopyLeft( true );
        }
        System.out.println( lOracle.getByName( "Common Public License" ) );
        System.out.println( lOracle.getByUrl( "http://www.eclipse.org/legal/cpl-v10.html" ) );
        System.out.println( "" );
//
        {
            lOracle.getAttributes( lOracle.getOrThrowByName( "d-fsl-1.0-en" ) ).setCopyLeft( true );
            lOracle.addLongName( lOracle.getOrThrowByName( "d-fsl-1.0-en" ), "Deutsche Freie Softwarelizenz (d-fsl) - (German Free Software License)" );
            lOracle.addLongName( lOracle.getOrThrowByName( "d-fsl-1.0-en" ), "Deutsche Freie Softwarelizenz" );
            lOracle.addLongName( lOracle.getOrThrowByName( "d-fsl-1.0-en" ), "German Free Software License" );
        }
        System.out.println( lOracle.getByName( "Deutsche Freie Softwarelizenz (d-fsl) - (German Free Software License)" ) );
        System.out.println( lOracle.getByUrl( "http://www.dipp.nrw.de/d-fsl/index_html/lizenzen/en/D-FSL-1_0_en.txt" ) );
        System.out.println( "" );
//
        {
            lOracle.getAttributes( lOracle.getOrThrowByName( "epl-1.0" ) ).setWeakCopyLeft( true );
        }
        System.out.println( lOracle.getByName( "Eclipse Public License (v. 1.0)" ) );
        System.out.println( lOracle.getByUrl( "http://www.eclipse.org/legal/epl-v10.html" ) );
        System.out.println( "" );
//
        // eupl-1.0 ?
        System.out.println( lOracle.getByName( "European Public License (v. 1.0)" ) );
        System.out.println( lOracle.getByUrl( "http://ec.europa.eu/idabc/en/document/6523" ) );
        System.out.println( "" );
//
        // ipl-1.0 ?
        System.out.println( lOracle.getByName( "IBM Public License" ) );
        System.out.println( lOracle.getByUrl( "http://www-128.ibm.com/developerworks/opensource/library/os-i18n2/os-ipl.html" ) );
        System.out.println( "" );
//
        System.out.println( lOracle.getByName( "Jabber Open Source License" ) );
        System.out.println( lOracle.getByUrl( "http://www.jabber.org/about/josl.shtml" ) );
        System.out.println( "" );
//
        lOracle.getAttributes( lOracle.getOrThrowByName( "lpl-1.02" ) ).setCopyLeft( true );
        lOracle.addUrl( lOracle.getOrThrowByName( "lpl-1.02" ), "http://plan9.bell-labs.com/plan9/license.html" );
//        System.out.println( lOracle.getByName( "Lucent Public License Version (v. 1.02)" ) );
//        System.out.println( lOracle.getByUrl( "http://plan9.bell-labs.com/plan9/license.html" ) );
//        System.out.println("");
//
        lOracle.getAttributes( lOracle.getOrThrowByName( "ngpl" ) ).setCopyLeft( true );
//        System.out.println( lOracle.getByName( "Nethack General Public License" ) );
//        System.out.println( lOracle.getByUrl( "http://www.nethack.org/common/license.html" ) );
//        System.out.println("");
//
        lOracle.getAttributes( lOracle.getOrThrowByName( "open-group" ) ).setCopyLeft( true );
        lOracle.addUrl( lOracle.getOrThrowByName( "open-group" ), "http://www.opengroup.org/openmotif/license" );
//        System.out.println( lOracle.getByName( "Open Group Public License" ) );
//        System.out.println( lOracle.getByUrl( "http://www.opengroup.org/openmotif/license" ) );
//        System.out.println("");
//
        lOracle.getAttributes( lOracle.getOrThrowByName( "osl-2.1" ) ).setCopyLeft( true );
//        System.out.println( lOracle.getByName( "Open Software License (OSL) (v 2.1)" ) );
//        System.out.println( lOracle.getByUrl( "http://opensource.org/licenses/osl-2.1.php" ) );
//        System.out.println("");
//
        System.out.println( lOracle.getByName( "RedHat eCos Public License (v. 1.1)" ) );
        System.out.println( lOracle.getByUrl( "http://ecos.sourceware.org/old-license.html" ) );
        System.out.println( "" );
//
        System.out.println( lOracle.getByName( "RedHat eCos Public License (v. 1.1)" ) );
        System.out.println( lOracle.getByUrl( "http://sources.redhat.com/ecos/license-overview.html" ) );
        System.out.println( "" );
//
        System.out.println( lOracle.getByName( "Salutation Public License" ) );
        System.out.println( lOracle.getByUrl( "http://web.archive.org/web/20050323201906/http://www.salutation.org/lite/lite_license.htm" ) );
        System.out.println( "" );
//
        System.out.println( lOracle.getByName( "Software AG License Terms (Quip License) (v. 1.3)" ) );
        System.out.println( lOracle.getByUrl( "http://www.cse.uconn.edu/~dqg/cse350/xml/quip/License.txt" ) );
        System.out.println( "" );
//
        lOracle.getAttributes( lOracle.getOrThrowByName( "vim" ) ).setCopyLeft( true );
        lOracle.addUrl( lOracle.getOrThrowByName( "vim" ), "http://www.vim.org/htmldoc/uganda.html" );
//        System.out.println( lOracle.getByName( "Vim License" ) );
//        System.out.println( lOracle.getByUrl( "http://www.vim.org/htmldoc/uganda.html" ) );
//        System.out.println("");

    }

    @Test
    public void startup() {
        buildAfterExternal();
        
        LOracle lOracle =
                //new LOracle();
                JSONStartup.start().spread();

        System.out.println( "=============== " + lOracle.getSingleLicenseCount() + " === " + lOracle.longNameMapper.size());

        testAll( lOracle );
    }

    @Ignore
    @Test
    public void lowercaseSPDX() {
        // System.out.println( Paths.get( "" ).toAbsolutePath() );

        Files_.list( Paths_.get__( "src/test/resources/de/pfabulist/loracle/spdx" ).toAbsolutePath_() ).
                forEach( src -> Files_.copy( src,
                                             Paths_.get__( "src/main/resources/de/pfabulist/loracle/urls" ).toAbsolutePath_().
                                                     resolve( n_(src.getFileName()).toString().toLowerCase( Locale.US ) ),
                                             StandardCopyOption.REPLACE_EXISTING ) );
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


/*
 $Id: LICENSE.txt,v 1.1.1.1 2004/07/01 13:59:13 jvanzyl Exp $

 Copyright 2002 (C) The Codehaus. All Rights Reserved.

 Redistribution and use of this software and associated documentation
 ("Software"), with or without modification, are permitted provided
 that the following conditions are met:

 1. Redistributions of source code must retain copyright
    statements and notices.  Redistributions must also contain a
    copy of this document.

 2. Redistributions in binary form must reproduce the
    above copyright notice, this list of conditions and the
    following disclaimer in the documentation and/or other
    materials provided with the distribution.

 3. The name "classworlds" must not be used to endorse or promote
    products derived from this Software without prior written
    permission of The Codehaus.  For written permission, please
    contact bob@codehaus.org.

 4. Products derived from this Software may not be called "classworlds"
    nor may "classworlds" appear in their names without prior written
    permission of The Codehaus. "classworlds" is a registered
    trademark of The Codehaus.

 5. Due credit should be given to The Codehaus.
    (http://classworlds.codehaus.org/).

 THIS SOFTWARE IS PROVIDED BY THE CODEHAUS AND CONTRIBUTORS
 ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 THE CODEHAUS OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 OF THE POSSIBILITY OF SUCH DAMAGE.

 */
