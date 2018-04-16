package kr.jm.metric.publisher;

import kr.jm.utils.accumulator.CountBytesSizeAccumulator;
import kr.jm.utils.flow.subscriber.JMSubscriberBuilder;
import kr.jm.utils.helper.JMThread;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class StringListInputTransferPublisherTest {

    private StringListTransferSubmissionPublisher
            stringListInputTransferPublisher;
    private CountBytesSizeAccumulator countBytesSizeAccumulator;

    @Before
    public void setUp() {
        this.stringListInputTransferPublisher =
                new StringListTransferSubmissionPublisher();
        this.countBytesSizeAccumulator = new CountBytesSizeAccumulator();
    }

    @Test
    public void testSubmitFilePath() {
        stringListInputTransferPublisher
                .subscribe(JMSubscriberBuilder.getSOPLSubscriber());
        stringListInputTransferPublisher
                .subscribe(JMSubscriberBuilder
                        .build(dataTransfer -> dataTransfer.getData().stream()
                                .map(String::getBytes)
                                .map(bytes -> bytes.length).forEach(
                                        countBytesSizeAccumulator::increaseCountAccumulateBytes)));
        stringListInputTransferPublisher.inputFilePath("testId",
                "src/test/resources/webAccessLogSample.txt");
        JMThread.sleep(1000);
        Assert.assertEquals(1024, countBytesSizeAccumulator.getCount());
        System.out.println(countBytesSizeAccumulator.getBytesSize());
    }
}