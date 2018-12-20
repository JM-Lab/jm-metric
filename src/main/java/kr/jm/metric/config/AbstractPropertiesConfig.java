package kr.jm.metric.config;

import kr.jm.utils.helper.JMLambda;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractPropertiesConfig extends AbstractConfig {

    protected Map<String, Object> properties;

    public Map<String, Object> getProperties() {
        return JMLambda.supplierIfNull(this.properties,
                () -> this.properties = new HashMap<>());
    }
}
