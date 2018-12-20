package kr.jm.metric.config.input;

import kr.jm.metric.input.FileInput;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FileInputConfig extends AbstractInputConfig {

    private String filePath;

    public FileInputConfig(String filePath) {
        this(filePath, null, null, filePath);
    }

    public FileInputConfig(String inputId, String filePath) {
        this(inputId, null, filePath);
    }

    public FileInputConfig(String inputId, Integer bulkSize, String filePath) {
        this(inputId, bulkSize, null, filePath);
    }

    public FileInputConfig(String inputId, Integer bulkSize,
            Long flushIntervalMillis, String filePath) {
        this(inputId, bulkSize, flushIntervalMillis, null, null, null,
                filePath);
    }

    public FileInputConfig(String inputId, Integer bulkSize,
            Long flushIntervalMillis, Long waitingMillis,
            Integer maxBufferCapacity, ChunkType chunkType, String filePath) {
        super(inputId, bulkSize, flushIntervalMillis, waitingMillis,
                maxBufferCapacity, chunkType);
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
