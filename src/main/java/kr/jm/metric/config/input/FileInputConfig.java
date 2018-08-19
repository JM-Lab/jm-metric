package kr.jm.metric.config.input;

import kr.jm.metric.input.FileInput;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * The type File input config.
 */
@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FileInputConfig extends AbstractInputConfig {

    private String filePath;

    /**
     * Instantiates a new File input config.
     *
     * @param filePath the file path
     */
    public FileInputConfig(String filePath) {
        this(filePath, null, null, filePath);
    }

    /**
     * Instantiates a new File input config.
     *
     * @param inputId  the input id
     * @param filePath the file path
     */
    public FileInputConfig(String inputId, String filePath) {
        this(inputId, null, null, filePath);
    }

    /**
     * Instantiates a new File input config.
     *
     * @param inputId              the input id
     * @param bulkSize             the bulk size
     * @param flushIntervalSeconds the flush interval seconds
     * @param filePath             the file path
     */
    public FileInputConfig(String inputId, Integer bulkSize,
            Integer flushIntervalSeconds, String filePath) {
        super(inputId, bulkSize, flushIntervalSeconds);
        this.filePath = filePath;
    }

    @Override
    public InputConfigType getInputConfigType() {
        return InputConfigType.FILE;
    }

    @Override
    public FileInput buildInput() {
        return new FileInput(this.filePath);
    }
}
