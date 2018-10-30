package kr.jm.metric;

import kr.jm.metric.config.JMMetricConfigManager;
import kr.jm.metric.data.FieldMap;
import kr.jm.metric.data.Transfer;
import kr.jm.metric.input.publisher.InputPublisher;
import kr.jm.metric.input.publisher.InputPublisherBuilder;
import kr.jm.metric.mutator.processor.MutatorProcessor;
import kr.jm.metric.mutator.processor.MutatorProcessorBuilder;
import kr.jm.metric.output.subscriber.OutputSubscriber;
import kr.jm.metric.output.subscriber.OutputSubscriberBuilder;
import kr.jm.utils.datastructure.JMCollections;
import kr.jm.utils.flow.processor.JMProcessor;
import kr.jm.utils.flow.processor.JMProcessorBuilder;
import kr.jm.utils.flow.processor.JMProcessorInterface;
import kr.jm.utils.helper.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Flow;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The type Jm metric.
 */
@Slf4j
public class JMMetric implements
        JMProcessorInterface<List<Transfer<String>>, List<Transfer<FieldMap>>>,
        AutoCloseable {

    @Getter
    private JMMetricConfigManager jmMetricConfigManager;
    @Getter
    private InputPublisher inputPublisher;
    @Getter
    private MutatorProcessor mutatorProcessor;
    @Getter
    private List<OutputSubscriber> outputSubscriberList;
    private JMProcessor<List<Transfer<FieldMap>>, List<Transfer<FieldMap>>>
            customProcessor;

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        new JMMetricMain().start(args);
    }

    /**
     * Instantiates a new Jm metric.
     */
    public JMMetric() {
        this(new JMMetricConfigManager());
    }

    /**
     * Instantiates a new Jm metric.
     *
     * @param jmMetricConfigManager the jm metric config manager
     */
    public JMMetric(JMMetricConfigManager jmMetricConfigManager) {
        this(jmMetricConfigManager, null);
    }

    /**
     * Instantiates a new Jm metric.
     *
     * @param mutatorConfigId the mutator config id
     */
    public JMMetric(String mutatorConfigId) {
        this(null, mutatorConfigId);
    }

    /**
     * Instantiates a new Jm metric.
     *
     * @param jmMetricConfigManager the jm metric config manager
     * @param mutatorConfigId       the mutator config id
     */
    public JMMetric(JMMetricConfigManager jmMetricConfigManager, String
            mutatorConfigId) {
        this(jmMetricConfigManager, null, mutatorConfigId);
    }

    /**
     * Instantiates a new Jm metric.
     *
     * @param inputId         the input id
     * @param mutatorConfigId the mutator config id
     * @param outputIds       the output ids
     */
    public JMMetric(String inputId, String mutatorConfigId,
            String... outputIds) {
        this(null, inputId, mutatorConfigId, outputIds);
    }

    /**
     * Instantiates a new Jm metric.
     *
     * @param jmMetricConfigManager the jm metric config manager
     * @param inputId               the input id
     * @param mutatorConfigId       the mutator config id
     * @param outputIds             the output ids
     */
    public JMMetric(JMMetricConfigManager jmMetricConfigManager,
            String inputId, String mutatorConfigId, String... outputIds) {
        this.jmMetricConfigManager =
                JMLambda.supplierIfNull(jmMetricConfigManager,
                        JMMetricConfigManager::new);
        withInputId(inputId).withMutatorId(mutatorConfigId)
                .withOutputIds(outputIds);
        String info =
                "Running with InputId = " + inputPublisher.getInputId() +
                        ", MutatorId = " + mutatorProcessor.getMutatorId() +
                        ", OutputIds = " + outputSubscriberList.stream()
                        .map(OutputSubscriber::getOutputId)
                        .collect(Collectors.joining(JMString.COMMA));
        log.info(info);
        System.out.println(info);
    }

    private JMMetric withOutputIds(String... outputIds) {
        this.outputSubscriberList = JMStream.buildStream(
                JMOptional.getOptional(outputIds)
                        .orElseGet(() -> new String[]{"Stdout"}))
                .map(this.jmMetricConfigManager::getOutputConfig)
                .map(OutputSubscriberBuilder::build)
                .collect(Collectors.toList());
        return this;
    }

    private JMMetric withMutatorId(String mutatorConfigId) {
        this.mutatorProcessor =
                MutatorProcessorBuilder.build(this.jmMetricConfigManager
                        .getMutatorConfig(
                                JMOptional.getOptional(mutatorConfigId)
                                        .orElse("Raw")));
        return this;
    }

    private JMMetric withInputId(String inputId) {
        this.inputPublisher = Optional.ofNullable(inputId)
                .map(this.jmMetricConfigManager::getInputConfig)
                .map(InputPublisherBuilder::build)
                .orElseGet(() -> InputPublisherBuilder
                        .buildTestInput("TestInput"));
        return this;
    }

    /**
     * Start.
     */
    public JMMetric start() {
        JMLog.info(log, "start", getInputId(), getMutatorId(),
                getOutputIdList());
        bindInputAndMutator();
        bindOutput();
        this.inputPublisher.start();
        return this;
    }

    protected void bindOutput() {
        this.outputSubscriberList.forEach(this::subscribe);
    }

    protected void bindInputAndMutator() {
        this.inputPublisher.subscribe(this.mutatorProcessor);
        Optional.ofNullable(this.customProcessor)
                .ifPresent(this.mutatorProcessor::subscribe);
    }

    @Override
    public void close() {
        JMLog.info(log, "close", getInputId(), getMutatorId(),
                getOutputIdList());
        this.inputPublisher.close();
        this.mutatorProcessor.close();
        this.outputSubscriberList.forEach(OutputSubscriber::close);
    }

    /**
     * With custom function jm metric.
     *
     * @param customFunction the custom function
     * @return the jm metric
     */
    public JMMetric withCustomFunction(
            Function<Transfer<FieldMap>, Map<String, Object>> customFunction) {
        this.customProcessor = JMProcessorBuilder
                .build((List<Transfer<FieldMap>> list) -> list.stream()
                        .map(mapTransfer -> mapTransfer.newWith(
                                buildNewFieldMap(customFunction, mapTransfer)))
                        .collect(Collectors.toList()));
        return this;
    }

    private FieldMap buildNewFieldMap(
            Function<Transfer<FieldMap>, Map<String, Object>> customFunction,
            Transfer<FieldMap> transfer) {
        return new FieldMap(customFunction
                .apply(transfer.newWith(transfer.getData().newFieldMap())));
    }

    /**
     * Gets input id.
     *
     * @return the input id
     */
    public String getInputId() {
        return this.inputPublisher.getInputId();
    }

    /**
     * Gets mutator id.
     *
     * @return the mutator id
     */
    public String getMutatorId() {
        return this.mutatorProcessor.getMutatorId();
    }

    /**
     * Gets output id list.
     *
     * @return the output id list
     */
    public List<String> getOutputIdList() {
        return JMCollections.buildNewList(this.outputSubscriberList,
                OutputSubscriber::getOutputId);
    }

    /**
     * Test input jm metric.
     *
     * @param data the data
     * @return the jm metric
     */
    public JMMetric testInput(String data) {
        inputPublisher.testInput(data);
        return this;
    }

    /**
     * Test input jm metric.
     *
     * @param dataList the data list
     * @return the jm metric
     */
    public JMMetric testInput(List<String> dataList) {
        inputPublisher.testInput(dataList);
        return this;
    }

    private Flow.Publisher<List<Transfer<FieldMap>>> getFinalPublisher() {
        return Objects.nonNull(
                this.customProcessor) ? this.customProcessor : this.mutatorProcessor;
    }

    @Override
    public void subscribe(
            Flow.Subscriber<? super List<Transfer<FieldMap>>> subscriber) {
        getFinalPublisher().subscribe(subscriber);
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        mutatorProcessor.onSubscribe(subscription);
    }

    @Override
    public void onNext(List<Transfer<String>> item) {
        mutatorProcessor.onNext(item);
    }

    @Override
    public void onError(Throwable throwable) {
        mutatorProcessor.onError(throwable);
    }

    @Override
    public void onComplete() {
        mutatorProcessor.onComplete();
    }

    @Override
    public JMMetric subscribeWith(
            Flow.Subscriber<List<Transfer<FieldMap>>>... subscribers) {
        JMProcessorInterface.super.subscribeWith(subscribers);
        return this;
    }

    @Override
    public JMMetric consumeWith(
            Consumer<List<Transfer<FieldMap>>>... consumers) {
        JMProcessorInterface.super.consumeWith(consumers);
        return this;
    }

}
