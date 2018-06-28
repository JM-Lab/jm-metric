package kr.jm.metric;

import kr.jm.metric.config.input.KafkaInputConfig;
import kr.jm.metric.config.output.KafkaOutputConfig;
import kr.jm.metric.input.publisher.InputPublisher;
import kr.jm.metric.input.publisher.InputPublisherBuilder;
import kr.jm.metric.mutating.processor.MutatingProcessor;
import kr.jm.metric.output.subscriber.OutputSubscriber;
import kr.jm.metric.output.subscriber.OutputSubscriberBuilder;
import kr.jm.utils.enums.OS;
import kr.jm.utils.flow.subscriber.JMSubscriberBuilder;
import kr.jm.utils.helper.*;
import kr.jm.utils.kafka.JMKafkaServer;
import kr.jm.utils.kafka.client.JMKafkaConsumer;
import kr.jm.utils.kafka.client.JMKafkaProducer;
import kr.jm.utils.zookeeper.JMZookeeperServer;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;
import java.util.concurrent.atomic.LongAdder;

import static kr.jm.utils.helper.JMThread.sleep;

public class JMMetricIMOTest {

    private static final String FileName = "webAccessLogSample.txt";

    private String inputTopic;
    private String outputTopic;
    private JMZookeeperServer embeddedZookeeper;
    private JMKafkaServer kafkaServer;
    private String bootstrapServers;
    private JMKafkaProducer kafkaProducer;
    private JMKafkaConsumer kafkaConsumer;

    private JMMetricConfigManager jmMetricConfigManager;
    private InputPublisher kafkaInputPublisher;
    private MutatingProcessor mutatingProcessor;
    private OutputSubscriber kafkaOutputSubscriber;

    /**
     * Sets the up.
     */
    @Before
    public void setUp() {
        Optional.of(JMPath.getPath(JMZookeeperServer.DEFAULT_ZOOKEEPER_DIR))
                .filter(JMPath::exists)
                .ifPresent(JMPathOperation::deleteDir);
        Optional.of(JMPath.getPath(JMKafkaServer.DEFAULT_KAFKA_LOG))
                .filter(JMPath::exists)
                .ifPresent(JMPathOperation::deleteDir);
        this.embeddedZookeeper =
                new JMZookeeperServer(OS.getAvailableLocalPort()).start();
        String zookeeperConnect = this.embeddedZookeeper.getZookeeperConnect();
        this.kafkaServer =
                new JMKafkaServer(zookeeperConnect, OS.getAvailableLocalPort())
                        .start();
        this.bootstrapServers = kafkaServer.getKafkaServerConnect();
        sleep(3000);
        this.kafkaProducer = new JMKafkaProducer(bootstrapServers);
        this.inputTopic = kafkaProducer.getDefaultTopic();
        kafkaProducer.sendStringList(JMResources.readLines(FileName));

        jmMetricConfigManager = new JMMetricConfigManager();
        KafkaInputConfig kafkaInputConfig =
                new KafkaInputConfig("KafkaInput", this.bootstrapServers, false,
                        inputTopic);
        jmMetricConfigManager.insertInputConfig(kafkaInputConfig);
        this.kafkaInputPublisher =
                InputPublisherBuilder.build(kafkaInputConfig);
        System.out.println(JMJson.toJsonString(kafkaInputConfig));
        this.mutatingProcessor = new MutatingProcessor(jmMetricConfigManager
                .getMutatingConfig("apacheAccessLogSample"));
        this.outputTopic = "kafkaOut-test";
        KafkaOutputConfig kafkaOutputConfig =
                new KafkaOutputConfig("KafkaOutput", bootstrapServers,
                        "remoteHost", outputTopic);
        this.kafkaOutputSubscriber =
                OutputSubscriberBuilder.build(kafkaOutputConfig);
        System.out.println(JMJson.toJsonString(kafkaOutputConfig));
    }

    /**
     * Tear down.
     */
    @After
    public void tearDown() {
        kafkaProducer.close();
        kafkaInputPublisher.close();
        kafkaOutputSubscriber.close();
        kafkaConsumer.shutdown();
        kafkaServer.stop();
        embeddedZookeeper.stop();
        Optional.of(JMPath.getPath(JMZookeeperServer.DEFAULT_ZOOKEEPER_DIR))
                .filter(JMPath::exists)
                .ifPresent(JMPathOperation::deleteDir);
        Optional.of(JMPath.getPath(JMKafkaServer.DEFAULT_KAFKA_LOG))
                .filter(JMPath::exists)
                .ifPresent(JMPathOperation::deleteDir);
    }

    @Test
    public void start() {
        LongAdder lineCount = new LongAdder();

        kafkaInputPublisher
                .subscribeWith(JMSubscriberBuilder.getSOPLSubscriber())
                .subscribeWith(JMSubscriberBuilder
                        .build(list -> lineCount.add(list.size())))
                .subscribeWith(
                        mutatingProcessor.subscribeWith(kafkaOutputSubscriber))
                .start();

        LongAdder indexAdder = new LongAdder();
        this.kafkaConsumer =
                new JMKafkaConsumer(false, bootstrapServers, "test",
                        consumerRecord -> {
                            indexAdder.increment();
                            System.out.println(consumerRecord.value());
                        }, outputTopic).start();
        JMThread.sleep(6000);
        Assert.assertEquals(1024, lineCount.intValue());
        Assert.assertEquals(1024, indexAdder.intValue());

    }
}