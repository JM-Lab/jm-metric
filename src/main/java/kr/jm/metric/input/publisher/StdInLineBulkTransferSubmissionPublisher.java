package kr.jm.metric.input.publisher;

import kr.jm.utils.StdInLineConsumer;

import static kr.jm.utils.flow.publisher.BulkSubmissionPublisher.DEFAULT_BULK_SIZE;
import static kr.jm.utils.flow.publisher.BulkSubmissionPublisher.DEFAULT_FLUSH_INTERVAL_SECONDS;

/**
 * The type Std in line bulk transfer submission publisher.
 */
public class StdInLineBulkTransferSubmissionPublisher extends
        StringBulkTransferSubmissionPublisher implements AutoCloseable {

    private StdInLineConsumer stdInLineConsumer;

    /**
     * Instantiates a new Std in line bulk transfer submission publisher.
     *
     * @param dataId the data id
     */
    public StdInLineBulkTransferSubmissionPublisher(String dataId) {
        this(dataId, DEFAULT_BULK_SIZE);
    }

    /**
     * Instantiates a new Std in line bulk transfer submission publisher.
     *
     * @param dataId   the data id
     * @param bulkSize the bulk size
     */
    public StdInLineBulkTransferSubmissionPublisher(String dataId,
            int bulkSize) {
        this(dataId, bulkSize, DEFAULT_FLUSH_INTERVAL_SECONDS);
    }

    /**
     * Instantiates a new Std in line bulk transfer submission publisher.
     *
     * @param dataId               the data id
     * @param bulkSize             the bulk size
     * @param flushIntervalSeconds the flush interval seconds
     */
    public StdInLineBulkTransferSubmissionPublisher(String dataId, int bulkSize,
            int flushIntervalSeconds) {
        super(bulkSize, flushIntervalSeconds);
        this.stdInLineConsumer =
                new StdInLineConsumer(line -> submitSingle(dataId, line));
    }

    /**
     * Consume std in std in line bulk transfer submission publisher.
     *
     * @return the std in line bulk transfer submission publisher
     */
    public StdInLineBulkTransferSubmissionPublisher consumeStdIn() {
        stdInLineConsumer.consumeStdIn();
        return this;
    }

    @Override
    public void close() throws Exception {
        stdInLineConsumer.close();
    }
}
