package kr.jm.metric.mutator.processor;

import kr.jm.metric.data.FieldMap;
import kr.jm.metric.data.Transfer;
import kr.jm.metric.mutator.MutatorInterface;
import kr.jm.utils.flow.processor.JMConcurrentTransformProcessor;
import kr.jm.utils.helper.JMLog;
import kr.jm.utils.helper.JMPredicate;
import kr.jm.utils.helper.JMString;
import kr.jm.utils.helper.JMThread;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Flow;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * The type Mutator processor.
 */
@Slf4j
public class MutatorProcessor extends
        JMConcurrentTransformProcessor<List<Transfer<String>>, List<Transfer<FieldMap>>> {

    @Getter
    private String mutatorId;

    public MutatorProcessor(MutatorInterface mutator) {
        this(0, mutator);
    }

    public MutatorProcessor(int workers, MutatorInterface mutator) {
        this(workers, Flow.defaultBufferSize(), mutator);
    }

    private MutatorProcessor(int workers, int maxBufferCapacity,
            MutatorInterface mutator) {
        super(Optional.ofNullable(workers).filter(JMPredicate.getGreater(0))
                        .map(JMThread::newThreadPool)
                        .orElseGet(JMThread::newThreadPoolWithAvailableProcessors),
                maxBufferCapacity, list -> process(mutator, list));
        this.mutatorId = mutator.getMutatorId();
        JMLog.info(log, "MutatorProcessor", maxBufferCapacity, mutator);
    }

    /**
     * Process list.
     *
     * @param mutator  the mutator
     * @param dataList the data list
     * @return the list
     */
    private static List<Transfer<FieldMap>> process(MutatorInterface mutator,
            List<Transfer<String>> dataList) {
        JMLog.info(log, "process", dataList.size() > 0 ? dataList.get(0)
                        .getInputId() : JMString.EMPTY, mutator.getMutatorId(),
                dataList.size());
        return dataList.stream().map(mutator).filter(fieldMapTransfer -> Objects
                .nonNull(fieldMapTransfer.getData()))
                .collect(Collectors.toList());
    }

    @Override
    public MutatorProcessor subscribeWith(
            Flow.Subscriber<List<Transfer<FieldMap>>>... subscribers) {
        super.subscribeWith(subscribers);
        return this;
    }

    @Override
    public MutatorProcessor consumeWith(
            Consumer<List<Transfer<FieldMap>>>... consumers) {
        super.consumeWith(consumers);
        return this;
    }

}
