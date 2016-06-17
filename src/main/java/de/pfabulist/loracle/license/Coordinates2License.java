package de.pfabulist.loracle.license;

import de.pfabulist.loracle.attribution.CopyrightHolder;
import de.pfabulist.loracle.mojo.Findings;
import de.pfabulist.unchecked.functiontypes.FunctionE;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import static de.pfabulist.nonnullbydefault.NonnullCheck._nn;
import static de.pfabulist.unchecked.NullCheck._orElseThrow;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

@SuppressWarnings( { "PMD.UnusedPrivateField" } )
public class Coordinates2License {

    @SuppressFBWarnings( { "URF_UNREAD_FIELD" } ) // txt only in tojson

    public static class LiCo {
        private Optional<String> license = Optional.empty();
        private Optional<CopyrightHolder> copyrightHolder = Optional.empty();
        private String scope = "plugin";
        private String message = "";
        private String licenseTxt = "";
        private String headerTxt = "";
        transient private boolean used = false;

        public Optional<String> getLicense() {
            return license;
        }

        public Optional<CopyrightHolder> getCopyrightHolder() {
            return copyrightHolder;
        }

        public void setLicense( Optional<LicenseID> license ) {
            this.license = license.map( Object::toString );
        }

        public String getScope() {
            return scope;
        }

        public void setMessage( String message ) {
            this.message = message;
        }

        public boolean isUsed() {
            return used;
        }

        public Optional<CopyrightHolder> getHolder() {
            return copyrightHolder;
        }

        public String getMessage() {
            return message;
        }

        public void setHolder( Optional<CopyrightHolder> holder ) {
            this.copyrightHolder = holder;
        }

        public void setLicenseTxt( String licenseTxt ) {
            this.licenseTxt = licenseTxt;
        }

        public void setHeaderTxt( String headerTxt ) {
            this.headerTxt = headerTxt;
        }
    }

    private Map<Coordinates, LiCo> list = new HashMap<>();
    private boolean andIsOr = false;
    @Nullable
    transient private Findings log;

    public void add( Coordinates coo ) {
        list.putIfAbsent( coo, new LiCo() );
        _nn( list.get( coo ) ).used = true;
    }

    public void updateScope( Coordinates coo, String scope ) {
        if( !list.containsKey( coo ) ) {
            throw new IllegalStateException( "no such " + coo );
        }

        LiCo liCo = _nn( list.get( coo ) );

        String oldScope = _nn( liCo.scope );

        if( getScopeLevel( scope ) < getScopeLevel( oldScope ) ) {
            liCo.scope = scope;
        }
    }

    int getScopeLevel( String scope ) {
        switch( scope ) {
            case "plugin":
                return 8;
            case "import":
                return 7;
            case "system":
                return 6;
            case "test":
                return 5;
            case "runtime":
                return 4;
            case "provided":
                return 3;
            case "optional":
                return 2;
            case "compile":
                return 1;
            default:
                getLog().warn( "unexpected scope " + scope );
                return 100;
        }
    }

    private Findings getLog() {
        return _orElseThrow( log, () -> new IllegalStateException( "no logger" ) );
    }

    public void determineLicenses( FunctionE<Coordinates, Optional<LicenseID>, Exception> f ) {
        list.forEach( ( c, coli ) -> {
            getLog().debug( "license for " + c + " is ...");
            if( !coli.getLicense().isPresent() ) {
                coli.setLicense( _nn( f.apply( c ) ) );
                getLog().debug( "license for " + c + " is (found) to be " + coli.getLicense());
            } else {
                getLog().debug( "license for " + c + " is known to be " + coli.getLicense().get());
            }
        } );
    }

    public Optional<LiCo> get( Coordinates coordinates ) {
        return Optional.ofNullable( list.get( coordinates ) );
    }

    public void checkCompatibility( BiFunction<Coordinates, String, String> f ) {
        list.forEach( ( c, coli ) ->
                              coli.getLicense().ifPresent( l -> {
                                  String message = _nn( f.apply( c, l ) );
                                  if( !message.isEmpty() ) {
                                      scopeDependingLog( c, message );
                                  }

                                  coli.setMessage( message );

                              } ) );

    }

    private void scopeDependingLog( Coordinates coo, String message ) {
        String scope = _orElseThrow( list.get( coo ), () -> new IllegalStateException( "huh" ) ).getScope();

        if( !scope.equals( "plugin" ) && !scope.equals( "test" ) ) {
            getLog().error( message );
        } else {
            getLog().warn( message );
        }

    }

    public void summery() {
        list.entrySet().stream().
                filter( e -> _nn( e.getValue() ).isUsed() ).
                sorted( ( a, b ) -> getScopeLevel( _nn( a.getValue() ).getScope() ) - getScopeLevel( _nn( b.getValue() ).getScope() ) ).
                forEach( e -> {
                    Coordinates c = _nn( e.getKey() );
                    LiCo lico = _nn( e.getValue() );

                    getLog().info( String.format( "%-80s %-10s %-50s ",
                                                  c,
                                                  lico.getScope(),
                                                  lico.getLicense().map( Object::toString ).orElse( "-" ) ) +
                                           lico.getHolder().map( Object::toString ).orElse( "-" ) );
                    if( !lico.getMessage().isEmpty() ) {
                        getLog().error( "   " + lico.getMessage() );
                    }
                } );
    }

    public void getHolders( BiFunction<Coordinates, String, Optional<CopyrightHolder>> f ) {
        list.forEach( ( c, lico ) -> lico.getLicense().ifPresent( l -> lico.setHolder( _nn( f.apply( c, l ) ) ) ) );
    }

    public void fromSrc( BiConsumer<Coordinates, LiCo> f ) {
        list.forEach( f );
    }

    public void setLog( Findings log ) {
        this.log = log;
    }

}
