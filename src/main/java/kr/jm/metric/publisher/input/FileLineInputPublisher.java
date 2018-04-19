package kr.jm.metric.publisher.input;

import kr.jm.utils.helper.JMFiles;

import static kr.jm.utils.flow.publisher.BulkSubmissionPublisher.DEFAULT_BULK_SIZE;
import static kr.jm.utils.flow.publisher.BulkSubmissionPublisher.DEFAULT_FLUSH_INTERVAL_SECONDS;

/**
 * The type Std in line bulk transfer submission publisher.
 */
public class FileLineInputPublisher extends AbstractInputPublisher {

    private String filePath;

    /**
     * Instantiates a new Std in line bulk transfer submission publisher.
     *
     * @param dataId   the data id
     * @param filePath the file path
     */
    public FileLineInputPublisher(String dataId, String filePath) {
        this(dataId, DEFAULT_BULK_SIZE, filePath);
    }

    /**
     * Instantiates a new Std in line bulk transfer submission publisher.
     *
     * @param dataId   the data id
     * @param bulkSize the bulk size
     * @param filePath the file path
     */
    public FileLineInputPublisher(String dataId,
            int bulkSize, String filePath) {
        this(dataId, bulkSize, DEFAULT_FLUSH_INTERVAL_SECONDS, filePath);
    }

    /**
     * Instantiates a new Std in line bulk transfer submission publisher.
     *
     * @param dataId               the data id
     * @param bulkSize             the bulk size
     * @param flushIntervalSeconds the flush interval seconds
     * @param filePath             the file path
     */
    public FileLineInputPublisher(String dataId, int bulkSize,
            int flushIntervalSeconds, String filePath) {
        super(dataId, bulkSize, flushIntervalSeconds);
        this.filePath = filePath;
    }

    @Override
    protected void startImpl() {
        JMFiles.getLineStream(this.filePath)
                .forEach(line -> submitSingle(this.dataId, line));
    }

    @Override
    protected void closeImpl() {
    }

}
