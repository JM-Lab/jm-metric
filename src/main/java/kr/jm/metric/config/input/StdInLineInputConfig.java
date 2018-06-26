package kr.jm.metric.config.input;

import kr.jm.metric.input.StdInLineInput;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StdInLineInputConfig extends AbstractInputConfig {

    public StdInLineInputConfig(String inputId) {
        super(inputId);
    }

    public StdInLineInputConfig(String inputId, Integer bulkSize,
            Integer flushIntervalSeconds) {
        super(inputId, bulkSize, flushIntervalSeconds);
    }

    @Override
    public InputConfigType getInputConfigType() {
        return InputConfigType.STDIN;
    }

    @Override
    public StdInLineInput buildInput() {
        return new StdInLineInput(this);
    }
}
