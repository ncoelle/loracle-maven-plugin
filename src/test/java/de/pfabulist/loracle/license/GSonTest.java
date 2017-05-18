//package de.pfabulist.loracle.license;
//
//import com.google.gson.GsonBuilder;
//import com.google.gson.JsonDeserializationContext;
//import com.google.gson.JsonDeserializer;
//import com.google.gson.JsonElement;
//import com.google.gson.JsonParseException;
//import org.junit.Test;
//
//import java.lang.reflect.Type;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * Copyright (c) 2006 - 2017, Stephan Pfab
// * SPDX-License-Identifier: BSD-2-Clause
// */
//
//public class GSonTest {
//
//    interface II {
//        String get();
//    }
//
//    public static class One implements II {
//
//        private final String one;
//
//        public One( String one ) {
//            this.one = one;
//        }
//
//        @Override
//        public String get() {
//            return one;
//        }
//
//        @Override
//        public String toString() {
//            return "One{" +
//                    "one='" + one + '\'' +
//                    '}';
//        }
//    }
//
//    public static class Two implements II {
//
//        private final String two;
//
//        public Two( String two ) {
//            this.two = two;
//        }
//
//        @Override
//        public String get() {
//            return "2" + two;
//        }
//
//        @Override
//        public String toString() {
//            return "Two{" +
//                    "two='" + two + '\'' +
//                    '}';
//        }
//    }
//
//    public class Ha {
//        public final Map<String,II> mem;
//
//        public Ha( Map<String, II> mem ) {
//            this.mem = mem;
//        }
//    }
//
//    public static class IIDE implements JsonDeserializer<II> {
//
//        @Override
//        public II deserialize( JsonElement json, Type typeOfT, JsonDeserializationContext context ) throws JsonParseException {
//            String str = json.getAsJsonPrimitive().toString();
//            if ( str.contains( "2" )) {
//                return new Two( str );
//            }
//
//            return new One( str );
//        }
//    }
//
//    @Test
//    public void tt() {
//        GsonBuilder b = new GsonBuilder();
//        b.registerTypeAdapter( II.class, new IIDE() );
//
//        II ret = b.create().fromJson( "22222", II.class );
//
//        System.out.println(ret);
//
//        II ret2 = b.create().fromJson( "fooo", II.class );
//
//        System.out.println(ret2);
//
//        Map<String,II> m  = new HashMap<>();
//        m.put( "111", new One( "aaaa" ) );
//        m.put( "222", new Two( "bb2bb" ) );
//
//        System.out.println( b.create().toJson( m ));
//
//        System.out.println( b.create().toJson( new Ha( m )));
//        System.out.println( new Ha( m ).mem.get( "111" ));
//
//        Map<String,II> back  = b.create().fromJson( b.create().toJson( m ), Map.class );
//
//        System.out.println(back.get( "222" ).getClass());
//
//
//    }
//}
