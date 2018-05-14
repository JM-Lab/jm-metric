package kr.jm.metric.output.config;

import kr.jm.utils.helper.JMLambda;
import lombok.Getter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

/**
 * The type Abstract output config.
 */
@ToString
public abstract class AbstractOutputConfig implements OutputConfigInterface {

    /**
     * The Config id.
     */
    @Getter
    protected String configId;
    /**
     * The Config.
     */
    protected Map<String, Object> config;
    private Map<String, Object> configWithAll;

    /**
     * Instantiates a new Abstract output config.
     */
    protected AbstractOutputConfig() {
    }

    @Override
    public Map<String, Object> getConfigWithAll() {
        return JMLambda
                .supplierIfNull(this.configWithAll,
                        () -> this.configWithAll = new HashMap<>() {{
                            putAll(getConfig());
                            putAll(buildChildConfig());
                            put("configId", configId);
                        }});
    }

    @Override
    public Map<String, Object> getConfig() {
        return JMLambda.supplierIfNull(this.config,
                () -> this.config = new HashMap<>());
    }

    /**
     * Build child config map.
     *
     * @return the map
     */
    protected abstract Map<String, Object> buildChildConfig();
}
