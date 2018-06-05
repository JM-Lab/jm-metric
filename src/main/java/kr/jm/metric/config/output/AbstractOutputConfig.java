package kr.jm.metric.config.output;

import kr.jm.metric.config.AbstractMapConfig;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * The type Abstract output properties.
 */
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractOutputConfig extends AbstractMapConfig implements
        OutputConfigInterface {
}
