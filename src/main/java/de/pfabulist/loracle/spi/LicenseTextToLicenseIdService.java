package de.pfabulist.loracle.spi;

import com.esotericsoftware.minlog.Log;
import de.pfabulist.loracle.fulltext.TextToLicenses;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;

import static de.pfabulist.roast.NonnullCheck.n_;

/**
 * Copyright (c) 2006 - 2017, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

public class LicenseTextToLicenseIdService {
    private static Optional<LicenseTextToLicenseIdService> service = Optional.empty();
    private final ServiceLoader<TextToLicenses> loader;
    private Optional<List<TextToLicenses>> all = Optional.empty();

    private LicenseTextToLicenseIdService() {
        loader = ServiceLoader.load( TextToLicenses.class );
    }

    public static synchronized LicenseTextToLicenseIdService getInstance() {
        if( !service.isPresent() ) {
            service = Optional.of( new LicenseTextToLicenseIdService() );
        }
        //noinspection ConstantConditions
        return service.get();
    }


    public synchronized List<TextToLicenses> getAll() {

        if ( !all.isPresent()) {

            List<TextToLicenses> list = new ArrayList<>();
            try {
                Iterator<TextToLicenses> providers = loader.iterator();
                while( providers.hasNext() ) {
                    list.add( n_( providers.next()));
                }
            } catch( ServiceConfigurationError serviceError ) {
                Log.warn( serviceError.getMessage() );
                return Collections.emptyList();
            }

            all = Optional.of( list );

        }

        return n_(all.get());
    }

}
