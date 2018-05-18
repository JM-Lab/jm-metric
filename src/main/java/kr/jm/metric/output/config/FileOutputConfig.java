package kr.jm.metric.output.config;

import kr.jm.metric.output.FileOutput;
import kr.jm.metric.output.config.type.OutputConfigType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

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
        super(enableJsonString);
        this.filePath = filePath;
    }

    @Override
    protected Map<String, Object> buildChildConfig() {
        return new HashMap<>(super.buildChildConfig()) {{
            put("filePath", filePath);
        }};
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
