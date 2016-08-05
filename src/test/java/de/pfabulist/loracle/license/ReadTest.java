package de.pfabulist.loracle.license;

import com.google.gson.Gson;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

public class ReadTest {

//    @Test
//    public void foo() {
//
//        List<String> alias = Arrays.asList( "foo", "bar" );
//
//        System.out.println( new Gson().toJson( IANALicense.simple( Optional.of( "BSD" ), alias ) ));
//        System.out.println( new Gson().fromJson( new Gson().toJson( IANALicense.simple( Optional.of( "BSD" ), alias ) ), IANALicense.class ));
//
//        List<IANALicense> l = new ArrayList<>();
//        l.add( IANALicense.simple( Optional.of( "BSD" ), alias ) );
//        System.out.println( new Gson().toJson( l ));
//
//        System.out.println( new Gson().fromJson( new Gson().toJson( l ), ArrayList.class ).get( 0 ).getClass());
//
////        * <pre>
////        * Type listType = new TypeToken&lt;List&lt;String&gt;&gt;() {}.getType();
////        * List&lt;String&gt; target = new LinkedList&lt;String&gt;();
////        * target.add("blah");
//
//        Type type = new TypeToken<List<IANALicense>>(){}.getType();
//
//        List<IANALicense> ll = new Gson().fromJson( new Gson().toJson( l ), type );
//        System.out.println( ll.get( 0 ).getClass());
//        System.out.println( ll.get( 0 ).getLongNames().getClass());
//
//
//
//
//    }

    public interface A {

    }

    public static class B implements A {
        private final int b;

        public B( int b ) {
            this.b = b;
        }
    }

    public static class C implements A {
        private final String c;

        public C( String c ) {
            this.c = c;
        }
    }


//    @Test
//    public void testDervied() {
//        List<A> l = new ArrayList<>();
//
//        l.add( new B( 5 ) );
//        l.add( new C( "foo" ) );
//
//        System.out.println( new Gson().toJson( l ));
//
//        Type type = new TypeToken<List<A>>(){}.getType();
//        List<A> ll = new Gson().fromJson( new Gson().toJson( l ), type );
//
//        System.out.println(ll.get(0));
//    }

    @Test
    public void testPrivate() {
        System.out.println( new Gson().toJson( new SingleLicense( "foo" ) ));
        System.out.println( new Gson().fromJson( new Gson().toJson( new SingleLicense( "foo" ) ), SingleLicense.class ));


        Map<String,Integer> m = new HashMap<>();
        m.put( "a",1 );
        m.put( "ccc", 42  );

        System.out.println( new Gson().toJson( m ));
    }
}
