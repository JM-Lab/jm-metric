package kr.jm.metric.config.output;

import kr.jm.metric.output.FileOutput;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FileOutputConfig extends StdoutLineOutputConfig {

    private String filePath;

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
