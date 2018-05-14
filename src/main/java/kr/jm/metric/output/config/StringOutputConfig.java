package kr.jm.metric.output.config;

import lombok.Getter;
import lombok.ToString;

import java.util.Map;

/**
 * The type String output config.
 */
@Getter
@ToString(callSuper = true)
public class StringOutputConfig extends AbstractOutputConfig {

    private boolean enableJsonString;

    /**
     * Instantiates a new String output config.
     *
     * @param enableJsonString the enable json string
     */
    public StringOutputConfig(boolean enableJsonString) {
        this.enableJsonString = enableJsonString;
    }

    @Override
    protected Map<String, Object> buildChildConfig() {
        return Map.of("enableJsonString", enableJsonString);
    }

}
