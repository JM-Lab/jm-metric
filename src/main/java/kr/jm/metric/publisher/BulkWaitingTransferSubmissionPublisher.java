package kr.jm.metric.publisher;

import kr.jm.utils.flow.subscriber.JMSubscriberBuilder;
import kr.jm.utils.helper.JMOptional;

import java.util.List;
import java.util.concurrent.Executor;

/**
 * The type Bulk waiting transfer submission publisher.
 *
 * @param <T> the type parameter
 */
public class BulkWaitingTransferSubmissionPublisher<T> extends
        WaitingTransferSubmissionPublisher<List<T>> {
    private BulkTransferSubmissionPublisher<T> bulkTransferSubmissionPublisher;

    /**
     * Instantiates a new Bulk waiting transfer submission publisher.
     */
    public BulkWaitingTransferSubmissionPublisher() {
        this(new BulkTransferSubmissionPublisher<>());
    }

    /**
     * Instantiates a new Bulk waiting transfer submission publisher.
     *
     * @param bulkTransferSubmissionPublisher the bulk transfer submission publisher
     */
    public BulkWaitingTransferSubmissionPublisher(
            BulkTransferSubmissionPublisher<T> bulkTransferSubmissionPublisher) {
        this(null, bulkTransferSubmissionPublisher);
    }

    /**
     * Instantiates a new Bulk waiting transfer submission publisher.
     *
     * @param queueSizeLimit                  the queue size limit
     * @param bulkTransferSubmissionPublisher the bulk transfer submission publisher
     */
    public BulkWaitingTransferSubmissionPublisher(int queueSizeLimit,
            BulkTransferSubmissionPublisher<T> bulkTransferSubmissionPublisher) {
        this(null, queueSizeLimit, bulkTransferSubmissionPublisher);
    }

    /**
     * Instantiates a new Bulk waiting transfer submission publisher.
     *
     * @param executor                        the executor
     * @param bulkTransferSubmissionPublisher the bulk transfer submission publisher
     */
    public BulkWaitingTransferSubmissionPublisher(Executor executor,
            BulkTransferSubmissionPublisher<T> bulkTransferSubmissionPublisher) {
        this(executor, getDefaultQueueSizeLimit(),
                bulkTransferSubmissionPublisher);
    }

    /**
     * Instantiates a new Bulk waiting transfer submission publisher.
     *
     * @param executor                        the executor
     * @param queueSizeLimit                  the queue size limit
     * @param bulkTransferSubmissionPublisher the bulk transfer submission publisher
     */
    public BulkWaitingTransferSubmissionPublisher(
            Executor executor, int queueSizeLimit,
            BulkTransferSubmissionPublisher<T> bulkTransferSubmissionPublisher) {
        super(executor, queueSizeLimit);
        bulkTransferSubmissionPublisher
                .subscribe(JMSubscriberBuilder.build(super::submit));
        this.bulkTransferSubmissionPublisher = bulkTransferSubmissionPublisher;
    }

    @Override
    public int submit(String dataId, List<T> dataList) {
        return JMOptional.getOptional(dataList)
                .map(list -> bulkTransferSubmissionPublisher
                        .submit(dataId, list)).orElse(0);
    }

    /**
     * Submit single int.
     *
     * @param dataId the data id
     * @param data   the data
     * @return the int
     */
    public int submitSingle(String dataId, T data) {
        return bulkTransferSubmissionPublisher.submitSingle(dataId, data);
    }

    /**
     * Flush.
     */
    public void flush() {bulkTransferSubmissionPublisher.flush();}
}
