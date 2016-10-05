package de.pfabulist.loracle.license;

import de.pfabulist.frex.Frex;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static de.pfabulist.frex.Frex.fullWord;
import static de.pfabulist.frex.Frex.txt;
import static de.pfabulist.loracle.license.FuzzyParser.TokTyp.andTok;
import static de.pfabulist.loracle.license.FuzzyParser.TokTyp.closeBracket;
import static de.pfabulist.loracle.license.FuzzyParser.TokTyp.openBracket;
import static de.pfabulist.loracle.license.FuzzyParser.TokTyp.orTok;
import static de.pfabulist.loracle.license.FuzzyParser.TokTyp.text;
import static de.pfabulist.loracle.license.LicenseIDs.isOr;
import static de.pfabulist.roast.NonnullCheck._nn;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

public class FuzzyParser {

    private final LOracle lOracle;

    public FuzzyParser( LOracle lOracle ) {
        this.lOracle = lOracle;
    }

    public enum TokTyp {
        openBracket,
        closeBracket,
        andTok,
        orTok,
        text
    }

    public static class Tok {

        final TokTyp typ;
        final Optional<LicenseID> content;

        Tok( TokTyp typ, Optional<LicenseID> content ) {
            this.typ = typ;
            this.content = content;
        }

        public static Tok open() {
            return new Tok( openBracket, Optional.empty() );
        }

        public static Tok closed() {
            return new Tok( closeBracket, Optional.empty() );
        }

        public static Tok and() {
            return new Tok( andTok, Optional.empty() );
        }

        public static Tok or() {
            return new Tok( orTok, Optional.empty() );
        }

        public static Tok single( LicenseID str ) {
            return new Tok( text, Optional.of( str ) );
        }

        @Override
        public String toString() {
            return typ + content.toString();
        }
    }

    public LicenseID parse( String in ) {
        return liBuilder( tok(  LOracle.trim( in )) );
    }

    private static Pattern first = Frex.any().zeroOrMore().lazy().var( "before" ).
            then( Frex.or( txt( ')' ).var( "closed" ),
                           txt( '(' ).var( "open" ),
                           fullWord( "or" ).var( "or" ),
                           fullWord( "and" ).var( "and" ) ) ).
            then( Frex.any().zeroOrMore().var( "rest" ) ).
            buildCaseInsensitivePattern();

    private Stream<Tok> tok( String in ) {

        List<Tok> ret = new ArrayList<>();

        String rest = in;
        while( true ) {

            Matcher matcher = first.matcher( rest );

            if( !matcher.matches() ) {
                parseSingleSPDX( ret, rest );
                break;
            }

            parseSingleSPDX( ret, _nn( matcher.group( "before" ) ) );

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

            rest = _nn( matcher.group( "rest" ) );
        }

        return ret.stream();

    }

    static Frex word = Frex.or( Frex.alphaNum(), txt( '-' ).or( txt( '.' ) ) ).oneOrMore();
    static Pattern namePattern = Frex.any().oneOrMore().lazy().var( "name" ).
//                    then( Frex.whitespace().zeroOrMore()).
//                    then( Frex.or( Frex.number(), txt('.')).zeroOrMore()).var( "name" ).
//                    then( Frex.whitespace().zeroOrMore()).
                    then( txt( '+' ).var( "plus" ).zeroOrOnce() ).
                    then( Frex.whitespace().zeroOrMore().
                            then( txt( "WITH" ) ).
                            then( Frex.whitespace().zeroOrMore() ).
                            then( word.var( "exception" ) ).zeroOrOnce() ).
                    buildCaseInsensitivePattern();

    public LicenseID getExtended( String nameExpr ) {

        Matcher matcher = namePattern.matcher( nameExpr );
        if( !matcher.matches() ) {
            // todo other licenses
            throw new IllegalArgumentException( "no such license: " + nameExpr );
        }

        String name = _nn( matcher.group( "name" ) );

        LicenseID license = lOracle.getByName1( name).
                orElseThrow( () -> new IllegalArgumentException( "no such license: " + name ) );

        boolean plus = matcher.group( "plus" ) != null;
        Optional<LicenseExclude> exception = Optional.ofNullable( matcher.group( "exception" ) ).
                map( lOracle::getExceptionOrThrow );

        if ( license instanceof SingleLicense ) {
            return lOracle.getOrLater( (SingleLicense) license, plus, exception );
        }

        throw new IllegalArgumentException( "combined license within combined" );
    }

    private void parseSingleSPDX( List<Tok> ret, String posSPDX ) {
        posSPDX = posSPDX.trim();
        if( !posSPDX.isEmpty() ) {

            LicenseID extended = getExtended( posSPDX );
            ret.add( Tok.single( extended ) );
        }
    }

    public static class Parsed {
        final Optional<LicenseID> license;
        final Optional<TokTyp> op;
        final boolean closed;

        public Parsed( Optional<LicenseID> license, Optional<TokTyp> op ) {
            this.license = license;
            this.op = op;
            this.closed = false;
        }

        public Parsed( LicenseID license, boolean closed ) {
            this.license = Optional.of( license );
            this.op = Optional.empty();
            this.closed = closed;
        }

        public static Parsed start() {
            return new Parsed( Optional.empty(), Optional.empty() );
        }

        public Parsed value( LOracle lOracle, LicenseID after, boolean closed ) {
            if( !op.isPresent() ) {
                if( license.isPresent() ) {
                    throw new IllegalArgumentException( "operator missing" );
                }

                return new Parsed( after, closed );
            }

            LicenseID current = license.orElseThrow( () -> new IllegalStateException( "license must be set here" ) );

            if( op.get() == andTok ) {
                if( isOr( current ) ) {
                    CompositeLicense or = (CompositeLicense) current;
                    return new Parsed( Optional.of( lOracle.getOr( or.getLeft(), lOracle.getAnd( or.getRight(), after ) ) ),
                                       Optional.empty() );
                }
                return new Parsed( Optional.of( lOracle.getAnd( current, after ) ), Optional.empty() );
            }

            if( op.get() == orTok ) {
                return new Parsed( Optional.of( lOracle.getOr( current, after ) ), Optional.empty() );
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

            return new Parsed( license, Optional.of( andTok ) );
        }

        public Parsed or() {
            if( op.isPresent() ) {
                throw new IllegalArgumentException( "2 operators" );
            }

            if( !license.isPresent() ) {
                throw new IllegalArgumentException( "no left side for operator" );
            }

            return new Parsed( license, Optional.of( orTok ) );
        }
    }

    LicenseID liBuilder( Stream<Tok> stream ) {

        Stack<Parsed> stack = new Stack<>();
        stack.push( Parsed.start() );

        stream.
                forEach( tok -> {

                    if( tok.typ == openBracket ) {
                        stack.push( Parsed.start() );

                    } else if( tok.typ == closeBracket ) {
                        Parsed ex = _nn( stack.pop() );
                        if( ex.op.isPresent() ) {
                            throw new IllegalArgumentException( "dangling operator" );
                        }

                        Parsed before = _nn( stack.pop() );
                        stack.push( before.value( lOracle,
                                                  ex.license.orElseThrow( () -> new IllegalArgumentException( "empty brackets" ) ),
                                                  true ) );

                    } else if( tok.typ == text ) {

                        Parsed before = _nn( stack.pop() );
                        stack.push( before.value( lOracle,
                                                  tok.content.orElseThrow( () -> new IllegalStateException( "text without content" ) ),
                                                  false ) );

                    } else if( tok.typ == andTok ) {

                        Parsed before = _nn( stack.pop() );
                        stack.push( before.and() );

                    } else if( tok.typ == orTok ) {

                        Parsed before = _nn( stack.pop() );
                        stack.push( before.or() );

                    }
                } );

        Parsed ret = _nn( stack.pop() );

        if( !stack.isEmpty() ) {
            throw new IllegalStateException( "not all brackets closed" );
        }

        if( ret.op.isPresent() ) {
            throw new IllegalStateException( "dangling operator" );
        }

        return ret.license.orElseThrow( () -> new IllegalStateException( "positive parse result without license" ) );
    }

}
