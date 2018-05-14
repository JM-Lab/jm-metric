package kr.jm.metric.output.config;

import lombok.Getter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

/**
 * The type File output config.
 */
@Getter
@ToString(callSuper = true)
public class FileOutputConfig extends StringOutputConfig {

    private String filePath;

    /**
     * Instantiates a new File output config.
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
}
