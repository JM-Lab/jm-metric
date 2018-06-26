package kr.jm.metric;

import kr.jm.metric.data.ConfigIdTransfer;
import kr.jm.metric.data.FieldMap;
import kr.jm.metric.data.Transfer;
import kr.jm.metric.input.publisher.InputPublisher;
import kr.jm.metric.input.publisher.InputPublisherBuilder;
import kr.jm.metric.output.subscriber.OutputSubscriber;
import kr.jm.metric.output.subscriber.OutputSubscriberBuilder;
import kr.jm.metric.processor.MutatingProcessor;
import kr.jm.utils.datastructure.JMCollections;
import kr.jm.utils.flow.processor.JMTransformProcessorInterface;
import kr.jm.utils.helper.JMLog;
import kr.jm.utils.helper.JMOptional;
import kr.jm.utils.helper.JMStream;
import kr.jm.utils.helper.JMThread;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Flow;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The type Jm metric.
 */
@Slf4j
public class JMMetric implements
        JMTransformProcessorInterface<List<Transfer<String>>,
                List<ConfigIdTransfer<FieldMap>>>, AutoCloseable {
    @Getter
    private JMMetricConfigManager jmMetricConfigManager;
    private InputPublisher inputPublisher;
    private MutatingProcessor mutatingProcessor;
    private List<OutputSubscriber> outputSubscriberList;

    public static void main(String[] args) {
        new JMMetricMain().main(args);
    }

    public JMMetric() {
        this(null);
    }

    public JMMetric(String mutatingConfigId) {
        this(null, mutatingConfigId);
    }

    public JMMetric(String inputId, String mutatingConfigId,
            String... outputIds) {
        this(new JMMetricConfigManager(),
                JMThread.newThreadPoolWithAvailableProcessors(), inputId,
                mutatingConfigId, outputIds);
    }

    public JMMetric(JMMetricConfigManager jmMetricConfigManager, String inputId,
            String mutatingConfigId, String... outputIds) {
        this(jmMetricConfigManager,
                JMThread.newThreadPoolWithAvailableProcessors(), inputId,
                mutatingConfigId, outputIds);
    }

    public JMMetric(JMMetricConfigManager jmMetricConfigManager,
            ExecutorService executor, String inputId, String mutatingConfigId,
            String... outputIds) {
        this(jmMetricConfigManager, executor, Flow.defaultBufferSize(), inputId,
                mutatingConfigId, outputIds);
    }

    private JMMetric(JMMetricConfigManager jmMetricConfigManager,
            ExecutorService executor, int maxBufferCapacity,
            String inputId, String mutatingConfigId, String... outputIds) {
        this.jmMetricConfigManager = jmMetricConfigManager;
        this.inputPublisher =
                InputPublisherBuilder.build(jmMetricConfigManager
                        .getInputConfig(
                                Optional.ofNullable(inputId).orElse("StdIn")));
        this.mutatingProcessor =
                this.inputPublisher.subscribeAndReturnSubcriber(
                        new MutatingProcessor(executor, maxBufferCapacity,
                                jmMetricConfigManager.getMutatingConfig(
                                        Optional.ofNullable(mutatingConfigId)
                                                .orElse("Raw"))));
        this.outputSubscriberList = JMStream.buildStream(
                JMOptional.getOptional(outputIds)
                        .orElseGet(() -> new String[]{"StdOut"}))
                .map(jmMetricConfigManager::getOutputConfig)
                .map(OutputSubscriberBuilder::build)
                .peek(mutatingProcessor::subscribe)
                .collect(Collectors.toList());
    }

    public void start() {
        JMLog.info(log, "start", getInputId(), getMutatingId(),
                getOutputIdList());
        this.inputPublisher.start();
    }

    @Override
    public void close() {
        JMLog.info(log, "close", getInputId(), getMutatingId(),
                getOutputIdList());
        this.inputPublisher.close();
        this.mutatingProcessor.close();
        this.outputSubscriberList.forEach(OutputSubscriber::close);
    }

    public String getInputId() {
        return this.inputPublisher.getInputId();
    }

    public String getMutatingId() {
        return this.mutatingProcessor.getMutatingId();
    }

    public List<String> getOutputIdList() {
        return JMCollections.buildNewList(this.outputSubscriberList,
                OutputSubscriber::getOutputId);
    }

    public void testInput(String data) {inputPublisher.testInput(data);}

    public void testInput(Stream<String> dataStream) {
        inputPublisher.testInput(dataStream);
    }

    @Override
    public void subscribe(
            Flow.Subscriber<? super List<ConfigIdTransfer<FieldMap>>> subscriber) {
        mutatingProcessor.subscribe(subscriber);
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        mutatingProcessor.onSubscribe(subscription);
    }

    @Override
    public void onNext(List<Transfer<String>> item) {
        mutatingProcessor.onNext(item);
    }

    @Override
    public void onError(Throwable throwable) {
        mutatingProcessor.onError(throwable);
    }

    @Override
    public void onComplete() {
        mutatingProcessor.onComplete();
    }

    public void printAllConfig() {
        jmMetricConfigManager.printAllConfig();
    }
}
