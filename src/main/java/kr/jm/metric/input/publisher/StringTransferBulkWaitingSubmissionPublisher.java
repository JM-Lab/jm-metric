package kr.jm.metric.input.publisher;

import kr.jm.metric.data.Transfer;
import kr.jm.utils.flow.publisher.BulkWaitingSubmissionPublisher;

public class StringTransferBulkWaitingSubmissionPublisher extends
        BulkWaitingSubmissionPublisher<Transfer<String>> {

    public StringTransferBulkWaitingSubmissionPublisher() {
        this(new StringTransferBulkSubmissionPublisher());
    }

    public StringTransferBulkWaitingSubmissionPublisher(
            StringTransferBulkSubmissionPublisher stringTransferBulkSubmissionPublisher) {
        this(stringTransferBulkSubmissionPublisher, getDefaultQueueSizeLimit());
    }

    public StringTransferBulkWaitingSubmissionPublisher(
            StringTransferBulkSubmissionPublisher
                    stringTransferBulkSubmissionPublisher, long waitingMillis) {
        this(stringTransferBulkSubmissionPublisher, waitingMillis,
                getDefaultQueueSizeLimit());
    }

    public StringTransferBulkWaitingSubmissionPublisher(
            StringTransferBulkSubmissionPublisher stringTransferBulkSubmissionPublisher,
            long waitingMillis, int queueSizeLimit) {
        super(stringTransferBulkSubmissionPublisher, waitingMillis,
                queueSizeLimit);
    }

}
