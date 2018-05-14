package kr.jm.metric.output;

import lombok.Getter;

import java.util.Map;

/**
 * The type String output config.
 */
public class StringOutputConfig extends AbstractOutputConfig {

    @Getter
    private boolean enableJsonString;

    /**
     * Instantiates a new String output config.
     *
     * @param configId         the config id
     * @param enableJsonString the enable json string
     */
    public StringOutputConfig(String configId, boolean enableJsonString) {
        super(configId);
        this.enableJsonString = enableJsonString;
    }

    @Override
    protected Map<String, Object> buildChildConfig() {
        return Map.of("enableJsonString", enableJsonString);
    }

}
