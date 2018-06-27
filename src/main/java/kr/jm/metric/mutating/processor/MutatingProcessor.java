package kr.jm.metric.mutating.processor;

import kr.jm.metric.config.mutating.MutatingConfig;
import kr.jm.metric.data.ConfigIdTransfer;
import kr.jm.metric.data.FieldMap;
import kr.jm.metric.data.Transfer;
import kr.jm.metric.mutating.FieldMapConfigIdTransferListMutating;
import kr.jm.utils.flow.processor.JMConcurrentTransformProcessor;
import kr.jm.utils.helper.JMThread;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Flow;
import java.util.function.Consumer;

public class MutatingProcessor extends
        JMConcurrentTransformProcessor<List<Transfer<String>>, List<ConfigIdTransfer<FieldMap>>> {

    private String mutatingId;

    public MutatingProcessor(MutatingConfig mutatingConfig) {
        this(JMThread.newThreadPoolWithAvailableProcessors(),
                mutatingConfig);
    }

    public MutatingProcessor(Executor executor, MutatingConfig mutatingConfig) {
        this(executor, Flow.defaultBufferSize(), mutatingConfig);
        this.mutatingId = mutatingConfig.getConfigId();
    }

    public MutatingProcessor(Executor executor, int maxBufferCapacity,
            MutatingConfig mutatingConfig) {
        super(executor, maxBufferCapacity,
                new FieldMapConfigIdTransferListMutating(mutatingConfig));
    }

    public String getMutatingId() {
        return this.mutatingId;
    }

    @Override
    public MutatingProcessor subscribeWith(
            Flow.Subscriber<List<ConfigIdTransfer<FieldMap>>>... subscribers) {
        super.subscribeWith(subscribers);
        return this;
    }

    @Override
    public MutatingProcessor consumeWith(
            Consumer<List<ConfigIdTransfer<FieldMap>>>... consumers) {
        super.consumeWith(consumers);
        return this;
    }
}
