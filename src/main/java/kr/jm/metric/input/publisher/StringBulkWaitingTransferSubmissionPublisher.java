package kr.jm.metric.input.publisher;

import java.io.File;
import java.util.concurrent.Executor;

/**
 * The type String bulk waiting transfer submission publisher.
 */
public class StringBulkWaitingTransferSubmissionPublisher extends
        BulkWaitingTransferSubmissionPublisher<String> implements
        StringListTransferSubmissionPublisherInterface {
    private StringBulkTransferSubmissionPublisher
            bulkTransferSubmissionPublisher;

    /**
     * Instantiates a new String bulk waiting transfer submission publisher.
     */
    public StringBulkWaitingTransferSubmissionPublisher() {
        this(new StringBulkTransferSubmissionPublisher());
    }

    /**
     * Instantiates a new String bulk waiting transfer submission publisher.
     *
     * @param bulkTransferSubmissionPublisher the bulk transfer submission publisher
     */
    public StringBulkWaitingTransferSubmissionPublisher(
            StringBulkTransferSubmissionPublisher bulkTransferSubmissionPublisher) {
        this(null, bulkTransferSubmissionPublisher);
    }

    /**
     * Instantiates a new String bulk waiting transfer submission publisher.
     *
     * @param executor                        the executor
     * @param bulkTransferSubmissionPublisher the bulk transfer submission publisher
     */
    public StringBulkWaitingTransferSubmissionPublisher(Executor executor,
            StringBulkTransferSubmissionPublisher bulkTransferSubmissionPublisher) {
        this(executor, getDefaultQueueSizeLimit(),
                bulkTransferSubmissionPublisher);
    }

    /**
     * Instantiates a new String bulk waiting transfer submission publisher.
     *
     * @param executor                        the executor
     * @param queueSizeLimit                  the queue size limit
     * @param bulkTransferSubmissionPublisher the bulk transfer submission publisher
     */
    public StringBulkWaitingTransferSubmissionPublisher(
            Executor executor, int queueSizeLimit,
            StringBulkTransferSubmissionPublisher bulkTransferSubmissionPublisher) {
        super(executor, queueSizeLimit, bulkTransferSubmissionPublisher);
        this.bulkTransferSubmissionPublisher = bulkTransferSubmissionPublisher;
    }

    public void inputFilePath(String filePath) {
        bulkTransferSubmissionPublisher.inputFilePath(filePath);
    }

    public void inputFilePath(String dataId, String filePath) {
        bulkTransferSubmissionPublisher.inputFilePath(dataId, filePath);
    }

    public void inputFile(File file) {
        bulkTransferSubmissionPublisher.inputFile(file);
    }

    public void inputFile(String dataId, File file) {
        bulkTransferSubmissionPublisher.inputFile(dataId, file);
    }

    public void inputClasspath(String resourceClasspath) {
        bulkTransferSubmissionPublisher.inputClasspath(resourceClasspath);
    }

    public void inputClasspath(String dataId, String resourceClasspath) {
        bulkTransferSubmissionPublisher
                .inputClasspath(dataId, resourceClasspath);
    }

    public void inputFilePathOrClasspath(
            String filePathOrResourceClasspath) {
        bulkTransferSubmissionPublisher
                .inputFilePathOrClasspath(filePathOrResourceClasspath);
    }

    public void inputFilePathOrClasspath(String dataId,
            String filePathOrResourceClasspath) {
        bulkTransferSubmissionPublisher
                .inputFilePathOrClasspath(dataId, filePathOrResourceClasspath);
    }
}
