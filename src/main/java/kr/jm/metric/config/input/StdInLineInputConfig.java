package kr.jm.metric.config.input;

import kr.jm.metric.input.StdInLineInput;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * The type Std in line input config.
 */
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StdInLineInputConfig extends AbstractInputConfig {

    /**
     * Instantiates a new Std in line input config.
     *
     * @param inputId the input id
     */
    public StdInLineInputConfig(String inputId) {
        super(inputId);
    }

    public StdInLineInputConfig(String inputId, ChunkType chunkType) {
        super(inputId, chunkType);
    }

    /**
     * Instantiates a new Std in line input config.
     *
     * @param inputId              the input id
     * @param bulkSize             the bulk size
     * @param flushIntervalSeconds the flush interval seconds
     */
    public StdInLineInputConfig(String inputId, Integer bulkSize,
            Integer flushIntervalSeconds) {
        super(inputId, bulkSize, flushIntervalSeconds);
    }

    public StdInLineInputConfig(String inputId, Integer bulkSize,
            Integer flushIntervalSeconds, Long waitingMillis,
            Integer queueSizeLimit, ChunkType chunkType) {
        super(inputId, bulkSize, flushIntervalSeconds, waitingMillis,
                queueSizeLimit, chunkType);
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
