package kr.jm.metric.output.config;

import kr.jm.utils.helper.JMLambda;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

/**
 * The type Abstract output properties.
 */
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractOutputConfig implements OutputConfigInterface {

    /**
     * The Config id.
     */
    @Getter
    protected String configId;
    /**
     * The Config.
     */
    protected Map<String, Object> properties;
    private Map<String, Object> configMap;

    @Override
    public Map<String, Object> extractConfigMap() {
        return JMLambda
                .supplierIfNull(this.configMap,
                        () -> this.configMap = new HashMap<>() {{
                            putAll(getProperties());
                            putAll(buildChildConfig());
                            put("configId", configId);
                        }});
    }

    @Override
    public Map<String, Object> getProperties() {
        return JMLambda.supplierIfNull(this.properties,
                () -> this.properties = new HashMap<>());
    }

    /**
     * Build child properties map.
     *
     * @return the map
     */
    protected abstract Map<String, Object> buildChildConfig();
}
