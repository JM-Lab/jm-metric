package kr.jm.metric.processor;

import kr.jm.metric.config.mutating.ApacheAccessLogMutatingConfig;
import kr.jm.metric.config.mutating.MutatingConfigManager;
import kr.jm.metric.data.ConfigIdTransfer;
import kr.jm.metric.data.FieldMap;
import kr.jm.metric.data.Transfer;
import kr.jm.metric.publisher.TransferSubmissionPublisher;
import kr.jm.metric.transformer.FieldMapConfigIdTransferListTransformer;
import kr.jm.utils.flow.processor.JMConcurrentTransformProcessor;
import kr.jm.utils.flow.processor.JMTransformProcessorBuilder;
import kr.jm.utils.flow.subscriber.JMSubscriberBuilder;
import kr.jm.utils.helper.JMJson;
import kr.jm.utils.helper.JMResources;
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
    private JMConcurrentTransformProcessor<List<Transfer<String>>, List<ConfigIdTransfer<FieldMap>>>
            fieldMapConfigIdTransferListTransformProcessor;
    private TransferSubmissionPublisher<List<String>>
            transferSubmissionPublisher;

    @Before
    public void setUp() {
        this.lineList = JMResources.readLines("webAccessLogSample.txt");
        MutatingConfigManager mutatingConfigManager = new MutatingConfigManager(
                List.of(new ApacheAccessLogMutatingConfig(CONFIG_ID,
                        "%h %l %u %t \"%r\" %>s %b " +
                                "\"%{Referer}i\" " +
                                "\"%{User-agent}i\" %D"))
        );

        this.fieldMapConfigIdTransferListTransformProcessor =
                JMTransformProcessorBuilder.buildWithThreadPool(
                        new FieldMapConfigIdTransferListTransformer(
                                mutatingConfigManager.getConfig(CONFIG_ID)));
        this.transferSubmissionPublisher = new TransferSubmissionPublisher<>();

    }

    @Test
    public void testProcess() throws ExecutionException, InterruptedException {
        TransferSubmissionPublisher<String> transferSubmissionPublisher =
                new TransferSubmissionPublisher<>();
        transferSubmissionPublisher.subscribeAndReturnSubcriber(
                JMTransformProcessorBuilder.build(List::of))
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

        this.transferSubmissionPublisher.submit(TEST_ID, lineList);
        System.out.println(
                JMJson.toJsonString(dataTransferCompletableFuture1.get()));
        Transfer<List<FieldMap>> transfer1 =
                dataTransferCompletableFuture1.get().get(0);
        Assert.assertEquals(1024, transfer1.getData().size());
    }

}