package de.pfabulist.loracle.license;

import de.pfabulist.loracle.attribution.CopyrightHolder;
import de.pfabulist.roast.nio.Files_;
import de.pfabulist.roast.nio.Paths_;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import javax.annotation.Nullable;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.*;

import static de.pfabulist.kleinod.text.Strings.getBytes;
import static de.pfabulist.roast.NonnullCheck._nn;
import static de.pfabulist.roast.NonnullCheck.n_;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

@SuppressWarnings( { "PMD.UnusedPrivateField" } )
public class Coordinates2License {

    @SuppressFBWarnings( { "URF_UNREAD_FIELD", "URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD" } )
    public static class MLicense {
        public String mavenLicenseName = "";
        public String byName = "-";
        public String mavenLicenseUrl = "";
        public String byUrl = "-";
        public String mavenLicenseComment = "";
        public String byComment = "-";

        public MLicense( String mavenLicenseName, String mavenLicenseUrl, String mavenLicenseComment ) {
            this.mavenLicenseName = mavenLicenseName;
            this.mavenLicenseUrl = mavenLicenseUrl;
            this.mavenLicenseComment = mavenLicenseComment;
        }

        public String getName() {
            return mavenLicenseName;
        }

        public String getUrl() {
            return mavenLicenseUrl;
        }

        public String getComment() {
            return mavenLicenseComment;
        }

        public void setByName( MappedLicense byName ) {
            this.byName = byName.toString();
        }

        public void setByUrl( MappedLicense byUrl ) {
            this.byUrl = byUrl.toString();
        }

        public void setByComment( MappedLicense byComment ) {
            this.byComment = byComment.toString();
        }
    }

    @SuppressFBWarnings( { "URF_UNREAD_FIELD" } ) // txt only in toJson
    public static class LiCo {
        private Optional<String> license = Optional.empty();
        private String licenseReason = "";
        private List<MLicense> mavenLicenses = Collections.emptyList();
        private Optional<CopyrightHolder> copyrightHolder = Optional.empty();
        private String holderReason = "";
        private String scope = "plugin";
        private String message = "";
        private String licenseTxt = "";
        private String headerTxt = "";
        private String licenseTxtLicense = "";
        private String headerLicense = "";
        private String pomLicense = "";
        private String byCoordinates = "";
        private String notice = "";
        private String pomHeaderLicense = "";
        private List<String> licenseFilenames = Collections.emptyList();

        transient private boolean used = false;
        transient private List<String> useedBy = new ArrayList<>();

        public String getPomHeader() {
            return pomHeader;
        }

        public void setPomHeader( String pomHeader ) {
            this.pomHeader = pomHeader;
        }

        private String pomHeader = "";

        public void setNoticeLicense( MappedLicense noticeLicense ) {
            this.noticeLicense = noticeLicense.toString();
        }

        private String noticeLicense = "";

        public Optional<String> getLicense() {
            return license;
        }

        public String getNotice() {
            return notice;
        }

        public void setNotice( String notice ) {
            this.notice = notice;
        }

        public Optional<CopyrightHolder> getCopyrightHolder() {
            return copyrightHolder;
        }

        public void setLicense( MappedLicense mlicense ) {
            mlicense.ifPresent( l -> {
                license = Optional.of( l.toString() );
                this.licenseReason = mlicense.toString();
            } );

            if( !mlicense.isPresent() ) {
                licenseReason = "";
                license = Optional.empty();
            }
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

        public String getLicenseReason() {
            return licenseReason;
        }

        public void setMLicenses( List<MLicense> mLicenses ) {
            this.mavenLicenses = mLicenses;
        }

        public List<MLicense> getMavenLicenses() {
            return mavenLicenses;
        }

        public String getLicenseTxt() {
            return licenseTxt;
        }

        public void setLicenseTxtLicense( MappedLicense licenseTxtLicense ) {
            if( licenseTxtLicense.isPresent() ) {
                this.licenseTxtLicense = licenseTxtLicense.toString();
            } else {
                this.licenseTxtLicense = licenseTxtLicense.getReason();
            }
        }

        public void setHeaderLicense( MappedLicense headerLicense ) {
            if( headerLicense.isPresent() ) {
                this.headerLicense = headerLicense.toString();
            } else {
                this.headerLicense = headerLicense.getReason();
            }
        }

        public String getLicenseTextLicense() {
            return licenseTxtLicense;
        }

        public String getHeaderLicense() {
            return headerLicense;
        }

        public void setPomLicense( MappedLicense pomLicense ) {
            if( pomLicense.isPresent() ) {
                this.pomLicense = pomLicense.toString();
            } else {
                this.pomLicense = pomLicense.getReason();
            }
        }

        public String getPomLicense() {
            return pomLicense;
        }

        public String getHeaderTxt() {
            return headerTxt;
        }

        public void setByCoordinates( MappedLicense byCoordinates ) {
            if( byCoordinates.isPresent() ) {
                this.byCoordinates = byCoordinates.toString();
            } else {
                this.byCoordinates = byCoordinates.getReason();
            }
        }

        public String getByCoordinates() {
            return byCoordinates;
        }

        public String getNoticeLicense() {
            return noticeLicense;
        }

        public String getPomHeaderLicense() {
            return pomHeaderLicense;
        }

        public void setPomHeaderLicense( MappedLicense license ) {
            this.pomHeaderLicense = license.toString();
        }

        public void setMavenLicenses( List<MLicense> mavenLicenses ) {
            this.mavenLicenses = mavenLicenses;
        }

        public void setLicenseFilenames( List<String> licenseFilenames ) {
            this.licenseFilenames = licenseFilenames;
        }

        public List<String> getLicenseFilenames() {
            return licenseFilenames;
        }
    }

    private Map<Coordinates, LiCo> list = new HashMap<>();
    private boolean andIsOr = false;
    @Nullable
    transient private Findings log;
    private transient Optional<Coordinates> self = Optional.empty();

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

    public static int getScopeLevel( String scope ) {
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
//                getLog().warn( "unexpected scope " + scope );
                return 100;
        }
    }

    private Findings getLog() {
        return n_( log, () -> new IllegalStateException( "no logger" ) );
    }

    public void update( BiConsumer<Coordinates, LiCo> f ) {
        list.forEach( ( c, coli ) -> {
            if( coli.isUsed() ) {
                f.accept( c, coli );
            }
        } );
    }

    public void update( Predicate<LiCo> pred, BiConsumer<Coordinates, LiCo> f ) {
        list.forEach( ( c, coli ) -> {
            if( coli.isUsed() && pred.test( coli ) ) {
                f.accept( c, coli );
            }
        } );
    }

    public void setSelf( Coordinates coo ) {
        self = Optional.of( coo );
    }

    public Optional<LiCo> get( Coordinates coordinates ) {
        return Optional.ofNullable( list.get( coordinates ) );
    }

    public void checkCompatibility( BiFunction<Coordinates, String, String> f ) {
        list.forEach( ( c, coli ) -> {
            if( coli.isUsed() ) {
                coli.getLicense().ifPresent( l -> {
                    String message = _nn( f.apply( c, l ) );
                    if( !message.isEmpty() ) {
                        scopeDependingLog( c, message );
                    }

                    coli.setMessage( message );

                } );
            }
        } );

    }

    private void scopeDependingLog( Coordinates coo, String message ) {
        String scope = n_( list.get( coo ), () -> new IllegalStateException( "huh" ) ).getScope();

        if( !scope.equals( "plugin" ) && !scope.equals( "test" ) ) {
            getLog().error( message );
        } else {
            getLog().warn( message );
        }

    }

    public void summery() {
        list.entrySet().stream().
                filter( e -> entryPred( e, ( coo, lico ) -> lico.isUsed() ) ).
                sorted( ( a, b ) -> {
                    int scope = getScopeLevel( _nn( a.getValue() ).getScope() ) - getScopeLevel( _nn( b.getValue() ).getScope() );
                    if( scope != 0 ) {
                        return scope;
                    }

                    return a.toString().compareTo( b.toString() );
                } ).
                forEach( e -> {
                    Coordinates c = _nn( e.getKey() );
                    LiCo lico = _nn( e.getValue() );

                    getLog().info( String.format( "%-80s %-10s %-50s ",
                                                  c,
                                                  lico.getScope(),
                                                  lico.getLicense().map( Object::toString ).orElse( "-" ) ) +
                                           lico.getHolder().map( Object::toString ).orElse( "-" ) );
                    if( !lico.getLicense().isPresent() ) {
                        lico.useedBy.forEach( u -> getLog().error( "   used by: " + u ) );
                        getLog().error( "   no license found" );
                    }

                    if( !lico.getMessage().isEmpty() ) {
                        lico.useedBy.forEach( u -> getLog().error( "   used by " + u ) );
                        getLog().error( "   " + lico.getMessage() );
                    }
                    getLog().debug( "    [sum]              " + lico.getLicenseReason() );
                    getLog().debug( "    by Coordinates     " + lico.getByCoordinates() );
                    getLog().debug( "    by Pom             " + lico.getPomLicense() );
                    lico.getMavenLicenses().forEach( ml -> {
                        getLog().debug( "    by Pom Licenses" );
                        getLog().debug( "       <" );
                        if( ml.byName.length() > 2 ) {
                            getLog().debug( "                       " + ml.byName );
                        } else {
                            getLog().debug( "                       [-] " + ml.getName() );
                        }

                        if( ml.byUrl.length() > 2 ) {
                            getLog().debug( "                       " + ml.byUrl );
                        } else {
                            getLog().debug( "                       [-] " + ml.getUrl() );
                        }

                        if( ml.byComment.length() > 2 ) {
                            getLog().debug( "                       " + ml.byComment );
                        } else {
                            getLog().debug( "                       [-] " + ml.getComment() );
                        }
                        getLog().debug( "       >" );
                    } );
                    getLog().debug( "    by Pom Header      " + ( lico.getPomHeader().isEmpty() ? "" : "[+] " ) + lico.getPomHeaderLicense() );
                    getLog().debug( "    by License Text    " + ( lico.getLicenseTxt().isEmpty() ? "" : "[+] " ) + lico.getLicenseTextLicense() );
                    getLog().debug( "    by Header          " + ( lico.getHeaderTxt().isEmpty() ? "" : "[+] " ) + lico.getHeaderLicense() );
                    getLog().debug( "    by Notice          " + ( lico.getNotice().isEmpty() ? "" : "[+] " ) + lico.getNoticeLicense() );
                    getLog().debug( "\n" );
                } );
    }

    public void generateNotice() {
        StringBuilder sb = new StringBuilder();

        if( !self.isPresent() ) {
            sb.append( "what ???\n\n" );
        } else {

            LiCo selfLico = _nn( get( _nn( self.get() ) ).get() );
            sb.append( "This is " ).append( self.map( Object::toString ).orElse( "?" ) ).append( "\n" );
            sb.append( "it is licensed under:  " ).append( selfLico.getLicense().get() ).append( "\n" );
            selfLico.getLicenseFilenames().forEach( fn -> sb.append( "see license file:      " ).append( fn ).append( "\n" ) );
            sb.append( "copyright holder:      " ).append( selfLico.getCopyrightHolder().map( Object::toString ).orElse( "" ) ).append( "\n\n" );
            sb.append( "It includes the following software:\n\n" );

//
//        sb.append(
//                "   =========================================================================\n" +
//                        "   ==  NOTICE file corresponding Notice file standard as described in                     ==\n" +
//                        "   ==  the loracle-maven-plugin ,                                   ==\n" +
//                        "   ==                       ==\n" +
//                        "   =========================================================================\n" +
//                        "\n" +
//                        "   This is <loracle-maven-plugin-version foo>\n" +
//                        "   copyright pfabulist.de licensed under BSD-2-clause\n" +
//                        "\n" +
//                        "   It includes the following software\n" +
//                        "   Please read the different LICENSE files present in the de.pfabulist.loracle directory of\n" +
//                        "   this distribution.\n\n\n" );
//

            list.entrySet().stream().
                    filter( e -> entryPred( e, ( coo, lico ) -> lico.isUsed() && getScopeLevel( lico.getScope() ) < getScopeLevel( "test" ) ) ).
                    filter( e -> !_nn( e.getKey() ).equals( self.get() ) ).
                    sorted( ( a, b ) -> entryComp( a, b, ( cooA, x, cooB, y ) -> cooA.toString().compareTo( cooB.toString() ) ) ).
                    forEach( ec( ( coo, lico ) -> {
                        sb.append( coo.getArtifactId() ).append( "\n" ).
                                append( "   full name:         " ).append( coo.toString() ).append( "\n" ).
                                append( "   licensed under:    " ).append( lico.getLicense().map( Object::toString ).orElse( "-" ) ).append( "\n" ).
                                append( "   copyright holder:  " ).append( lico.getCopyrightHolder().map( Object::toString ).orElse( "" ) ).append( "\n" );
                        lico.getLicenseFilenames().forEach( fn -> sb.append( "   see license file:  " ).append( fn ).append( "\n" ) );
                        sb.append( "\n" );
                    } ) );

        }

        Path ff = Paths_.get_( "target/generated-sources/loracle/licenses/" + self.map( c -> c.getArtifactId() + "/" ).orElse( "" ) + "NOTICE.txt" );
        Files_.createDirectories( _nn( ff.getParent()) );
        Files_.write( ff, getBytes( sb.toString() ) );

    }

    public void getHolders( BiFunction<Coordinates, String, Optional<CopyrightHolder>> f ) {
        list.forEach( ( c, lico ) -> {
            if( lico.isUsed() ) {
                lico.getLicense().ifPresent( l -> lico.setHolder( _nn( f.apply( c, l ) ) ) );
            }
        } );
    }

    public void fromSrc( BiConsumer<Coordinates, LiCo> f ) {
        list.forEach( ( c, lico ) -> {
            if( lico.isUsed() ) {
                f.accept( c, lico );
            }
        } );
    }

    public void fromJar( BiConsumer<Coordinates, LiCo> f ) {
        list.forEach( ( c, lico ) -> {
            if( lico.isUsed() && !lico.getLicense().isPresent() ) {
                f.accept( c, lico );
            }
        } );
    }

    public void setLog( Findings log ) {
        this.log = log;
    }

    public boolean andIsOr() {
        return andIsOr;
    }

    public void setAndIsOr( boolean andIsOr ) {
        if( this.andIsOr != andIsOr ) {
            list.forEach( ( c, coli ) -> coli.setLicense( MappedLicense.empty() ) ); // todo just and, or ?
        }
        this.andIsOr = andIsOr;
    }

    public static <K, V> boolean entryPred( Map.Entry<K, V> entry, BiPredicate<K, V> bipred ) {
        return bipred.test( entry.getKey(), entry.getValue() );
    }

    public static <K, V> void entryConsumer( Map.Entry<K, V> entry, BiConsumer<K, V> func ) {
        func.accept( entry.getKey(), entry.getValue() );
    }

    public static <K, V> Consumer<Map.Entry<K, V>> ec( BiConsumer<K, V> func ) {
        return e -> func.accept( e.getKey(), e.getValue() );
    }

    public interface Function4<A, B, C, D, R> {
        R apply( A a, B b, C c, D d );
    }

    public static <K, V> int entryComp( Map.Entry<K, V> a, Map.Entry<K, V> b, Function4<K, V, K, V, Integer> f4 ) {
        return f4.apply( _nn( a.getKey() ), _nn( a.getValue() ), _nn( b.getKey() ), _nn( b.getValue() ) );
    }

    public void addUse( Coordinates coo, String use ) {
        get( coo ).ifPresent( liCo -> liCo.useedBy.add( use ) );
    }

}
