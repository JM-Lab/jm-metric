package kr.jm.metric.publisher;

import kr.jm.metric.data.Transfer;
import kr.jm.metric.input.publisher.InputPublisherBuilder;
import kr.jm.utils.accumulator.CountBytesSizeAccumulator;
import kr.jm.utils.helper.JMConsumer;
import kr.jm.utils.helper.JMThread;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class StringTransferBulkWaitingSubmissionPublisherTest {


    private StringTransferBulkWaitingSubmissionPublisher
            stringTransferBulkWaitingSubmissionPublisher;

    private CountBytesSizeAccumulator countBytesSizeAccumulator;

    @Before
    public void setUp() {
        this.stringTransferBulkWaitingSubmissionPublisher =
                new StringTransferBulkWaitingSubmissionPublisher();
        this.countBytesSizeAccumulator = new CountBytesSizeAccumulator();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testInsert() {
        stringTransferBulkWaitingSubmissionPublisher
                .consume(JMConsumer.getSOPL());
        stringTransferBulkWaitingSubmissionPublisher.consume(
                list -> list.stream().map(Transfer::getData)
                        .map(String::getBytes).map(bytes -> bytes.length)
                        .forEach(
                                countBytesSizeAccumulator::increaseCountAccumulateBytes));
        InputPublisherBuilder.buildResourceInput("webAccessLogSample.txt")
                .consumeWith(
                        stringTransferBulkWaitingSubmissionPublisher::submit)
                .start();
        JMThread.sleep(4000);
        Assert.assertEquals(1024, countBytesSizeAccumulator.getCount());
        System.out.println(countBytesSizeAccumulator.getBytesSize());
    }
}