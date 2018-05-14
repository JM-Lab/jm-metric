package kr.jm.metric.output;

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

    @Getter
    private String configId;
    private Map<String, Object> config;
    private Map<String, Object> configWithAll;

    /**
     * Instantiates a new Abstract output config.
     *
     * @param configId the config id
     */
    public AbstractOutputConfig(String configId) {
        this.configId = configId;
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
