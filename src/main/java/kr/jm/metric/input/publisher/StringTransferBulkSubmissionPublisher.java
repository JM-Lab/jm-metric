package kr.jm.metric.input.publisher;

import kr.jm.metric.data.Transfer;
import kr.jm.utils.flow.publisher.BulkSubmissionPublisher;

public class StringTransferBulkSubmissionPublisher extends BulkSubmissionPublisher<Transfer<String>> {

    public StringTransferBulkSubmissionPublisher() {
        this(DEFAULT_BULK_SIZE);
    }

    public StringTransferBulkSubmissionPublisher(int bulkSize) {
        this(bulkSize, DEFAULT_FLUSH_INTERVAL_Millis);
    }

    public StringTransferBulkSubmissionPublisher(int bulkSize, long flushIntervalSeconds) {
        this(new TransferSubmissionPublisher<>(), bulkSize, flushIntervalSeconds);
    }

    public StringTransferBulkSubmissionPublisher(TransferSubmissionPublisher<String> transferSubmissionPublisher,
            int bulkSize, long flushIntervalSeconds) {
        super(transferSubmissionPublisher, bulkSize, flushIntervalSeconds);
    }

}
