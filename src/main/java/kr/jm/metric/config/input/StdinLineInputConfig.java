package kr.jm.metric.config.input;

import kr.jm.metric.input.StdinLineInput;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StdinLineInputConfig extends AbstractInputConfig {

    public StdinLineInputConfig(String inputId) {
        super(inputId);
    }

    public StdinLineInputConfig(String inputId, ChunkType chunkType) {
        super(inputId, chunkType);
    }

    public StdinLineInputConfig(String inputId, Integer bulkSize,
            Long flushIntervalMillis) {
        super(inputId, bulkSize, flushIntervalMillis);
    }

    public StdinLineInputConfig(String inputId, Integer bulkSize,
            Long flushIntervalMillis, Long waitingMillis,
            Integer maxBufferCapacity, ChunkType chunkType) {
        super(inputId, bulkSize, flushIntervalMillis, waitingMillis,
                maxBufferCapacity, chunkType);
    }

    @Override
    public InputConfigType getInputConfigType() {
        return InputConfigType.STDIN;
    }

    @Override
    public StdinLineInput buildInput() {
        return new StdinLineInput(this);
    }
}
