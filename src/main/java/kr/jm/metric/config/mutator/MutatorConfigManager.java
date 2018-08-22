package kr.jm.metric.config.mutator;

import kr.jm.metric.config.AbstractListConfigManager;

import java.util.List;

/**
 * The type Mutator config manager.
 */
public class MutatorConfigManager extends
        AbstractListConfigManager<MutatorConfigInterface> {

    /**
     * The constant MUTATING_CONFIG_TYPE.
     */
    public static final String MUTATING_CONFIG_TYPE = "mutatorConfigType";

    /**
     * Instantiates a new Mutator config manager.
     *
     * @param configFilename the config filename
     */
    public MutatorConfigManager(String configFilename) {
        super(configFilename);
    }

    /**
     * Instantiates a new Mutator config manager.
     *
     * @param configList the config list
     */
    public MutatorConfigManager(List<MutatorConfigInterface> configList) {
        super(configList);
    }

    @Override
    protected Class<MutatorConfigInterface> extractConfigClass(
            String configTypeString) {
        return MutatorConfigType.valueOf(configTypeString).getConfigClass();
    }

    @Override
    protected String getConfigTypeKey() {
        return MUTATING_CONFIG_TYPE;
    }

    @Override
    protected String extractConfigId(MutatorConfigInterface mutatorConfig) {
        return mutatorConfig.getMutatorId();
    }

}
