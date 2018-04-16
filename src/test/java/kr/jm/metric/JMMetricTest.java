package kr.jm.metric;

import kr.jm.metric.config.ApacheAccessLogMetricConfig;
import kr.jm.metric.config.DelimiterMetricConfig;
import kr.jm.metric.config.MetricConfig;
import kr.jm.metric.config.field.DateFormatType;
import kr.jm.metric.config.field.FieldConfig;
import kr.jm.metric.data.ConfigIdTransfer;
import kr.jm.metric.data.FieldMap;
import kr.jm.metric.processor.FieldMapListTransformerProcessor;
import kr.jm.metric.subscriber.output.FileOutputSubscriber;
import kr.jm.metric.subscriber.output.FileOutputSubscriberBuilder;
import kr.jm.utils.flow.subscriber.JMSubscriberBuilder;
import kr.jm.utils.helper.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class JMMetricTest {
    static {
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "DEBUG");
    }

    private static final String FileName = "webAccessLogSample.txt";
    private JMMetric jmMetric;

    @Before
    public void setUp() {
        this.jmMetric = new JMMetric(true);
        String configFilePathOrClasspath = "JMMetricConfig.json";
        String stringFromClasspathOrFilePath = JMResources
                .getStringWithClasspathOrFilePath(configFilePathOrClasspath);
        System.out.println(stringFromClasspathOrFilePath);
        this.jmMetric.loadConfig(configFilePathOrClasspath);
    }

    @After
    public void tearDown() {
        this.jmMetric.close();
    }

    @Test
    public void loadMetricConfig() {
        MetricConfig apacheCommonLog =
                jmMetric.getConfig("apacheAccessLogSample");
        System.out.println(JMJson.toJsonString(apacheCommonLog));
        MetricConfig nginxAccessLogSample =
                jmMetric.getConfig("nginxAccessLogSample");
        System.out.println(JMJson.toJsonString(nginxAccessLogSample));
        assertEquals(DateFormatType.CUSTOM,
                nginxAccessLogSample.getFieldConfig().getDateFormat()
                        .get("timestamp").getDateFormatType());
        String targetString =
                "127.0.0.1 - frank [10/Oct/2000:13:55:36 -0700] \"GET /apache_pb.gif HTTP/1.0\" 200 2326 \"http://www.example.com/start.html\" \"Mozilla/4.08 [en] (Win98; I ;Nav)\"";
        Map<String, String> fieldStringMap =
                nginxAccessLogSample.getMetricBuilder()
                        .buildFieldObjectMap(nginxAccessLogSample,
                                targetString);
        System.out.println(fieldStringMap);
        Assert.assertEquals(
                "{remoteUser=frank, request=GET /apache_pb.gif HTTP/1.0, referer=http://www.example.com/start.html, remoteHost=127.0.0.1, sizeByte=2326, userAgent=Mozilla/4.08 [en] (Win98; I ;Nav), timestamp=10/Oct/2000:13:55:36 -0700, statusCode=200}",
                fieldStringMap.toString());
        Map<String, MetricConfig> nestedFormat =
                nginxAccessLogSample.getFieldConfig().getFormat();
        System.out.println(nestedFormat);
        MetricConfig requestMetricConfig = nestedFormat.get("request");
        requestMetricConfig = requestMetricConfig.getMetricConfigType()
                .transform(requestMetricConfig);
        assertTrue(requestMetricConfig instanceof DelimiterMetricConfig);
        System.out.println(requestMetricConfig.getClass());
    }

    @Test
    public void testRemoveDataId() {
        MetricConfig delimiterSampleMetricConfig =
                jmMetric.getConfig("delimiterSample");
        System.out.println(delimiterSampleMetricConfig.getBindDataIds());
        jmMetric.bindDataIdToConfigId("testData",
                "apacheAccessLogSample");
        List<String> dataIdList = jmMetric.getConfigList().stream().map
                (MetricConfig::getBindDataIds).flatMap(Set::stream)
                .collect(Collectors.toList());
        System.out.println(dataIdList);
        List<String> inputConfigIdList =
                jmMetric.getConfigIdList("testData");
        System.out.println(inputConfigIdList);
        assertEquals(2, inputConfigIdList.size());
        System.out.println(dataIdList);
        jmMetric.removeDataId("testData");
        assertEquals(0, delimiterSampleMetricConfig.getBindDataIds().size());
        dataIdList = jmMetric.getConfigList().stream().map
                (MetricConfig::getBindDataIds).flatMap(Set::stream)
                .collect(Collectors.toList());
        System.out.println(dataIdList);
        assertEquals(0, jmMetric.getConfigIdList("testData").size());
    }

    @Test
    public void testInput() {
        LongAdder count = new LongAdder();
        LongAdder lineCount = new LongAdder();
        ApacheAccessLogMetricConfig apacheAccessLogSample =
                (ApacheAccessLogMetricConfig) jmMetric.getConfig
                        ("apacheAccessLogSample");
        FieldConfig fieldConfig = jmMetric.getConfig("nginxAccessLogSample")
                .getFieldConfig();
        ApacheAccessLogMetricConfig inputConfig =
                new ApacheAccessLogMetricConfig("apache", fieldConfig,
                        apacheAccessLogSample.getFormat());
        jmMetric.insertConfig(inputConfig);
        ApacheAccessLogMetricConfig inputConfig2 =
                new ApacheAccessLogMetricConfig("apache2", fieldConfig,
                        apacheAccessLogSample.getFormat());
        jmMetric.insertConfig(inputConfig2);
        Optional<Path> pathAsOpt1 =
                JMPathOperation.createTempFilePathAsOpt(Paths.get("test1.txt"));
        assertTrue(pathAsOpt1.isPresent());
        Path path1 = pathAsOpt1.get();
        FileOutputSubscriber<ConfigIdTransfer<List<FieldMap>>>
                fileOutputSubscriber1 = FileOutputSubscriberBuilder
                .buildJsonString(path1.toAbsolutePath().toString());
        Optional<Path> pathAsOpt2 =
                JMPathOperation.createTempFilePathAsOpt(Paths.get("test2.txt"));
        assertTrue(pathAsOpt2.isPresent());
        Path path2 = pathAsOpt2.get();
        FieldMapListTransformerProcessor
                fieldMapListTransformerProcessor =
                new FieldMapListTransformerProcessor();
        FileOutputSubscriber<List<FieldMap>> fileOutputSubscriber2 =
                fieldMapListTransformerProcessor
                        .subscribeAndReturnSubcriber(FileOutputSubscriberBuilder
                                .buildJsonStringList(path2.toString(),
                                        list -> list));

        jmMetric.bindDataIdToConfigId(FileName, "apache")
                .bindDataIdToConfigId(FileName, "apache2");
        jmMetric.subscribeWith(JMSubscriberBuilder.getSOPLSubscriber())
                .consumeWith(fieldMapList -> count.increment())
                .consumeWith(fieldMapList -> lineCount.add(fieldMapList.stream()
                        .count()));
        jmMetric.subscribeConfigIdTransferWith(fileOutputSubscriber1)
                .subscribeConfigIdTransferWith(
                        fieldMapListTransformerProcessor);
        jmMetric.inputClasspath(FileName);
        JMThread.sleep(3000);
        fileOutputSubscriber1.close();
        fileOutputSubscriber2.close();
        jmMetric.close();
        System.out.println(count);
        assertEquals(22, count.longValue());
        System.out.println(lineCount);
        assertEquals(2048, lineCount.longValue());

        System.out.println(JMFiles.readString(path1));
        List<String> readLineList = JMFiles.readLines(path1);
        System.out.println(readLineList.size());
        assertEquals(22, readLineList.size());

        System.out.println(JMFiles.readString(path2));
        List<String> readLineList2 = JMFiles.readLines(path2);
        System.out.println(readLineList2.size());
        assertEquals(2048, readLineList2.size());

    }

}