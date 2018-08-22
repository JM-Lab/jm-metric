package kr.jm.metric.config.input;

import kr.jm.metric.config.AbstractListConfigManager;

/**
 * The type Input config manager.
 */
public class InputConfigManager extends
        AbstractListConfigManager<InputConfigInterface> {
    private static final String INPUT_CONFIG_TYPE = "inputConfigType";

    /**
     * Instantiates a new Input config manager.
     *
     * @param configFilename the config filename
     */
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
