package kr.jm.metric.input.publisher;

import kr.jm.utils.accumulator.CountBytesSizeAccumulator;
import kr.jm.utils.helper.JMConsumer;
import kr.jm.utils.helper.JMThread;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class StringBulkWaitingTransferSubmissionPublisherTest {


    private StringBulkWaitingTransferSubmissionPublisher
            stringBulkTransferWaitingSubmissionPublisher;

    private CountBytesSizeAccumulator countBytesSizeAccumulator;

    @Before
    public void setUp() throws Exception {
        this.stringBulkTransferWaitingSubmissionPublisher =
                new StringBulkWaitingTransferSubmissionPublisher();
        this.countBytesSizeAccumulator = new CountBytesSizeAccumulator();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testInsert() {
        stringBulkTransferWaitingSubmissionPublisher
                .consume(JMConsumer.getSOPL());
        stringBulkTransferWaitingSubmissionPublisher.consume(
                dataTransfer -> dataTransfer.getData().stream()
                        .map(String::getBytes).map(bytes -> bytes.length)
                        .forEach(
                                countBytesSizeAccumulator::increaseCountAccumulateBytes));
        stringBulkTransferWaitingSubmissionPublisher
                .inputFilePathOrClasspath("testId", "webAccessLogSample.txt");
        JMThread.sleep(2000);
        Assert.assertEquals(1024, countBytesSizeAccumulator.getCount());
        System.out.println(countBytesSizeAccumulator.getBytesSize());
    }
}