package kr.jm.metric.input.publisher;

import kr.jm.metric.data.Transfer;
import kr.jm.utils.flow.publisher.BulkSubmissionPublisher;

/**
 * The type String transfer bulk submission publisher.
 */
public class StringTransferBulkSubmissionPublisher extends
        BulkSubmissionPublisher<Transfer<String>> {

    /**
     * Instantiates a new String transfer bulk submission publisher.
     */
    public StringTransferBulkSubmissionPublisher() {
        this(DEFAULT_BULK_SIZE);
    }

    /**
     * Instantiates a new String transfer bulk submission publisher.
     *
     * @param bulkSize the bulk size
     */
    public StringTransferBulkSubmissionPublisher(int bulkSize) {
        this(bulkSize, DEFAULT_FLUSH_INTERVAL_Millis);
    }

    /**
     * Instantiates a new String transfer bulk submission publisher.
     *
     * @param bulkSize             the bulk size
     * @param flushIntervalSeconds the flush interval seconds
     */
    public StringTransferBulkSubmissionPublisher(
            int bulkSize, long flushIntervalSeconds) {
        this(new TransferSubmissionPublisher<>(), bulkSize,
                flushIntervalSeconds);
    }

    /**
     * Instantiates a new String transfer bulk submission publisher.
     *
     * @param transferSubmissionPublisher the transfer submission publisher
     * @param bulkSize                    the bulk size
     * @param flushIntervalSeconds        the flush interval seconds
     */
    public StringTransferBulkSubmissionPublisher(
            TransferSubmissionPublisher<String> transferSubmissionPublisher,
            int bulkSize, long flushIntervalSeconds) {
        super(transferSubmissionPublisher, bulkSize, flushIntervalSeconds);
    }

}
