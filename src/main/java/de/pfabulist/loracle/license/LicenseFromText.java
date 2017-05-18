package de.pfabulist.loracle.license;

import de.pfabulist.roast.collection.P;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import static de.pfabulist.roast.NonnullCheck._nn;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

public class LicenseFromText {

    private final LOracle lOracle;
    private final Findings log;

    public LicenseFromText( LOracle lOracle, Findings log ) {
        this.lOracle = lOracle;
        this.log = log;
    }

//    public MappedLicense getLicense( String txt ) {
//        String norm = new Normalizer().norm( txt );
//
//        return byFragments2.stream().
//                map( f -> match( f, norm ) ).
//                filter( Optional::isPresent ).
//                findFirst().map( l -> MappedLicense.of( l, "by full text" ) ).
//                orElse( MappedLicense.empty() );
//
//    }

    // todo exceptions

    public MappedLicense getLicense( String txt ) {
//        List<String> norm = new ArrayList<>();
//        norm.add( new Normalizer().norm( txt ) );

        List<P<String, MappedLicense>> textPieceToLicense = new ArrayList<>();
        textPieceToLicense.add( P.of( new Normalizer().norm( txt ), MappedLicense.empty() ));

        And and = new And( lOracle, log, true );

        return byFragments2.stream().
                map( f -> match3( f, textPieceToLicense ) ).
                filter( Optional::isPresent ).
                map( l -> MappedLicense.of( l, "by full text" ) ).
                reduce( and::and ).
                orElseGet( MappedLicense::empty );

    }

    // todo used ?
//    private Optional<LicenseID> match( SimpleMatch2 simpl, String in ) {
//        int pos = 0;
//        for( int i = 0; i < simpl.getFragments().size(); i++ ) {
//            String frag = _nn( simpl.getFragments().get( i ) );
//            int nextPos = in.indexOf( frag, pos );
//            if( nextPos < 0 || nextPos > pos + _nn( simpl.getMaxAny().get( i ) ) ) {
////                if ( nextPos > 0 ) {
////                    String duh = in.substring( pos );
////                    String foo = in.substring( nextPos );
////                    int g = 0;
////                }
//                return Optional.empty();
//            }
//
//            pos = nextPos + frag.length();
//        }
//
//        return Optional.of( lOracle.getOrThrowByName( simpl.getLicense() ) );
//    }

    public Optional<LicenseID> match3( SimpleMatch2 frag, List<P<String, MappedLicense>> txts ) {

        WorkOnOne<P<String, MappedLicense>> woo = new WorkOnOne<>( txts.stream() );
        List<P<String, MappedLicense>> ll = woo.work( t -> match3( frag, t ),
                                                      ( rr, t ) -> Splitter.cutMiddle( t.i0, rr.i0, rr.i1, lOracle.getByName( frag.getLicense() ) ) );

        if( woo.foundp() ) {
            txts.clear();
            txts.addAll( ll );
            return Optional.of( lOracle.getOrThrowByName( frag.getLicense() ) );
        }

        return Optional.empty();

    }

    public Optional<P<Integer, Integer>> match3( SimpleMatch2 simpl, P<String, MappedLicense> pair ) {

        pair.with( ( t, l ) -> true );

        if( pair.i1.isPresent() ) {
            return Optional.empty();
        }

        String txt = pair.i0;

        int startPos = Integer.MAX_VALUE;
        int pos = 0;
        for( int i = 0; i < simpl.getFragments().size(); i++ ) {
            String frag = _nn( simpl.getFragments().get( i ) );
            int nextPos = txt.indexOf( frag, pos );
            if( nextPos < 0 || nextPos > pos + _nn( simpl.getMaxAny().get( i ) ) ) {
//                if ( nextPos > 0 ) {
//                    String duh = in.substring( pos );
//                    String foo = in.substring( nextPos );
//                    int g = 0;
//                }
                return Optional.empty();
            }

            if( nextPos < startPos ) {
                startPos = nextPos;
            }

            pos = nextPos + frag.length();
        }

        return Optional.of( P.of( startPos, pos ) );
    }

//    private boolean findBSD3( String norm ) {
//        int pos = norm.indexOf( BSD3 );
//        if( pos >= 0 ) {
//            pos = norm.indexOf( BSD3_part2, pos + BSD3.length() );
//            if( pos > -1 ) {
//                return true;
//            }
//        }
//
//        pos = norm.indexOf( BSD3_nonums );
//        if( pos >= 0 ) {
//            pos = norm.indexOf( BSD3_part2, pos + BSD3_nonums.length() );
//            if( pos > -1 ) {
//                return true;
//            }
//        }
//        return false;
//    }

    @SuppressWarnings( "PMD.SystemPrintln" )
    public void firstDiffDetail( String norm, String frag ) {
        int start = norm.indexOf( frag.substring( 0, Math.min( 50, frag.length() ) ) );

        if( start >= 0 ) {
            System.out.println( "  :: matched start" );

            for( int i = 0; i < frag.length(); i++ ) {
                System.out.print( norm.charAt( i + start ) );
                if( norm.charAt( i + start ) != frag.charAt( i ) ) {
                    System.out.println( "\nexpected: <" + frag.charAt( i ) + "> got <" + norm.charAt( i + start ) + ">" );
                    return;
                }
            }

            System.out.println( "\n  :: matched !" );
        } else {
            System.out.println( "  :: start does NOT match: " + frag /* .substring( 0, Math.min( 50, frag.length() ) ) */ );
        }
        System.out.println( "" );
    }

    @SuppressWarnings( "PMD.SystemPrintln" )
    public void firstDiff( String txt ) {
        String norm = new Normalizer().norm( txt );

        byFragments2.forEach( frag -> {
            System.out.println( frag.getLicense() );
            frag.getFragments().forEach( f -> firstDiffDetail( norm, f ) );
            System.out.println( "end: " + frag.getLicense() + "\n" );
        } );
    }

    private static class SimpleMatch2 {
        private final String license;
        private final List<String> fragments = new ArrayList<>();
        private final List<Integer> maxAny = new ArrayList<>();

        private SimpleMatch2( String license, Object... frags ) {
            this.license = license;
            for( int i = -1; i < frags.length - 1; i += 2 ) {
                this.fragments.add( (String) frags[ i + 1 ] );
                this.maxAny.add( i > -1 ? (Integer) frags[ i ] : 10000 );
            }
        }

        public String getLicense() {
            return license;
        }

        public List<String> getFragments() {
            return fragments;
        }

        public List<Integer> getMaxAny() {
            return maxAny;
        }
    }

    private static class Simple {
        private final String license;
        private final Pattern pattern;

        private Simple( String license, Pattern pattern ) {
            this.license = license;
            this.pattern = pattern;
        }

        public String getLicense() {
            return license;
        }

        public Pattern getPattern() {
            return pattern;
        }
    }

    private static final String MIT = "Permission is hereby granted, free of charge, to any person obtaining a " +
            "copy of this software and associated documentation files (the \"Software\"), " +
            "to deal in the Software without restriction, including without limitation " +
            "the rights to use, copy, modify, merge, publish, distribute, sublicense, " +
            "and/or sell copies of the Software, and to permit persons to whom the " +
            "Software is furnished to do so, subject to the following conditions: " +
            "" +
            "The above copyright notice and this permission notice shall be included " +
            "in all copies or substantial portions of the Software. " +
            "" +
            "THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS " +
            "OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF " +
            "MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN " +
            "NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, " +
            "DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR " +
            "OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE " +
            "USE OR OTHER DEALINGS IN THE SOFTWARE.";

//    private static final Pattern BSD3_pat = Frex.txt( "Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met: " ).
//            then( Frex.or( Frex.txt( "1. " ), Frex.txt( "- " ) ).zeroOrOnce() ).
//            then( Frex.txt( "Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer. " ) ).
//            then( Frex.or( Frex.txt( "2. " ), Frex.txt( "- " ) ).zeroOrOnce() ).
//            then( Frex.txt( "Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution. " ) ).
//            then( Frex.or( Frex.txt( "3. " ), Frex.txt( "- " ) ).zeroOrOnce() ).
//            then( Frex.txt( "Neither the name of " ) ).
//            then( Frex.any().atMost( 50 ) ). // the copyright holder nor the names of its contributors
//            then( Frex.txt( "may be used to endorse or promote products derived from this software without specific prior written permission. " ) ).
//            then( Frex.txt( "THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS " ) ).
//            then( Frex.or( Frex.txt( '"' ), Frex.txt( '“' ) ) ).
//            then( Frex.txt( "AS IS" ) ).
//            then( Frex.or( Frex.txt( '"' ), Frex.txt( '“' ) ) ).
//            then( Frex.txt( " AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE." ) ).
//            buildPattern();
//
//    private static final String BSD3 = "Redistribution and use in source and binary forms, with or without " +
//            "modification, are permitted provided that the following conditions " +
//            "are met: " +
//            "1. Redistributions of source code must retain the above copyright " +
//            "notice, this list of conditions and the following disclaimer. " +
//            "2. Redistributions in binary form must reproduce the above copyright " +
//            "notice, this list of conditions and the following disclaimer in the " +
//            "documentation and/or other materials provided with the distribution. " +
//            "3. Neither the name of";
//
//    private static final String BSD3_1 = "Redistribution and use in source and binary forms, with or without " +
//            "modification, are permitted provided that the following conditions " +
//            "are met: ";
//    private static final String BSD3_2 = "Redistributions of source code must retain the above copyright " +
//            "notice, this list of conditions and the following disclaimer. ";
//    private static final String BSD3_3 = "Redistributions in binary form must reproduce the above copyright " +
//            "notice, this list of conditions and the following disclaimer in the " +
//            "documentation and/or other materials provided with the distribution. ";
//    private static final String BSD3_4 = "Neither the name of";
//    private static final String BSD3_5 = "may be used to endorse or promote products derived from this software without specific prior written permission. " +
//            "THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS ";
//    private static final String BSD3_6 = "AS IS ";
//    private static final String BSD3_7 = " AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.";
//
//    private static final String BSD3_nonums = "Redistribution and use in source and binary forms, with or without " +
//            "modification, are permitted provided that the following conditions are met: " +
//            "" +
//            "Redistributions of source code must retain the above copyright notice, this list of " +
//            "conditions and the following disclaimer. " +
//            "" +
//            "Redistributions in binary form must reproduce " +
//            "the above copyright notice, this list of conditions and the following disclaimer in " +
//            "the documentation and/or other materials provided with the distribution. " +
//            "" +
//            "Neither the name of";
//    //Hamcrest nor the names of its contributors "+
//    // "may be used to endorse " +
////            "or promote products derived from this software without specific prior written " +
////            "permission. " +
////            "" +
////            "THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS \"AS IS\" AND ANY " +
////            "EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES " +
////            "OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT " +
////            "SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, " +
////            "INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED " +
////            "TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR " +
////            "BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN " +
////            "CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY " +
////            "WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH " +
////            "DAMAGE.";
//
//    private static String BSD3_part2_2 = "may be used to endorse " +
//            "or promote products derived from this software without specific prior written " +
//            "permission. " +
//            "" +
//            "THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS “AS IS” AND ANY " +
//            "EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES " +
//            "OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT " +
//            "SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, " +
//            "INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED " +
//            "TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR " +
//            "BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN " +
//            "CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY " +
//            "WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH " +
//            "DAMAGE.";
//
//    private static String BSD3_part2 = "may be used to endorse " +
//            "or promote products derived from this software without specific prior written " +
//            "permission. " +
//            "" +
//            "THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS \"AS IS\" AND ANY " +
//            "EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES " +
//            "OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT " +
//            "SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, " +
//            "INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED " +
//            "TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR " +
//            "BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN " +
//            "CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY " +
//            "WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH " +
//            "DAMAGE.";

    private static final String APACHE2 = "Apache License " +
            "Version 2.0, January 2004 " +
            "http://www.apache.org/licenses/ " +
            "" +
            "TERMS AND CONDITIONS FOR USE, REPRODUCTION, AND DISTRIBUTION " +
            "" +
            "1. Definitions. " +
            "" +
            "\"License\" shall mean the terms and conditions for use, reproduction, " +
            "and distribution as defined by Sections 1 through 9 of this document. " +
            "" +
            "\"Licensor\" shall mean the copyright owner or entity authorized by " +
            "the copyright owner that is granting the License. " +
            "" +
            "\"Legal Entity\" shall mean the union of the acting entity and all " +
            "other entities that control, are controlled by, or are under common " +
            "control with that entity. For the purposes of this definition, " +
            "\"control\" means (i) the power, direct or indirect, to cause the " +
            "direction or management of such entity, whether by contract or " +
            "otherwise, or (ii) ownership of fifty percent (50%) or more of the " +
            "outstanding shares, or (iii) beneficial ownership of such entity. " +
            "" +
            "\"You\" (or \"Your\") shall mean an individual or Legal Entity " +
            "exercising permissions granted by this License. " +
            "" +
            "\"Source\" form shall mean the preferred form for making modifications, " +
            "including but not limited to software source code, documentation " +
            "source, and configuration files. " +
            "" +
            "\"Object\" form shall mean any form resulting from mechanical " +
            "transformation or translation of a Source form, including but " +
            "not limited to compiled object code, generated documentation, " +
            "and conversions to other media types. " +
            "" +
            "\"Work\" shall mean the work of authorship, whether in Source or " +
            "Object form, made available under the License, as indicated by a " +
            "copyright notice that is included in or attached to the work " +
            "(an example is provided in the Appendix below). " +
            "" +
            "\"Derivative Works\" shall mean any work, whether in Source or Object " +
            "form, that is based on (or derived from) the Work and for which the " +
            "editorial revisions, annotations, elaborations, or other modifications " +
            "represent, as a whole, an original work of authorship. For the purposes " +
            "of this License, Derivative Works shall not include works that remain " +
            "separable from, or merely link (or bind by name) to the interfaces of, " +
            "the Work and Derivative Works thereof. " +
            "" +
            "\"Contribution\" shall mean any work of authorship, including " +
            "the original version of the Work and any modifications or additions " +
            "to that Work or Derivative Works thereof, that is intentionally " +
            "submitted to Licensor for inclusion in the Work by the copyright owner " +
            "or by an individual or Legal Entity authorized to submit on behalf of " +
            "the copyright owner. For the purposes of this definition, \"submitted\" " +
            "means any form of electronic, verbal, or written communication sent " +
            "to the Licensor or its representatives, including but not limited to " +
            "communication on electronic mailing lists, source code control systems, " +
            "and issue tracking systems that are managed by, or on behalf of, the " +
            "Licensor for the purpose of discussing and improving the Work, but " +
            "excluding communication that is conspicuously marked or otherwise " +
            "designated in writing by the copyright owner as \"Not a Contribution.\" " +
            "" +
            "\"Contributor\" shall mean Licensor and any individual or Legal Entity " +
            "on behalf of whom a Contribution has been received by Licensor and " +
            "subsequently incorporated within the Work. " +
            "" +
            "2. Grant of Copyright License. Subject to the terms and conditions of " +
            "this License, each Contributor hereby grants to You a perpetual, " +
            "worldwide, non-exclusive, no-charge, royalty-free, irrevocable " +
            "copyright license to reproduce, prepare Derivative Works of, " +
            "publicly display, publicly perform, sublicense, and distribute the " +
            "Work and such Derivative Works in Source or Object form. " +
            "" +
            "3. Grant of Patent License. Subject to the terms and conditions of " +
            "this License, each Contributor hereby grants to You a perpetual, " +
            "worldwide, non-exclusive, no-charge, royalty-free, irrevocable " +
            "(except as stated in this section) patent license to make, have made, " +
            "use, offer to sell, sell, import, and otherwise transfer the Work, " +
            "where such license applies only to those patent claims licensable " +
            "by such Contributor that are necessarily infringed by their " +
            "Contribution(s) alone or by combination of their Contribution(s) " +
            "with the Work to which such Contribution(s) was submitted. If You " +
            "institute patent litigation against any entity (including a " +
            "cross-claim or counterclaim in a lawsuit) alleging that the Work " +
            "or a Contribution incorporated within the Work constitutes direct " +
            "or contributory patent infringement, then any patent licenses " +
            "granted to You under this License for that Work shall terminate " +
            "as of the date such litigation is filed. " +
            "" +
            "4. Redistribution. You may reproduce and distribute copies of the " +
            "Work or Derivative Works thereof in any medium, with or without " +
            "modifications, and in Source or Object form, provided that You " +
            "meet the following conditions: " +
            "" +
            "(a) You must give any other recipients of the Work or " +
            "Derivative Works a copy of this License; and " +
            "" +
            "(b) You must cause any modified files to carry prominent notices " +
            "stating that You changed the files; and " +
            "" +
            "(c) You must retain, in the Source form of any Derivative Works " +
            "that You distribute, all copyright, patent, trademark, and " +
            "attribution notices from the Source form of the Work, " +
            "excluding those notices that do not pertain to any part of " +
            "the Derivative Works; and " +
            "" +
            "(d) If the Work includes a \"NOTICE\" text file as part of its " +
            "distribution, then any Derivative Works that You distribute must " +
            "include a readable copy of the attribution notices contained " +
            "within such NOTICE file, excluding those notices that do not " +
            "pertain to any part of the Derivative Works, in at least one " +
            "of the following places: within a NOTICE text file distributed " +
            "as part of the Derivative Works; within the Source form or " +
            "documentation, if provided along with the Derivative Works; or, " +
            "within a display generated by the Derivative Works, if and " +
            "wherever such third-party notices normally appear. The contents " +
            "of the NOTICE file are for informational purposes only and " +
            "do not modify the License. You may add Your own attribution " +
            "notices within Derivative Works that You distribute, alongside " +
            "or as an addendum to the NOTICE text from the Work, provided " +
            "that such additional attribution notices cannot be construed " +
            "as modifying the License. " +
            "" +
            "You may add Your own copyright statement to Your modifications and " +
            "may provide additional or different license terms and conditions " +
            "for use, reproduction, or distribution of Your modifications, or " +
            "for any such Derivative Works as a whole, provided Your use, " +
            "reproduction, and distribution of the Work otherwise complies with " +
            "the conditions stated in this License. " +
            "" +
            "5. Submission of Contributions. Unless You explicitly state otherwise, " +
            "any Contribution intentionally submitted for inclusion in the Work " +
            "by You to the Licensor shall be under the terms and conditions of " +
            "this License, without any additional terms or conditions. " +
            "Notwithstanding the above, nothing herein shall supersede or modify " +
            "the terms of any separate license agreement you may have executed " +
            "with Licensor regarding such Contributions. " +
            "" +
            "6. Trademarks. This License does not grant permission to use the trade " +
            "names, trademarks, service marks, or product names of the Licensor, " +
            "except as required for reasonable and customary use in describing the " +
            "origin of the Work and reproducing the content of the NOTICE file. " +
            "" +
            "7. Disclaimer of Warranty. Unless required by applicable law or " +
            "agreed to in writing, Licensor provides the Work (and each " +
            "Contributor provides its Contributions) on an \"AS IS\" BASIS, " +
            "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or " +
            "implied, including, without limitation, any warranties or conditions " +
            "of TITLE, NON-INFRINGEMENT, MERCHANTABILITY, or FITNESS FOR A " +
            "PARTICULAR PURPOSE. You are solely responsible for determining the " +
            "appropriateness of using or redistributing the Work and assume any " +
            "risks associated with Your exercise of permissions under this License. " +
            "" +
            "8. Limitation of Liability. In no event and under no legal theory, " +
            "whether in tort (including negligence), contract, or otherwise, " +
            "unless required by applicable law (such as deliberate and grossly " +
            "negligent acts) or agreed to in writing, shall any Contributor be " +
            "liable to You for damages, including any direct, indirect, special, " +
            "incidental, or consequential damages of any character arising as a " +
            "result of this License or out of the use or inability to use the " +
            "Work (including but not limited to damages for loss of goodwill, " +
            "work stoppage, computer failure or malfunction, or any and all " +
            "other commercial damages or losses), even if such Contributor " +
            "has been advised of the possibility of such damages. " +
            "" +
            "9. Accepting Warranty or Additional Liability. While redistributing " +
            "the Work or Derivative Works thereof, You may choose to offer, " +
            "and charge a fee for, acceptance of support, warranty, indemnity, " +
            "or other liability obligations and/or rights consistent with this " +
            "License. However, in accepting such obligations, You may act only " +
            "on Your own behalf and on Your sole responsibility, not on behalf " +
            "of any other Contributor, and only if You agree to indemnify, " +
            "defend, and hold each Contributor harmless for any liability " +
            "incurred by, or claims asserted against, such Contributor by reason " +
            "of your accepting any such warranty or additional liability. " +
            "" +
            "END OF TERMS AND CONDITIONS";

    private final static String EPL10 = "Eclipse Public License - v 1.0 " +
            "" +
            "THE ACCOMPANYING PROGRAM IS PROVIDED UNDER THE TERMS OF THIS ECLIPSE PUBLIC " +
            "LICENSE (\"AGREEMENT\"). ANY USE, REPRODUCTION OR DISTRIBUTION OF THE PROGRAM " +
            "CONSTITUTES RECIPIENT'S ACCEPTANCE OF THIS AGREEMENT. " +
            "" +
            "1. DEFINITIONS " +
            "" +
            "\"Contribution\" means: " +
            "" +
            "a) in the case of the initial Contributor, the initial code and " +
            "  documentation distributed under this Agreement, and " +
            "b) in the case of each subsequent Contributor: " +
            "" +
            "i) changes to the Program, and " +
            "" +
            "ii) additions to the Program; " +
            "" +
            "where such changes and/or additions to the Program originate from and are " +
            "distributed by that particular Contributor. A Contribution 'originates' from a " +
            "Contributor if it was added to the Program by such Contributor itself or anyone " +
            "acting on such Contributor's behalf. Contributions do not include additions to " +
            "the Program which: (i) are separate modules of software distributed in " +
            "conjunction with the Program under their own license agreement, and (ii) are " +
            "not derivative works of the Program. " +
            "" +
            "\"Contributor\" means any person or entity that distributes the Program. " +
            "" +
            "\"Licensed Patents \" mean patent claims licensable by a Contributor which are " +
            "necessarily infringed by the use or sale of its Contribution alone or when " +
            "combined with the Program. " +
            "" +
            "\"Program\" means the Contributions distributed in accordance with this Agreement. " +
            "" +
            "\"Recipient\" means anyone who receives the Program under this Agreement, " +
            "including all Contributors. " +
            "" +
            "2. GRANT OF RIGHTS " +
            "" +
            "a) Subject to the terms of this Agreement, each Contributor hereby grants " +
            "Recipient a non-exclusive, worldwide, royalty-free copyright license to " +
            "reproduce, prepare derivative works of, publicly display, publicly perform, " +
            "distribute and sublicense the Contribution of such Contributor, if any, and " +
            "such derivative works, in source code and object code form. " +
            "" +
            "b) Subject to the terms of this Agreement, each Contributor hereby grants " +
            "Recipient a non-exclusive, worldwide, royalty-free patent license under " +
            "Licensed Patents to make, use, sell, offer to sell, import and otherwise " +
            "transfer the Contribution of such Contributor, if any, in source code and " +
            "object code form. This patent license shall apply to the combination of the " +
            "Contribution and the Program if, at the time the Contribution is added by the " +
            "Contributor, such addition of the Contribution causes such combination to be " +
            "covered by the Licensed Patents. The patent license shall not apply to any " +
            "other combinations which include the Contribution. No hardware per se is " +
            "licensed hereunder. " +
            "" +
            "c) Recipient understands that although each Contributor grants the " +
            "licenses to its Contributions set forth herein, no assurances are provided by " +
            "any Contributor that the Program does not infringe the patent or other " +
            "intellectual property rights of any other entity. Each Contributor disclaims " +
            "any liability to Recipient for claims brought by any other entity based on " +
            "infringement of intellectual property rights or otherwise. As a condition to " +
            "exercising the rights and licenses granted hereunder, each Recipient hereby " +
            "assumes sole responsibility to secure any other intellectual property rights " +
            "needed, if any. For example, if a third party patent license is required to " +
            "allow Recipient to distribute the Program, it is Recipient's responsibility to " +
            "acquire that license before distributing the Program. " +
            "" +
            "d) Each Contributor represents that to its knowledge it has sufficient " +
            "copyright rights in its Contribution, if any, to grant the copyright license " +
            "set forth in this Agreement. " +
            "" +
            "3. REQUIREMENTS " +
            "" +
            "A Contributor may choose to distribute the Program in object code form under " +
            "its own license agreement, provided that: " +
            "" +
            "a) it complies with the terms and conditions of this Agreement; and " +
            "" +
            "b) its license agreement: " +
            "" +
            "i) effectively disclaims on behalf of all Contributors all warranties and " +
            "conditions, express and implied, including warranties or conditions of title " +
            "and non-infringement, and implied warranties or conditions of merchantability " +
            "and fitness for a particular purpose; " +
            "" +
            "ii) effectively excludes on behalf of all Contributors all liability for " +
            "damages, including direct, indirect, special, incidental and consequential " +
            "damages, such as lost profits; " +
            "" +
            "iii) states that any provisions which differ from this Agreement are " +
            "offered by that Contributor alone and not by any other party; and " +
            "" +
            "iv) states that source code for the Program is available from such " +
            "Contributor, and informs licensees how to obtain it in a reasonable manner on " +
            "or through a medium customarily used for software exchange. " +
            "" +
            "When the Program is made available in source code form: " +
            "" +
            "a) it must be made available under this Agreement; and " +
            "" +
            "b) a copy of this Agreement must be included with each copy of the " +
            "Program. " +
            "" +
            "Contributors may not remove or alter any copyright notices contained within the " +
            "Program. " +
            "" +
            "Each Contributor must identify itself as the originator of its Contribution, if " +
            "any, in a manner that reasonably allows subsequent Recipients to identify the " +
            "originator of the Contribution. " +
            "" +
            "4. COMMERCIAL DISTRIBUTION " +
            "" +
            "Commercial distributors of software may accept certain responsibilities with " +
            "respect to end users, business partners and the like. While this license is " +
            "intended to facilitate the commercial use of the Program, the Contributor who " +
            "includes the Program in a commercial product offering should do so in a manner " +
            "which does not create potential liability for other Contributors. Therefore, if " +
            "a Contributor includes the Program in a commercial product offering, such " +
            "Contributor (\"Commercial Contributor\") hereby agrees to defend and indemnify " +
            "every other Contributor (\"Indemnified Contributor\") against any losses, damages " +
            "and costs (collectively \"Losses\") arising from claims, lawsuits and other legal " +
            "actions brought by a third party against the Indemnified Contributor to the " +
            "extent caused by the acts or omissions of such Commercial Contributor in " +
            "connection with its distribution of the Program in a commercial product " +
            "offering. The obligations in this section do not apply to any claims or Losses " +
            "relating to any actual or alleged intellectual property infringement. In order " +
            "to qualify, an Indemnified Contributor must: a) promptly notify the Commercial " +
            "Contributor in writing of such claim, and b) allow the Commercial Contributor " +
            "to control, and cooperate with the Commercial Contributor in, the defense and " +
            "any related settlement negotiations. The Indemnified Contributor may " +
            "participate in any such claim at its own expense. " +
            "" +
            "For example, a Contributor might include the Program in a commercial product " +
            "offering, Product X. That Contributor is then a Commercial Contributor. If that " +
            "Commercial Contributor then makes performance claims, or offers warranties " +
            "related to Product X, those performance claims and warranties are such " +
            "Commercial Contributor's responsibility alone. Under this section, the " +
            "Commercial Contributor would have to defend claims against the other " +
            "Contributors related to those performance claims and warranties, and if a court " +
            "requires any other Contributor to pay any damages as a result, the Commercial " +
            "Contributor must pay those damages. " +
            "" +
            "5. NO WARRANTY " +
            "" +
            "EXCEPT AS EXPRESSLY SET FORTH IN THIS AGREEMENT, THE PROGRAM IS PROVIDED ON AN " +
            "\"AS IS\" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, EITHER EXPRESS OR " +
            "IMPLIED INCLUDING, WITHOUT LIMITATION, ANY WARRANTIES OR CONDITIONS OF TITLE, " +
            "NON-INFRINGEMENT, MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE. Each " +
            "Recipient is solely responsible for determining the appropriateness of using " +
            "and distributing the Program and assumes all risks associated with its exercise " +
            "of rights under this Agreement, including but not limited to the risks and " +
            "costs of program errors, compliance with applicable laws, damage to or loss of " +
            "data, programs or equipment, and unavailability or interruption of operations. " +
            "" +
            "6. DISCLAIMER OF LIABILITY " +
            "" +
            "EXCEPT AS EXPRESSLY SET FORTH IN THIS AGREEMENT, NEITHER RECIPIENT NOR ANY " +
            "CONTRIBUTORS SHALL HAVE ANY LIABILITY FOR ANY DIRECT, INDIRECT, INCIDENTAL, " +
            "SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING WITHOUT LIMITATION LOST " +
            "PROFITS), HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, " +
            "STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY " +
            "WAY OUT OF THE USE OR DISTRIBUTION OF THE PROGRAM OR THE EXERCISE OF ANY RIGHTS " +
            "GRANTED HEREUNDER, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGES. " +
            "" +
            "7. GENERAL " +
            "" +
            "If any provision of this Agreement is invalid or unenforceable under applicable " +
            "law, it shall not affect the validity or enforceability of the remainder of the " +
            "terms of this Agreement, and without further action by the parties hereto, such " +
            "provision shall be reformed to the minimum extent necessary to make such " +
            "provision valid and enforceable. " +
            "" +
            "If Recipient institutes patent litigation against any " +
            "entity (including a cross-claim or counterclaim in a lawsuit) alleging that the " +
            "Program itself (excluding combinations of the Program with other software or " +
            "hardware) infringes such Recipient's patent(s), then such Recipient's rights " +
            "granted under Section 2(b) shall terminate as of the date such litigation is " +
            "filed. " +
            "" +
            "All Recipient's rights under this Agreement shall terminate if it fails to " +
            "comply with any of the material terms or conditions of this Agreement and does " +
            "not cure such failure in a reasonable period of time after becoming aware of " +
            "such noncompliance. If all Recipient's rights under this Agreement terminate, " +
            "Recipient agrees to cease use and distribution of the Program as soon as " +
            "reasonably practicable. However, Recipient's obligations under this Agreement " +
            "and any licenses granted by Recipient relating to the Program shall continue " +
            "and survive. " +
            "" +
            "Everyone is permitted to copy and distribute copies of this Agreement, but in " +
            "order to avoid inconsistency the Agreement is copyrighted and may only be " +
            "modified in the following manner. The Agreement Steward reserves the right to " +
            "publish new versions (including revisions) of this Agreement from time to time. " +
            "No one other than the Agreement Steward has the right to modify this Agreement. " +
            "The Eclipse Foundation is the initial Agreement Steward. The Eclipse Foundation may assign the responsibility to " +
            "serve as the Agreement Steward to a suitable separate entity. Each new version " +
            "of the Agreement will be given a distinguishing version number. The Program " +
            "(including Contributions) may always be distributed subject to the version of " +
            "the Agreement under which it was received. In addition, after a new version of " +
            "the Agreement is published, Contributor may elect to distribute the Program " +
            "(including its Contributions) under the new version. Except as expressly stated " +
            "in Sections 2(a) and 2(b) above, Recipient receives no rights or licenses to " +
            "the intellectual property of any Contributor under this Agreement, whether " +
            "expressly, by implication, estoppel or otherwise. All rights in the Program not " +
            "expressly granted under this Agreement are reserved. " +
            "" +
            "This Agreement is governed by the laws of the State of New York and the " +
            "intellectual property laws of the United States of America. No party to this " +
            "Agreement will bring a legal action under this Agreement more than one year " +
            "after the cause of action arose. Each party waives its rights to a jury trial " +
            "in any resulting litigation";

    private final static String LGPL21_preamble = "GNU LESSER GENERAL PUBLIC LICENSE " +
            "Version 2.1, February 1999 " +
            "" +
            "Copyright (C) 1991, 1999 Free Software Foundation, Inc. " +
            "51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA " +
            "Everyone is permitted to copy and distribute verbatim copies " +
            "of this license document, but changing it is not allowed. " +
            "" +
            "[This is the first released version of the Lesser GPL. It also counts " +
            "as the successor of the GNU Library Public License, version 2, hence " +
            "the version number 2.1.] " +
            "" +
            "Preamble " +
            "" +
            "The licenses for most software are designed to take away your " +
            "freedom to share and change it. By contrast, the GNU General Public " +
            "Licenses are intended to guarantee your freedom to share and change " +
            "free software--to make sure the software is free for all its users. " +
            "" +
            "This license, the Lesser General Public License, applies to some " +
            "specially designated software packages--typically libraries--of the " +
            "Free Software Foundation and other authors who decide to use it. You " +
            "can use it too, but we suggest you first think carefully about whether " +
            "this license or the ordinary General Public License is the better " +
            "strategy to use in any particular case, based on the explanations below. " +
            "" +
            "When we speak of free software, we are referring to freedom of use, " +
            "not price. Our General Public Licenses are designed to make sure that " +
            "you have the freedom to distribute copies of free software (and charge " +
            "for this service if you wish); that you receive source code or can get " +
            "it if you want it; that you can change the software and use pieces of " +
            "it in new free programs; and that you are informed that you can do " +
            "these things. " +
            "" +
            "To protect your rights, we need to make restrictions that forbid " +
            "distributors to deny you these rights or to ask you to surrender these " +
            "rights. These restrictions translate to certain responsibilities for " +
            "you if you distribute copies of the library or if you modify it. " +
            "" +
            "For example, if you distribute copies of the library, whether gratis " +
            "or for a fee, you must give the recipients all the rights that we gave " +
            "you. You must make sure that they, too, receive or can get the source " +
            "code. If you link other code with the library, you must provide " +
            "complete object files to the recipients, so that they can relink them " +
            "with the library after making changes to the library and recompiling " +
            "it. And you must show them these terms so they know their rights. " +
            "" +
            "We protect your rights with a two-step method: (1) we copyright the " +
            "library, and (2) we offer you this license, which gives you legal " +
            "permission to copy, distribute and/or modify the library. " +
            "" +
            "To protect each distributor, we want to make it very clear that " +
            "there is no warranty for the free library. Also, if the library is " +
            "modified by someone else and passed on, the recipients should know " +
            "that what they have is not the original version, so that the original " +
            "author's reputation will not be affected by problems that might be " +
            "introduced by others. " +
            "\f " +
            "Finally, software patents pose a constant threat to the existence of " +
            "any free program. We wish to make sure that a company cannot " +
            "effectively restrict the users of a free program by obtaining a " +
            "restrictive license from a patent holder. Therefore, we insist that " +
            "any patent license obtained for a version of the library must be " +
            "consistent with the full freedom of use specified in this license. " +
            "" +
            "Most GNU software, including some libraries, is covered by the " +
            "ordinary GNU General Public License. This license, the GNU Lesser " +
            "General Public License, applies to certain designated libraries, and " +
            "is quite different from the ordinary General Public License. We use " +
            "this license for certain libraries in order to permit linking those " +
            "libraries into non-free programs. " +
            "" +
            "When a program is linked with a library, whether statically or using " +
            "a shared library, the combination of the two is legally speaking a " +
            "combined work, a derivative of the original library. The ordinary " +
            "General Public License therefore permits such linking only if the " +
            "entire combination fits its criteria of freedom. The Lesser General " +
            "Public License permits more lax criteria for linking other code with " +
            "the library. " +
            "" +
            "We call this license the \"Lesser\" General Public License because it " +
            "does Less to protect the user's freedom than the ordinary General " +
            "Public License. It also provides other free software developers Less " +
            "of an advantage over competing non-free programs. These disadvantages " +
            "are the reason we use the ordinary General Public License for many " +
            "libraries. However, the Lesser license provides advantages in certain " +
            "special circumstances. " +
            "" +
            "For example, on rare occasions, there may be a special need to " +
            "encourage the widest possible use of a certain library, so that it becomes " +
            "a de-facto standard. To achieve this, non-free programs must be " +
            "allowed to use the library. A more frequent case is that a free " +
            "library does the same job as widely used non-free libraries. In this " +
            "case, there is little to gain by limiting the free library to free " +
            "software only, so we use the Lesser General Public License. " +
            "" +
            "In other cases, permission to use a particular library in non-free " +
            "programs enables a greater number of people to use a large body of " +
            "free software. For example, permission to use the GNU C Library in " +
            "non-free programs enables many more people to use the whole GNU " +
            "operating system, as well as its variant, the GNU/Linux operating " +
            "system. " +
            "" +
            "Although the Lesser General Public License is Less protective of the " +
            "users' freedom, it does ensure that the user of a program that is " +
            "linked with the Library has the freedom and the wherewithal to run " +
            "that program using a modified version of the Library. " +
            "" +
            "The precise terms and conditions for copying, distribution and " +
            "modification follow. Pay close attention to the difference between a " +
            "\"work based on the library\" and a \"work that uses the library\". The " +
            "former contains code derived from the library, whereas the latter must " +
            "be combined with the library in order to run";

    private final static String APACHE11_head = "The Apache Software License, Version 1.1 " +
            "" +
            "Copyright (c) 2000-2002 The Apache Software Foundation. All rights " +
            "reserved. " +
            "" +
            "Redistribution and use in source and binary forms, with or without " +
            "modification, are permitted provided that the following conditions " +
            "are met: " +
            "" +
            "1. Redistributions of source code must retain the above copyright " +
            "notice, this list of conditions and the following disclaimer. " +
            "" +
            "2. Redistributions in binary form must reproduce the above copyright " +
            "notice, this list of conditions and the following disclaimer in " +
            "the documentation and/or other materials provided with the " +
            "distribution. " +
            "" +
            "3. The end-user documentation included with the redistribution, " +
            "if any, must include the following acknowledgment:";

    private final static String APACHE11_head2 = "The Apache Software License, Version 1.1 " +
            "" +
            "Copyright (c) 2000 The Apache Software Foundation. All rights " +
            "reserved. " +
            "" +
            "Redistribution and use in source and binary forms, with or without " +
            "modification, are permitted provided that the following conditions " +
            "are met: " +
            "" +
            "1. Redistributions of source code must retain the above copyright " +
            "notice, this list of conditions and the following disclaimer. " +
            "" +
            "2. Redistributions in binary form must reproduce the above copyright " +
            "notice, this list of conditions and the following disclaimer in " +
            "the documentation and/or other materials provided with the " +
            "distribution";

//    private final static List<SimpleMatch> byFragments = Arrays.asList(
//            new SimpleMatch( "bsd-3-clause", BSD3, BSD3_part2 ),
//            new SimpleMatch( "bsd-3-clause", BSD3_nonums, BSD3_part2 ),
//            new SimpleMatch( "bsd-3-clause", BSD3_nonums, BSD3_part2_2 ),
//            new SimpleMatch( "apache-2", APACHE2 ),
//            new SimpleMatch( "epl-1.0", EPL10 ),
//            new SimpleMatch( "lgpl-2.1", LGPL21_preamble ),
//            new SimpleMatch( "apache-1.1", APACHE11_head ),
//            new SimpleMatch( "apache-1.1", APACHE11_head2 )
//    );

    private final static List<SimpleMatch2> byFragments2 = Arrays.asList(
            new SimpleMatch2( "mit", MIT ),
            new SimpleMatch2( "bsd-3-clause",
                              "Redistribution and use in source and binary forms, with or without " +
                                      "modification, are permitted provided that the following conditions " +
                                      "are met: ",
                              3, // 1. - *
                              "Redistributions of source code must retain the above copyright " +
                                      "notice, this list of conditions and the following disclaimer. ",
                              3,
                              "Redistributions in binary form must reproduce the above copyright " +
                                      "notice, this list of conditions and the following disclaimer in the " +
                                      "documentation and/or other materials provided with the distribution. ",
                              15,
                              /* Neither t*/"he name", // its either: "the name may not be used" or "neither the ... may be used"
                              // the names of
                              150, // organization /*"may "*/
                              "be used to endorse or promote products derived from this software without ",
                              12, // specific
                              "prior written permission. ",
                              67, // For written permission, please contact clarkware@clarkware.com.
                              "THIS SOFTWARE IS PROVIDED ",  // BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS ", or "AUTHOR"
                              150,
                              "AS IS",
                              2, // " or variants, e.g. ''
                              " AND ANY EXPRESS", // ed
                              3,
                              "OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO," +
                                      " THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. " +
                                      "IN NO EVENT SHALL ",
                              150,  //THE COPYRIGHT", 10, OR CONTRIBUTORS ,    or AUTHOR
                              "BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE." ),
//            new SimpleMatch( "bsd-3-clause", BSD3_nonums, BSD3_part2 ),
//            new SimpleMatch( "bsd-3-clause", BSD3_nonums, BSD3_part2_2 ),
            new SimpleMatch2( "apache-2", APACHE2 ),
            new SimpleMatch2( "epl-1.0", EPL10 ),
            new SimpleMatch2( "lgpl-2.1", LGPL21_preamble ),
            new SimpleMatch2( "apache-1.1", APACHE11_head ),
            new SimpleMatch2( "apache-1.1", APACHE11_head2 )
    );

//    private final static List<Simple> byPatterns = Arrays.asList(
//            new Simple( "mit", Frex.txt( MIT ).buildPattern() ),
//            new Simple( "bsd-3-clause", BSD3_pat )
//
//    );

}
