package kr.jm.metric.processor;

import kr.jm.metric.config.mutating.MutatingConfig;
import kr.jm.metric.data.ConfigIdTransfer;
import kr.jm.metric.data.FieldMap;
import kr.jm.metric.data.Transfer;
import kr.jm.metric.transformer.FieldMapConfigIdTransferListTransformer;
import kr.jm.utils.flow.processor.JMConcurrentTransformProcessor;
import kr.jm.utils.helper.JMThread;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Flow;

public class MutatingProcessor extends
        JMConcurrentTransformProcessor<List<Transfer<String>>, List<ConfigIdTransfer<FieldMap>>> {

    private String mutatingId;

    public MutatingProcessor(MutatingConfig mutatingConfig) {
        this(JMThread.newThreadPoolWithAvailableProcessors(),
                mutatingConfig);
    }

    public MutatingProcessor(Executor executor,
            MutatingConfig mutatingConfig) {
        this(executor, Flow.defaultBufferSize(), mutatingConfig);
        this.mutatingId = mutatingConfig.getConfigId();
    }

    public MutatingProcessor(Executor executor,
            int maxBufferCapacity, MutatingConfig mutatingConfig) {
        super(executor, maxBufferCapacity,
                new FieldMapConfigIdTransferListTransformer(mutatingConfig));
    }

    public String getMutatingId() {
        return this.mutatingId;
    }
}
