ianal-but-maven-plugin [![Build Status](https://travis-ci.org/openCage/ianal-but-maven-plugin.svg?branch=master)](https://travis-ci.org/openCage/ianal-but-maven-plugin) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/de.pfabulist/ianal-but-maven-plugin/badge.svg)](https://maven-badges.herokuapp.com/maven-central/de.pfabulist/ianal-but-maven-plugin) 
======= 

I Am Not A Lawyer But I'll fail your build if something license related looks fishy.
 
Ianal checks that every that
  
1. every dependency has a declared license 

2. all licenses are compatible

## Use
    
        <plugins>
            <plugin>
                <groupId>de.pfabulist</groupId>
                <artifactId>ianal-but-maven-plugin</artifactId>
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


## Obvious note

IANAL so, a breaking build does not necessarily mean that this license combination is forbidden, 
or not breaking implies your save in court.

## License determination

Ianal looks at the license block of each pom.
Using the name and the url it tries to map it to a standard SPDX open software license.
If that fails it looks in a list o known artifacts.
 
As there a generally accepted standards the content of these tags varies wildly.
IANAL uses some regex to interpret them.

## License Standards

There are several attempts to list open software licenses. IANAL is based of spdx.

## Working Philosophy

Maven happily gets artifacts from the net. IANAL works offline.
It sits so late in the goal chain that it is a valid assumption that all dependencies are in the local repo already.
Some license references are to urls. IANAL will not check the content. It will only look in the local repo.
IANAL plays it safe, i.e. if it might be a problem it breaks the build.

 
## Maven surprises

Maven has a license block, but almost no idea what to do with it, i.e. there is no understanding what to put in it.
Thus 

* license names are effectively free text
* license urls may be any url including relative links, 
   e.g. ./Licence.txt is a reference to a license text located in the dir as this pom. But this text is not distributed with the artifact  


## License Surprises

Or not really surprises. Law especially international law is complicated. That means for example that 
 
 * any license requirements are hard to read or understand for simple coders :-)
 * any effects are heavly disputed by the pros (real lawyers)
 * different countries might have different interpretations
 
## Language

Law is tricky. There are a couple often used terms. But even those are not precise in thr mathematical sense.
e.g. license compatiblity does not says which artifact is using which and also not how.
Thus:

### Definition: Upstream / Downstream Compatible

Artifact A depends (maven sense) of artifact B. B is upstream, A is downstream.
A does not include B or parts of B. It uses B, derives classes from it ...

If B's license allows the use of it in artifact with license A then B is downstream compatible with A and
A is upstream compatible with B.





 


