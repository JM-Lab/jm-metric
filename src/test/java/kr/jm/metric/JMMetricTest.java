package kr.jm.metric;

import kr.jm.metric.config.mutating.ApacheAccessLogMutatingConfig;
import kr.jm.metric.config.mutating.DelimiterMutatingConfig;
import kr.jm.metric.config.mutating.MutatingConfig;
import kr.jm.metric.config.mutating.field.DateFormatType;
import kr.jm.metric.config.mutating.field.FieldConfig;
import kr.jm.metric.data.FieldMap;
import kr.jm.metric.output.subscriber.OutputSubscriber;
import kr.jm.metric.output.subscriber.OutputSubscriberBuilder;
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
        String configFilePathOrClasspath = "MutatingConfig.json";
        String stringFromClasspathOrFilePath = JMResources
                .getStringWithClasspathOrFilePath(configFilePathOrClasspath);
        System.out.println(stringFromClasspathOrFilePath);
        this.jmMetric.getMutatingConfigManager()
                .loadConfig(configFilePathOrClasspath);
    }

    @After
    public void tearDown() {
        this.jmMetric.close();
    }

    @Test
    public void testMutatingConfig() {
        MutatingConfig apacheCommonLog =
                jmMetric.getMutatingConfig("apacheAccessLogSample");
        System.out.println(JMJson.toJsonString(apacheCommonLog));
        MutatingConfig nginxAccessLogSample =
                jmMetric.getMutatingConfig("nginxAccessLogSample");
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
        Map<String, MutatingConfig> nestedFormat =
                nginxAccessLogSample.getFieldConfig().getFormat();
        System.out.println(nestedFormat);
        MutatingConfig requestMutatingConfig = nestedFormat.get("request");
        requestMutatingConfig = requestMutatingConfig.getMutatingConfigType()
                .transform(requestMutatingConfig);
        assertTrue(requestMutatingConfig instanceof DelimiterMutatingConfig);
        System.out.println(requestMutatingConfig.getClass());
    }

    @Test
    public void testRemoveDataId() {
        MutatingConfig delimiterSampleMutatingConfig =
                jmMetric.getMutatingConfig("delimiterSample");
        System.out.println(delimiterSampleMutatingConfig.getBindDataIds());
        jmMetric.bindDataIdToConfigId("testData",
                "apacheAccessLogSample");
        List<String> dataIdList =
                jmMetric.getMutatingConfigMap().values().stream()
                        .map(MutatingConfig::getBindDataIds)
                        .flatMap(Set::stream)
                        .collect(Collectors.toList());
        System.out.println(dataIdList);
        List<String> inputConfigIdList =
                jmMetric.getConfigIdList("testData");
        System.out.println(inputConfigIdList);
        assertEquals(2, inputConfigIdList.size());
        System.out.println(dataIdList);
        jmMetric.removeDataId("testData");
        assertEquals(0, delimiterSampleMutatingConfig.getBindDataIds().size());
        dataIdList = jmMetric.getMutatingConfigMap().values().stream()
                .map(MutatingConfig::getBindDataIds).flatMap(Set::stream)
                .collect(Collectors.toList());
        System.out.println(dataIdList);
        assertEquals(0, jmMetric.getConfigIdList("testData").size());
    }

    @Test
    public void testInput() {
        LongAdder count = new LongAdder();
        LongAdder lineCount = new LongAdder();
        ApacheAccessLogMutatingConfig apacheAccessLogSample =
                (ApacheAccessLogMutatingConfig) jmMetric.getMutatingConfig
                        ("apacheAccessLogSample");
        FieldConfig fieldConfig =
                jmMetric.getMutatingConfig("nginxAccessLogSample")
                        .getFieldConfig();
        ApacheAccessLogMutatingConfig inputConfig =
                new ApacheAccessLogMutatingConfig("apache", fieldConfig,
                        apacheAccessLogSample.getFormat());
        jmMetric.insertConfig(inputConfig);
        ApacheAccessLogMutatingConfig inputConfig2 =
                new ApacheAccessLogMutatingConfig("apache2", fieldConfig,
                        apacheAccessLogSample.getFormat());
        jmMetric.insertConfig(inputConfig2);
        Optional<Path> pathAsOpt1 =
                JMPathOperation.createTempFilePathAsOpt(Paths.get("test1.txt"));
        assertTrue(pathAsOpt1.isPresent());
        Path path1 = pathAsOpt1.get();
        OutputSubscriber<List<FieldMap>> fileOutputSubscriber1 =
                OutputSubscriberBuilder
                        .buildJsonStringFile(path1.toAbsolutePath().toString());

        jmMetric.bindDataIdToConfigId(FileName, "apache")
                .bindDataIdToConfigId(FileName, "apache2");
        jmMetric.subscribeWith(JMSubscriberBuilder.getSOPLSubscriber())
                .subscribeWith(fileOutputSubscriber1)
                .consumeWith(fieldMapList -> count.increment())
                .consumeWith(fieldMapList -> lineCount
                        .add(fieldMapList.getData().stream().count()));
        jmMetric.inputClasspath(FileName);
        JMThread.sleep(6000);
        fileOutputSubscriber1.close();
        jmMetric.close();
        System.out.println(count);
        assertEquals(22, count.longValue());
        System.out.println(lineCount);
        assertEquals(2048, lineCount.longValue());

        System.out.println(JMFiles.readString(path1));
        List<String> readLineList = JMFiles.readLines(path1);
        System.out.println(readLineList.size());
        assertEquals(2048, readLineList.size());

    }

}