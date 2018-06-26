package kr.jm.metric;

import kr.jm.metric.input.publisher.InputPublisher;
import kr.jm.metric.input.publisher.InputPublisherBuilder;
import kr.jm.metric.output.subscriber.OutputSubscriber;
import kr.jm.metric.output.subscriber.OutputSubscriberBuilder;
import kr.jm.metric.processor.FieldMapConfigIdTransferListTransformProcessor;
import kr.jm.utils.datastructure.JMCollections;
import kr.jm.utils.exception.JMExceptionManager;
import kr.jm.utils.helper.JMLog;
import kr.jm.utils.helper.JMStream;
import kr.jm.utils.helper.JMThread;
import lombok.experimental.Delegate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Flow;
import java.util.stream.Stream;

/**
 * The type Jm metric.
 */
public class JMMetric extends FieldMapConfigIdTransferListTransformProcessor {

    @Delegate
    private JMMetricConfigManager jmMetricConfigManager;

    private InputPublisher inputPublisher;
    private List<OutputSubscriber> outputSubscriberList;

    public static void main(String[] args) {
        new JMMetricMain().main(args);
    }

    public JMMetric() {
        this(new JMMetricConfigManager(), "Raw");
    }

    public JMMetric(JMMetricConfigManager jmMetricConfigManager,
            String mutatingConfigId) {
        this(JMThread.newThreadPoolWithAvailableProcessors(),
                Flow.defaultBufferSize(), jmMetricConfigManager, null,
                mutatingConfigId);
    }

    public JMMetric(String inputId, String mutatingConfigId,
            String... outputIds) {
        this(JMThread.newThreadPoolWithAvailableProcessors(), inputId,
                mutatingConfigId, outputIds);
    }

    public JMMetric(ExecutorService executor, String inputId,
            String mutatingConfigId, String... outputIds) {
        this(executor, Flow.defaultBufferSize(), new JMMetricConfigManager(),
                inputId, mutatingConfigId, outputIds);
    }

    private JMMetric(ExecutorService executor, int maxBufferCapacity,
            JMMetricConfigManager jmMetricConfigManager, String inputId,
            String mutatingConfigId, String... outputIds) {
        super(executor, maxBufferCapacity,
                jmMetricConfigManager.getMutatingConfigManager());
        this.jmMetricConfigManager = jmMetricConfigManager;
        Optional.ofNullable(inputId)
                .ifPresentOrElse(this::setInput, () -> setInput("StdIn"));
        bindInputIdToMutatingConfigId(inputId, mutatingConfigId);
        this.outputSubscriberList = new ArrayList<>();
        JMStream.buildStream(outputIds).forEach(this::addOutput);
    }

    public void setInput(String inputId) {
        this.jmMetricConfigManager.getInputConfigManager()
                .getMutatingConfigAsOpt(inputId)
                .map(InputPublisherBuilder::build)
                .map(inputPublisher -> this.inputPublisher = inputPublisher)
                .ifPresentOrElse(
                        inputPublisher -> inputPublisher.subscribe(this),
                        () -> logAndThrowConfigError("setInput", inputId));
    }

    private void logAndThrowConfigError(String methodName, String... params) {
        JMExceptionManager.handleExceptionAndThrowRuntimeEx(log,
                JMExceptionManager.newRunTimeException(
                        "No Input Config Occur !!!"),
                "setInput", params);
    }

    public void addOutput(String outputId) {
        this.jmMetricConfigManager.getOutputConfigManager()
                .getMutatingConfigAsOpt
                        (outputId)
                .map(OutputSubscriberBuilder::build)
                .map(outputSubscriber -> JMCollections
                        .addAndGet(this.outputSubscriberList, outputSubscriber))
                .ifPresentOrElse(this::subscribe,
                        () -> logAndThrowConfigError("setOutput", outputId));
    }

    public void start() {
        JMLog.info(log, "start", getInputId(), getOutputIdList());
        this.inputPublisher.start();
    }

    @Override
    public void close() {
        JMLog.info(log, "close", getInputId(), getOutputIdList());
        Optional.ofNullable(this.inputPublisher)
                .ifPresent(InputPublisher::close);
        super.close();
        this.outputSubscriberList.forEach(OutputSubscriber::close);
    }

    public String getInputId() {
        return this.inputPublisher.getInputId();
    }

    public List<String> getOutputIdList() {
        return JMCollections.buildNewList(this.outputSubscriberList,
                OutputSubscriber::getOutputId);
    }

    public void testInput(String data) {inputPublisher.testInput(data);}

    public void testInput(Stream<String> dataStream) {
        inputPublisher.testInput(dataStream);
    }
}
