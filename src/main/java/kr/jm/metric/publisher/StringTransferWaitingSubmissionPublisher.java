package kr.jm.metric.publisher;

import kr.jm.metric.data.Transfer;
import kr.jm.utils.flow.publisher.WaitingSubmissionPublisher;
import kr.jm.utils.flow.subscriber.JMSubscriberBuilder;

public class StringTransferWaitingSubmissionPublisher extends
        WaitingSubmissionPublisher<Transfer<String>> {

    private TransferSubmissionPublisher<String> transferSubmissionPublisher;

    public StringTransferWaitingSubmissionPublisher() {
        this(getDefaultQueueSizeLimit());
    }

    public StringTransferWaitingSubmissionPublisher(long waitingMillis) {
        this(waitingMillis, getDefaultQueueSizeLimit());
    }

    public StringTransferWaitingSubmissionPublisher(long waitingMillis,
            int queueSizeLimit) {
        this(waitingMillis, queueSizeLimit,
                new TransferSubmissionPublisher<>());
    }

    public StringTransferWaitingSubmissionPublisher(long waitingMillis,
            int queueSizeLimit,
            TransferSubmissionPublisher<String> transferSubmissionPublisher) {
        super(waitingMillis, queueSizeLimit);
        this.transferSubmissionPublisher = transferSubmissionPublisher;
        this.transferSubmissionPublisher
                .subscribe(JMSubscriberBuilder.build(this::submit));
    }
}
