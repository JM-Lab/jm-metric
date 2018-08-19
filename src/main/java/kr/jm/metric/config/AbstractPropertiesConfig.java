package kr.jm.metric.config;

import kr.jm.utils.helper.JMLambda;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

/**
 * The type Abstract properties config.
 */
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractPropertiesConfig extends AbstractConfig {

    /**
     * The Properties.
     */
    protected Map<String, Object> properties;

    /**
     * Gets properties.
     *
     * @return the properties
     */
    public Map<String, Object> getProperties() {
        return JMLambda.supplierIfNull(this.properties,
                () -> this.properties = new HashMap<>());
    }
}
