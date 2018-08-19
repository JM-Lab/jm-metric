package kr.jm.metric.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.slf4j.Logger;

/**
 * The type Abstract config.
 */
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractConfig implements ConfigInterface {
    /**
     * The Log.
     */
    protected Logger log = org.slf4j.LoggerFactory.getLogger(getClass());
}
