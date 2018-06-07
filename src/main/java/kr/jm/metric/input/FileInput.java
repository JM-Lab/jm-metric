package kr.jm.metric.input;

import kr.jm.utils.JMFileAppender;
import kr.jm.utils.helper.JMFiles;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;

/**
 * The type File output.
 */
@Slf4j
public class FileInput extends AbstractInput {

    private JMFileAppender fileAppender;

    public FileInput(String filePath) {
        this.fileAppender = new JMFileAppender(filePath);
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
    protected void startImpl(Consumer<List<String>> inputConsumer) {
        inputConsumer.accept(JMFiles.readLines(getFilePath()));
    }

    @Override
    protected void closeImpl() {
        this.fileAppender.close();
    }

}
