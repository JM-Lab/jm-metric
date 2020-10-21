package kr.jm.metric.output;

import kr.jm.metric.config.output.FileOutputConfig;
import kr.jm.metric.data.Transfer;
import kr.jm.utils.helper.JMFileAppender;
import lombok.ToString;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@ToString(callSuper = true)
public class FileOutput extends StdoutLineOutput {

    private final JMFileAppender fileAppender;

    public FileOutput(FileOutputConfig fileOutputConfig) {
        this(fileOutputConfig, null);
    }

    public FileOutput(String filePath) {
        this(filePath, null);
    }

    public FileOutput(String filePath,
            Function<List<Transfer<Map<String, Object>>>, List<Object>> transformOutputObjectFunction) {
        this(false, filePath, transformOutputObjectFunction);
    }

    public FileOutput(boolean enableJsonString, String filePath) {
        this(enableJsonString, filePath, null);
    }

    public FileOutput(boolean enableJsonString, String filePath,
            Function<List<Transfer<Map<String, Object>>>, List<Object>> transformOutputObjectFunction) {
        this(new FileOutputConfig(enableJsonString, filePath),
                transformOutputObjectFunction);
    }

    public FileOutput(FileOutputConfig fileOutputConfig,
            Function<List<Transfer<Map<String, Object>>>, List<Object>> transformOutputObjectFunction) {
        super(fileOutputConfig, transformOutputObjectFunction);
        this.fileAppender = new JMFileAppender(fileOutputConfig.getFilePath());
    }

    public Path getFilePath() {
        return this.fileAppender.getFilePath();
    }

    @Override
    protected void writeString(String string) {
        this.fileAppender.appendLine(string);
    }

    @Override
    protected void closeImpl() {
        this.fileAppender.close();
    }

}
