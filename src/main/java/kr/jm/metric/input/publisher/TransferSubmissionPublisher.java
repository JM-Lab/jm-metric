package kr.jm.metric.input.publisher;

import kr.jm.metric.data.Transfer;
import kr.jm.utils.flow.publisher.JMSubmissionPublisher;

import java.util.List;

public class TransferSubmissionPublisher<T> extends
        JMSubmissionPublisher<List<Transfer<T>>> implements
        TransferSubmissionPublisherInterface<T> {

    public TransferSubmissionPublisher(int publishers, int maxBufferCapacity,
            long waitingMillis) {
        super(publishers, maxBufferCapacity, waitingMillis);
    }

    public TransferSubmissionPublisher(int publishers, int maxBufferCapacity) {
        super(publishers, maxBufferCapacity);
    }

    public TransferSubmissionPublisher(int publishers) {
        super(publishers);
    }

    public TransferSubmissionPublisher() {
    }
}
