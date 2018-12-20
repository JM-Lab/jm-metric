package kr.jm.metric.config.output;

import kr.jm.metric.output.StdoutLineOutput;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StdoutLineOutputConfig extends AbstractOutputConfig {

    private boolean enableJsonString;

    public StdoutLineOutputConfig(boolean enableJsonString) {
        this("StdoutLineOutput-Json-" + enableJsonString, enableJsonString);
    }

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
