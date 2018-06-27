package kr.jm.metric;

import kr.jm.metric.config.ConfigInterface;
import kr.jm.metric.config.mutating.ApacheAccessLogMutatingConfig;
import kr.jm.metric.config.mutating.DelimiterMutatingConfig;
import kr.jm.metric.config.mutating.MutatingConfig;
import kr.jm.metric.config.mutating.field.DateFormatType;
import kr.jm.metric.config.mutating.field.FieldConfig;
import kr.jm.metric.input.publisher.InputPublisherBuilder;
import kr.jm.metric.output.subscriber.OutputSubscriber;
import kr.jm.metric.output.subscriber.OutputSubscriberBuilder;
import kr.jm.utils.flow.subscriber.JMSubscriberBuilder;
import kr.jm.utils.helper.JMFiles;
import kr.jm.utils.helper.JMJson;
import kr.jm.utils.helper.JMPathOperation;
import kr.jm.utils.helper.JMThread;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.LongAdder;

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
        this.jmMetric = new JMMetric();
    }

    @After
    public void tearDown() {
        this.jmMetric.close();
    }

    @Test
    public void testMutatingConfig() {
        MutatingConfig apacheCommonLog =
                jmMetric.getJmMetricConfigManager()
                        .getMutatingConfig("apacheAccessLogSample");
        System.out.println(JMJson.toJsonString(apacheCommonLog));
        MutatingConfig nginxAccessLogSample =
                jmMetric.getJmMetricConfigManager()
                        .getMutatingConfig("nginxAccessLogSample");
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
        requestMutatingConfig =
                ConfigInterface.transformConfig(requestMutatingConfig,
                        requestMutatingConfig.getMutatingConfigType()
                                .getConfigClass());
        assertTrue(requestMutatingConfig instanceof DelimiterMutatingConfig);
        System.out.println(requestMutatingConfig.getClass());
    }

    @Test
    public void testInput() {
        LongAdder count = new LongAdder();
        LongAdder lineCount = new LongAdder();
        JMMetricConfigManager jmMetricConfigManager = new
                JMMetricConfigManager();
        ApacheAccessLogMutatingConfig apacheAccessLogSample =
                (ApacheAccessLogMutatingConfig) jmMetricConfigManager
                        .getMutatingConfig
                                ("apacheAccessLogSample");
        FieldConfig fieldConfig =
                jmMetricConfigManager.getMutatingConfig("nginxAccessLogSample")
                        .getFieldConfig();
        ApacheAccessLogMutatingConfig mutatingConfig =
                new ApacheAccessLogMutatingConfig("apache", fieldConfig,
                        apacheAccessLogSample.getFormat());
        jmMetricConfigManager.insertMutatingConfig(mutatingConfig);
        ApacheAccessLogMutatingConfig mutatingConfig2 =
                new ApacheAccessLogMutatingConfig("apache2", fieldConfig,
                        apacheAccessLogSample.getFormat());
        jmMetricConfigManager.insertMutatingConfig(mutatingConfig2);
        Optional<Path> pathAsOpt1 =
                JMPathOperation.createTempFilePathAsOpt(Paths.get("test1.txt"));
        assertTrue(pathAsOpt1.isPresent());
        Path path1 = pathAsOpt1.get();
        OutputSubscriber fileOutputSubscriber1 = OutputSubscriberBuilder
                .buildFileOutput(path1.toAbsolutePath().toString());

        jmMetric = new JMMetric(jmMetricConfigManager, null, "apache");

        jmMetric.subscribeWith(JMSubscriberBuilder.getSOPLSubscriber())
                .subscribeWith(fileOutputSubscriber1)
                .consumeWith(configIdTransferList -> count.increment())
                .consumeWith(configIdTransferList -> lineCount
                        .add(configIdTransferList.size()));
        InputPublisherBuilder.buildResourceInput(FileName)
                .subscribeWith(jmMetric).start();
        JMThread.sleep(3000);
        fileOutputSubscriber1.close();
        jmMetric.close();
        System.out.println(count);
        assertEquals(11, count.longValue());
        System.out.println(lineCount);
        assertEquals(1024, lineCount.longValue());

        System.out.println(JMFiles.readString(path1));
        List<String> readLineList = JMFiles.readLines(path1);
        System.out.println(readLineList.size());
        assertEquals(1024, readLineList.size());

    }

}