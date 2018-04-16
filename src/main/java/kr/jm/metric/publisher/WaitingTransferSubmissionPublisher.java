package kr.jm.metric.publisher;

import kr.jm.metric.data.Transfer;
import kr.jm.utils.flow.publisher.WaitingSubmissionPublisher;

import java.util.concurrent.Executor;

/**
 * The type Waiting transfer submission publisher.
 *
 * @param <T> the type parameter
 */
public class WaitingTransferSubmissionPublisher<T> extends
        WaitingSubmissionPublisher<Transfer<T>> implements
        TransferSubmissionPublisherInterface<T> {
    /**
     * Instantiates a new Waiting transfer submission publisher.
     */
    public WaitingTransferSubmissionPublisher() {
    }

    /**
     * Instantiates a new Waiting transfer submission publisher.
     *
     * @param queueSizeLimit the queue size limit
     */
    public WaitingTransferSubmissionPublisher(int queueSizeLimit) {
        super(queueSizeLimit);
    }

    /**
     * Instantiates a new Waiting transfer submission publisher.
     *
     * @param executor the executor
     */
    public WaitingTransferSubmissionPublisher(
            Executor executor) {
        super(executor);
    }

    /**
     * Instantiates a new Waiting transfer submission publisher.
     *
     * @param executor       the executor
     * @param queueSizeLimit the queue size limit
     */
    public WaitingTransferSubmissionPublisher(
            Executor executor, int queueSizeLimit) {
        super(executor, queueSizeLimit);
    }
}
