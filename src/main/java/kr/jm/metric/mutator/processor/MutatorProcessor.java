package kr.jm.metric.mutator.processor;

import kr.jm.metric.data.FieldMap;
import kr.jm.metric.data.Transfer;
import kr.jm.metric.mutator.MutatorInterface;
import kr.jm.utils.flow.processor.JMConcurrentTransformProcessor;
import kr.jm.utils.flow.processor.JMTransformProcessorInterface;
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
public class MutatorProcessor implements
        JMTransformProcessorInterface<List<Transfer<String>>,
                List<Transfer<FieldMap>>>, AutoCloseable {

    private JMConcurrentTransformProcessor<List<Transfer<String>>,
            List<Transfer<FieldMap>>> jmConcurrentTransformProcessor;

    @Getter
    private String mutatorId;
    private MatchFilter matchFilter;

    public MutatorProcessor(MutatorInterface mutator) {
        this(0, mutator, null);
    }

    public MutatorProcessor(int workers, MutatorInterface mutator) {
        this(workers, mutator, null);
    }

    public MutatorProcessor(int workers, MutatorInterface mutator,
            MatchFilter matchFilter) {
        this(workers, Flow.defaultBufferSize(), mutator, matchFilter);
    }

    private MutatorProcessor(int workers, int maxBufferCapacity,
            MutatorInterface mutator, MatchFilter matchFilter) {
        this.jmConcurrentTransformProcessor =
                new JMConcurrentTransformProcessor<>(
                        Optional.ofNullable(workers)
                                .filter(JMPredicate.getGreater(0))
                                .map(JMThread::newThreadPool).orElseGet(
                                JMThread::newThreadPoolWithAvailableProcessors),
                        maxBufferCapacity, list -> process(list, mutator));
        this.mutatorId = mutator.getMutatorId();
        this.matchFilter = matchFilter;
        JMLog.info(log, "MutatorProcessor", maxBufferCapacity, mutator);
    }


    /**
     * Process list.
     *
     * @param dataList the data list
     * @param mutator  the mutator
     * @return the list
     */
    private List<Transfer<FieldMap>> process(
            List<Transfer<String>> dataList, MutatorInterface mutator) {
        JMLog.info(log, "process", dataList.size() > 0 ? dataList.get(0)
                        .getInputId() : JMString.EMPTY, mutator.getMutatorId(),
                dataList.size());
        return dataList.stream().map(mutator).filter(fieldMapTransfer -> Objects
                .nonNull(fieldMapTransfer.getData())).filter(this::isPassed)
                .collect(Collectors.toList());
    }

    private boolean isPassed(Transfer<FieldMap> fieldMapTransfer) {
        return Objects.isNull(matchFilter) || !matchFilter
                .filter(fieldMapTransfer.getData());
    }


    @Override
    public MutatorProcessor subscribeWith(
            Flow.Subscriber<List<Transfer<FieldMap>>>... subscribers) {
        jmConcurrentTransformProcessor.subscribeWith(subscribers);
        return this;
    }

    @Override
    public MutatorProcessor consumeWith(
            Consumer<List<Transfer<FieldMap>>>... consumers) {
        jmConcurrentTransformProcessor.consumeWith(consumers);
        return this;
    }

    @Override
    public void subscribe(
            Flow.Subscriber<? super List<Transfer<FieldMap>>> subscriber) {
        jmConcurrentTransformProcessor.subscribe(subscriber);
    }

    @Override
    public void close() {jmConcurrentTransformProcessor.close();}

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        jmConcurrentTransformProcessor.onSubscribe(subscription);
    }

    @Override
    public void onNext(
            List<Transfer<String>> item) {
        jmConcurrentTransformProcessor.onNext(item);
    }

    @Override
    public void onError(Throwable throwable) {
        jmConcurrentTransformProcessor.onError(throwable);
    }

    @Override
    public void onComplete() {jmConcurrentTransformProcessor.onComplete();}


}
