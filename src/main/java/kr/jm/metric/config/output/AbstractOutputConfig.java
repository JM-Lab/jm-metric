package kr.jm.metric.config.output;

import kr.jm.metric.config.AbstractPropertiesConfig;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * The type Abstract output properties.
 */
@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractOutputConfig extends
        AbstractPropertiesConfig implements
        OutputConfigInterface {
    protected String outputId;
}
