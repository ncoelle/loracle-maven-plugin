package de.pfabulist.loracle.license;


/**
 * Copyright (c) 2006 - 2017, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

 public interface Findings {



    
     boolean isDebugEnabled();

     void debug( CharSequence content );
    
     void debug( CharSequence content, Throwable error );
    
     void debug( Throwable error );
    
     boolean isInfoEnabled();

     void info( CharSequence content );
    
     void info( CharSequence content, Throwable error );

     void info( Throwable error );

     boolean isWarnEnabled();
    
     void warn( CharSequence content );
    
     void warn( CharSequence content, Throwable error );
    
     void warn( Throwable error );
    
     boolean isErrorEnabled();
    
     void error( CharSequence content );
    
     void error( CharSequence content, Throwable error );
    
     void error( Throwable error );

     void throwOnError();
}
