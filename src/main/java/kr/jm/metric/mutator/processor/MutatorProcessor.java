package kr.jm.metric.mutator.processor;

import kr.jm.metric.data.FieldMap;
import kr.jm.metric.data.Transfer;
import kr.jm.metric.mutator.MutatorInterface;
import kr.jm.utils.flow.processor.JMProcessor;
import kr.jm.utils.flow.processor.JMProcessorBuilder;
import kr.jm.utils.flow.processor.JMProcessorInterface;
import kr.jm.utils.helper.JMLog;
import kr.jm.utils.helper.JMString;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Flow;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * The type Mutator processor.
 */
@Slf4j
@ToString
public class MutatorProcessor implements
        JMProcessorInterface<List<Transfer<String>>, List<Transfer<FieldMap>>> {

    @Getter
    private String mutatorId;
    private int workers;
    private MatchFilter matchFilter;
    private JMProcessor<List<Transfer<String>>, List<Transfer<FieldMap>>>
            jmProcessor;

    public MutatorProcessor(int workers, MutatorInterface mutator,
            MatchFilter matchFilter) {
        this.mutatorId = mutator.getMutatorId();
        this.workers = workers;
        this.jmProcessor = JMProcessorBuilder
                .buildWithThreadPool(workers, list -> process(list, mutator));
        this.matchFilter = matchFilter;
        JMLog.info(log, "MutatorProcessor", this.mutatorId, this.workers,
                mutator, matchFilter);
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
        this.jmProcessor.subscribeWith(subscribers);
        return this;
    }

    @Override
    public MutatorProcessor consumeWith(
            Consumer<List<Transfer<FieldMap>>>... consumers) {
        this.jmProcessor.consumeWith(consumers);
        return this;
    }

    @Override
    public void subscribe(
            Flow.Subscriber<? super List<Transfer<FieldMap>>> subscriber) {
        this.jmProcessor.subscribe(subscriber);
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.jmProcessor.onSubscribe(subscription);
    }

    @Override
    public void onNext(
            List<Transfer<String>> item) {
        this.jmProcessor.onNext(item);
    }

    @Override
    public void onError(Throwable throwable) {
        this.jmProcessor.onError(throwable);
    }

    @Override
    public void onComplete() {this.jmProcessor.onComplete();}


}
