package kr.jm.metric;

import kr.jm.metric.config.MetricConfigManager;
import kr.jm.metric.data.ConfigIdTransfer;
import kr.jm.metric.data.FieldMap;
import kr.jm.metric.processor.FieldMapListConfigIdTransferTransformProcessor;
import kr.jm.metric.publisher.StringBulkWaitingTransferSubmissionPublisher;
import kr.jm.metric.publisher.StringListTransferSubmissionPublisher;
import kr.jm.metric.publisher.StringListTransferSubmissionPublisherInterface;
import kr.jm.utils.flow.publisher.JMPublisherInterface;
import kr.jm.utils.helper.*;
import lombok.experimental.Delegate;
import org.slf4j.Logger;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Flow;

/**
 * The type Jm metric.
 */
public class JMMetric implements
        JMPublisherInterface<ConfigIdTransfer<List<FieldMap>>>, AutoCloseable {
    private static final Logger log =
            org.slf4j.LoggerFactory.getLogger(JMMetric.class);
    private static final String METRIC_CONFIG_URL =
            Optional.ofNullable(
                    JMResources.getSystemProperty("jm.metric.properties"))
                    .orElse("config/JMMetricConfig.json");

    @Delegate
    private MetricConfigManager metricConfigManager;
    private StringListTransferSubmissionPublisherInterface
            stringListTransferSubmissionPublisher;
    private FieldMapListConfigIdTransferTransformProcessor
            fieldMapListConfigIdTransferTransformProcessor;
//    private FieldMapListTransformerProcessor fieldMapListTransformerProcessor;

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
    public JMMetric() {
        this(false);
    }

    /**
     * Instantiates a new Jm metric.
     *
     * @param isWaiting the is waiting
     */
    public JMMetric(boolean isWaiting) {
        this(isWaiting, new MetricConfigManager());
    }

    /**
     * Instantiates a new Jm metric.
     *
     * @param isWaiting           the is waiting
     * @param metricConfigManager the metric properties manager
     */
    public JMMetric(boolean isWaiting,
            MetricConfigManager metricConfigManager) {
        this(isWaiting, JMThread.newThreadPoolWithAvailableProcessors(),
                metricConfigManager);
    }

    /**
     * Instantiates a new Jm metric.
     *
     * @param metricConfigManager the metric properties manager
     */
    public JMMetric(MetricConfigManager metricConfigManager) {
        this(JMThread.newThreadPoolWithAvailableProcessors(),
                metricConfigManager);
    }

    /**
     * Instantiates a new Jm metric.
     *
     * @param stringListTransferSubmissionPublisher the string list transfer submission publisher
     */
    public JMMetric(
            StringListTransferSubmissionPublisherInterface stringListTransferSubmissionPublisher) {
        this(JMThread.newThreadPoolWithAvailableProcessors(),
                stringListTransferSubmissionPublisher);
    }

    /**
     * Instantiates a new Jm metric.
     *
     * @param executor                              the executor
     * @param stringListTransferSubmissionPublisher the string list transfer submission publisher
     */
    public JMMetric(ExecutorService executor,
            StringListTransferSubmissionPublisherInterface
                    stringListTransferSubmissionPublisher) {
        this(executor, new MetricConfigManager(),
                stringListTransferSubmissionPublisher);
    }

    /**
     * Instantiates a new Jm metric.
     *
     * @param executor            the executor
     * @param metricConfigManager the metric properties manager
     */
    public JMMetric(ExecutorService executor,
            MetricConfigManager metricConfigManager) {
        this(false, executor, metricConfigManager);
    }

    /**
     * Instantiates a new Jm metric.
     *
     * @param isWaiting the is waiting
     * @param executor  the executor
     */
    public JMMetric(boolean isWaiting, ExecutorService executor) {
        this(isWaiting, executor, new MetricConfigManager());
    }

    /**
     * Instantiates a new Jm metric.
     *
     * @param isWaiting           the is waiting
     * @param executor            the executor
     * @param metricConfigManager the metric properties manager
     */
    public JMMetric(boolean isWaiting, ExecutorService executor,
            MetricConfigManager metricConfigManager) {
        this(executor, metricConfigManager,
                isWaiting ? new StringBulkWaitingTransferSubmissionPublisher()
                        : new StringListTransferSubmissionPublisher());
    }

    /**
     * Instantiates a new Jm metric.
     *
     * @param metricConfigManager                   the metric properties manager
     * @param stringListTransferSubmissionPublisher the string list transfer submission publisher
     */
    public JMMetric(MetricConfigManager metricConfigManager,
            StringListTransferSubmissionPublisherInterface
                    stringListTransferSubmissionPublisher) {
        this(JMThread.newThreadPoolWithAvailableProcessors(),
                metricConfigManager, stringListTransferSubmissionPublisher);
    }

    /**
     * Instantiates a new Jm metric.
     *
     * @param executor                              the executor
     * @param metricConfigManager                   the metric properties manager
     * @param stringListTransferSubmissionPublisher the string list transfer submission publisher
     */
    public JMMetric(ExecutorService executor,
            MetricConfigManager metricConfigManager,
            StringListTransferSubmissionPublisherInterface
                    stringListTransferSubmissionPublisher) {
        this(executor, Flow.defaultBufferSize(), metricConfigManager,
                stringListTransferSubmissionPublisher);
    }

    /**
     * Instantiates a new Jm metric.
     *
     * @param executor                              the executor
     * @param maxBufferCapacity                     the max buffer capacity
     * @param metricConfigManager                   the metric properties manager
     * @param stringListTransferSubmissionPublisher the string list transfer submission publisher
     */
    public JMMetric(ExecutorService executor, int maxBufferCapacity,
            MetricConfigManager metricConfigManager,
            StringListTransferSubmissionPublisherInterface
                    stringListTransferSubmissionPublisher) {
        this.stringListTransferSubmissionPublisher =
                stringListTransferSubmissionPublisher;
        this.fieldMapListConfigIdTransferTransformProcessor =
                this.stringListTransferSubmissionPublisher
                        .subscribeAndReturnSubcriber(
                                new FieldMapListConfigIdTransferTransformProcessor(
                                        executor, maxBufferCapacity,
                                        metricConfigManager));
        this.metricConfigManager = metricConfigManager;
        this.metricConfigManager
                .loadConfig(JMJson.withJsonResource("JMMetricConfig.json",
                        JMJson.LIST_MAP_TYPE_REFERENCE));
        Optional.of(JMPath.getPath(METRIC_CONFIG_URL)).filter(JMPath::exists)
                .map(Path::toFile).ifPresent(file -> this.metricConfigManager
                .loadConfig(JMJson.withJsonFile(file,
                        JMJson.LIST_MAP_TYPE_REFERENCE)));
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

//    @Override
//    public void subscribe(Flow.Subscriber<? super List<FieldMap>> subscriber) {
//        getFieldMapListTransformerProcessor().subscribe(subscriber);
//    }
//
//    private FieldMapListTransformerProcessor getFieldMapListTransformerProcessor() {
//        return JMLambda.supplierIfNull(this.fieldMapListTransformerProcessor,
//                () -> this.fieldMapListTransformerProcessor =
//                        fieldMapListConfigIdTransferTransformProcessor
//                                .subscribeAndReturnProcessor(
//                                        new FieldMapListTransformerProcessor()));
//
//    }
//
//    /**
//     * Subscribe properties id transfer with jm metric.
//     *
//     * @param subscriber the subscriber
//     * @return the jm metric
//     */
//    public JMMetric subscribeConfigIdTransferWith(
//            Flow.Subscriber<? super ConfigIdTransfer<List<FieldMap>>> subscriber) {
//        subscribe(subscriber);
//        return this;
//    }

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
}
