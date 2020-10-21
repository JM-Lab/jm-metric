JMMetric
========
JMMetric is an open source to prepare data for analytics, Java Reactive Stream Data Processing Framework that ingests various format data from a multitude of inputs, transforms it into FieldMap (Semi-Structured Data with flat key), sends it to outputs and then use it to make your Metric.

## Useful Features  :
* **Flow Package ([Reactive Programming with JDK 9 Flow API](https://community.oracle.com/docs/DOC-1006738) Utility, [Reactive Streams 
in Java 9](https://dzone.com/articles/reactive-streams-in-java-9)) 
Compatibility**
* **Input Support**

`STDIN` `FILE` `KAFKA`
* **Input Format Support**

`DELIMITER` `KEY_VALUE_DELIMITER` `FORMATTED` `APACHE_ACCESS_LOG` `NGINX_ACCESS_LOG` `JSON` `RAW`
* **Output Support**

`STDOUT` `FILE` `KAFKA` `ELASTICSEARCH`

## version
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/kr.jmlab/jm-metric/badge.svg)](http://search.maven.org/#artifactdetails%7Ckr.jmlab%7Cjm-metric%7C0.2.7%7Cjar)

## Prerequisites:
* Java 11 or later
* git
* maven 

## Usage
Gradle:
```groovy
compile 'kr.jmlab:jm-metric:0.2.7'
```
Maven:
```xml
<dependency>
    <groupId>kr.jmlab</groupId>
    <artifactId>jm-metric</artifactId>
    <version>0.2.7</version>
</dependency>
```

## Installation
Checkout the source code:
```cmd
git clone https://github.com/JM-Lab/jm-metric.git
cd jm-metric
git checkout -b 0.2.7 origin/0.2.7
mvn install -Dmaven.test.skip=true
```
### For Example :
- **[HelloJMMetric.jsh](https://github.com/JM-Lab/jm-metric/tree/master/jsh/HelloJMMetric.jsh)**
```cmd
jshell --class-path bin/jm-metric.jar -startup jsh/HelloJMMetric.jsh
```
- [Interact with JShell](https://docs.oracle.com/javase/9/jshell/)
```jshell
/env --class-path bin/jm-metric.jar

import kr.jm.metric.JMMetric;
JMMetric jmMetric = new JMMetric().start().testInput("Hello JMMetric !!!");
```
> Result
```json
{"meta.inputId":"TestInput","meta.processTimestamp":"2018-11-06T07:30:20.345Z","rawData":"Hello JMMetric !!!"}
```
```jshell
import kr.jm.metric.config.mutator.ApacheAccessLogMutatorConfig;
jmMetric = new JMMetric(jmMetric.getJmMetricConfigManager().insertMutatorConfig(new ApacheAccessLogMutatorConfig("CustomApacheLog", "%h %l %u %t \"%r\" %>s %b \"%{Referer}i\" \"%{User-agent}i\" %D")), "CustomApacheLog").start().testInput("223.62.219.101 - - [08/Jun/2015:16:59:59 +0900] \"POST /app/5315 HTTP/1.1\" 200 1100 \"-\" \"Dalvik/1.6.0 (Linux; U; Android 4.4.2; SHV-E330S Build/KOT49H)\" 45195");
```
> Result
```json
{"remoteUser":"-","requestTime":"45195","request":"POST /app/5315 HTTP/1.1","referer":"-","meta.inputId":"TestInput","meta.processTimestamp":"2018-11-06T07:57:47.114Z","receivedTimestamp":"08/Jun/2015:16:59:59 +0900","remoteHost":"223.62.219.101","sizeByte":"1100","userAgent":"Dalvik/1.6.0 (Linux; U; Android 4.4.2; SHV-E330S Build/KOT49H)","remoteLogName":"-","statusCode":"200"}
```
## LICENSE
Copyright 2018 Jemin Huh (JM)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

<http://www.apache.org/licenses/LICENSE-2.0>

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.