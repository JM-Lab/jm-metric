package kr.jm.metric.config.input;

import kr.jm.metric.config.AbstractPropertiesConfig;
import kr.jm.utils.helper.JMLambda;
import kr.jm.utils.helper.JMThread;
import lombok.*;

import java.util.concurrent.Flow;

import static kr.jm.utils.flow.publisher.BulkSubmissionPublisher.DEFAULT_BULK_SIZE;
import static kr.jm.utils.flow.publisher.BulkSubmissionPublisher.DEFAULT_FLUSH_INTERVAL_Millis;

/**
 * The type Abstract input config.
 */
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public abstract class AbstractInputConfig extends
        AbstractPropertiesConfig implements InputConfigInterface {

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
    protected Long flushIntervalMillis;

    /**
     * The Waiting millis.
     */
    protected Long waitingMillis;
    /**
     * The Queue size limit.
     */
    protected Integer maxBufferCapacity;

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
     * @param inputId             the input id
     * @param bulkSize            the bulk size
     * @param flushIntervalMillis the flush interval seconds
     */
    public AbstractInputConfig(String inputId, Integer bulkSize,
            Long flushIntervalMillis) {
        this(inputId, bulkSize, flushIntervalMillis, null, null, null);
    }

    @Override
    public Integer getBulkSize() {
        return JMLambda.supplierIfNull(bulkSize,
                () -> this.bulkSize = DEFAULT_BULK_SIZE);
    }

    @Override
    public Long getFlushIntervalMillis() {
        return JMLambda.supplierIfNull(this.flushIntervalMillis,
                () -> this.flushIntervalMillis =
                        DEFAULT_FLUSH_INTERVAL_Millis);
    }

    @Override
    public Long getWaitingMillis() {
        return JMLambda.supplierIfNull(waitingMillis,
                () -> this.waitingMillis = JMThread.DEFAULT_WAITING_MILLIS);
    }

    @Override
    public Integer getMaxBufferCapacity() {
        return JMLambda.supplierIfNull(maxBufferCapacity,
                () -> this.maxBufferCapacity = Flow.defaultBufferSize());
    }
}
