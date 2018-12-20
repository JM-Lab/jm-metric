package kr.jm.metric.config.mutator;

import kr.jm.metric.config.AbstractListConfigManager;

import java.util.List;

public class MutatorConfigManager extends
        AbstractListConfigManager<MutatorConfigInterface> {

    public static final String MUTATING_CONFIG_TYPE = "mutatorConfigType";

    public MutatorConfigManager(String configFilename) {
        super(configFilename);
    }

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
