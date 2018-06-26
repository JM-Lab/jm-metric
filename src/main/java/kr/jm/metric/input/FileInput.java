package kr.jm.metric.input;

import kr.jm.metric.config.input.FileInputConfig;
import kr.jm.metric.data.Transfer;
import kr.jm.utils.helper.JMFiles;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Consumer;

/**
 * The type File output.
 */
@Slf4j
public class FileInput extends AbstractInput<FileInputConfig> {

    public FileInput(FileInputConfig inputConfig) {
        super(inputConfig);
    }

    @Override
    protected void startImpl(Consumer<Transfer<String>> inputConsumer) {
        JMFiles.getLineStream(this.inputConfig.getFilePath())
                .forEach(this::newTransfer);
    }

    public FileInput(String filePath) {
        this(new FileInputConfig(filePath));
    }

    @Override
    protected void closeImpl() {

    }

}
