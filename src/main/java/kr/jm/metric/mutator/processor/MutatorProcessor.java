package kr.jm.metric.mutator.processor;

import kr.jm.metric.data.Transfer;
import kr.jm.metric.mutator.MutatorInterface;
import kr.jm.utils.flow.processor.JMConcurrentProcessor;
import kr.jm.utils.flow.processor.JMProcessorBuilder;
import kr.jm.utils.flow.processor.JMProcessorInterface;
import kr.jm.utils.helper.JMLog;
import kr.jm.utils.helper.JMString;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Flow;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Slf4j
@ToString
public class MutatorProcessor implements
        JMProcessorInterface<List<Transfer<String>>, List<Transfer<Map<String, Object>>>>,
        AutoCloseable {

    @Getter
    private String mutatorId;
    private int workers;
    private MatchFilter matchFilter;
    private JMConcurrentProcessor<List<Transfer<String>>, List<Transfer<Map<String, Object>>>>
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


    private List<Transfer<Map<String, Object>>> process(
            List<Transfer<String>> dataList, MutatorInterface mutator) {
        JMLog.info(log, "process", dataList.size() > 0 ? dataList.get(0)
                        .getInputId() : JMString.EMPTY, mutator.getMutatorId(),
                dataList.size());
        return dataList.stream().map(mutator).filter(fieldMapTransfer -> Objects
                .nonNull(fieldMapTransfer.getData())).filter(this::isPassed)
                .collect(Collectors.toList());
    }

    private boolean isPassed(Transfer<Map<String, Object>> fieldMapTransfer) {
        return Objects.isNull(matchFilter) || !matchFilter
                .filter(fieldMapTransfer.getData());
    }


    @Override
    public MutatorProcessor subscribeWith(
            Flow.Subscriber<List<Transfer<Map<String, Object>>>>... subscribers) {
        this.jmProcessor.subscribeWith(subscribers);
        return this;
    }

    @Override
    public MutatorProcessor consumeWith(
            Consumer<List<Transfer<Map<String, Object>>>>... consumers) {
        this.jmProcessor.consumeWith(consumers);
        return this;
    }

    @Override
    public void subscribe(
            Flow.Subscriber<? super List<Transfer<Map<String, Object>>>> subscriber) {
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


    @Override
    public void close() {
        this.jmProcessor.close();
    }
}
