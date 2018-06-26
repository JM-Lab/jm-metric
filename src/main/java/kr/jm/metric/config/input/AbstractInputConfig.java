package kr.jm.metric.config.input;

import kr.jm.metric.config.AbstractPropertiesConfig;
import lombok.*;

@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public abstract class AbstractInputConfig extends
        AbstractPropertiesConfig implements
        InputConfigInterface {

    protected String inputId;
    protected Integer bulkSize;
    protected Integer flushIntervalSeconds;
    protected Long waitingMillis;
    protected Integer queueSizeLimit;

    public AbstractInputConfig(String inputId) {
        this(inputId, null, null);
    }

    public AbstractInputConfig(String inputId, Integer bulkSize,
            Integer flushIntervalSeconds) {
        this(inputId, bulkSize, flushIntervalSeconds, null, null);
    }
}
