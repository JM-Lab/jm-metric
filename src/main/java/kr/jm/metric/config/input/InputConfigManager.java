package kr.jm.metric.config.input;

import kr.jm.metric.config.AbstractConfigManager;

public class InputConfigManager extends
        AbstractConfigManager<InputConfigInterface> {
    private static final String INPUT_CONFIG_TYPE = "inputConfigType";

    public InputConfigManager(String configFilename) {
        super(configFilename);
    }

    @Override
    protected Class<InputConfigInterface> extractConfigClass(
            String configTypeString) {
        return InputConfigType.valueOf(configTypeString).getConfigClass();
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
