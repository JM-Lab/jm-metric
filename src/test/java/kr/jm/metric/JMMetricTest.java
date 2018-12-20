package kr.jm.metric;

import kr.jm.metric.config.ConfigInterface;
import kr.jm.metric.config.JMMetricConfigManager;
import kr.jm.metric.config.input.FileInputConfig;
import kr.jm.metric.config.mutator.*;
import kr.jm.metric.config.mutator.field.DateFormatType;
import kr.jm.metric.config.mutator.field.FieldConfig;
import kr.jm.metric.data.FieldMap;
import kr.jm.metric.data.Transfer;
import kr.jm.metric.output.subscriber.OutputSubscriber;
import kr.jm.metric.output.subscriber.OutputSubscriberBuilder;
import kr.jm.utils.JMWordSplitter;
import kr.jm.utils.flow.subscriber.JMSubscriberBuilder;
import kr.jm.utils.helper.*;
import kr.jm.utils.stats.generator.WordCountGenerator;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.LongAdder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class JMMetricTest {

    private JMMetric jmMetric;

    @After
    public void tearDown() {
        this.jmMetric.close();
    }

    @Test
    public void testMutatorConfig() {
        this.jmMetric = new JMMetric("NginxAccessLog");
        JMMetricConfigManager jmMetricConfigManager =
                jmMetric.getJmMetricConfigManager();
        MutatorConfigInterface apacheCommonLog =
                jmMetricConfigManager
                        .getMutatorConfig("ApacheAccessLog");
        System.out.println(JMJson.toJsonString(apacheCommonLog));
        MutatorConfigInterface nginxAccessLogSampleConfig =
                jmMetricConfigManager
                        .getMutatorConfig("NginxAccessLog");
        System.out.println(JMJson.toJsonString(nginxAccessLogSampleConfig));
        assertEquals(DateFormatType.CUSTOM,
                nginxAccessLogSampleConfig.getFieldConfig().getDateFormat()
                        .get("timeLocal").getDateFormatType());
        String targetString =
                "127.0.0.1 - frank [10/Oct/2000:13:55:36 -0700] \"GET /apache_pb.gif HTTP/1.0\" 200 2326 \"http://www.example.com/start.html\" \"Mozilla/4.08 [en] (Win98; I ;Nav)\"";
        Map fieldStringMap = nginxAccessLogSampleConfig
                .buildMutator().mutate(targetString);
        System.out.println(fieldStringMap);
        Assert.assertEquals(
                "{request=GET /apache_pb.gif HTTP/1.0, referer=http://www.example.com/start.html, remoteHost=127.0.0.1, requestUrl=/apache_pb.gif, requestMethod=GET, alterFieldName=127.0.0.1|/apache_pb.gif, sizeByte=2326.0, userAgent=Mozilla/4.08 [en] (Win98; I ;Nav), requestProtocol=HTTP/1.0, timeLocal=2000-10-10T20:55:36.000Z, statusCode=200}",
                fieldStringMap.toString());
        Map<String, Map<String, Object>> nestedFormat =
                nginxAccessLogSampleConfig.getFieldConfig().getFormat();
        System.out.println(nestedFormat);
        Map<String, Object> requestMutatorConfigMap =
                nestedFormat.get("request");
        AbstractMutatorConfig mutatorConfig = ConfigInterface
                .transformConfig(requestMutatorConfigMap,
                        MutatorConfigType.valueOf(
                                requestMutatorConfigMap.get("mutatorConfigType")
                                        .toString()).getConfigClass());
        assertTrue(mutatorConfig instanceof DelimiterMutatorConfig);
        System.out.println(mutatorConfig.getClass());
    }

    @Test
    public void testInput() {
        LongAdder count = new LongAdder();
        LongAdder lineCount = new LongAdder();

        JMMetricConfigManager jmMetricConfigManager =
                new JMMetricConfigManager();
        ApacheAccessLogMutatorConfig apacheAccessLogSample =
                (ApacheAccessLogMutatorConfig) jmMetricConfigManager
                        .getMutatorConfig("ApacheAccessLog");
        FieldConfig fieldConfig =
                jmMetricConfigManager.getMutatorConfig("NginxAccessLog")
                        .getFieldConfig();
        ApacheAccessLogMutatorConfig mutatorConfig =
                new ApacheAccessLogMutatorConfig("apache", fieldConfig,
                        apacheAccessLogSample.getFormat());
        jmMetricConfigManager.insertMutatorConfig(mutatorConfig);
        ApacheAccessLogMutatorConfig mutatorConfig2 =
                new ApacheAccessLogMutatorConfig("apache2", fieldConfig,
                        apacheAccessLogSample.getFormat());
        jmMetricConfigManager.insertMutatorConfig(mutatorConfig2);

        String fileName =
                JMResources.getURL("webAccessLogSample.txt").getPath();
        jmMetricConfigManager
                .insertInputConfig(
                        new FileInputConfig(fileName, 100, fileName));
        Optional<Path> pathAsOpt1 =
                JMPathOperation.createTempFilePathAsOpt(Paths.get("test1.txt"));
        assertTrue(pathAsOpt1.isPresent());
        Path path1 = pathAsOpt1.get();
        OutputSubscriber fileOutputSubscriber1 = OutputSubscriberBuilder
                .buildFileOutput(path1.toAbsolutePath().toString());


        jmMetric = new JMMetric(jmMetricConfigManager, fileName, "apache")
                .subscribeWith(JMSubscriberBuilder.getSOPLSubscriber())
                .subscribeWith(fileOutputSubscriber1)
                .consumeWith(transferList -> count.increment())
                .consumeWith(transferList -> lineCount
                        .add(transferList.size()))
                .start();
        JMThread.sleep(5000);

        fileOutputSubscriber1.close();

        System.out.println(count);
        assertEquals(11, count.longValue());
        System.out.println(lineCount);
        assertEquals(1024, lineCount.longValue());

        System.out.println(JMFiles.readString(path1));
        List<String> readLineList = JMFiles.readLines(path1);
        System.out.println(readLineList.size());
        assertEquals(1024, readLineList.size());
    }

    @Test
    public void testWithCustomFunction() {
        this.jmMetric = new JMMetric("NginxAccessLog");
        List<Transfer<FieldMap>> resultList = new ArrayList<>();
        jmMetric.withCustomFunction(fieldMapTransfer -> {
            FieldMap fieldMap = fieldMapTransfer.getData();
            fieldMap.put("wordCount", WordCountGenerator
                    .buildCountMap(JMWordSplitter.splitAsStream(
                            fieldMap.get("userAgent").toString())));
            fieldMap.remove("meta.processTimestamp");
            return fieldMap;
        }).subscribeWith(JMSubscriberBuilder.getSOPLSubscriber())
                .consumeWith(mutatorIdTransferList -> resultList
                        .addAll(mutatorIdTransferList)).start();
        String targetString =
                "127.0.0.1 - frank [10/Oct/2000:13:55:36 -0700] \"GET /apache_pb.gif HTTP/1.0\" 200 2326 \"http://www.example.com/start.html\" \"Mozilla/4.08 [en] (Win98; I ;Nav)\"";
        jmMetric.testInput(targetString);
        JMThread.sleep(3000);
        FieldMap fieldStringMap = resultList.get(0).getData();
        System.out.println(fieldStringMap);
        Assert.assertEquals(
                "{request=GET /apache_pb.gif HTTP/1.0, referer=http://www.example.com/start.html, remoteHost=127.0.0.1, wordCount.I=1, requestMethod=GET, wordCount.en=1, meta.field.custom.customObject.bool=false, userAgent=Mozilla/4.08 [en] (Win98; I ;Nav), wordCount.08=1, wordCount.4=1, meta.inputId=TestInput, meta.field.custom.customKey=customValue, wordCount.Nav=1, requestUrl=/apache_pb.gif, meta.field.unit.timeLocal=Second, meta.field.custom.customList=[hello, world], alterFieldName=127.0.0.1|/apache_pb.gif, wordCount.Mozilla=1, sizeByte=2326.0, wordCount.Win98=1, requestProtocol=HTTP/1.0, timeLocal=2000-10-10T20:55:36.000Z, statusCode=200}",
                fieldStringMap.toString());
        System.out.println(JMJson.toJsonString(resultList));

    }

    @Test
    public void test() {
        jmMetric = new JMMetric().start().testInput("Hello World !!!");
        JMThread.sleep(3000);
    }
}