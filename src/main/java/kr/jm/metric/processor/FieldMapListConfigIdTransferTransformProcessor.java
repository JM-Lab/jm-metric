package kr.jm.metric.processor;

import kr.jm.metric.config.MetricConfigManager;
import kr.jm.metric.config.field.FieldMeta;
import kr.jm.metric.data.ConfigIdTransfer;
import kr.jm.metric.data.FieldMap;
import kr.jm.metric.data.Transfer;
import kr.jm.metric.transformer.FieldMapListConfigIdTransferListTransformer;
import kr.jm.utils.flow.processor.JMConcurrentTransformProcessor;
import kr.jm.utils.flow.processor.JMTransformProcessorBuilder;
import kr.jm.utils.flow.processor.JMTransformProcessorInterface;
import kr.jm.utils.helper.JMLog;
import kr.jm.utils.helper.JMThread;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.Flow;

import static kr.jm.utils.flow.processor.JMTransformProcessorBuilder.buildCollectionEach;

/**
 * The type Field map list config id transfer transform processor.
 */
@Slf4j
public class FieldMapListConfigIdTransferTransformProcessor implements
        JMTransformProcessorInterface<Transfer<List<String>>,
                ConfigIdTransfer<List<FieldMap>>>, AutoCloseable {

    private JMConcurrentTransformProcessor<Transfer<List<String>>,
            List<ConfigIdTransfer<List<FieldMap>>>>
            concurrentTransformProcessor;
    private JMTransformProcessorInterface<List<ConfigIdTransfer<List<FieldMap>>>, ConfigIdTransfer<List<FieldMap>>>
            outputFieldMapListConfigIdTransferProcessor;

    /**
     * Instantiates a new Field map list config id transfer transform processor.
     */
    public FieldMapListConfigIdTransferTransformProcessor() {
        this(new MetricConfigManager());
    }

    /**
     * Instantiates a new Field map list config id transfer transform processor.
     *
     * @param metricConfigManager the metric config manager
     */
    public FieldMapListConfigIdTransferTransformProcessor(
            MetricConfigManager metricConfigManager) {
        this(JMThread.newThreadPoolWithAvailableProcessors(),
                metricConfigManager);
    }

    /**
     * Instantiates a new Field map list config id transfer transform processor.
     *
     * @param executor            the executor
     * @param metricConfigManager the metric config manager
     */
    public FieldMapListConfigIdTransferTransformProcessor(Executor executor,
            MetricConfigManager metricConfigManager) {
        this(executor, Flow.defaultBufferSize(), metricConfigManager);
    }

    /**
     * Instantiates a new Field map list config id transfer transform processor.
     *
     * @param executor            the executor
     * @param maxBufferCapacity   the max buffer capacity
     * @param metricConfigManager the metric config manager
     */
    public FieldMapListConfigIdTransferTransformProcessor(Executor executor,
            int maxBufferCapacity, MetricConfigManager metricConfigManager) {
        this.concurrentTransformProcessor = JMTransformProcessorBuilder
                .buildWithThreadPool(executor, maxBufferCapacity,
                        new FieldMapListConfigIdTransferListTransformer(
                                metricConfigManager));
        this.outputFieldMapListConfigIdTransferProcessor =
                this.concurrentTransformProcessor.subscribeAndReturn(
                        buildCollectionEach(this::buildFinalMeta));
    }

    private ConfigIdTransfer<List<FieldMap>> buildFinalMeta(
            ConfigIdTransfer<List<FieldMap>> fieldMapListConfigIdTransfer) {
        Optional.ofNullable(fieldMapListConfigIdTransfer.getFieldMeta())
                .map(FieldMeta::extractFieldMetaMap)
                .ifPresent(extractFlatMap -> fieldMapListConfigIdTransfer
                        .putMeta("field", extractFlatMap));
        return fieldMapListConfigIdTransfer;
    }

    @Override
    public void close() {
        JMLog.debug(log, "close");
        concurrentTransformProcessor.close();
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        JMLog.debug(log, "onSubscribe", subscription);
        concurrentTransformProcessor.onSubscribe(subscription);
    }

    @Override
    public void onNext(
            Transfer<List<String>> item) {
        JMLog.debug(log, "onNext", item);
        if (item.getData().size() > 0)
            concurrentTransformProcessor.onNext(item);
    }

    @Override
    public void onError(Throwable throwable) {
        JMLog.debug(log, "onError", throwable);
        concurrentTransformProcessor.onError(throwable);
    }

    @Override
    public void onComplete() {
        JMLog.debug(log, "onComplete");
        concurrentTransformProcessor.onComplete();
    }

    @Override
    public void subscribe(
            Flow.Subscriber<? super ConfigIdTransfer<List<FieldMap>>>
                    subscriber) {
        JMLog.debug(log, "subscribeWith", subscriber);
        outputFieldMapListConfigIdTransferProcessor.subscribe(subscriber);
    }


}
