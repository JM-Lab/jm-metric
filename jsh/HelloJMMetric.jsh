/env --class-path ~/.m2/repository/com/github/jm-lab/jm-metric/0.1.0/jm-metric-0.1.0-jar-with-dependencies.jar

import kr.jm.metric.JMMetric;
import kr.jm.metric.config.ApacheAccessLogMetricConfig;
import kr.jm.utils.flow.subscriber.JMSubscriberBuilder;
import kr.jm.utils.helper.JMJson;
import kr.jm.utils.helper.JMString;

JMMetric jmMetric = new JMMetric();
jmMetric.bindDataIdToConfigId("sampleData", "Raw");
jmMetric.subscribeWith(JMSubscriberBuilder.getSOPLSubscriber(fieldMapList -> "[FieldMap Result]" + JMString.LINE_SEPARATOR + JMJson.toPrettyJsonString(fieldMapList)));
jmMetric.inputSingle("sampleData", "Hello JMMetric !!!");

jmMetric.insertConfig(new ApacheAccessLogMetricConfig("myAccessLog","%h %l %u %t \"%r\" %>s %b \"%{Referer}i\" \"%{User-agent}i\" %D"));
jmMetric.bindDataIdToConfigId("sampleAccessLog", "myAccessLog");
jmMetric.inputSingle("sampleAccessLog", "223.62.219.101 - - [08/Jun/2015:16:59:59 +0900] \"POST /app/5315 HTTP/1.1\" 200 1100 \"-\" \"Dalvik/1.6.0 (Linux; U; Android 4.4.2; SHV-E330S Build/KOT49H)\" 45195");

