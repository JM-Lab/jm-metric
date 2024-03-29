package kr.jm.metric;

import kr.jm.metric.data.Transfer;
import kr.jm.utils.JMThread;
import kr.jm.utils.flow.processor.JMProcessor;
import kr.jm.utils.flow.processor.JMProcessorBuilder;
import kr.jm.utils.flow.subscriber.JMSubscriberBuilder;
import kr.jm.utils.helper.JMWordSplitter;
import kr.jm.utils.stats.generator.JMWordCountGenerator;
import org.junit.After;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static kr.jm.metric.config.mutator.field.FieldConfig.RAW_DATA;

public class JMMetricSampleTest {

    // jshell --class-path .m2/repository/com/github/jm-lab/jm-metric/0.1.0-SNAPSHOT/jm-metric-0.1.0-jar-with-dependencies.jar
    private JMMetric jmMetric;

    @After
    public void tearDown() {
        jmMetric.close();
    }

    @Test
    public void test() {
        jmMetric = new JMMetric();
        jmMetric.getJmMetricConfigManager().printAllConfig();
    }


    @Test
    public void test1() {
        jmMetric = new JMMetric("Json");
        jmMetric.consumeWith(System.out::println);
        jmMetric.testInput("{\"Hello\": \"World !!!\"}");
        JMThread.sleep(1000);
    }

    @Test
    public void test2() {
        jmMetric = new JMMetric("Raw");
        jmMetric.consumeWith(System.out::println)
                .subscribe(JMSubscriberBuilder.getJsonStringSOPLSubscriber());
        jmMetric.testInput("Hello JMMetric !!!");
        JMThread.sleep(1000);

        JMProcessor<List<Transfer<Map<String, Object>>>, Stream<String>>
                wordStreamProcessor =
                JMProcessorBuilder
                        .build(t -> t.stream().map(Transfer::getData)
                                .map(fieldMap -> fieldMap.get(RAW_DATA))
                                .map(Object::toString)
                                .flatMap(JMWordSplitter.getInstance()::splitAsStream));
        jmMetric.subscribeWith(wordStreamProcessor);
        wordStreamProcessor
                .subscribeAndReturnProcessor(JMProcessorBuilder
                        .build(JMWordCountGenerator.getInstance()::buildCountMap))
                .subscribe(JMSubscriberBuilder.getJsonStringSOPLSubscriber());
        jmMetric.testInput("Hello JMMetric !!!");

        JMThread.sleep(1000);
    }

    @Test
    public void test3() {
        jmMetric = new JMMetric("CombinedLogFormat");
        jmMetric.consumeWith(System.out::println)
                .subscribeWith(
                        JMSubscriberBuilder.getJsonStringSOPLSubscriber());
        jmMetric.testInput(
                "141.248.111.36 - - [09/Apr/2018:18:03:52 +0900] \"POST /wp-content HTTP/1.0\" 200 4968 \"http://www.mccann.com/explore/about/\" \"Mozilla/5.0 (compatible; MSIE 7.0; Windows NT 6.0; Trident/5.0)\"");
        jmMetric.testInput(
                "223.62.219.101 - - [08/Jun/2015:16:59:59 +0900] \"POST /app/5315 HTTP/1.1\" 200 1100 \"-\" \"Dalvik/1.6.0 (Linux; U; Android 4.4.2; SHV-E330S Build/KOT49H)\"");

        JMThread.sleep(1000);
    }

}
