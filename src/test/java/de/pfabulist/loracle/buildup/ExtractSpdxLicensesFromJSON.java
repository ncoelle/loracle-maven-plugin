package de.pfabulist.loracle.buildup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import de.pfabulist.frex.Frex;
import de.pfabulist.loracle.license.LOracle;
import de.pfabulist.loracle.license.LicenseID;
import de.pfabulist.unchecked.Unchecked;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Map;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

@SuppressFBWarnings( { "URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD", "UUF_UNUSED_PUBLIC_OR_PROTECTED_FIELD", "DM_DEFAULT_ENCODING" })
public class ExtractSpdxLicensesFromJSON {

    public ExtractSpdxLicensesFromJSON() {
    }

    private static class LInfo {
        public String name = "nn";
        public String url = "uu";
        public boolean osiApproved;
        public String license = "li";
    }

    public void go( LOracle lOracle ) {

        Type type = new TypeToken<Map<String, LInfo>>() {}.getType();

        byte[] buf = new byte[ 3000000 ];

        int got = 0;
        try( InputStream in = getClass().getResourceAsStream( "/de/pfabulist/loracle/spdx-full.json.txt" ) ) {
            got = in.read( buf );
        } catch( IOException e ) {
            throw Unchecked.u( e);
        }


        Map<String, LInfo> map = new Gson().fromJson( new String( buf, 0, got ), type );

        for( Map.Entry<String, LInfo> entry : map.entrySet() ) {
            // name
            LicenseID license = lOracle.newSingle( entry.getKey(), true );

            // longname
            if ( license.getId().equals( "bsd-3-clause" )) {
                lOracle.addLongName( license, "bsd 3 clause" );
                lOracle.addLongName( license, "new bsd" );
                lOracle.addLongName( license, "revised bsd" );
            } else {
                lOracle.addLongName( license, entry.getValue().name );
            }

            // url
            String url = entry.getValue().url;
            if ( url != null &&
                 !entry.getKey().equals( "IBM-pibs" ) && // strange url
                 !license.getId().equals( "cddl-1.1" ) // url points to exception
              ) {

                // filter non unique urls

                Arrays.stream( url.split( Frex.txt( '\n' ).buildPatternString() ) ).
                        filter( u -> !u.equals( "http://opensource.org/licenses/Artistic-1.0" ) &&
                                !u.equals( "http://www.microsoft.com/opensource/licenses.mspx" ) &&
                                !u.equals( "http://opensource.org/licenses/MPL-2.0" ) &&
                                !u.equals( "http://www.mozilla.org/MPL/2.0/" ) &&
                                !u.equals( "https://fedoraproject.org/wiki/Licensing/Henry_Spencer_Reg-Ex_Library_License" )

                        ).
                        forEach( u -> lOracle.addUrl( license, u ) );
            }

            // osi approved
            lOracle.setOsiApproval( license, entry.getValue().osiApproved );
        }
    }
}
