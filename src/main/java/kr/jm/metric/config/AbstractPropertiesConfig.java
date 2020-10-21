package kr.jm.metric.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractPropertiesConfig extends AbstractConfig {

    protected Map<String, Object> properties;

    public Map<String, Object> getProperties() {
        return Objects.requireNonNullElseGet(this.properties, () -> this.properties = new HashMap<>());
    }
}
