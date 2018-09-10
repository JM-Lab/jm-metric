package kr.jm.metric.output;

import kr.jm.metric.config.output.FileOutputConfig;
import kr.jm.metric.data.FieldMap;
import kr.jm.metric.data.Transfer;
import kr.jm.utils.JMFileAppender;
import lombok.ToString;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Function;

/**
 * The type File output.
 */
@ToString(callSuper = true)
public class FileOutput extends StdoutLineOutput {

    private JMFileAppender fileAppender;

    /**
     * Instantiates a new File output.
     *
     * @param fileOutputConfig the file output config
     */
    public FileOutput(FileOutputConfig fileOutputConfig) {
        this(fileOutputConfig, null);
    }

    /**
     * Instantiates a new File output.
     *
     * @param filePath the file path
     */
    public FileOutput(String filePath) {
        this(filePath, null);
    }

    /**
     * Instantiates a new File output.
     *
     * @param filePath                      the file path
     * @param transformOutputObjectFunction the transform output object function
     */
    public FileOutput(String filePath,
            Function<List<Transfer<FieldMap>>, List<Object>> transformOutputObjectFunction) {
        this(false, filePath, transformOutputObjectFunction);
    }

    /**
     * Instantiates a new File output.
     *
     * @param enableJsonString the enable json string
     * @param filePath         the file path
     */
    public FileOutput(boolean enableJsonString, String filePath) {
        this(enableJsonString, filePath, null);
    }

    /**
     * Instantiates a new File output.
     *
     * @param enableJsonString              the enable json string
     * @param filePath                      the file path
     * @param transformOutputObjectFunction the transform output object function
     */
    public FileOutput(boolean enableJsonString, String filePath,
            Function<List<Transfer<FieldMap>>, List<Object>> transformOutputObjectFunction) {
        this(new FileOutputConfig(enableJsonString, filePath),
                transformOutputObjectFunction);
    }

    /**
     * Instantiates a new File output.
     *
     * @param fileOutputConfig              the file output config
     * @param transformOutputObjectFunction the transform output object function
     */
    public FileOutput(FileOutputConfig fileOutputConfig,
            Function<List<Transfer<FieldMap>>, List<Object>> transformOutputObjectFunction) {
        super(fileOutputConfig, transformOutputObjectFunction);
        this.fileAppender = new JMFileAppender(fileOutputConfig.getFilePath());
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
