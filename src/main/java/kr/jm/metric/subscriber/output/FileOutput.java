package kr.jm.metric.subscriber.output;

import kr.jm.utils.JMFileAppender;
import kr.jm.utils.helper.JMPath;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;

/**
 * The type File output.
 */
@Slf4j
public class FileOutput extends AbstractStringOutput {

    private JMFileAppender fileAppender;

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
    public FileOutput(String filePath, boolean enableJsonString) {
        super(enableJsonString);
        changeFile(filePath);
    }

    /**
     * Gets file path.
     *
     * @return the file path
     */
    public Path getFilePath() {
        return this.fileAppender.getFilePath();
    }

    /**
     * Change file.
     *
     * @param filePath the file path
     */
    public void changeFile(String filePath) {
        this.fileAppender = new JMFileAppender(JMPath.getPath(filePath));
    }

    @Override
    protected void writeString(String string) {
        this.fileAppender.appendLine(string);
    }

    @Override
    public void close() {
        this.fileAppender.close();
    }
}
