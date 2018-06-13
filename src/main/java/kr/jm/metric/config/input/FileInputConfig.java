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

    public FileInputConfig(String dataId, String filePath) {
        this(dataId, null, null, filePath);
    }

    public FileInputConfig(String dataId, Integer bulkSize,
            Integer flushIntervalSeconds, String filePath) {
        super(dataId, bulkSize, flushIntervalSeconds);
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
