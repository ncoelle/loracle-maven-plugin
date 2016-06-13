loracle-maven-plugin [![Build Status](https://travis-ci.org/openCage/loracle-but-maven-plugin.svg?branch=master)](https://travis-ci.org/openCage/loracle-but-maven-plugin) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/de.pfabulist/loracle-but-maven-plugin/badge.svg)](https://maven-badges.herokuapp.com/maven-central/de.pfabulist/loracle-but-maven-plugin)
======= 

LOracle maven plugin is the the License Oracle.

I Am Not A Lawyer But I'll fail your build if something license related looks fishy.
 
It checks checks that
  
 * every dependency has a declared known license
 * license compatibility is valid (some)

Note

 * does not generate notes listing all dependend licenses
 * does not set licence headers in source

## Use
    
        <plugins>
            <plugin>
                <groupId>de.pfabulist</groupId>
                <artifactId>loracle-maven-plugin</artifactId>
                <version>-latest-</version>
                <executions>
                    <execution>
                        <phase>install</phase>
                        <goals>
                            <goal>license-check</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>

### Configuration options

 * Declare Licenses directly

        <licenseDeclarations>
            <licenseDeclaration>
                <coordinates>a.b:foo:1.0</coordinates>
                <license>apache-2</license>
            </licenseDeclaration>
        </licenseDeclarations>

   The coordinates allow '*' to declare patterns. The '*' only works within groupid and artifactid

 * Url Declarations

        <urlDeclarations>
            <urlDeclaration>
                <url>a.b:foo:1.0</url>
                <license>apache-2</license>
                <checkedAt>2016-02-03</checkedAt>
            </urlDeclaration>
        </urlDeclarations>

    Urls may declare a license but these tend to be volatile over time. So its only good for a while

 * allowUrlsCheckedDaysBefore

        <allowUrlsCheckedDaysBefore>100</allowUrlsCheckedDaysBefore>

 To allow urls that are only good for a time.
 Note: The declaration is by absolute time and this flag relative, i.e. you builds will fail eventually and force you to recheck the urls.

 * stopOnError

        <stopOnError>false</stopOnError>

  To prevent build breakage set this flag. It meant for multi module builds, to see all results in one run.

 * andIsOr

        <andIsOr>true</andIsOr>

 There is no maven defined meaning of 2 or more license declarations in a pom. The defensive approach is to
        assume 'and' combination. But checking the artifact it is often meant as 'or'.
        This flag switches the behaviour to 'or'.






## older notes


## Obvious note

IANAL: a breaking build does not necessarily mean that this license combination is forbidden, 
or not breaking implies you're save in court.

## License determination

LOracle looks at the license block of each pom.
Using the name and the url it tries to map it to a standard SPDX open software license.
If that fails it looks in a list of known artifacts.
 
As there a no generally accepted standards the content of these tags varies wildly.
IANAL uses some regex to interpret them.

## License Standards

There are several attempts to list open software licenses. IANAL is based of spdx.

## Working Philosophy

### Offline

Maven happily gets artifacts from the net. IANAL works offline.
It sits so late in the goal chain that it is a valid assumption that all dependencies are in the local repo already.
Some license references are to urls. IANAL will not check the content. It will only look in the local repo.
IANAL plays it safe, i.e. if there might be a problem it breaks the build.

### SPDX

There are many list of licenses and many ways to specify a license by name or url. Url are especially great because neither there exact string or content is fix.
But 

But SPDX garanties name and url uniqueness and stability.
Thus IANAL tries to base as much as possible SPDX standards. 

 
## Maven surprises

Maven has a license block, but almost no idea what to do with it, i.e. there is no understanding what to put in it.
Thus 

* license names are effectively free text
* license urls may be any url including relative links, 
   e.g. ./Licence.txt is a reference to a license text located in the dir as this pom. But this text is not distributed with the artifact  


## License Surprises

Or not really surprises. Law especially international law is complicated. That means for example that 
 
 * any license requirements are hard to read or understand for simple coders :-)
 * any effects are heavily disputed by the pros (real lawyers)
 * different countries might have different interpretations
 * or later: Some licenses have a 'or later' clause build in, i.e. the content might change.
 * some licenses are combined with unclear effects

The only certain thing: if you use GPL2/3 wrong you'll lose in court.  
 
## Language

Law is tricky. There are a couple often used terms. But even those are not precise in the mathematical sense.
e.g. license compatibility does not says which artifact is using which and also not how.
Thus:

### Definition: Upstream / Downstream Compatible

Artifact A depends (maven sense) of artifact B. B is upstream, A is downstream.
A does not include B or parts of B. It uses B, derives classes from it ...

If B's license allows the use of it in artifact with license A then B is downstream compatible with A and
A is upstream compatible with B.


# Blog

## License check part 0: License ID or SPDX to the rescue

TLDR;
License ID is hard but thanks to SPDX not totally impossible.

Arguing about licenses requires identifying them first.
You should be able to go from a text or reference to an identifier: a text pointing to exactly one license and vice versa.
license reference -> licenseID
So just pick the list of standard licenses with its standard license ids.
 Oh my.
 
Until recently there were a lot of lists which all had a couple of common properties: 
 
 * non holds all licenses
 * non is clear on license id
 * they can change at any time
 * they are fuzzy about multi licenses issues

e.g. opensource.org/licenses, gnu, fedora, dejacode, tldr/licenses, wikipedia, versioneye

Then came SPDX/licenses. A versioned list of (many) licenses which defined ids and text.
Version 2 even decides how to precisely describe license variants like "or later" or exceptions.
That could close the question of license identification if it would not be for 2 problems:

  * spdx does not list all licenses
  * spdx ids are pretty much not used (yet)
    
### Mapping common License References to SPDX Ids
  
So let's take what is actually used and map it to a spdx license.
What is actually used?
  
  * non standardized license names
  * URLs (non standard and with changing content)
  * License texts (rarely)
  * Artifact Ids
  
#### Mapping License Names (Normalization)
  
To give you a taste of the problem here is a report from an other tool:
    
    * Apache 2
    * Apache-2.0
    * Apache License 2.0
    * The Apache License 2.0
    * Apache software License v2.0
    * Apache
    
So how many different licenses are these ?
Trick question. One real License: Apache-2.0 (SPDX) and a vague reference. The last could mean several licenses.

For everyone saying so what apache is apache have look at the discussions about GPL2.0 versus GPL3.0 on the linux
kernel list, e.g. version matter.

Now if you (like me) stared enough ways to state the same license you might think that just throwing out some
irrelevant fill works and converting everything to one case might do the trick.
Which is what I mostly do. That is a normalization of license names.
  
*Fill words* 

Most license names include a relevant part (e.g. GNU Lesser 3.0) and some extra fill words 
(The Gnu Lesser Public License Version 3.0). So filter:
 
 * License
 * The
 * Version
 * Software
 * Agreement
 * Free
 * Open
 * Public
 * v
 ...
 
Map vNumber to Number, convert to lowercase, remove ',', '-' and the Apache list becomes
  
    * apache 2
    * apache 2.0
    * apache 2.0
    * apache 2.0
    * apache 2.0
    * apache
    
i.e. we are down to 3. As mentioned before 'apache' can not be decided. Apache 2 can interpreted as apache 2.0.
This and cases like "new bsd" == "bsd 3 clause" means that several 'noramal' strings can map to the same license.    

The removal of fill words has one slight problem. There are actually license names composed completely out of fill words, e.g.      
"Open Public License v1.0" and "Open Software License 1.0". In these cases normalization has to keep all fill words.   
 
*Are we done now*
 
Are you kidding me. We are just starting.
 
There license modifiers, exceptions, and multiple licenses to handle. And who says you even get a name (but that later) 
    

#### Mapping License Names (License Modifiers)

Especially in GNU land there is a common theme: Take a standard license and add a little modification.
I won't go into interpretation of the meaning here. I am not a lawyer and I am still concerned with 
 identification here.
 
SPDX 2.0 standardized a syntax here, e.g. GPL-2.0+ with Classpath-exception-2.0 and (MIT or BSD-2-Clause).
Writing a parers for this is just a nice little exercise. Good right ?
  Well nobody is using it (yet).
  
So what are they using ? Don't ask.
  Well I did ask. 
  Lets play a game. What is ?:
  
  * GPL-2.0 or later
  * GPL2.0, CDDL1.0
  * GPL2 with classpath exception
  
Answer (SPDX):
  * GPL-2.0+
  * - (is ',' 'and' or 'or')
  * - (which classpath exception)
  
As it turns out, License modification varies so much that apart from 'or later' the actually used names can not identify the
license.

#### Mapping by Name (Multiple Maven Licenses)
 
 Well this is easy right, 2 licenses L1 and L2 means just L1 and L2 or or ....
  f*ck. Even if Maven means 'and', do all users understand this ?
   I assume not (I don't know what it means)
   
 Better use individual artifact mapping in that case.  
   

#### Mapping by Name (Non SPDX)

Although there are 600+ licenses listed by SPDX (not counting modifications) there many more in use.
So I added some of them (e.g aop-pd). Otherwise most project would have license holes.
 
 Why is that problem? Now see no normalization and also possible future normalization by SPDX with a different id, ouch.

And I want to reason eventually reason bit about license compatibility. And non SPDX licenses are just harder.  

But others are doing things like that, I try to borrow as much as possible (Fedora, DejaCode, TLDR/Licenses)

#### Mapping By Url (Normalization)

If there is no name there might be a url. There is just a little problem. 
Many different urls can point to the same page. And the content of a page is not guarantied to stay the same.
 Except for SPDX urls which nobody uses, you know the song.
 
But it is actually not a complete disaster. Even working offline, i.e. with the url string only.
 Apart from SPDX, GNU, Fedora and opensource.org are actually used and keep their urls (mostly).
 
So if you rip out "https://", "http://", "www.", any endings (e.g. html, txt ..) and in the case of 
opensource.org also "-license" before the ending and you get a normal form that matches something.
 Be careful though. Some licenses share the url (e.g. with or without exception) and thus can not be used 
 for license identification.

### Mapping By Url (time based)

So you found a license at http://i.am.a.license/i/promise.really and looking at its content shout "ah bsd-2-clause".
 Can you then safely map this url as bsd-2-clause?
  Well no. The content may change at any time. But how about risking it for a while ? You just checked it.
   It probably is good for a while. So lets do that. Store the mapping with date and only allow it for the next x days.
 
### Mapping by License Text
 
 Hard to believe but the cases I am looking at have almost never any license test. (poms and java artifacts)
 So there is no answer here (yet).
  
### Mapping by Artifact ID/ Coordinates
  
  As flexible (i.e. fuzzy) in many ways maven can be artifact if is not one of it, i.e. fully resolved Coordinates 
    (no more SNAPSHOT) uniquely identify an artifact in a context. Controlling that context is topic for a differnt post.
      Here I assume precise identification. 
      
  So a map of coordinated to SPDX license is possible. And actually in some cases the only answer.
  i.e. someone has to research the actual license used by an artifact and list it.
   
### Conflicts
   
Now with many different ways to identify a license it just might be possible that there are different answers.
   Well yes. e.g. The nice Kryo lib lists BSD-2-clause and BSD-3-clause. Which is it ? 
   The diverse mapping method have different degree of certainty. ArtifactMapping > Name Mapping > UrlMapping.
     That can help. But to be sure only individual research can decide here.
     
     
### Summery
     
As long a the world does not move en mass to SPDX license identifiers license identification stays a game.
     Good for me. Because this is a side project and fiddling with heuristics is fun.
      If you want an easy check that does most of the simple work: use the plugin and look at the warning once in a while.
      If it stops the build. Check the artifact.
     If you need law advice please consult a lawyer in your relevant country. 
      
      
  
  
 
  
