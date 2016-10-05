package de.pfabulist.loracle.mojo;

import de.pfabulist.roast.unchecked.Unchecked;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;

import java.util.ArrayList;
import java.util.List;

import static de.pfabulist.roast.NonnullCheck._nn;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

public class Findings implements Log {

    private final Log mavenLog;
    private final List<String> fails = new ArrayList<>();

    public Findings( Log mavenLog ) {
        this.mavenLog = mavenLog;
    }

    @Override
    public boolean isDebugEnabled() {
        return mavenLog.isDebugEnabled();
    }

    @Override
    public void debug( CharSequence content ) {
        mavenLog.debug( content );
    }

    @Override
    public void debug( CharSequence content, Throwable error ) {
        mavenLog.debug( content, error );
    }

    @Override
    public void debug( Throwable error ) {
        mavenLog.debug( error );
    }

    @Override
    public boolean isInfoEnabled() {
        return mavenLog.isInfoEnabled();
    }

    @Override
    public void info( CharSequence content ) {
        mavenLog.info( content );
    }

    @Override
    public void info( CharSequence content, Throwable error ) {
        mavenLog.info( content, error );
    }

    @Override
    public void info( Throwable error ) {
        mavenLog.info( error );
    }

    @Override
    public boolean isWarnEnabled() {
        return mavenLog.isWarnEnabled();
    }

    @Override
    public void warn( CharSequence content ) {
        mavenLog.warn( content );
    }

    @Override
    public void warn( CharSequence content, Throwable error ) {
        mavenLog.warn( content, error );
    }

    @Override
    public void warn( Throwable error ) {
        mavenLog.warn( error );
    }

    @Override
    public boolean isErrorEnabled() {
        return mavenLog.isErrorEnabled();
    }

    @Override
    public void error( CharSequence content ) {
        fails.add( _nn( content ).toString() );
        mavenLog.error( content );
    }

    @Override
    public void error( CharSequence content, Throwable error ) {
        fails.add( _nn( content ).toString() );
        mavenLog.error( content, error );

    }

    @Override
    public void error( Throwable error ) {
        fails.add( _nn( error ).getMessage() );
        mavenLog.error( error );
    }

    public void throwOnError() {
        if( !fails.isEmpty() ) {
            throw Unchecked.u( new MojoFailureException( fails.get( 0 ) ) );
        }
    }
}
