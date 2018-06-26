package kr.jm.metric.processor;

import kr.jm.metric.config.mutating.MutatingConfigManager;
import kr.jm.metric.data.ConfigIdTransfer;
import kr.jm.metric.data.FieldMap;
import kr.jm.metric.data.Transfer;
import kr.jm.metric.transformer.FieldMapConfigIdTransferListTransformer;
import kr.jm.utils.flow.processor.JMConcurrentTransformProcessor;
import kr.jm.utils.helper.JMThread;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Flow;

/**
 * The type Field map list properties id transfer transform processor.
 */
public class FieldMapConfigIdTransferListTransformProcessor extends
        JMConcurrentTransformProcessor<List<Transfer<String>>, List<ConfigIdTransfer<FieldMap>>> {
    /**
     * Instantiates a new Field map list properties id transfer transform processor.
     *
     * @param mutatingConfigManager the metric properties manager
     */
    public FieldMapConfigIdTransferListTransformProcessor(
            MutatingConfigManager mutatingConfigManager) {
        this(JMThread.newThreadPoolWithAvailableProcessors(),
                mutatingConfigManager);
    }

    /**
     * Instantiates a new Field map list properties id transfer transform processor.
     *
     * @param executor              the executor
     * @param mutatingConfigManager the metric properties manager
     */
    public FieldMapConfigIdTransferListTransformProcessor(Executor executor,
            MutatingConfigManager mutatingConfigManager) {
        this(executor, Flow.defaultBufferSize(), mutatingConfigManager);
    }

    /**
     * Instantiates a new Field map list properties id transfer transform processor.
     *
     * @param executor              the executor
     * @param maxBufferCapacity     the max buffer capacity
     * @param mutatingConfigManager the metric properties manager
     */
    public FieldMapConfigIdTransferListTransformProcessor(Executor executor,
            int maxBufferCapacity,
            MutatingConfigManager mutatingConfigManager) {
        super(executor, maxBufferCapacity,
                new FieldMapConfigIdTransferListTransformer(
                        mutatingConfigManager));
    }

}
