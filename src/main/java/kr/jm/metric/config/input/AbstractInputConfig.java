package kr.jm.metric.config.input;

import kr.jm.metric.config.AbstractPropertiesConfig;
import kr.jm.utils.JMThread;
import lombok.*;

import java.util.Objects;
import java.util.concurrent.Flow;

import static kr.jm.utils.flow.publisher.BulkSubmissionPublisher.DEFAULT_BULK_SIZE;
import static kr.jm.utils.flow.publisher.BulkSubmissionPublisher.DEFAULT_FLUSH_INTERVAL_Millis;

@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public abstract class AbstractInputConfig extends
        AbstractPropertiesConfig implements InputConfigInterface {

    @Getter
    protected String inputId;
    protected Integer bulkSize;
    protected Long flushIntervalMillis;

    protected Long waitingMillis;
    protected Integer maxBufferCapacity;

    @Getter
    protected ChunkType chunkType;


    public AbstractInputConfig(String inputId) {
        this(inputId, null);
    }

    public AbstractInputConfig(String inputId, ChunkType chunkType) {
        this(inputId, null, null, null, null, chunkType);
    }

    public AbstractInputConfig(String inputId, Integer bulkSize,
            Long flushIntervalMillis) {
        this(inputId, bulkSize, flushIntervalMillis, null, null, null);
    }

    @Override
    public Integer getBulkSize() {
        return Objects.requireNonNullElseGet(bulkSize, () -> this.bulkSize = DEFAULT_BULK_SIZE);
    }

    @Override
    public Long getFlushIntervalMillis() {
        return Objects.requireNonNullElseGet(this.flushIntervalMillis,
                () -> this.flushIntervalMillis = DEFAULT_FLUSH_INTERVAL_Millis);
    }

    @Override
    public Long getWaitingMillis() {
        return Objects.requireNonNullElseGet(waitingMillis,
                () -> this.waitingMillis = JMThread.DEFAULT_WAITING_MILLIS);
    }

    @Override
    public Integer getMaxBufferCapacity() {
        return Objects.requireNonNullElseGet(maxBufferCapacity,
                () -> this.maxBufferCapacity = Flow.defaultBufferSize());
    }
}
