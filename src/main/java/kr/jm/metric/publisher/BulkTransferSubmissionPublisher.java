package kr.jm.metric.publisher;

import kr.jm.metric.data.Transfer;
import kr.jm.utils.flow.publisher.BulkSubmissionPublisher;
import kr.jm.utils.flow.subscriber.JMSubscriberBuilder;
import kr.jm.utils.helper.JMString;

import java.util.List;

/**
 * The type Bulk transfer submission publisher.
 *
 * @param <T> the type parameter
 */
@SuppressWarnings("SynchronizeOnNonFinalField")
public class BulkTransferSubmissionPublisher<T> extends
        TransferSubmissionPublisher<List<T>> {
    private String dataId;
    private BulkSubmissionPublisher<T> bulkSubmissionPublisher;

    /**
     * Instantiates a new Bulk transfer submission publisher.
     */
    public BulkTransferSubmissionPublisher() {
        this(new BulkSubmissionPublisher<>());
    }

    /**
     * Instantiates a new Bulk transfer submission publisher.
     *
     * @param bulkSize the bulk size
     */
    public BulkTransferSubmissionPublisher(int bulkSize) {
        this(new BulkSubmissionPublisher<>(bulkSize));
    }

    /**
     * Instantiates a new Bulk transfer submission publisher.
     *
     * @param bulkSize             the bulk size
     * @param flushIntervalSeconds the flush interval seconds
     */
    public BulkTransferSubmissionPublisher(
            int bulkSize, int flushIntervalSeconds) {
        this(new BulkSubmissionPublisher<>(bulkSize, flushIntervalSeconds));
    }

    /**
     * Instantiates a new Bulk transfer submission publisher.
     *
     * @param bulkSubmissionPublisher the bulk submission publisher
     */
    public BulkTransferSubmissionPublisher(
            BulkSubmissionPublisher<T> bulkSubmissionPublisher) {
        this.dataId = JMString.EMPTY;
        bulkSubmissionPublisher.subscribe(JMSubscriberBuilder
                .build(list -> super.submit(this.dataId, list)));
        this.bulkSubmissionPublisher = bulkSubmissionPublisher;
    }

    @Override
    public int submit(Transfer<List<T>> item) {
        return item.getData().size() > 0 ? super.submit(item) : 0;
    }

    @Override
    public int submit(String dataId, List<T> dataList) {
        synchronized (this.dataId) {
            flush(dataId);
            return bulkSubmissionPublisher.submit(dataList);
        }
    }

    /**
     * Submit single int.
     *
     * @param dataId the data id
     * @param data   the data
     * @return the int
     */
    public int submitSingle(String dataId, T data) {
        synchronized (this.dataId) {
            flush(dataId);
            return bulkSubmissionPublisher.submitSingle(data);
        }
    }

    /**
     * Flush.
     */
    public void flush() {
        bulkSubmissionPublisher.flush();
    }

    private void flush(String dataId) {
        synchronized (this.dataId) {
            if (!dataId.equals(this.dataId))
                flush();
            this.dataId = dataId;
        }
    }
}
