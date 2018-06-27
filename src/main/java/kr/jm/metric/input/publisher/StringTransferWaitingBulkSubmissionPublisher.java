package kr.jm.metric.input.publisher;

import kr.jm.metric.data.Transfer;
import kr.jm.utils.flow.publisher.WaitingBulkSubmissionPublisher;

public class StringTransferWaitingBulkSubmissionPublisher extends
        WaitingBulkSubmissionPublisher<Transfer<String>> {

    public StringTransferWaitingBulkSubmissionPublisher() {
        this(new StringTransferWaitingSubmissionPublisher());
    }

    public StringTransferWaitingBulkSubmissionPublisher(
            StringTransferWaitingSubmissionPublisher stringTransferWaitingSubmissionPublisher) {
        this(stringTransferWaitingSubmissionPublisher, DEFAULT_BULK_SIZE);
    }

    public StringTransferWaitingBulkSubmissionPublisher(
            StringTransferWaitingSubmissionPublisher
                    stringTransferWaitingSubmissionPublisher,
            int bulkSize) {
        this(stringTransferWaitingSubmissionPublisher, bulkSize,
                DEFAULT_FLUSH_INTERVAL_SECONDS);
    }

    public StringTransferWaitingBulkSubmissionPublisher(
            StringTransferWaitingSubmissionPublisher
                    stringTransferBulkSubmissionPublisher,
            int bulkSize, int flushIntervalSeconds) {
        super(stringTransferBulkSubmissionPublisher, bulkSize,
                flushIntervalSeconds);
    }

}
