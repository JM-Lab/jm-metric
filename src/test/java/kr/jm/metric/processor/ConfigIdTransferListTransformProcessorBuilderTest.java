package kr.jm.metric.processor;

import kr.jm.metric.config.MetricConfigManager;
import kr.jm.metric.config.ApacheAccessLogMetricConfig;
import kr.jm.metric.data.ConfigIdTransfer;
import kr.jm.metric.data.FieldMap;
import kr.jm.metric.data.Transfer;
import kr.jm.metric.input.publisher.TransferSubmissionPublisher;
import kr.jm.metric.transformer.FieldMapConfigIdTransferListTransformer;
import kr.jm.metric.transformer.FieldMapListConfigIdTransferListTransformer;
import kr.jm.utils.collections.JMListMap;
import kr.jm.utils.flow.processor.JMConcurrentTransformProcessor;
import kr.jm.utils.flow.processor.JMTransformProcessorBuilder;
import kr.jm.utils.flow.subscriber.JMSubscriberBuilder;
import kr.jm.utils.helper.JMJson;
import kr.jm.utils.helper.JMResources;
import kr.jm.utils.helper.JMThread;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ConfigIdTransferListTransformProcessorBuilderTest {

    private static final String TEST_ID = "testId";
    private static final String CONFIG_ID = "apacheAccessLogSample";
    private List<String> lineList;
    private JMConcurrentTransformProcessor<Transfer<String>, List<ConfigIdTransfer<FieldMap>>>
            fieldMapConfigIdTransferListTransformProcessor;
    private JMConcurrentTransformProcessor<Transfer<List<String>>, List<ConfigIdTransfer<List<FieldMap>>>>
            fieldMapListConfigIdTransferListTransformProcessor;
    private TransferSubmissionPublisher<List<String>>
            transferSubmissionPublisher;

    @Before
    public void setUp() throws Exception {
        this.lineList = JMResources.readLines("webAccessLogSample.txt");
        MetricConfigManager metricConfigManager = new MetricConfigManager(
                List.of(new ApacheAccessLogMetricConfig(CONFIG_ID,
                        "%h %l %u %t \"%r\" %>s %b " +
                                "\"%{Referer}i\" " +
                                "\"%{User-agent}i\" %D")
                        .withBindDataIds(TEST_ID, TEST_ID + "1",
                                TEST_ID + "2"))
        );
        this.fieldMapConfigIdTransferListTransformProcessor =
                JMTransformProcessorBuilder.buildWithThreadPool(
                        new FieldMapConfigIdTransferListTransformer(
                                metricConfigManager));
        this.fieldMapListConfigIdTransferListTransformProcessor
                = JMTransformProcessorBuilder.buildWithThreadPool(
                new FieldMapListConfigIdTransferListTransformer(
                        metricConfigManager));
        this.transferSubmissionPublisher = new TransferSubmissionPublisher<>();
        this.transferSubmissionPublisher
                .subscribe(fieldMapListConfigIdTransferListTransformProcessor);
    }

    @Test
    public void testProcess() throws ExecutionException, InterruptedException {
        TransferSubmissionPublisher<String> transferSubmissionPublisher =
                new TransferSubmissionPublisher<>();
        transferSubmissionPublisher
                .subscribe(fieldMapConfigIdTransferListTransformProcessor);
        CompletableFuture<List<Transfer<List<FieldMap>>>>
                dataTransferCompletableFuture1 = new CompletableFuture<>();
        Transfer<List<FieldMap>> finalTransfer =
                new Transfer<>(TEST_ID, new ArrayList<>());
        fieldMapConfigIdTransferListTransformProcessor.subscribe(
                JMSubscriberBuilder
                        .build(configIdFieldMapDataTransferMap -> configIdFieldMapDataTransferMap
                                .stream().map(Transfer::getData)
                                .forEach(finalTransfer.getData()::add)));
        dataTransferCompletableFuture1
                .complete(List.of(finalTransfer));
        lineList.forEach(
                line -> transferSubmissionPublisher.submit(TEST_ID, line));


        CompletableFuture<List<ConfigIdTransfer<List<FieldMap>>>>
                dataTransferCompletableFuture2 = new CompletableFuture<>();
        fieldMapListConfigIdTransferListTransformProcessor
                .subscribe(JMSubscriberBuilder
                        .build(dataTransferCompletableFuture2::complete));
        this.transferSubmissionPublisher.submit(TEST_ID, lineList);
        List<ConfigIdTransfer<List<FieldMap>>> dataTransferMap2 =
                dataTransferCompletableFuture2.get();
        System.out.println(JMJson.toJsonString(dataTransferMap2));
        Assert.assertEquals(1, dataTransferMap2.size());
        Assert.assertEquals(1024,
                dataTransferMap2.get(0).getData().size());

        System.out.println(
                JMJson.toJsonString(dataTransferCompletableFuture1.get()));
        Transfer<List<FieldMap>> transfer1 =
                dataTransferCompletableFuture1.get().get(0);
        Assert.assertEquals(1024, transfer1.getData().size());
    }

    @Test
    public void testProcessInParallel() throws ExecutionException,
            InterruptedException {
        JMListMap<String, Transfer<List<FieldMap>>> resultMap =
                new JMListMap<>();
        fieldMapListConfigIdTransferListTransformProcessor
                .subscribe(JMSubscriberBuilder
                        .build(configIdFieldMapListDataTransferList -> configIdFieldMapListDataTransferList
                                .stream().forEach(dataTransfer -> resultMap
                                        .add(dataTransfer.getDataId(),
                                                dataTransfer))));
        JMThread.runAsync(() ->
                transferSubmissionPublisher.submit(TEST_ID + 1, lineList));
        JMThread.runAsync(() ->
                transferSubmissionPublisher.submit(TEST_ID + 2, lineList));
        JMThread.sleep(1000);
        System.out.println(JMJson.toJsonString(resultMap));
        Assert.assertEquals(2, resultMap.size());
        List<FieldMap> resultData1 =
                resultMap.get(TEST_ID + 1).get(0).getData();
        List<FieldMap> resultData2 =
                resultMap.get(TEST_ID + 1).get(0).getData();
        Assert.assertEquals(1024, resultData1.size());
        Assert.assertEquals(1024, resultData2.size());
        Assert.assertEquals(resultData1.toString(), resultData2.toString());
    }
}