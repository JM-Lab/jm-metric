package kr.jm.metric.mutator.processor;

import kr.jm.metric.config.mutator.ApacheAccessLogMutatorConfig;
import kr.jm.metric.config.mutator.MutatorConfigManager;
import kr.jm.metric.config.mutator.field.FieldConfig;
import kr.jm.metric.input.publisher.InputPublisher;
import kr.jm.metric.input.publisher.InputPublisherBuilder;
import kr.jm.metric.output.subscriber.OutputSubscriber;
import kr.jm.metric.output.subscriber.OutputSubscriberBuilder;
import kr.jm.utils.flow.subscriber.JMSubscriberBuilder;
import kr.jm.utils.helper.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.LongAdder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MutatorProcessorTest {
    static {
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "DEBUG");
    }

    private static final String ResourceName = "webAccessLogSample.txt";
    private MutatorConfigManager mutatorConfigManager;
    private MutatorProcessor mutatorProcessor;

    @Before
    public void setUp() {
        String configFilePathOrClasspath = "Mutator.json";
        this.mutatorConfigManager =
                new MutatorConfigManager(configFilePathOrClasspath);

        String stringFromClasspathOrFilePath = JMResources
                .getStringWithClasspathOrFilePath(configFilePathOrClasspath);
        System.out.println(stringFromClasspathOrFilePath);
        this.mutatorConfigManager
                .insertConfigMapList(JMJson.withRestOrFilePathOrClasspath(
                        configFilePathOrClasspath,
                        JMJson.LIST_MAP_TYPE_REFERENCE));
    }

    @After
    public void tearDown() {
        this.mutatorProcessor.close();
    }

    @Test
    public void testInput() {
        LongAdder count = new LongAdder();
        LongAdder lineCount = new LongAdder();
        ApacheAccessLogMutatorConfig apacheAccessLogSample =
                (ApacheAccessLogMutatorConfig) mutatorConfigManager
                        .getConfig("ApacheAccessLog");
        FieldConfig fieldConfig = mutatorConfigManager
                .getConfig("CombinedLogFormat")
                .getFieldConfig();
        ApacheAccessLogMutatorConfig apacheCommonLogMetricConfig =
                new ApacheAccessLogMutatorConfig("apache", fieldConfig,
                        apacheAccessLogSample.getFormat());
        mutatorConfigManager
                .insertConfig(apacheCommonLogMetricConfig);
        ApacheAccessLogMutatorConfig apacheCommonLogMetricConfig2 =
                new ApacheAccessLogMutatorConfig("apache2", fieldConfig,
                        apacheAccessLogSample.getFormat());
        mutatorConfigManager
                .insertConfig(apacheCommonLogMetricConfig2);
        Optional<Path> pathAsOpt1 =
                JMPathOperation.createTempFilePathAsOpt(Paths.get("test1.txt"));
        assertTrue(pathAsOpt1.isPresent());
        Path path1 = pathAsOpt1.get();
        OutputSubscriber fileOutputSubscriber1 = OutputSubscriberBuilder
                .buildFileOutput(path1.toAbsolutePath().toString(),
                        mutatorIdTransfers -> List.of(mutatorIdTransfers));

        this.mutatorProcessor = MutatorProcessorBuilder
                .build(mutatorConfigManager.getConfig("apache"));
        mutatorProcessor.subscribe(
                JMSubscriberBuilder.getJsonStringSOPLSubscriber());
        mutatorProcessor.subscribe(
                JMSubscriberBuilder
                        .build(mutatorIdTransfer -> count.increment()));
        mutatorProcessor.subscribe(
                JMSubscriberBuilder.build(mutatorIdTransferList -> lineCount
                        .add(mutatorIdTransferList.size())));
        mutatorProcessor.subscribe(fileOutputSubscriber1);
        InputPublisher inputPublisher =
                InputPublisherBuilder.buildResourceInput(ResourceName)
                        .subscribeWith(mutatorProcessor).start();
        JMThread.sleep(3000);
        inputPublisher.close();
        fileOutputSubscriber1.close();
        mutatorProcessor.close();
        System.out.println(count);
        assertEquals(11, count.longValue());
        System.out.println(lineCount);
        assertEquals(1024, lineCount.longValue());

        System.out.println(JMFiles.readString(path1));
        List<String> readLineList = JMFiles.readLines(path1);
        System.out.println(readLineList.size());
        assertEquals(11, readLineList.size());

    }
}