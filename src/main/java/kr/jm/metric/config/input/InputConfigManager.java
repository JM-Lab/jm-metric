package kr.jm.metric.config.input;

import com.fasterxml.jackson.core.type.TypeReference;
import kr.jm.metric.config.AbstractConfigManager;

public class InputConfigManager extends
        AbstractConfigManager<InputConfigInterface> {
    private static final String INPUT_CONFIG_TYPE = "inputConfigType";

    public InputConfigManager(String configFilename) {
        super(configFilename);
    }

    @Override
    protected TypeReference<InputConfigInterface> extractConfigTypeReference(
            String configTypeString) {
        return InputConfigType.valueOf(configTypeString).getTypeReference();
    }

    @Override
    protected String getConfigTypeKey() {
        return INPUT_CONFIG_TYPE;
    }

    @Override
    protected String extractConfigId(InputConfigInterface inputConfig) {
        return inputConfig.getInputId();
    }

}
