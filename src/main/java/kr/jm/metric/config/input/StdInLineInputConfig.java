package kr.jm.metric.config.input;

import kr.jm.metric.input.StdLineInput;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StdInLineInputConfig extends AbstractInputConfig {

    public StdInLineInputConfig(String dataId) {
        super(dataId);
    }

    public StdInLineInputConfig(String dataId, Integer bulkSize,
            Integer flushIntervalSeconds) {
        super(dataId, bulkSize, flushIntervalSeconds);
    }

    @Override
    public InputConfigType getInputConfigType() {
        return InputConfigType.STDIN;
    }

    @Override
    public StdLineInput buildInput() {
        return new StdLineInput(this);
    }
}
