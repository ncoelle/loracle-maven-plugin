package de.pfabulist.loracle.license;

import de.pfabulist.unchecked.Unchecked;
import de.pfabulist.unchecked.functiontypes.ConsumerE;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import static de.pfabulist.nonnullbydefault.NonnullCheck._nn;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

public class MappedLicense {
    private final @Nullable LicenseID license;
    private final String reason;

    private final static MappedLicense empty = new MappedLicense();

    private MappedLicense() {
        license = null;
        reason = "";
    }

    private MappedLicense( LicenseID licenseID, String reason ) {
        this.license = licenseID;
        this.reason = reason;

        if (reason.isEmpty() ) {
            throw new IllegalArgumentException( "can't have a MappedLicense without reason" );
        }
    }

    public static MappedLicense empty() {
        return empty;
    }

    public static MappedLicense of( LicenseID licenseID, String reason ) {
        return new MappedLicense( licenseID, reason );
    }

    public static MappedLicense of( Optional<LicenseID> licenseID, String reason ) {
        return licenseID.map( l -> of( l, reason) ).orElse( empty() );
    }

    public boolean isPresent() {
        return license != null;
    }

    public LicenseID orElseThrow( Supplier<Exception> ex ) {
        if ( !isPresent()) {
            throw Unchecked.u( _nn(ex.get()));
        }

        return _nn( license );
    }

    public void ifPresent( ConsumerE<LicenseID, Exception> con ) {
        if ( isPresent() ) {
            con.accept( license );
        }
    }

    public MappedLicense addReason( String more ) {
        if ( !isPresent() ) {
            return MappedLicense.empty();
        }
        return new MappedLicense( _nn(license), reason + " && " + more );
    }

    @Override
    public String toString() {
        if ( isPresent() ) {
            return _nn(license) + " [" + reason + "]";
        }

        return "-";
    }

    public String getReason() {
        return reason;
    }

    public Optional<LicenseID> noReason() {
        return Optional.ofNullable( license );
    }

    @Override
    @SuppressFBWarnings( "NP_METHOD_PARAMETER_TIGHTENS_ANNOTATION")
    public boolean equals( @Nullable Object o ) {
        if( this == o ) { return true; }
        if( o == null || getClass() != o.getClass() ) { return false; }

        MappedLicense that = (MappedLicense) o;

        return license != null ? license.equals( that.license ) : that.license == null;

    }

    @Override
    public int hashCode() {
        return license != null ? license.hashCode() : 0;
    }

    public <U> U orElse( Function<LicenseID, U> f, U els ) {
        if ( isPresent()) {
            return _nn(f.apply( _nn(license )));
        }

        return els;
    }
}
