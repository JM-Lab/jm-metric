package kr.jm.metric.input.publisher;

import kr.jm.metric.data.Transfer;
import kr.jm.utils.flow.publisher.WaitingSubmissionPublisher;
import kr.jm.utils.flow.subscriber.JMSubscriberBuilder;

/**
 * The type String transfer waiting submission publisher.
 */
public class StringTransferWaitingSubmissionPublisher extends
        WaitingSubmissionPublisher<Transfer<String>> {

    /**
     * Instantiates a new String transfer waiting submission publisher.
     */
    public StringTransferWaitingSubmissionPublisher() {
        this(getDefaultQueueSizeLimit());
    }

    /**
     * Instantiates a new String transfer waiting submission publisher.
     *
     * @param waitingMillis the waiting millis
     */
    public StringTransferWaitingSubmissionPublisher(long waitingMillis) {
        this(waitingMillis, getDefaultQueueSizeLimit());
    }

    /**
     * Instantiates a new String transfer waiting submission publisher.
     *
     * @param waitingMillis  the waiting millis
     * @param queueSizeLimit the queue size limit
     */
    public StringTransferWaitingSubmissionPublisher(long waitingMillis,
            int queueSizeLimit) {
        this(waitingMillis, queueSizeLimit,
                new TransferSubmissionPublisher<>());
    }

    /**
     * Instantiates a new String transfer waiting submission publisher.
     *
     * @param waitingMillis               the waiting millis
     * @param queueSizeLimit              the queue size limit
     * @param transferSubmissionPublisher the transfer submission publisher
     */
    public StringTransferWaitingSubmissionPublisher(long waitingMillis,
            int queueSizeLimit,
            TransferSubmissionPublisher<String> transferSubmissionPublisher) {
        super(waitingMillis, queueSizeLimit);
        transferSubmissionPublisher
                .subscribe(JMSubscriberBuilder.build(this::submit));
    }
}
