package kr.jm.metric.processor;

import kr.jm.metric.config.mutating.ApacheAccessLogMutatingConfig;
import kr.jm.metric.config.mutating.MutatingConfigManager;
import kr.jm.metric.config.mutating.field.FieldConfig;
import kr.jm.metric.data.ConfigIdTransfer;
import kr.jm.metric.data.FieldMap;
import kr.jm.metric.publisher.StringBulkWaitingTransferSubmissionPublisher;
import kr.jm.metric.publisher.StringListTransferSubmissionPublisherInterface;
import kr.jm.utils.flow.subscriber.JMFileSubscriber;
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

public class FieldMapListConfigIdTransferTransformProcessorTest {
    static {
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "DEBUG");
    }

    private static final String FileName = "webAccessLogSample.txt";
    private FieldMapListConfigIdTransferTransformProcessor
            fieldMapListConfigIdTransferTransformProcessor;
    private MutatingConfigManager mutatingConfigManager;

    @Before
    public void setUp() {
        this.mutatingConfigManager = new MutatingConfigManager();
        this.fieldMapListConfigIdTransferTransformProcessor =
                new FieldMapListConfigIdTransferTransformProcessor(
                        mutatingConfigManager);
        String configFilePathOrClasspath = "MutatingConfig.json";
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
        this.fieldMapListConfigIdTransferTransformProcessor.close();
    }

    @Test
    public void testInput() {
        LongAdder count = new LongAdder();
        LongAdder lineCount = new LongAdder();
        ApacheAccessLogMutatingConfig apacheAccessLogSample =
                (ApacheAccessLogMutatingConfig) mutatingConfigManager
                        .getMutatingConfig("apacheAccessLogSample");
        FieldConfig fieldConfig = mutatingConfigManager
                .getMutatingConfig("nginxAccessLogSample")
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
        JMFileSubscriber<ConfigIdTransfer<List<FieldMap>>>
                fileOutputSubscriber1 =
                JMSubscriberBuilder.buildJsonStringFileSubscriber(
                        path1.toAbsolutePath().toString());


        mutatingConfigManager
                .bindDataIdToConfigId(FileName, "apache");
        mutatingConfigManager
                .bindDataIdToConfigId(FileName, "apache2");
        fieldMapListConfigIdTransferTransformProcessor.subscribe(
                JMSubscriberBuilder.getJsonStringSOPLSubscriber());
        fieldMapListConfigIdTransferTransformProcessor.subscribe(
                JMSubscriberBuilder
                        .build(configIdTransfer -> count.increment()));
        fieldMapListConfigIdTransferTransformProcessor.subscribe(
                JMSubscriberBuilder
                        .build(configIdTransfer -> lineCount
                                .add(configIdTransfer.getData().stream()
                                        .count())));
        fieldMapListConfigIdTransferTransformProcessor
                .subscribe(fileOutputSubscriber1);

        StringListTransferSubmissionPublisherInterface
                stringListTransferSubmissionPublisher =
                new StringBulkWaitingTransferSubmissionPublisher();
        stringListTransferSubmissionPublisher
                .subscribe(fieldMapListConfigIdTransferTransformProcessor);
        stringListTransferSubmissionPublisher.inputClasspath(FileName);
        JMThread.sleep(3000);
        fileOutputSubscriber1.close();
        fieldMapListConfigIdTransferTransformProcessor.close();
        System.out.println(count);
        assertEquals(22, count.longValue());
        System.out.println(lineCount);
        assertEquals(2048, lineCount.longValue());

        System.out.println(JMFiles.readString(path1));
        List<String> readLineList = JMFiles.readLines(path1);
        System.out.println(readLineList.size());
        assertEquals(22, readLineList.size());

    }
}