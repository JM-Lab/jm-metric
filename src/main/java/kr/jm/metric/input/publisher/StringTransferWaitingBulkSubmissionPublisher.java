package kr.jm.metric.input.publisher;

import kr.jm.metric.data.Transfer;
import kr.jm.utils.flow.publisher.WaitingBulkSubmissionPublisher;

/**
 * The type String transfer waiting bulk submission publisher.
 */
public class StringTransferWaitingBulkSubmissionPublisher extends
        WaitingBulkSubmissionPublisher<Transfer<String>> {

    /**
     * Instantiates a new String transfer waiting bulk submission publisher.
     */
    public StringTransferWaitingBulkSubmissionPublisher() {
        this(new StringTransferWaitingSubmissionPublisher());
    }

    /**
     * Instantiates a new String transfer waiting bulk submission publisher.
     *
     * @param stringTransferWaitingSubmissionPublisher the string transfer waiting submission publisher
     */
    public StringTransferWaitingBulkSubmissionPublisher(
            StringTransferWaitingSubmissionPublisher stringTransferWaitingSubmissionPublisher) {
        this(stringTransferWaitingSubmissionPublisher, DEFAULT_BULK_SIZE);
    }

    /**
     * Instantiates a new String transfer waiting bulk submission publisher.
     *
     * @param stringTransferWaitingSubmissionPublisher the string transfer waiting submission publisher
     * @param bulkSize                                 the bulk size
     */
    public StringTransferWaitingBulkSubmissionPublisher(
            StringTransferWaitingSubmissionPublisher
                    stringTransferWaitingSubmissionPublisher,
            int bulkSize) {
        this(stringTransferWaitingSubmissionPublisher, bulkSize,
                DEFAULT_FLUSH_INTERVAL_SECONDS);
    }

    /**
     * Instantiates a new String transfer waiting bulk submission publisher.
     *
     * @param stringTransferBulkSubmissionPublisher the string transfer bulk submission publisher
     * @param bulkSize                              the bulk size
     * @param flushIntervalSeconds                  the flush interval seconds
     */
    public StringTransferWaitingBulkSubmissionPublisher(
            StringTransferWaitingSubmissionPublisher
                    stringTransferBulkSubmissionPublisher,
            int bulkSize, int flushIntervalSeconds) {
        super(stringTransferBulkSubmissionPublisher, bulkSize,
                flushIntervalSeconds);
    }

}
