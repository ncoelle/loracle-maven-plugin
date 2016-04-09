package de.pfabulist.ianalb.model.oracle;

import de.pfabulist.frex.Frex;
import de.pfabulist.ianalb.model.license.AndLicense;
import de.pfabulist.ianalb.model.license.IBLicense;
import de.pfabulist.ianalb.model.license.Licenses;
import de.pfabulist.ianalb.model.license.OrLicense;
import de.pfabulist.ianalb.model.license.SPDXLicense;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static de.pfabulist.frex.Frex.fullWord;
import static de.pfabulist.frex.Frex.txt;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

public class SPDXParser {

    private final Licenses licenses;

    public SPDXParser( Licenses licenses ) {
        this.licenses = licenses;
    }

    public static class Tok {

        final char typ;
        final Optional<SPDXLicense> content;

        Tok( char typ, Optional<SPDXLicense> content ) {
            this.typ = typ;
            this.content = content;
        }

        public static Tok open() {
            return new Tok( '(', Optional.empty() );
        }

        public static Tok closed() {
            return new Tok( ')', Optional.empty() );
        }

        public static Tok and() {
            return new Tok( 'a', Optional.empty() );
        }

        public static Tok or() {
            return new Tok( 'o', Optional.empty() );
        }

        public static Tok single( SPDXLicense str ) {
            return new Tok( 'c', Optional.of( str ) );
        }

        @Override
        public String toString() {
            return typ + content.toString();
        }
    }

    public SPDXLicense parse( String in ) {
        return liBuilder( tok( in ));
    }

    public Stream<Tok> tok( String in ) {

        List<Tok> ret = new ArrayList<>();
        Pattern first = Frex.any().zeroOrMore().lazy().var( "before" ).
                then( Frex.or( txt( ')' ).var( "closed" ),
                               txt( '(' ).var( "open" ),
                               fullWord( "or" ).var( "or" ),
                               fullWord( "and" ).var( "and" ) ) ).
                then( Frex.any().zeroOrMore().var( "rest" ) ).
                buildCaseInsensitivePattern();

        String rest = in;
        while( true ) {

            Matcher matcher = first.matcher( rest );

            if( !matcher.matches() ) {
                parseSingleSPDX( licenses, ret, rest );
                break;
            }

            parseSingleSPDX( licenses, ret, matcher.group( "before" ) );

            if( matcher.group( "closed" ) != null ) {
                ret.add( Tok.closed() );
            } else if( matcher.group( "open" ) != null ) {
                ret.add( Tok.open() );
            } else if( matcher.group( "or" ) != null ) {
                ret.add( Tok.or() );
            } else if( matcher.group( "and" ) != null ) {
                ret.add( Tok.and() );
            } else {
                throw new IllegalStateException( "huh" );
            }

            rest = matcher.group( "rest" );
        }

        return ret.stream();

    }

    private void parseSingleSPDX( Licenses licenses, List<Tok> ret, String posSPDX ) {
        posSPDX = posSPDX.trim();
        if( !posSPDX.isEmpty() ) {
            IBLicense license = licenses.getOrThrowByName( posSPDX );
            if( license instanceof SPDXLicense ) {
                ret.add( Tok.single( (SPDXLicense) license ) );
            } else {
                throw new IllegalArgumentException( "no SPDX License" );
            }
        }
    }


    public static class Parsed {
        final Optional<SPDXLicense> license;
        final Optional<Character> op;
        final boolean closed;

        public Parsed( Optional<SPDXLicense> license, Optional<Character> op ) {
            this.license = license;
            this.op = op;
            this.closed = false;
        }

        public Parsed( SPDXLicense license, boolean closed )  {
            this.license = Optional.of( license );
            this.op = Optional.empty();
            this.closed = closed;
        }

        public static Parsed start() {
            return new Parsed( Optional.empty(), Optional.empty() );
        }

        public Parsed value( SPDXLicense after, boolean closed ) {
            if( !op.isPresent() ) {
                if( license.isPresent() ) {
                    throw new IllegalArgumentException( "operator missing" );
                }

                return new Parsed( after, closed );
            }

            if( op.get() == 'a' ) {
                if ( license.get() instanceof OrLicense ) {
                    OrLicense or = (OrLicense)license.get();
                    return new Parsed( Optional.of( new OrLicense( or.getLeft(), new AndLicense( or.getRight(), after ) )),
                                       Optional.empty());
                }
                return new Parsed( Optional.of( new AndLicense( license.get(), after ) ), Optional.empty() );
            }

            if( op.get() == 'o' ) {
                return new Parsed( Optional.of( new OrLicense( license.get(), after ) ), Optional.empty() );
            }

            throw new IllegalArgumentException( "huh" );
        }

        public Parsed and() {
            if( op.isPresent() ) {
                throw new IllegalArgumentException( "2 operators" );
            }

            if( !license.isPresent() ) {
                throw new IllegalArgumentException( "no left side for operator" );
            }

            return new Parsed( license, Optional.of( 'a' ) );
        }

        public Parsed or() {
            if( op.isPresent() ) {
                throw new IllegalArgumentException( "2 operators" );
            }

            if( !license.isPresent() ) {
                throw new IllegalArgumentException( "no left side for operator" );
            }

            return new Parsed( license, Optional.of( 'o' ) );
        }
    }

    SPDXLicense liBuilder( Stream<Tok> stream ) {

        Stack<Parsed> stack = new Stack<>();
        stack.push( Parsed.start() );

//        System.out.println( "\n\n\n\n\n" );

        stream.
                //peek( System.out::println ).
                forEach( tok -> {

                    if( tok.typ == '(' ) {
                        stack.push( Parsed.start() );

                    } else if( tok.typ == ')' ) {
                        Parsed ex = stack.pop();
                        if( ex.op.isPresent() ) {
                            throw new IllegalArgumentException( "dangling operator" );
                        }

                        if( !ex.license.isPresent() ) {
                            throw new IllegalArgumentException( "empty brackets" );
                        }

                        Parsed before = stack.pop();
                        stack.push( before.value( ex.license.get(), true ) );

                    } else if( tok.typ == 'c' ) {

                        Parsed before = stack.pop();
                        stack.push( before.value( tok.content.get(), false ) );

                    } else if( tok.typ == 'a' ) {

                        Parsed before = stack.pop();
                        stack.push( before.and() );

                    } else if( tok.typ == 'o' ) {

                        Parsed before = stack.pop();
                        stack.push( before.or() );

                    }
                } );

        Parsed ret = stack.pop();

        if( !stack.isEmpty() ) {
            throw new IllegalStateException( "not all brackets closed" );
        }

        if( ret.op.isPresent() ) {
            throw new IllegalStateException( "dangling operator" );
        }

        return ret.license.get();
    }

}
