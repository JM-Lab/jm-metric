package kr.jm.metric.input.publisher;

import kr.jm.metric.data.Transfer;
import kr.jm.utils.flow.publisher.BulkSubmissionPublisher;
import kr.jm.utils.flow.subscriber.JMSubscriberBuilder;

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
        this(bulkSize, DEFAULT_FLUSH_INTERVAL_SECONDS);
    }

    /**
     * Instantiates a new String transfer bulk submission publisher.
     *
     * @param bulkSize             the bulk size
     * @param flushIntervalSeconds the flush interval seconds
     */
    public StringTransferBulkSubmissionPublisher(
            int bulkSize, int flushIntervalSeconds) {
        this(bulkSize, flushIntervalSeconds,
                new TransferSubmissionPublisher<>());
    }

    /**
     * Instantiates a new String transfer bulk submission publisher.
     *
     * @param bulkSize                    the bulk size
     * @param flushIntervalSeconds        the flush interval seconds
     * @param transferSubmissionPublisher the transfer submission publisher
     */
    public StringTransferBulkSubmissionPublisher(int bulkSize,
            int flushIntervalSeconds,
            TransferSubmissionPublisher<String> transferSubmissionPublisher) {
        super(bulkSize, flushIntervalSeconds);
        transferSubmissionPublisher
                .subscribe(JMSubscriberBuilder.build(this::submitSingle));
    }


}
