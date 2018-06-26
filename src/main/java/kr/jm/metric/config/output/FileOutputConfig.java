package kr.jm.metric.config.output;

import kr.jm.metric.output.FileOutput;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * The type File output properties.
 */
@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FileOutputConfig extends StdOutputConfig {

    private String filePath;

    /**
     * Instantiates a new File output properties.
     *
     * @param enableJsonString the enable json string
     * @param filePath         the file path
     */
    public FileOutputConfig(boolean enableJsonString, String filePath) {
        this(filePath + "Json-" + enableJsonString, enableJsonString, filePath);
    }

    public FileOutputConfig(String outputId, boolean enableJsonString,
            String filePath) {
        super(outputId, enableJsonString);
        this.filePath = filePath;
    }

    @Override
    public OutputConfigType getOutputConfigType() {
        return OutputConfigType.FILE;
    }

    @Override
    public FileOutput buildOutput() {
        return new FileOutput(this);
    }


}
