//package de.pfabulist.loracle.mojo;
//
///**
// * Copyright (c) 2006 - 2016, Stephan Pfab
// * SPDX-License-Identifier: BSD-2-Clause
// */
//
//import org.apache.maven.plugin.testing.MojoRule;
//import org.apache.maven.plugin.testing.WithoutMojo;
//
//import org.junit.Rule;
//import static org.junit.Assert.*;
//import org.junit.Test;
//
//public class LOracleMojoTest
//{
//    @Rule
//    public MojoRule rule = new MojoRule()
//    {
//        @Override
//        protected void before() throws Throwable
//        {
//        }
//
//        @Override
//        protected void after()
//        {
//        }
//    };
//
//    /**
//     * @throws Exception if any
//     */
//    @Test
//    public void testSomething()
//            throws Exception
//    {
//        //rule.
////        File pom = rule.getTestFile( "src/test/resources/unit/project-to-test/pom.xml" );
////        assertNotNull( pom );
////        assertTrue( pom.exists() );
////
////        MyMojo myMojo = (MyMojo) rule.lookupMojo( "touch", pom );
////        assertNotNull( myMojo );
////        myMojo.execute();
////
////        ...
//    }
//
////    /** Do not need the MojoRule. */
////    @WithoutMojo
////    @Test
////    public void testSomethingWhichDoesNotNeedTheMojoAndProbablyShouldBeExtractedIntoANewClassOfItsOwn()
////    {
////        ...
////    }
//
//}