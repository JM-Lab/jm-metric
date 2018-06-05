package kr.jm.metric.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * The type Abstract output properties.
 */
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractConfig implements ConfigInterface {
    protected String configId;
}
