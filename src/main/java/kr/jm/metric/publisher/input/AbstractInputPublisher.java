package kr.jm.metric.publisher.input;

import kr.jm.metric.publisher.StringBulkTransferSubmissionPublisher;
import kr.jm.utils.helper.JMLog;
import lombok.Getter;

import static kr.jm.utils.flow.publisher.BulkSubmissionPublisher.DEFAULT_BULK_SIZE;
import static kr.jm.utils.flow.publisher.BulkSubmissionPublisher.DEFAULT_FLUSH_INTERVAL_SECONDS;

/**
 * The type Std in line bulk transfer submission publisher.
 */
public abstract class AbstractInputPublisher extends
        StringBulkTransferSubmissionPublisher implements
        InputPublisherInterface {

    @Getter
    protected String dataId;

    /**
     * Instantiates a new Std in line bulk transfer submission publisher.
     *
     * @param dataId the data id
     */
    public AbstractInputPublisher(String dataId) {
        this(dataId, DEFAULT_BULK_SIZE);
    }

    /**
     * Instantiates a new Std in line bulk transfer submission publisher.
     *
     * @param dataId   the data id
     * @param bulkSize the bulk size
     */
    public AbstractInputPublisher(String dataId,
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
    public AbstractInputPublisher(String dataId,
            int bulkSize, int flushIntervalSeconds) {
        super(bulkSize, flushIntervalSeconds);
        this.dataId = dataId;
    }

    @Override
    public void start() {
        JMLog.info(log, "start");
        startImpl();
    }

    protected abstract void startImpl();
}
