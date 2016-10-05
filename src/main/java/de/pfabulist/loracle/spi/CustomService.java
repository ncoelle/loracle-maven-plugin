package de.pfabulist.loracle.spi;

import com.esotericsoftware.minlog.Log;
import de.pfabulist.loracle.custom.LoracleCustom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;

import static de.pfabulist.roast.NonnullCheck._nn;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */


public class CustomService {

    private static Optional<CustomService> service = Optional.empty();
    private final ServiceLoader<LoracleCustom> loader;
    private Optional<List<LoracleCustom>> all = Optional.empty();

    private CustomService() {
        loader = ServiceLoader.load( LoracleCustom.class );
    }

    public static synchronized CustomService getInstance() {
        if( !service.isPresent() ) {
            service = Optional.of( new CustomService() );
        }
        //noinspection ConstantConditions
        return service.get();
    }


    public synchronized List<LoracleCustom> getAll() {

        if ( !all.isPresent()) {
            List<LoracleCustom> list = new ArrayList<>();
            try {
                Iterator<LoracleCustom> providers = loader.iterator();
                while( providers.hasNext() ) {
                    list.add( _nn( providers.next()));
                }
            } catch( ServiceConfigurationError serviceError ) {
                Log.warn( serviceError.getMessage() );
                return Collections.emptyList();
            }

            all = Optional.of( list );

        }

        return _nn(all.get());
    }

}
