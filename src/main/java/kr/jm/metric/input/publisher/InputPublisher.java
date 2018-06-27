package kr.jm.metric.input.publisher;

import kr.jm.metric.config.input.InputConfigInterface;
import kr.jm.metric.data.Transfer;
import kr.jm.metric.input.InputInterface;
import kr.jm.utils.exception.JMExceptionManager;
import kr.jm.utils.flow.publisher.WaitingSubmissionPublisher;
import kr.jm.utils.helper.JMLog;
import lombok.Getter;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Flow;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static kr.jm.utils.flow.publisher.WaitingSubmissionPublisher.getDefaultQueueSizeLimit;

public class InputPublisher extends
        StringTransferWaitingBulkSubmissionPublisher implements AutoCloseable {

    @Getter
    protected String inputId;
    protected InputInterface input;

    public InputPublisher(InputConfigInterface inputConfig) {
        this(inputConfig.getInputId(), inputConfig.getBulkSize(),
                inputConfig.getFlushIntervalSeconds(),
                inputConfig.getWaitingMillis(), inputConfig.getQueueSizeLimit(),
                inputConfig.buildInput());
    }

    public InputPublisher(String inputId, Integer bulkSize,
            Integer flushIntervalSeconds, InputInterface input) {
        this(inputId, bulkSize, flushIntervalSeconds,
                null, null, input);
    }

    public InputPublisher(String inputId, Integer bulkSize,
            Integer flushIntervalSeconds, Long waitingMillis,
            Integer queueSizeLimit, InputInterface input) {
        super(new StringTransferWaitingSubmissionPublisher(
                        Optional.ofNullable(waitingMillis).orElseGet(
                                () -> Long.valueOf(getDefaultQueueSizeLimit())),
                        Optional.ofNullable(queueSizeLimit).orElseGet(
                                WaitingSubmissionPublisher::getDefaultQueueSizeLimit)),
                Optional.ofNullable(bulkSize).orElse(DEFAULT_BULK_SIZE),
                Optional.ofNullable(flushIntervalSeconds)
                        .orElse(DEFAULT_FLUSH_INTERVAL_SECONDS));
        this.inputId = inputId;
        this.input = input;
    }

    public void start() {
        JMLog.info(log, "start", inputId);
        input.start(this::submitSingle);
    }


    @Override
    public void close() {
        JMLog.info(log, "close", inputId);
        try {
            input.close();
        } catch (Exception e) {
            JMExceptionManager.logException(log, e, "close");
        }
    }

    @Override
    public InputPublisher subscribeWith(
            Flow.Subscriber<List<Transfer<String>>>... subscribers) {
        return (InputPublisher) super.subscribeWith(subscribers);
    }

    @Override
    public InputPublisher consumeWith(
            Consumer<List<Transfer<String>>>... consumers) {
        return (InputPublisher) super.consumeWith(consumers);
    }

    public void testInput(String data) {
        submitSingle(new Transfer<>(this.inputId, data));
    }

    public void testInput(Stream<String> dataStream) {
        dataStream.forEach(this::testInput);
    }

}
