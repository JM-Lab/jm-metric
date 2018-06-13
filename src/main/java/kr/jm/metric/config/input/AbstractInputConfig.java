package kr.jm.metric.config.input;

import kr.jm.metric.config.AbstractPropertiesConfig;
import kr.jm.utils.flow.publisher.BulkSubmissionPublisher;
import kr.jm.utils.helper.JMLambda;
import lombok.*;

@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public abstract class AbstractInputConfig extends
        AbstractPropertiesConfig implements
        InputConfigInterface {
    @Getter
    protected String inputId;
    protected Integer bulkSize;
    protected Integer flushIntervalSeconds;

    public AbstractInputConfig(String inputId) {
        this(inputId, null, null);
    }

    public Integer getBulkSize() {
        return JMLambda.supplierIfNull(this.bulkSize, () -> this.bulkSize =
                BulkSubmissionPublisher.DEFAULT_BULK_SIZE);
    }

    public Integer getFlushIntervalSeconds() {
        return JMLambda.supplierIfNull(this.flushIntervalSeconds,
                () -> this.flushIntervalSeconds =
                        BulkSubmissionPublisher.DEFAULT_FLUSH_INTERVAL_SECONDS);
    }
}
