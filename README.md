JMLab JMMetric Framework
==============================
JMMetric is an open source, Java Reactive Stream Data Processing Framework that ingests various format data, transforms it to FieldMap (flat key map), and then use it to build Semi-Structured Data And Metric.

## version
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/kr.jmlab/jm-metric/badge.svg)](http://search.maven.org/#artifactdetails%7Ckr.jmlab%7Cjm-metric%7C0.1.0%7Cjar)

## Prerequisites:
* Java 9 or later

## Usage
Gradle:
```groovy
compile 'kr.jmlab:jm-metric:0.1.0'
```
Maven:
```xml
<dependency>
    <groupId>com.github.jm-lab</groupId>
    <artifactId>jm-metric</artifactId>
    <version>0.1.0</version>
</dependency>
```

## Installation
Checkout the source code:

    git clone https://github.com/JM-Lab/jm-metric.git
    cd jm-metric
    git checkout -b 0.1.0 origin/0.1.0
    mvn install

## Useful Utilities With New Features Of Java 9  :
* **Flow Package ([Reactive Programming with JDK 9 Flow API](https://community.oracle.com/docs/DOC-1006738) Utility, [Reactive Streams 
in Java 9](https://dzone.com/articles/reactive-streams-in-java-9)) 
Compatibility**
* **Various Format Data Support**

    `DELIMITER` `KEY_VALUE_DELIMITER` `FORMATTED` `APACHE_ACCESS_LOG` `NGINX_ACCESS_LOG` `JSON` `RAW`

### For Example :
```cmd
git clone https://github.com/JM-Lab/jm-metric.git
cd jm-metric
jshell -startup jsh/HelloJMMetric.jsh 
```
* **[HelloJMMetric.jsh](https://github.com/JM-Lab/jm-metric/tree/master/jsh/HelloJMMetric.jsh)**
```jshell
/env --class-path ~/.m2/repository/com/github/jm-lab/jm-metric/0.1.0/jm-metric-0.1.0-jar-with-dependencies.jar

import kr.jm.metric.JMMetric;
import kr.jm.metric.config.ApacheAccessLogMetricConfig;
import kr.jm.utils.flow.subscriber.JMSubscriberBuilder;
import kr.jm.utils.helper.JMJson;
import kr.jm.utils.helper.JMString;

JMMetric jmMetric = new JMMetric();
jmMetric.bindDataIdToConfigId("sampleData", "Raw");
jmMetric.subscribeWith(JMSubscriberBuilder.getSOPLSubscriber(fieldMapList -> "[Metric Result]" + JMString.LINE_SEPARATOR + JMJson.toPrettyJsonString(fieldMapList)));
jmMetric.inputSingle("sampleData", "Hello JMMetric !!!");
```
```
[FieldMap Result]
[ {
  "meta.dataId" : "sampleData",
  "meta.configId" : "Raw",
  "rawData" : "Hello JMMetric !!!",
  "meta.timestamp" : 1523608214841
} ]
```
```jshell
jmMetric.insertConfig(new ApacheAccessLogMetricConfig("myAccessLog","%h %l %u %t \"%r\" %>s %b \"%{Referer}i\" \"%{User-agent}i\" %D"));
jmMetric.bindDataIdToConfigId("sampleAccessLog", "myAccessLog");
jmMetric.inputSingle("sampleAccessLog", "223.62.219.101 - - [08/Jun/2015:16:59:59 +0900] \"POST /app/5315 HTTP/1.1\" 200 1100 \"-\" \"Dalvik/1.6.0 (Linux; U; Android 4.4.2; SHV-E330S Build/KOT49H)\" 45195");
```
```
[FieldMap Result]
[ {
  "remoteUser" : "-",
  "request" : "POST /app/5315 HTTP/1.1",
  "referer" : "-",
  "remoteHost" : "223.62.219.101",
  "meta.configId" : "myAccessLog",
  "userAgent" : "Dalvik/1.6.0 (Linux; U; Android 4.4.2; SHV-E330S Build/KOT49H)",
  "rawData" : "223.62.219.101 - - [08/Jun/2015:16:59:59 +0900] \"POST /app/5315 HTTP/1.1\" 200 1100 \"-\" \"Dalvik/1.6.0 (Linux; U; Android 4.4.2; SHV-E330S Build/KOT49H)\" 45195",
  "meta.timestamp" : 1523608215121,
  "requestTime" : "45195",
  "meta.dataId" : "sampleAccessLog",
  "sizeByte" : "1100",
  "remoteLogName" : "-",
  "timestamp" : "08/Jun/2015:16:59:59 +0900",
  "statusCode" : "200"
} ]
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