package kr.jm.metric.processor;

import kr.jm.metric.config.mutating.ApacheAccessLogMutatingConfig;
import kr.jm.metric.config.mutating.MutatingConfigManager;
import kr.jm.metric.config.mutating.field.FieldConfig;
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

public class MutatingProcessorTest {
    static {
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "DEBUG");
    }

    private static final String ResourceName = "webAccessLogSample.txt";
    private MutatingConfigManager mutatingConfigManager;
    private MutatingProcessor mutatingProcessor;

    @Before
    public void setUp() {
        String configFilePathOrClasspath = "MutatingConfig.json";
        this.mutatingConfigManager =
                new MutatingConfigManager(configFilePathOrClasspath);

        String stringFromClasspathOrFilePath = JMResources
                .getStringWithClasspathOrFilePath(configFilePathOrClasspath);
        System.out.println(stringFromClasspathOrFilePath);
        this.mutatingConfigManager
                .insertConfigMapList(JMJson.withRestOrFilePathOrClasspath(
                        configFilePathOrClasspath,
                        JMJson.LIST_MAP_TYPE_REFERENCE));
    }

    @After
    public void tearDown() {
        this.mutatingProcessor.close();
    }

    @Test
    public void testInput() {
        LongAdder count = new LongAdder();
        LongAdder lineCount = new LongAdder();
        ApacheAccessLogMutatingConfig apacheAccessLogSample =
                (ApacheAccessLogMutatingConfig) mutatingConfigManager
                        .getConfig("apacheAccessLogSample");
        FieldConfig fieldConfig = mutatingConfigManager
                .getConfig("nginxAccessLogSample")
                .getFieldConfig();
        ApacheAccessLogMutatingConfig apacheCommonLogMetricConfig =
                new ApacheAccessLogMutatingConfig("apache", fieldConfig,
                        apacheAccessLogSample.getFormat());
        mutatingConfigManager
                .insertConfig(apacheCommonLogMetricConfig);
        ApacheAccessLogMutatingConfig apacheCommonLogMetricConfig2 =
                new ApacheAccessLogMutatingConfig("apache2", fieldConfig,
                        apacheAccessLogSample.getFormat());
        mutatingConfigManager
                .insertConfig(apacheCommonLogMetricConfig2);
        Optional<Path> pathAsOpt1 =
                JMPathOperation.createTempFilePathAsOpt(Paths.get("test1.txt"));
        assertTrue(pathAsOpt1.isPresent());
        Path path1 = pathAsOpt1.get();
        OutputSubscriber fileOutputSubscriber1 = OutputSubscriberBuilder
                .buildFileOutput(path1.toAbsolutePath().toString(),
                        configIdTransfers -> List.of(configIdTransfers));

        this.mutatingProcessor =
                new MutatingProcessor(
                        mutatingConfigManager.getConfig("apache"));
        mutatingProcessor.subscribe(
                JMSubscriberBuilder.getJsonStringSOPLSubscriber());
        mutatingProcessor.subscribe(
                JMSubscriberBuilder
                        .build(configIdTransfer -> count.increment()));
        mutatingProcessor.subscribe(
                JMSubscriberBuilder.build(configIdTransferList -> lineCount
                        .add(configIdTransferList.size())));
        mutatingProcessor.subscribe(fileOutputSubscriber1);
        InputPublisherBuilder.buildResourceInput(ResourceName)
                .subscribeWith(mutatingProcessor)
                .start();
        JMThread.sleep(3000);
        fileOutputSubscriber1.close();
        mutatingProcessor.close();
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