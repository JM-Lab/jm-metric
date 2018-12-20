package kr.jm.metric.config.output;

import kr.jm.metric.config.AbstractListConfigManager;

public class OutputConfigManager extends
        AbstractListConfigManager<OutputConfigInterface> {
    private static final String OUTPUT_CONFIG_TYPE = "outputConfigType";

    public OutputConfigManager(String configFilename) {
        super(configFilename);
    }

    @Override
    protected Class<OutputConfigInterface> extractConfigClass(
            String configTypeString) {
        return OutputConfigType.valueOf(configTypeString).getConfigClass();
    }

    @Override
    protected String getConfigTypeKey() {
        return OUTPUT_CONFIG_TYPE;
    }

    @Override
    protected String extractConfigId(OutputConfigInterface inputConfig) {
        return inputConfig.getOutputId();
    }

}
