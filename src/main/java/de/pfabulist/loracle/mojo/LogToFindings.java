//package de.pfabulist.loracle.mojo;
//
//import de.pfabulist.loracle.license.Findings;
//import org.apache.maven.plugin.logging.Log;
//
///**
// * Copyright (c) 2006 - 2017, Stephan Pfab
// * SPDX-License-Identifier: BSD-2-Clause
// */
//
//public class LogToFindings {
//
//    public static Findings toFindings( Log log ) {
//           if ( log instanceof Findings ) {
//               return ((Findings)log);
//           }
//
//           return new Findings() {
//               @Override
//               public boolean isDebugEnabled() {
//                   return log.isDebugEnabled();
//               }
//
//               @Override
//               public void debug( CharSequence content ) {
//                  log.debug( content );
//               }
//
//               @Override
//               public void debug( CharSequence content, Throwable error ) {
//                  log.debug( content, error );
//               }
//
//               @Override
//               public void debug( Throwable error ) {
//                log.debug( error );
//               }
//
//               @Override
//               public boolean isInfoEnabled() {
//                   return log.isInfoEnabled();
//               }
//
//               @Override
//               public void info( CharSequence content ) {
//                  log.info( content );
//
//               }
//
//               @Override
//               public void info( CharSequence content, Throwable error ) {
//                    log.info( content, error );
//               }
//
//               @Override
//               public void info( Throwable error ) {
//                    log.info( error );
//               }
//
//               @Override
//               public boolean isWarnEnabled() {
//                   return log.isWarnEnabled();
//               }
//
//               @Override
//               public void warn( CharSequence content ) {
//                    log.warn( content );
//               }
//
//               @Override
//               public void warn( CharSequence content, Throwable error ) {
//                    log.warn( content, error );
//               }
//
//               @Override
//               public void warn( Throwable error ) {
//                    log.warn( error );
//               }
//
//               @Override
//               public boolean isErrorEnabled() {
//                   return log.isErrorEnabled();
//               }
//
//               @Override
//               public void error( CharSequence content ) {
//                    log.error( content );
//               }
//
//               @Override
//               public void error( CharSequence content, Throwable error ) {
//                    log.error( content, error );
//               }
//
//               @Override
//               public void error( Throwable error ) {
//                    log.error( error );
//               }
//
//               @Override
//               public void throwOnError() {
//
//               }
//           }
//    }
//
//}
