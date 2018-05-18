package kr.jm.metric.output;

import kr.jm.metric.output.config.FileOutputConfig;
import kr.jm.utils.JMFileAppender;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;

/**
 * The type File output.
 */
@Slf4j
public class FileOutput<T> extends StdOutput<T> {

    private JMFileAppender fileAppender;

    /**
     * Instantiates a new File output.
     *
     * @param fileOutputConfig the file output properties
     */
    public FileOutput(FileOutputConfig fileOutputConfig) {
        super(fileOutputConfig);
        this.fileAppender = new JMFileAppender(fileOutputConfig.getFilePath());
    }

    /**
     * Instantiates a new File output.
     *
     * @param filePath the file path
     */
    public FileOutput(String filePath) {
        this(filePath, false);
    }

    /**
     * Instantiates a new File output.
     *
     * @param filePath         the file path
     * @param enableJsonString the enable json string
     */
    public FileOutput(String filePath,
            boolean enableJsonString) {
        this(new FileOutputConfig(enableJsonString, filePath));
    }

    /**
     * Gets file path.
     *
     * @return the file path
     */
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
