package kr.jm.metric.config;

import kr.jm.utils.helper.JMLambda;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

/**
 * The type Abstract output properties.
 */
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractMapConfig extends AbstractConfig implements
        MapConfigInterface {

    protected Map<String, Object> properties;

    public Map<String, Object> getProperties() {
        return JMLambda.supplierIfNull(this.properties,
                () -> this.properties = new HashMap<>());
    }
}
