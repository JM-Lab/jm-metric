package kr.jm.metric.publisher;

import kr.jm.utils.flow.subscriber.JMSubscriberBuilder;

import java.util.List;

import static kr.jm.utils.flow.publisher.BulkSubmissionPublisher.DEFAULT_BULK_SIZE;
import static kr.jm.utils.flow.publisher.BulkSubmissionPublisher.DEFAULT_FLUSH_INTERVAL_SECONDS;

/**
 * The type Waiting bulk transfer submission publisher.
 *
 * @param <T> the type parameter
 */
public class WaitingBulkTransferSubmissionPublisher<T> extends
        BulkTransferSubmissionPublisher<T> {

    /**
     * Instantiates a new Waiting bulk transfer submission publisher.
     *
     * @param waitingTransferSubmissionPublisher the waiting transfer submission publisher
     */
    public WaitingBulkTransferSubmissionPublisher(
            WaitingTransferSubmissionPublisher<List<T>> waitingTransferSubmissionPublisher) {
        this(DEFAULT_BULK_SIZE, waitingTransferSubmissionPublisher);
    }

    /**
     * Instantiates a new Waiting bulk transfer submission publisher.
     *
     * @param bulkSize                           the bulk size
     * @param waitingTransferSubmissionPublisher the waiting transfer submission publisher
     */
    public WaitingBulkTransferSubmissionPublisher(int bulkSize,
            WaitingTransferSubmissionPublisher<List<T>> waitingTransferSubmissionPublisher) {
        this(bulkSize, DEFAULT_FLUSH_INTERVAL_SECONDS,
                waitingTransferSubmissionPublisher);
    }

    /**
     * Instantiates a new Waiting bulk transfer submission publisher.
     *
     * @param bulkSize                           the bulk size
     * @param flushIntervalSeconds               the flush interval seconds
     * @param waitingTransferSubmissionPublisher the waiting transfer submission publisher
     */
    public WaitingBulkTransferSubmissionPublisher(int bulkSize,
            int flushIntervalSeconds,
            WaitingTransferSubmissionPublisher<List<T>> waitingTransferSubmissionPublisher) {
        super(bulkSize, flushIntervalSeconds);
        waitingTransferSubmissionPublisher
                .subscribe(JMSubscriberBuilder.build(this::submit));
    }
}
