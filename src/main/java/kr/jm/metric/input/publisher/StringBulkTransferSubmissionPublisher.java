package kr.jm.metric.input.publisher;

import kr.jm.utils.flow.publisher.BulkSubmissionPublisher;
import kr.jm.utils.flow.publisher.StringBulkSubmissionPublisher;

/**
 * The type String bulk transfer submission publisher.
 */
public class StringBulkTransferSubmissionPublisher extends
        BulkTransferSubmissionPublisher<String> implements
        StringListTransferSubmissionPublisherInterface {
    /**
     * Instantiates a new String bulk transfer submission publisher.
     */
    public StringBulkTransferSubmissionPublisher() {
        this(new StringBulkSubmissionPublisher());
    }

    /**
     * Instantiates a new String bulk transfer submission publisher.
     *
     * @param bulkSize the bulk size
     */
    public StringBulkTransferSubmissionPublisher(int bulkSize) {
        super(new StringBulkSubmissionPublisher(bulkSize));
    }

    /**
     * Instantiates a new String bulk transfer submission publisher.
     *
     * @param bulkSize             the bulk size
     * @param flushIntervalSeconds the flush interval seconds
     */
    public StringBulkTransferSubmissionPublisher(int bulkSize,
            int flushIntervalSeconds) {
        super(new StringBulkSubmissionPublisher(bulkSize,
                flushIntervalSeconds));
    }

    /**
     * Instantiates a new String bulk transfer submission publisher.
     *
     * @param bulkSubmissionPublisher the bulk submission publisher
     */
    public StringBulkTransferSubmissionPublisher(
            BulkSubmissionPublisher<String> bulkSubmissionPublisher) {
        super(bulkSubmissionPublisher);
    }
}
