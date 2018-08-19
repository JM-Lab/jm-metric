package kr.jm.metric.config.output;

import kr.jm.metric.config.AbstractPropertiesConfig;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * The type Abstract output config.
 */
@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractOutputConfig extends
        AbstractPropertiesConfig implements
        OutputConfigInterface {
    /**
     * The Output id.
     */
    protected String outputId;

    /**
     * Instantiates a new Abstract output config.
     *
     * @param outputId the output id
     */
    public AbstractOutputConfig(String outputId) {
        this.outputId = outputId;
    }
}
