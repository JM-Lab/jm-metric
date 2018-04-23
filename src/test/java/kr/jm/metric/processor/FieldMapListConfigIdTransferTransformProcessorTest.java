package kr.jm.metric.processor;

import kr.jm.metric.config.ApacheAccessLogMetricConfig;
import kr.jm.metric.config.MetricConfigManager;
import kr.jm.metric.config.field.FieldConfig;
import kr.jm.metric.data.ConfigIdTransfer;
import kr.jm.metric.data.FieldMap;
import kr.jm.metric.publisher.StringBulkWaitingTransferSubmissionPublisher;
import kr.jm.metric.publisher.StringListTransferSubmissionPublisherInterface;
import kr.jm.metric.subscriber.OutputSubscriber;
import kr.jm.metric.subscriber.OutputSubscriberBuilder;
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
    private MetricConfigManager metricConfigManager;

    @Before
    public void setUp() {
        this.metricConfigManager = new MetricConfigManager();
        this.fieldMapListConfigIdTransferTransformProcessor =
                new FieldMapListConfigIdTransferTransformProcessor(
                        metricConfigManager);
        String configFilePathOrClasspath = "JMMetricConfig.json";
        String stringFromClasspathOrFilePath = JMResources
                .getStringWithClasspathOrFilePath(configFilePathOrClasspath);
        System.out.println(stringFromClasspathOrFilePath);
        this.metricConfigManager
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
        ApacheAccessLogMetricConfig apacheAccessLogSample =
                (ApacheAccessLogMetricConfig) metricConfigManager
                        .getConfig("apacheAccessLogSample");
        FieldConfig fieldConfig = metricConfigManager
                .getConfig("nginxAccessLogSample")
                .getFieldConfig();
        ApacheAccessLogMetricConfig apacheCommonLogMetricConfig =
                new ApacheAccessLogMetricConfig("apache", fieldConfig,
                        apacheAccessLogSample.getFormat());
        metricConfigManager
                .insertConfig(apacheCommonLogMetricConfig);
        ApacheAccessLogMetricConfig apacheCommonLogMetricConfig2 =
                new ApacheAccessLogMetricConfig("apache2", fieldConfig,
                        apacheAccessLogSample.getFormat());
        metricConfigManager
                .insertConfig(apacheCommonLogMetricConfig2);
        Optional<Path> pathAsOpt1 =
                JMPathOperation.createTempFilePathAsOpt(Paths.get("test1.txt"));
        assertTrue(pathAsOpt1.isPresent());
        Path path1 = pathAsOpt1.get();
        OutputSubscriber<ConfigIdTransfer<List<FieldMap>>>
                fileOutputSubscriber1 = OutputSubscriberBuilder
                .buildFileToJsonString(path1.toAbsolutePath().toString());


        metricConfigManager
                .bindDataIdToConfigId(FileName, "apache");
        metricConfigManager
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