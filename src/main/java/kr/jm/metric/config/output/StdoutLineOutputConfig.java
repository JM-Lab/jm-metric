package kr.jm.metric.config.output;

import kr.jm.metric.output.StdoutLineOutput;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * The type Std output config.
 */
@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StdoutLineOutputConfig extends AbstractOutputConfig {

    private boolean enableJsonString;

    /**
     * Instantiates a new Std output config.
     *
     * @param enableJsonString the enable json string
     */
    public StdoutLineOutputConfig(boolean enableJsonString) {
        this("StdoutLineOutput-Json-" + enableJsonString, enableJsonString);
    }

    /**
     * Instantiates a new Std output config.
     *
     * @param outputId         the output id
     * @param enableJsonString the enable json string
     */
    public StdoutLineOutputConfig(String outputId, boolean enableJsonString) {
        super(outputId);
        this.enableJsonString = enableJsonString;
    }

    @Override
    public StdoutLineOutput buildOutput() {
        return new StdoutLineOutput(this);
    }

    @Override
    public OutputConfigType getOutputConfigType() {
        return OutputConfigType.STDOUT;
    }
}
