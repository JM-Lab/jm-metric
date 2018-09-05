package kr.jm.metric.input;

import kr.jm.metric.config.input.FileInputConfig;
import kr.jm.metric.data.Transfer;
import kr.jm.utils.helper.JMFiles;
import lombok.ToString;

import java.util.function.Consumer;

/**
 * The type File input.
 */
@ToString(callSuper = true)
public class FileInput extends AbstractInput<FileInputConfig> {

    /**
     * Instantiates a new File input.
     *
     * @param inputConfig the input config
     */
    public FileInput(FileInputConfig inputConfig) {
        super(inputConfig);
    }

    @Override
    protected void startImpl(Consumer<Transfer<String>> inputConsumer) {
        JMFiles.getLineStream(this.inputConfig.getFilePath())
                .map(this::newTransfer).forEach(inputConsumer::accept);
    }

    /**
     * Instantiates a new File input.
     *
     * @param filePath the file path
     */
    public FileInput(String filePath) {
        this(new FileInputConfig(filePath));
    }

    @Override
    protected void closeImpl() {

    }

}
