package kr.jm.metric.config.input;

import kr.jm.metric.config.AbstractPropertiesConfig;
import kr.jm.utils.flow.publisher.WaitingSubmissionPublisher;
import kr.jm.utils.helper.JMLambda;
import lombok.*;

import static kr.jm.utils.flow.publisher.BulkSubmissionPublisher.DEFAULT_BULK_SIZE;
import static kr.jm.utils.flow.publisher.BulkSubmissionPublisher.DEFAULT_FLUSH_INTERVAL_SECONDS;

/**
 * The type Abstract input config.
 */
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public abstract class AbstractInputConfig extends
        AbstractPropertiesConfig implements
        InputConfigInterface {

    /**
     * The Input id.
     */
    @Getter
    protected String inputId;
    /**
     * The Bulk size.
     */
    protected Integer bulkSize;
    /**
     * The Flush interval seconds.
     */
    protected Integer flushIntervalSeconds;
    /**
     * The Waiting millis.
     */
    protected Long waitingMillis;
    /**
     * The Queue size limit.
     */
    protected Integer queueSizeLimit;

    @Getter
    protected ChunkType chunkType;

    /**
     * Instantiates a new Abstract input config.
     *
     * @param inputId the input id
     */
    public AbstractInputConfig(String inputId) {
        this(inputId, null);
    }

    public AbstractInputConfig(String inputId, ChunkType chunkType) {
        this(inputId, null, null, null, null, chunkType);
    }

    /**
     * Instantiates a new Abstract input config.
     *
     * @param inputId              the input id
     * @param bulkSize             the bulk size
     * @param flushIntervalSeconds the flush interval seconds
     */
    public AbstractInputConfig(String inputId, Integer bulkSize,
            Integer flushIntervalSeconds) {
        this(inputId, bulkSize, flushIntervalSeconds, null, null, null);
    }

    @Override
    public Integer getBulkSize() {
        return JMLambda.supplierIfNull(bulkSize,
                () -> this.bulkSize = DEFAULT_BULK_SIZE);
    }

    @Override
    public Integer getFlushIntervalSeconds() {
        return JMLambda.supplierIfNull(flushIntervalSeconds,
                () -> this.flushIntervalSeconds =
                        DEFAULT_FLUSH_INTERVAL_SECONDS);
    }

    @Override
    public Long getWaitingMillis() {
        return JMLambda.supplierIfNull(waitingMillis,
                () -> this.waitingMillis = (long) WaitingSubmissionPublisher
                        .getDefaultQueueSizeLimit());

    }

    @Override
    public Integer getQueueSizeLimit() {
        return JMLambda.supplierIfNull(queueSizeLimit,
                () -> this.queueSizeLimit =
                        WaitingSubmissionPublisher.getDefaultQueueSizeLimit());
    }
}
