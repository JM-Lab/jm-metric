package kr.jm.metric.output;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * The type File output config.
 */
public class FileOutputConfig extends StringOutputConfig {

    @Getter
    private String filePath;

    /**
     * Instantiates a new File output config.
     *
     * @param configId         the config id
     * @param enableJsonString the enable json string
     * @param filePath         the file path
     */
    public FileOutputConfig(String configId, boolean enableJsonString,
            String filePath) {
        super(configId, enableJsonString);
        this.filePath = filePath;
    }

    @Override
    protected Map<String, Object> buildChildConfig() {
        return new HashMap<>(super.buildChildConfig()) {{
            put("filePath", filePath);
        }};
    }
}
