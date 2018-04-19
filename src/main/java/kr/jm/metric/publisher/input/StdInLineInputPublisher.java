package kr.jm.metric.publisher.input;

import kr.jm.utils.StdInLineConsumer;

import static kr.jm.utils.flow.publisher.BulkSubmissionPublisher.DEFAULT_BULK_SIZE;
import static kr.jm.utils.flow.publisher.BulkSubmissionPublisher.DEFAULT_FLUSH_INTERVAL_SECONDS;

/**
 * The type Std in line bulk transfer submission publisher.
 */
public class StdInLineInputPublisher extends
        AbstractInputPublisher {

    private StdInLineConsumer stdInLineConsumer;

    /**
     * Instantiates a new Std in line bulk transfer submission publisher.
     *
     * @param dataId the data id
     */
    public StdInLineInputPublisher(String dataId) {
        this(dataId, DEFAULT_BULK_SIZE);
    }

    /**
     * Instantiates a new Std in line bulk transfer submission publisher.
     *
     * @param dataId   the data id
     * @param bulkSize the bulk size
     */
    public StdInLineInputPublisher(String dataId,
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
    public StdInLineInputPublisher(String dataId, int bulkSize,
            int flushIntervalSeconds) {
        super(dataId, bulkSize, flushIntervalSeconds);
    }

    @Override
    protected void startImpl() {
        this.stdInLineConsumer =
                new StdInLineConsumer(line -> submitSingle(dataId, line))
                        .consumeStdIn();
    }

    @Override
    protected void closeImpl() {
        stdInLineConsumer.close();
    }

}
