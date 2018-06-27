package kr.jm.metric.config.mutating;

import kr.jm.metric.config.AbstractConfigManager;

import java.util.List;

/**
 * The type Metric properties manager.
 */
public class MutatingConfigManager extends
        AbstractConfigManager<MutatingConfig> {

    private static final String MUTATING_CONFIG_TYPE = "mutatingConfigType";

    public MutatingConfigManager(String configFilename) {
        super(configFilename);
    }

    /**
     * Instantiates a new Metric properties manager.
     *
     * @param configList the properties list
     */
    public MutatingConfigManager(List<MutatingConfig> configList) {
        super(configList);
    }

    @Override
    protected Class<MutatingConfig> extractConfigClass(
            String configTypeString) {
        return MutatingConfigType.valueOf(configTypeString).getConfigClass();
    }

    @Override
    protected String getConfigTypeKey() {
        return MUTATING_CONFIG_TYPE;
    }

    @Override
    protected String extractConfigId(MutatingConfig mutatingConfig) {
        return mutatingConfig.getConfigId();
    }

}
