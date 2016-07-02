package de.pfabulist.loracle.license;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Copyright (c) 2006 - 2016, Stephan Pfab
 * SPDX-License-Identifier: BSD-2-Clause
 */

public class LicenseFromText {

    private final LOracle lOracle;

    public LicenseFromText( LOracle lOracle ) {
        this.lOracle = lOracle;
    }

    public MappedLicense getLicense( String txt ) {
        String norm = norm( txt );
        if ( norm.contains( MIT )) {
            return MappedLicense.of( lOracle.getOrThrowByName( "mit" ), "by full text" );
        }
        if ( norm.contains( BSD3 )) {
            return MappedLicense.of( lOracle.getOrThrowByName( "bsd-3-clause" ), "by full text" );
        }
        if ( norm.contains( BSD3_nonums )) {
            return MappedLicense.of( lOracle.getOrThrowByName( "bsd-3-clause" ), "by full text" );
        }
        if ( norm.contains( APACHE2 )) {
            return MappedLicense.of( lOracle.getOrThrowByName( "apache-2" ), "by full text" );
        }
        if ( norm.contains( EPL10 )) {
            return MappedLicense.of( lOracle.getOrThrowByName( "epl-1.0" ), "by full text" );
        }
        if ( norm.contains( LGPL21_preamble )) {
            return MappedLicense.of( lOracle.getOrThrowByName( "lgpl-2.1" ), "by full text" );
        }
        if ( norm.contains( APACHE11_head )) {
            return MappedLicense.of( lOracle.getOrThrowByName( "apache-1.1" ), "by full text" );
        }
        return MappedLicense.empty();
    }

    private String norm( String txt ) {
        return Arrays.stream( txt.split( "\n" ) ).
                map( l -> {
                    String str = l.replace( "\r", "" ).trim();
                    if( str.startsWith( "*" ) ) {
                        return str.substring( 1 ).trim();
                    } else if( str.startsWith( "//" ) ) {
                        return str.substring( 2 ).trim();
                    } else if( str.startsWith( "!" ) ) {
                        return str.substring( 1 ).trim();
                    } else {
                        return str;
                    }
                } ).
                collect( Collectors.joining( "\n" ) );
    }

    @SuppressWarnings( "PMD.SystemPrintln" )
    public int firstDiff( String txt ) {
        String norm = norm( txt );

        for ( int i = 0; i < APACHE2.length(); i++ ) {
            System.out.print(norm.charAt( i + 1));
            if ( norm.charAt( i + 1) != APACHE2.charAt( i )) {
                return i;
            }
        }

//        int pos = norm.indexOf( "Redistribution" );
//        if ( pos < 1) {
//            return -1;
//        }
//
//        for ( int i = 0; i < BSD3.length(); i++ ) {
//            System.out.print(norm.charAt( i + pos));
//            if ( norm.charAt( i + pos) != BSD3.charAt( i )) {
//                return i;
//            }
//        }
//
//
        return -1;
    }

    private static final String MIT = "Permission is hereby granted, free of charge, to any person obtaining a\n" +
            "copy of this software and associated documentation files (the \"Software\"),\n" +
            "to deal in the Software without restriction, including without limitation\n" +
            "the rights to use, copy, modify, merge, publish, distribute, sublicense,\n" +
            "and/or sell copies of the Software, and to permit persons to whom the\n" +
            "Software is furnished to do so, subject to the following conditions:\n" +
            "\n" +
            "The above copyright notice and this permission notice shall be included\n" +
            "in all copies or substantial portions of the Software.\n" +
            "\n" +
            "THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS\n" +
            "OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF\n" +
            "MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN\n" +
            "NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,\n" +
            "DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR\n" +
            "OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE\n" +
            "USE OR OTHER DEALINGS IN THE SOFTWARE.\n";

    private static final String BSD3 = "Redistribution and use in source and binary forms, with or without\n" +
            "modification, are permitted provided that the following conditions\n" +
            "are met:\n" +
            "1. Redistributions of source code must retain the above copyright\n" +
            "notice, this list of conditions and the following disclaimer.\n" +
            "2. Redistributions in binary form must reproduce the above copyright\n" +
            "notice, this list of conditions and the following disclaimer in the\n" +
            "documentation and/or other materials provided with the distribution.\n" +
            "3. Neither the name of the copyright holders nor the names of its\n" +
            "contributors may be used to endorse or promote products derived from\n" +
            "this software without specific prior written permission.\n" +
            "\n" +
            "THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS \"AS IS\"\n" +
            "AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE\n" +
            "IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE\n" +
            "ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE\n" +
            "LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR\n" +
            "CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF\n" +
            "SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS\n" +
            "INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN\n" +
            "CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)\n" +
            "ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF\n" +
            "THE POSSIBILITY OF SUCH DAMAGE.\n";

    private static final String BSD3_nonums = "Redistribution and use in source and binary forms, with or without\n" +
            "modification, are permitted provided that the following conditions are met:\n" +
            "\n" +
            "Redistributions of source code must retain the above copyright notice, this list of\n" +
            "conditions and the following disclaimer. Redistributions in binary form must reproduce\n" +
            "the above copyright notice, this list of conditions and the following disclaimer in\n" +
            "the documentation and/or other materials provided with the distribution.\n" +
            "\n" +
            "Neither the name of Hamcrest nor the names of its contributors may be used to endorse\n" +
            "or promote products derived from this software without specific prior written\n" +
            "permission.\n" +
            "\n" +
            "THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS \"AS IS\" AND ANY\n" +
            "EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES\n" +
            "OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT\n" +
            "SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,\n" +
            "INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED\n" +
            "TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR\n" +
            "BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN\n" +
            "CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY\n" +
            "WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH\n" +
            "DAMAGE.";

    private static final String APACHE2 = "Apache License\n" +
            "Version 2.0, January 2004\n" +
            "http://www.apache.org/licenses/\n" +
            "\n" +
            "TERMS AND CONDITIONS FOR USE, REPRODUCTION, AND DISTRIBUTION\n" +
            "\n" +
            "1. Definitions.\n" +
            "\n" +
            "\"License\" shall mean the terms and conditions for use, reproduction,\n" +
            "and distribution as defined by Sections 1 through 9 of this document.\n" +
            "\n" +
            "\"Licensor\" shall mean the copyright owner or entity authorized by\n" +
            "the copyright owner that is granting the License.\n" +
            "\n" +
            "\"Legal Entity\" shall mean the union of the acting entity and all\n" +
            "other entities that control, are controlled by, or are under common\n" +
            "control with that entity. For the purposes of this definition,\n" +
            "\"control\" means (i) the power, direct or indirect, to cause the\n" +
            "direction or management of such entity, whether by contract or\n" +
            "otherwise, or (ii) ownership of fifty percent (50%) or more of the\n" +
            "outstanding shares, or (iii) beneficial ownership of such entity.\n" +
            "\n" +
            "\"You\" (or \"Your\") shall mean an individual or Legal Entity\n" +
            "exercising permissions granted by this License.\n" +
            "\n" +
            "\"Source\" form shall mean the preferred form for making modifications,\n" +
            "including but not limited to software source code, documentation\n" +
            "source, and configuration files.\n" +
            "\n" +
            "\"Object\" form shall mean any form resulting from mechanical\n" +
            "transformation or translation of a Source form, including but\n" +
            "not limited to compiled object code, generated documentation,\n" +
            "and conversions to other media types.\n" +
            "\n" +
            "\"Work\" shall mean the work of authorship, whether in Source or\n" +
            "Object form, made available under the License, as indicated by a\n" +
            "copyright notice that is included in or attached to the work\n" +
            "(an example is provided in the Appendix below).\n" +
            "\n" +
            "\"Derivative Works\" shall mean any work, whether in Source or Object\n" +
            "form, that is based on (or derived from) the Work and for which the\n" +
            "editorial revisions, annotations, elaborations, or other modifications\n" +
            "represent, as a whole, an original work of authorship. For the purposes\n" +
            "of this License, Derivative Works shall not include works that remain\n" +
            "separable from, or merely link (or bind by name) to the interfaces of,\n" +
            "the Work and Derivative Works thereof.\n" +
            "\n" +
            "\"Contribution\" shall mean any work of authorship, including\n" +
            "the original version of the Work and any modifications or additions\n" +
            "to that Work or Derivative Works thereof, that is intentionally\n" +
            "submitted to Licensor for inclusion in the Work by the copyright owner\n" +
            "or by an individual or Legal Entity authorized to submit on behalf of\n" +
            "the copyright owner. For the purposes of this definition, \"submitted\"\n" +
            "means any form of electronic, verbal, or written communication sent\n" +
            "to the Licensor or its representatives, including but not limited to\n" +
            "communication on electronic mailing lists, source code control systems,\n" +
            "and issue tracking systems that are managed by, or on behalf of, the\n" +
            "Licensor for the purpose of discussing and improving the Work, but\n" +
            "excluding communication that is conspicuously marked or otherwise\n" +
            "designated in writing by the copyright owner as \"Not a Contribution.\"\n" +
            "\n" +
            "\"Contributor\" shall mean Licensor and any individual or Legal Entity\n" +
            "on behalf of whom a Contribution has been received by Licensor and\n" +
            "subsequently incorporated within the Work.\n" +
            "\n" +
            "2. Grant of Copyright License. Subject to the terms and conditions of\n" +
            "this License, each Contributor hereby grants to You a perpetual,\n" +
            "worldwide, non-exclusive, no-charge, royalty-free, irrevocable\n" +
            "copyright license to reproduce, prepare Derivative Works of,\n" +
            "publicly display, publicly perform, sublicense, and distribute the\n" +
            "Work and such Derivative Works in Source or Object form.\n" +
            "\n" +
            "3. Grant of Patent License. Subject to the terms and conditions of\n" +
            "this License, each Contributor hereby grants to You a perpetual,\n" +
            "worldwide, non-exclusive, no-charge, royalty-free, irrevocable\n" +
            "(except as stated in this section) patent license to make, have made,\n" +
            "use, offer to sell, sell, import, and otherwise transfer the Work,\n" +
            "where such license applies only to those patent claims licensable\n" +
            "by such Contributor that are necessarily infringed by their\n" +
            "Contribution(s) alone or by combination of their Contribution(s)\n" +
            "with the Work to which such Contribution(s) was submitted. If You\n" +
            "institute patent litigation against any entity (including a\n" +
            "cross-claim or counterclaim in a lawsuit) alleging that the Work\n" +
            "or a Contribution incorporated within the Work constitutes direct\n" +
            "or contributory patent infringement, then any patent licenses\n" +
            "granted to You under this License for that Work shall terminate\n" +
            "as of the date such litigation is filed.\n" +
            "\n" +
            "4. Redistribution. You may reproduce and distribute copies of the\n" +
            "Work or Derivative Works thereof in any medium, with or without\n" +
            "modifications, and in Source or Object form, provided that You\n" +
            "meet the following conditions:\n" +
            "\n" +
            "(a) You must give any other recipients of the Work or\n" +
            "Derivative Works a copy of this License; and\n" +
            "\n" +
            "(b) You must cause any modified files to carry prominent notices\n" +
            "stating that You changed the files; and\n" +
            "\n" +
            "(c) You must retain, in the Source form of any Derivative Works\n" +
            "that You distribute, all copyright, patent, trademark, and\n" +
            "attribution notices from the Source form of the Work,\n" +
            "excluding those notices that do not pertain to any part of\n" +
            "the Derivative Works; and\n" +
            "\n" +
            "(d) If the Work includes a \"NOTICE\" text file as part of its\n" +
            "distribution, then any Derivative Works that You distribute must\n" +
            "include a readable copy of the attribution notices contained\n" +
            "within such NOTICE file, excluding those notices that do not\n" +
            "pertain to any part of the Derivative Works, in at least one\n" +
            "of the following places: within a NOTICE text file distributed\n" +
            "as part of the Derivative Works; within the Source form or\n" +
            "documentation, if provided along with the Derivative Works; or,\n" +
            "within a display generated by the Derivative Works, if and\n" +
            "wherever such third-party notices normally appear. The contents\n" +
            "of the NOTICE file are for informational purposes only and\n" +
            "do not modify the License. You may add Your own attribution\n" +
            "notices within Derivative Works that You distribute, alongside\n" +
            "or as an addendum to the NOTICE text from the Work, provided\n" +
            "that such additional attribution notices cannot be construed\n" +
            "as modifying the License.\n" +
            "\n" +
            "You may add Your own copyright statement to Your modifications and\n" +
            "may provide additional or different license terms and conditions\n" +
            "for use, reproduction, or distribution of Your modifications, or\n" +
            "for any such Derivative Works as a whole, provided Your use,\n" +
            "reproduction, and distribution of the Work otherwise complies with\n" +
            "the conditions stated in this License.\n" +
            "\n" +
            "5. Submission of Contributions. Unless You explicitly state otherwise,\n" +
            "any Contribution intentionally submitted for inclusion in the Work\n" +
            "by You to the Licensor shall be under the terms and conditions of\n" +
            "this License, without any additional terms or conditions.\n" +
            "Notwithstanding the above, nothing herein shall supersede or modify\n" +
            "the terms of any separate license agreement you may have executed\n" +
            "with Licensor regarding such Contributions.\n" +
            "\n" +
            "6. Trademarks. This License does not grant permission to use the trade\n" +
            "names, trademarks, service marks, or product names of the Licensor,\n" +
            "except as required for reasonable and customary use in describing the\n" +
            "origin of the Work and reproducing the content of the NOTICE file.\n" +
            "\n" +
            "7. Disclaimer of Warranty. Unless required by applicable law or\n" +
            "agreed to in writing, Licensor provides the Work (and each\n" +
            "Contributor provides its Contributions) on an \"AS IS\" BASIS,\n" +
            "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or\n" +
            "implied, including, without limitation, any warranties or conditions\n" +
            "of TITLE, NON-INFRINGEMENT, MERCHANTABILITY, or FITNESS FOR A\n" +
            "PARTICULAR PURPOSE. You are solely responsible for determining the\n" +
            "appropriateness of using or redistributing the Work and assume any\n" +
            "risks associated with Your exercise of permissions under this License.\n" +
            "\n" +
            "8. Limitation of Liability. In no event and under no legal theory,\n" +
            "whether in tort (including negligence), contract, or otherwise,\n" +
            "unless required by applicable law (such as deliberate and grossly\n" +
            "negligent acts) or agreed to in writing, shall any Contributor be\n" +
            "liable to You for damages, including any direct, indirect, special,\n" +
            "incidental, or consequential damages of any character arising as a\n" +
            "result of this License or out of the use or inability to use the\n" +
            "Work (including but not limited to damages for loss of goodwill,\n" +
            "work stoppage, computer failure or malfunction, or any and all\n" +
            "other commercial damages or losses), even if such Contributor\n" +
            "has been advised of the possibility of such damages.\n" +
            "\n" +
            "9. Accepting Warranty or Additional Liability. While redistributing\n" +
            "the Work or Derivative Works thereof, You may choose to offer,\n" +
            "and charge a fee for, acceptance of support, warranty, indemnity,\n" +
            "or other liability obligations and/or rights consistent with this\n" +
            "License. However, in accepting such obligations, You may act only\n" +
            "on Your own behalf and on Your sole responsibility, not on behalf\n" +
            "of any other Contributor, and only if You agree to indemnify,\n" +
            "defend, and hold each Contributor harmless for any liability\n" +
            "incurred by, or claims asserted against, such Contributor by reason\n" +
            "of your accepting any such warranty or additional liability.\n" +
            "\n" +
            "END OF TERMS AND CONDITIONS";

    private final static String EPL10 = "Eclipse Public License - v 1.0\n" +
            "\n" +
            "THE ACCOMPANYING PROGRAM IS PROVIDED UNDER THE TERMS OF THIS ECLIPSE PUBLIC\n" +
            "LICENSE (\"AGREEMENT\"). ANY USE, REPRODUCTION OR DISTRIBUTION OF THE PROGRAM\n" +
            "CONSTITUTES RECIPIENT'S ACCEPTANCE OF THIS AGREEMENT.\n" +
            "\n" +
            "1. DEFINITIONS\n" +
            "\n" +
            "\"Contribution\" means:\n" +
            "\n" +
            "a) in the case of the initial Contributor, the initial code and\n" +
            "   documentation distributed under this Agreement, and\n" +
            "b) in the case of each subsequent Contributor:\n" +
            "\n" +
            "i) changes to the Program, and\n" +
            "\n" +
            "ii) additions to the Program;\n" +
            "\n" +
            "where such changes and/or additions to the Program originate from and are\n" +
            "distributed by that particular Contributor. A Contribution 'originates' from a\n" +
            "Contributor if it was added to the Program by such Contributor itself or anyone\n" +
            "acting on such Contributor's behalf. Contributions do not include additions to\n" +
            "the Program which: (i) are separate modules of software distributed in\n" +
            "conjunction with the Program under their own license agreement, and (ii) are\n" +
            "not derivative works of the Program. \n" +
            "\n" +
            "\"Contributor\" means any person or entity that distributes the Program.\n" +
            "\n" +
            "\"Licensed Patents \" mean patent claims licensable by a Contributor which are\n" +
            "necessarily infringed by the use or sale of its Contribution alone or when\n" +
            "combined with the Program.\n" +
            "\n" +
            "\"Program\" means the Contributions distributed in accordance with this Agreement.\n" +
            "\n" +
            "\"Recipient\" means anyone who receives the Program under this Agreement,\n" +
            "including all Contributors.\n" +
            "\n" +
            "2. GRANT OF RIGHTS\n" +
            "\n" +
            "a) Subject to the terms of this Agreement, each Contributor hereby grants\n" +
            "Recipient a non-exclusive, worldwide, royalty-free copyright license to\n" +
            "reproduce, prepare derivative works of, publicly display, publicly perform,\n" +
            "distribute and sublicense the Contribution of such Contributor, if any, and\n" +
            "such derivative works, in source code and object code form.\n" +
            "\n" +
            "b) Subject to the terms of this Agreement, each Contributor hereby grants\n" +
            "Recipient a non-exclusive, worldwide, royalty-free patent license under\n" +
            "Licensed Patents to make, use, sell, offer to sell, import and otherwise\n" +
            "transfer the Contribution of such Contributor, if any, in source code and\n" +
            "object code form. This patent license shall apply to the combination of the\n" +
            "Contribution and the Program if, at the time the Contribution is added by the\n" +
            "Contributor, such addition of the Contribution causes such combination to be\n" +
            "covered by the Licensed Patents. The patent license shall not apply to any\n" +
            "other combinations which include the Contribution. No hardware per se is\n" +
            "licensed hereunder. \n" +
            "\n" +
            "c) Recipient understands that although each Contributor grants the\n" +
            "licenses to its Contributions set forth herein, no assurances are provided by\n" +
            "any Contributor that the Program does not infringe the patent or other\n" +
            "intellectual property rights of any other entity. Each Contributor disclaims\n" +
            "any liability to Recipient for claims brought by any other entity based on\n" +
            "infringement of intellectual property rights or otherwise. As a condition to\n" +
            "exercising the rights and licenses granted hereunder, each Recipient hereby\n" +
            "assumes sole responsibility to secure any other intellectual property rights\n" +
            "needed, if any. For example, if a third party patent license is required to\n" +
            "allow Recipient to distribute the Program, it is Recipient's responsibility to\n" +
            "acquire that license before distributing the Program.\n" +
            "\n" +
            "d) Each Contributor represents that to its knowledge it has sufficient\n" +
            "copyright rights in its Contribution, if any, to grant the copyright license\n" +
            "set forth in this Agreement. \n" +
            "\n" +
            "3. REQUIREMENTS\n" +
            "\n" +
            "A Contributor may choose to distribute the Program in object code form under\n" +
            "its own license agreement, provided that:\n" +
            "\n" +
            "a) it complies with the terms and conditions of this Agreement; and\n" +
            "\n" +
            "b) its license agreement:\n" +
            "\n" +
            "i) effectively disclaims on behalf of all Contributors all warranties and\n" +
            "conditions, express and implied, including warranties or conditions of title\n" +
            "and non-infringement, and implied warranties or conditions of merchantability\n" +
            "and fitness for a particular purpose; \n" +
            "\n" +
            "ii) effectively excludes on behalf of all Contributors all liability for\n" +
            "damages, including direct, indirect, special, incidental and consequential\n" +
            "damages, such as lost profits; \n" +
            "\n" +
            "iii) states that any provisions which differ from this Agreement are\n" +
            "offered by that Contributor alone and not by any other party; and\n" +
            "\n" +
            "iv) states that source code for the Program is available from such\n" +
            "Contributor, and informs licensees how to obtain it in a reasonable manner on\n" +
            "or through a medium customarily used for software exchange. \n" +
            "\n" +
            "When the Program is made available in source code form:\n" +
            "\n" +
            "a) it must be made available under this Agreement; and\n" +
            "\n" +
            "b) a copy of this Agreement must be included with each copy of the\n" +
            "Program. \n" +
            "\n" +
            "Contributors may not remove or alter any copyright notices contained within the\n" +
            "Program.\n" +
            "\n" +
            "Each Contributor must identify itself as the originator of its Contribution, if\n" +
            "any, in a manner that reasonably allows subsequent Recipients to identify the\n" +
            "originator of the Contribution.\n" +
            "\n" +
            "4. COMMERCIAL DISTRIBUTION\n" +
            "\n" +
            "Commercial distributors of software may accept certain responsibilities with\n" +
            "respect to end users, business partners and the like. While this license is\n" +
            "intended to facilitate the commercial use of the Program, the Contributor who\n" +
            "includes the Program in a commercial product offering should do so in a manner\n" +
            "which does not create potential liability for other Contributors. Therefore, if\n" +
            "a Contributor includes the Program in a commercial product offering, such\n" +
            "Contributor (\"Commercial Contributor\") hereby agrees to defend and indemnify\n" +
            "every other Contributor (\"Indemnified Contributor\") against any losses, damages\n" +
            "and costs (collectively \"Losses\") arising from claims, lawsuits and other legal\n" +
            "actions brought by a third party against the Indemnified Contributor to the\n" +
            "extent caused by the acts or omissions of such Commercial Contributor in\n" +
            "connection with its distribution of the Program in a commercial product\n" +
            "offering. The obligations in this section do not apply to any claims or Losses\n" +
            "relating to any actual or alleged intellectual property infringement. In order\n" +
            "to qualify, an Indemnified Contributor must: a) promptly notify the Commercial\n" +
            "Contributor in writing of such claim, and b) allow the Commercial Contributor\n" +
            "to control, and cooperate with the Commercial Contributor in, the defense and\n" +
            "any related settlement negotiations. The Indemnified Contributor may\n" +
            "participate in any such claim at its own expense.\n" +
            "\n" +
            "For example, a Contributor might include the Program in a commercial product\n" +
            "offering, Product X. That Contributor is then a Commercial Contributor. If that\n" +
            "Commercial Contributor then makes performance claims, or offers warranties\n" +
            "related to Product X, those performance claims and warranties are such\n" +
            "Commercial Contributor's responsibility alone. Under this section, the\n" +
            "Commercial Contributor would have to defend claims against the other\n" +
            "Contributors related to those performance claims and warranties, and if a court\n" +
            "requires any other Contributor to pay any damages as a result, the Commercial\n" +
            "Contributor must pay those damages.\n" +
            "\n" +
            "5. NO WARRANTY\n" +
            "\n" +
            "EXCEPT AS EXPRESSLY SET FORTH IN THIS AGREEMENT, THE PROGRAM IS PROVIDED ON AN\n" +
            "\"AS IS\" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, EITHER EXPRESS OR\n" +
            "IMPLIED INCLUDING, WITHOUT LIMITATION, ANY WARRANTIES OR CONDITIONS OF TITLE,\n" +
            "NON-INFRINGEMENT, MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE. Each\n" +
            "Recipient is solely responsible for determining the appropriateness of using\n" +
            "and distributing the Program and assumes all risks associated with its exercise\n" +
            "of rights under this Agreement, including but not limited to the risks and\n" +
            "costs of program errors, compliance with applicable laws, damage to or loss of\n" +
            "data, programs or equipment, and unavailability or interruption of operations.\n" +
            "\n" +
            "6. DISCLAIMER OF LIABILITY\n" +
            "\n" +
            "EXCEPT AS EXPRESSLY SET FORTH IN THIS AGREEMENT, NEITHER RECIPIENT NOR ANY\n" +
            "CONTRIBUTORS SHALL HAVE ANY LIABILITY FOR ANY DIRECT, INDIRECT, INCIDENTAL,\n" +
            "SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING WITHOUT LIMITATION LOST\n" +
            "PROFITS), HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,\n" +
            "STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY\n" +
            "WAY OUT OF THE USE OR DISTRIBUTION OF THE PROGRAM OR THE EXERCISE OF ANY RIGHTS\n" +
            "GRANTED HEREUNDER, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.\n" +
            "\n" +
            "7. GENERAL\n" +
            "\n" +
            "If any provision of this Agreement is invalid or unenforceable under applicable\n" +
            "law, it shall not affect the validity or enforceability of the remainder of the\n" +
            "terms of this Agreement, and without further action by the parties hereto, such\n" +
            "provision shall be reformed to the minimum extent necessary to make such\n" +
            "provision valid and enforceable.\n" +
            "\n" +
            "If Recipient institutes patent litigation against any\n" +
            "entity (including a cross-claim or counterclaim in a lawsuit) alleging that the\n" +
            "Program itself (excluding combinations of the Program with other software or\n" +
            "hardware) infringes such Recipient's patent(s), then such Recipient's rights\n" +
            "granted under Section 2(b) shall terminate as of the date such litigation is\n" +
            "filed.\n" +
            "\n" +
            "All Recipient's rights under this Agreement shall terminate if it fails to\n" +
            "comply with any of the material terms or conditions of this Agreement and does\n" +
            "not cure such failure in a reasonable period of time after becoming aware of\n" +
            "such noncompliance. If all Recipient's rights under this Agreement terminate,\n" +
            "Recipient agrees to cease use and distribution of the Program as soon as\n" +
            "reasonably practicable. However, Recipient's obligations under this Agreement\n" +
            "and any licenses granted by Recipient relating to the Program shall continue\n" +
            "and survive.\n" +
            "\n" +
            "Everyone is permitted to copy and distribute copies of this Agreement, but in\n" +
            "order to avoid inconsistency the Agreement is copyrighted and may only be\n" +
            "modified in the following manner. The Agreement Steward reserves the right to\n" +
            "publish new versions (including revisions) of this Agreement from time to time.\n" +
            "No one other than the Agreement Steward has the right to modify this Agreement.\n" +
            "The Eclipse Foundation is the initial Agreement Steward. The Eclipse Foundation may assign the responsibility to\n" +
            "serve as the Agreement Steward to a suitable separate entity. Each new version\n" +
            "of the Agreement will be given a distinguishing version number. The Program\n" +
            "(including Contributions) may always be distributed subject to the version of\n" +
            "the Agreement under which it was received. In addition, after a new version of\n" +
            "the Agreement is published, Contributor may elect to distribute the Program\n" +
            "(including its Contributions) under the new version. Except as expressly stated\n" +
            "in Sections 2(a) and 2(b) above, Recipient receives no rights or licenses to\n" +
            "the intellectual property of any Contributor under this Agreement, whether\n" +
            "expressly, by implication, estoppel or otherwise. All rights in the Program not\n" +
            "expressly granted under this Agreement are reserved.\n" +
            "\n" +
            "This Agreement is governed by the laws of the State of New York and the\n" +
            "intellectual property laws of the United States of America. No party to this\n" +
            "Agreement will bring a legal action under this Agreement more than one year\n" +
            "after the cause of action arose. Each party waives its rights to a jury trial\n" +
            "in any resulting litigation";

    private final static String LGPL21_preamble = "GNU LESSER GENERAL PUBLIC LICENSE\n" +
            "Version 2.1, February 1999\n" +
            "\n" +
            "Copyright (C) 1991, 1999 Free Software Foundation, Inc.\n" +
            "51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA\n" +
            "Everyone is permitted to copy and distribute verbatim copies\n" +
            "of this license document, but changing it is not allowed.\n" +
            "\n" +
            "[This is the first released version of the Lesser GPL.  It also counts\n" +
            "as the successor of the GNU Library Public License, version 2, hence\n" +
            "the version number 2.1.]\n" +
            "\n" +
            "Preamble\n" +
            "\n" +
            "The licenses for most software are designed to take away your\n" +
            "freedom to share and change it.  By contrast, the GNU General Public\n" +
            "Licenses are intended to guarantee your freedom to share and change\n" +
            "free software--to make sure the software is free for all its users.\n" +
            "\n" +
            "This license, the Lesser General Public License, applies to some\n" +
            "specially designated software packages--typically libraries--of the\n" +
            "Free Software Foundation and other authors who decide to use it.  You\n" +
            "can use it too, but we suggest you first think carefully about whether\n" +
            "this license or the ordinary General Public License is the better\n" +
            "strategy to use in any particular case, based on the explanations below.\n" +
            "\n" +
            "When we speak of free software, we are referring to freedom of use,\n" +
            "not price.  Our General Public Licenses are designed to make sure that\n" +
            "you have the freedom to distribute copies of free software (and charge\n" +
            "for this service if you wish); that you receive source code or can get\n" +
            "it if you want it; that you can change the software and use pieces of\n" +
            "it in new free programs; and that you are informed that you can do\n" +
            "these things.\n" +
            "\n" +
            "To protect your rights, we need to make restrictions that forbid\n" +
            "distributors to deny you these rights or to ask you to surrender these\n" +
            "rights.  These restrictions translate to certain responsibilities for\n" +
            "you if you distribute copies of the library or if you modify it.\n" +
            "\n" +
            "For example, if you distribute copies of the library, whether gratis\n" +
            "or for a fee, you must give the recipients all the rights that we gave\n" +
            "you.  You must make sure that they, too, receive or can get the source\n" +
            "code.  If you link other code with the library, you must provide\n" +
            "complete object files to the recipients, so that they can relink them\n" +
            "with the library after making changes to the library and recompiling\n" +
            "it.  And you must show them these terms so they know their rights.\n" +
            "\n" +
            "We protect your rights with a two-step method: (1) we copyright the\n" +
            "library, and (2) we offer you this license, which gives you legal\n" +
            "permission to copy, distribute and/or modify the library.\n" +
            "\n" +
            "To protect each distributor, we want to make it very clear that\n" +
            "there is no warranty for the free library.  Also, if the library is\n" +
            "modified by someone else and passed on, the recipients should know\n" +
            "that what they have is not the original version, so that the original\n" +
            "author's reputation will not be affected by problems that might be\n" +
            "introduced by others.\n" +
            "\f\n" +
            "Finally, software patents pose a constant threat to the existence of\n" +
            "any free program.  We wish to make sure that a company cannot\n" +
            "effectively restrict the users of a free program by obtaining a\n" +
            "restrictive license from a patent holder.  Therefore, we insist that\n" +
            "any patent license obtained for a version of the library must be\n" +
            "consistent with the full freedom of use specified in this license.\n" +
            "\n" +
            "Most GNU software, including some libraries, is covered by the\n" +
            "ordinary GNU General Public License.  This license, the GNU Lesser\n" +
            "General Public License, applies to certain designated libraries, and\n" +
            "is quite different from the ordinary General Public License.  We use\n" +
            "this license for certain libraries in order to permit linking those\n" +
            "libraries into non-free programs.\n" +
            "\n" +
            "When a program is linked with a library, whether statically or using\n" +
            "a shared library, the combination of the two is legally speaking a\n" +
            "combined work, a derivative of the original library.  The ordinary\n" +
            "General Public License therefore permits such linking only if the\n" +
            "entire combination fits its criteria of freedom.  The Lesser General\n" +
            "Public License permits more lax criteria for linking other code with\n" +
            "the library.\n" +
            "\n" +
            "We call this license the \"Lesser\" General Public License because it\n" +
            "does Less to protect the user's freedom than the ordinary General\n" +
            "Public License.  It also provides other free software developers Less\n" +
            "of an advantage over competing non-free programs.  These disadvantages\n" +
            "are the reason we use the ordinary General Public License for many\n" +
            "libraries.  However, the Lesser license provides advantages in certain\n" +
            "special circumstances.\n" +
            "\n" +
            "For example, on rare occasions, there may be a special need to\n" +
            "encourage the widest possible use of a certain library, so that it becomes\n" +
            "a de-facto standard.  To achieve this, non-free programs must be\n" +
            "allowed to use the library.  A more frequent case is that a free\n" +
            "library does the same job as widely used non-free libraries.  In this\n" +
            "case, there is little to gain by limiting the free library to free\n" +
            "software only, so we use the Lesser General Public License.\n" +
            "\n" +
            "In other cases, permission to use a particular library in non-free\n" +
            "programs enables a greater number of people to use a large body of\n" +
            "free software.  For example, permission to use the GNU C Library in\n" +
            "non-free programs enables many more people to use the whole GNU\n" +
            "operating system, as well as its variant, the GNU/Linux operating\n" +
            "system.\n" +
            "\n" +
            "Although the Lesser General Public License is Less protective of the\n" +
            "users' freedom, it does ensure that the user of a program that is\n" +
            "linked with the Library has the freedom and the wherewithal to run\n" +
            "that program using a modified version of the Library.\n" +
            "\n" +
            "The precise terms and conditions for copying, distribution and\n" +
            "modification follow.  Pay close attention to the difference between a\n" +
            "\"work based on the library\" and a \"work that uses the library\".  The\n" +
            "former contains code derived from the library, whereas the latter must\n" +
            "be combined with the library in order to run";

    private final static String APACHE11_head = "The Apache Software License, Version 1.1\n" +
            "\n" +
            "Copyright (c) 2000-2002 The Apache Software Foundation.  All rights\n" +
            "reserved. \n" +
            "\n" +
            "Redistribution and use in source and binary forms, with or without\n" +
            "modification, are permitted provided that the following conditions\n" +
            "are met:\n" +
            "\n" +
            "1. Redistributions of source code must retain the above copyright\n" +
            "notice, this list of conditions and the following disclaimer.\n" +
            "\n" +
            "2. Redistributions in binary form must reproduce the above copyright\n" +
            "notice, this list of conditions and the following disclaimer in\n" +
            "the documentation and/or other materials provided with the\n" +
            "distribution.\n" +
            "\n" +
            "3. The end-user documentation included with the redistribution,\n" +
            "if any, must include the following acknowledgment:";

}
