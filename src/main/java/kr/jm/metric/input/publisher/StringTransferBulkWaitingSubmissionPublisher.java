package kr.jm.metric.input.publisher;

import kr.jm.metric.data.Transfer;
import kr.jm.utils.flow.publisher.BulkWaitingSubmissionPublisher;

/**
 * The type String transfer bulk waiting submission publisher.
 */
public class StringTransferBulkWaitingSubmissionPublisher extends
        BulkWaitingSubmissionPublisher<Transfer<String>> {

    /**
     * Instantiates a new String transfer bulk waiting submission publisher.
     */
    public StringTransferBulkWaitingSubmissionPublisher() {
        this(new StringTransferBulkSubmissionPublisher());
    }

    /**
     * Instantiates a new String transfer bulk waiting submission publisher.
     *
     * @param stringTransferBulkSubmissionPublisher the string transfer bulk submission publisher
     */
    public StringTransferBulkWaitingSubmissionPublisher(
            StringTransferBulkSubmissionPublisher stringTransferBulkSubmissionPublisher) {
        this(stringTransferBulkSubmissionPublisher, getDefaultQueueSizeLimit());
    }

    /**
     * Instantiates a new String transfer bulk waiting submission publisher.
     *
     * @param stringTransferBulkSubmissionPublisher the string transfer bulk submission publisher
     * @param waitingMillis                         the waiting millis
     */
    public StringTransferBulkWaitingSubmissionPublisher(
            StringTransferBulkSubmissionPublisher
                    stringTransferBulkSubmissionPublisher, long waitingMillis) {
        this(stringTransferBulkSubmissionPublisher, waitingMillis,
                getDefaultQueueSizeLimit());
    }

    /**
     * Instantiates a new String transfer bulk waiting submission publisher.
     *
     * @param stringTransferBulkSubmissionPublisher the string transfer bulk submission publisher
     * @param waitingMillis                         the waiting millis
     * @param queueSizeLimit                        the queue size limit
     */
    public StringTransferBulkWaitingSubmissionPublisher(
            StringTransferBulkSubmissionPublisher stringTransferBulkSubmissionPublisher,
            long waitingMillis, int queueSizeLimit) {
        super(stringTransferBulkSubmissionPublisher, waitingMillis,
                queueSizeLimit);
    }

}
