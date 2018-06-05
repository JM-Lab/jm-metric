package kr.jm.metric;

import kr.jm.metric.config.MetricConfigManager;
import kr.jm.metric.data.ConfigIdTransfer;
import kr.jm.metric.data.FieldMap;
import kr.jm.metric.output.OutputManager;
import kr.jm.metric.output.subscriber.OutputSubscriberBuilder;
import kr.jm.metric.processor.FieldMapListConfigIdTransferTransformProcessor;
import kr.jm.metric.publisher.StringBulkWaitingTransferSubmissionPublisher;
import kr.jm.metric.publisher.StringListTransferSubmissionPublisher;
import kr.jm.metric.publisher.StringListTransferSubmissionPublisherInterface;
import kr.jm.utils.flow.publisher.JMPublisherInterface;
import kr.jm.utils.helper.JMLog;
import kr.jm.utils.helper.JMOptional;
import kr.jm.utils.helper.JMStream;
import kr.jm.utils.helper.JMThread;
import lombok.experimental.Delegate;
import org.slf4j.Logger;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Flow;

/**
 * The type Jm metric.
 */
public class JMMetric implements
        JMPublisherInterface<ConfigIdTransfer<List<FieldMap>>>, AutoCloseable {
    private static final Logger log =
            org.slf4j.LoggerFactory.getLogger(JMMetric.class);

    @Delegate
    private MetricConfigManager metricConfigManager;
    @Delegate
    private OutputManager outputManager;
    private StringListTransferSubmissionPublisherInterface
            stringListTransferSubmissionPublisher;
    private FieldMapListConfigIdTransferTransformProcessor
            fieldMapListConfigIdTransferTransformProcessor;

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        new JMMetricMain().main(args);
    }

    /**
     * Instantiates a new Jm metric.
     */
    public JMMetric(String... outputConfigIds) {
        this(false, outputConfigIds);
    }

    /**
     * Instantiates a new Jm metric.
     *
     * @param isWaiting the is waiting
     */
    public JMMetric(boolean isWaiting, String... outputConfigIds) {
        this(isWaiting, JMThread.newThreadPoolWithAvailableProcessors(),
                outputConfigIds);
    }

    /**
     * Instantiates a new Jm metric.
     *
     * @param stringListTransferSubmissionPublisher the string list transfer submission publisher
     */
    public JMMetric(
            StringListTransferSubmissionPublisherInterface stringListTransferSubmissionPublisher,
            String... outputConfigIds) {
        this(JMThread.newThreadPoolWithAvailableProcessors(),
                stringListTransferSubmissionPublisher, outputConfigIds);
    }

    /**
     * Instantiates a new Jm metric.
     *
     * @param executor the executor
     */
    public JMMetric(ExecutorService executor, String... outputConfigIds) {
        this(false, executor, outputConfigIds);
    }

    /**
     * Instantiates a new Jm metric.
     *
     * @param isWaiting the is waiting
     * @param executor  the executor
     */
    public JMMetric(boolean isWaiting, ExecutorService executor,
            String... outputConfigIds) {
        this(executor,
                isWaiting ? new StringBulkWaitingTransferSubmissionPublisher() : new StringListTransferSubmissionPublisher(),
                outputConfigIds);
    }

    /**
     * Instantiates a new Jm metric.
     *
     * @param executor                              the executor
     * @param stringListTransferSubmissionPublisher the string list transfer submission publisher
     */
    public JMMetric(ExecutorService executor,
            StringListTransferSubmissionPublisherInterface stringListTransferSubmissionPublisher,
            String... outputConfigIds) {
        this(executor, Flow.defaultBufferSize(),
                stringListTransferSubmissionPublisher, outputConfigIds);
    }

    /**
     * Instantiates a new Jm metric.
     *
     * @param executor                              the executor
     * @param maxBufferCapacity                     the max buffer capacity
     * @param stringListTransferSubmissionPublisher the string list transfer submission publisher
     */
    public JMMetric(ExecutorService executor, int maxBufferCapacity,
            StringListTransferSubmissionPublisherInterface
                    stringListTransferSubmissionPublisher,
            String... outputConfigIds) {
        this.metricConfigManager = new MetricConfigManager();
        this.outputManager = new OutputManager();
        this.stringListTransferSubmissionPublisher =
                stringListTransferSubmissionPublisher;
        this.fieldMapListConfigIdTransferTransformProcessor =
                this.stringListTransferSubmissionPublisher
                        .subscribeAndReturnSubcriber(
                                new FieldMapListConfigIdTransferTransformProcessor(
                                        executor, maxBufferCapacity,
                                        metricConfigManager));
        JMStream.buildStream(outputConfigIds).forEach(this::addOutput);
    }

    /**
     * Input file path.
     *
     * @param filePath the file path
     */
    public void inputFilePath(String filePath) {
        stringListTransferSubmissionPublisher.inputFilePath(filePath);
    }

    /**
     * Input file path.
     *
     * @param dataId   the data id
     * @param filePath the file path
     */
    public void inputFilePath(String dataId, String filePath) {
        stringListTransferSubmissionPublisher
                .inputFilePath(dataId, filePath);
    }

    /**
     * Input file.
     *
     * @param file the file
     */
    public void inputFile(File file) {
        stringListTransferSubmissionPublisher.inputFile(file);
    }

    /**
     * Input file.
     *
     * @param dataId the data id
     * @param file   the file
     */
    public void inputFile(String dataId, File file) {
        stringListTransferSubmissionPublisher.inputFile(dataId, file);
    }

    /**
     * Input classpath.
     *
     * @param resourceClasspath the resource classpath
     */
    public void inputClasspath(String resourceClasspath) {
        stringListTransferSubmissionPublisher.inputClasspath(resourceClasspath);
    }

    /**
     * Input classpath.
     *
     * @param dataId            the data id
     * @param resourceClasspath the resource classpath
     */
    public void inputClasspath(String dataId, String resourceClasspath) {
        stringListTransferSubmissionPublisher
                .inputClasspath(dataId, resourceClasspath);
    }

    /**
     * Input file path or classpath.
     *
     * @param filePathOrResourceClasspath the file path or resource classpath
     */
    public void inputFilePathOrClasspath(String filePathOrResourceClasspath) {
        stringListTransferSubmissionPublisher
                .inputFilePathOrClasspath(filePathOrResourceClasspath);
    }

    /**
     * Input file path or classpath.
     *
     * @param dataId                      the data id
     * @param filePathOrResourceClasspath the file path or resource classpath
     */
    public void inputFilePathOrClasspath(String dataId,
            String filePathOrResourceClasspath) {
        stringListTransferSubmissionPublisher
                .inputFilePathOrClasspath(dataId,
                        filePathOrResourceClasspath);
    }

    /**
     * Input.
     *
     * @param dataId   the data id
     * @param dataList the data list
     */
    public void input(String dataId, List<String> dataList) {
        JMOptional.getOptional(dataList).ifPresent(list ->
                stringListTransferSubmissionPublisher
                        .submit(dataId, list));
    }

    /**
     * Input single.
     *
     * @param dataId the data id
     * @param data   the data
     */
    public void inputSingle(String dataId, String data) {
        stringListTransferSubmissionPublisher.submit(dataId, List.of(data));
    }

    @Override
    public void close() throws RuntimeException {
        JMLog.info(log, "close");
        fieldMapListConfigIdTransferTransformProcessor.close();
    }

    @Override
    public void subscribe(
            Flow.Subscriber<? super ConfigIdTransfer<List<FieldMap>>> subscriber) {
        fieldMapListConfigIdTransferTransformProcessor.subscribe(subscriber);
    }

    public void addOutput(String outputConfigId) {
        subscribe(OutputSubscriberBuilder
                .build(outputManager.getOutput(outputConfigId)));
    }
}
