package kr.jm.metric.config.output;

import com.fasterxml.jackson.core.type.TypeReference;
import kr.jm.metric.config.AbstractConfigManager;

public class OutputConfigManager extends
        AbstractConfigManager<OutputConfigInterface> {
    private static final String OUTPUT_CONFIG_TYPE = "outputConfigType";

    public OutputConfigManager(String configFilename) {
        super(configFilename);
    }

    @Override
    protected TypeReference<OutputConfigInterface> extractConfigTypeReference(
            String configTypeString) {
        return OutputConfigType.valueOf(configTypeString).getTypeReference();
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
