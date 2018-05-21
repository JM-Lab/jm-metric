package kr.jm.metric.config.output;

import kr.jm.metric.output.StdOutput;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Map;

/**
 * The type String output properties.
 */
@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StdOutputConfig extends AbstractMapOutputConfig {

    private boolean enableJsonString;

    /**
     * Instantiates a new String output properties.
     *
     * @param enableJsonString the enable json string
     */
    public StdOutputConfig(boolean enableJsonString) {
        this.enableJsonString = enableJsonString;
    }

    @Override
    protected Map<String, Object> buildChildConfig() {
        return Map.of("enableJsonString", enableJsonString);
    }

    @Override
    public StdOutput buildOutput() {
        return new StdOutput(this);
    }

    @Override
    public OutputConfigType getOutputConfigType() {
        return OutputConfigType.STDOUT;
    }
}
