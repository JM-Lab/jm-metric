package kr.jm.metric.input.publisher;

import kr.jm.metric.config.input.ChunkType;
import kr.jm.metric.data.Transfer;
import kr.jm.metric.input.InputInterface;
import kr.jm.utils.JMOptional;
import kr.jm.utils.JMString;
import kr.jm.utils.flow.publisher.BulkSubmissionPublisher;
import kr.jm.utils.exception.JMException;
import kr.jm.utils.helper.JMJson;
import kr.jm.utils.helper.JMLog;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Flow;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

@Slf4j
public class InputPublisher implements TransferSubmissionPublisherInterface<String>, AutoCloseable {

    private final BulkSubmissionPublisher<Transfer<String>> transferBulkSubmissionPublisher;

    @Getter
    protected String inputId;
    protected final InputInterface input;

    private final Consumer<Transfer<String>> chunkConsumer;

    public InputPublisher(BulkSubmissionPublisher<Transfer<String>> transferBulkSubmissionPublisher,
            InputInterface input) {
        this(transferBulkSubmissionPublisher, input, ChunkType.NONE);
    }

    public InputPublisher(BulkSubmissionPublisher<Transfer<String>> transferBulkSubmissionPublisher,
            InputInterface input, ChunkType chunkType) {
        this.transferBulkSubmissionPublisher = transferBulkSubmissionPublisher;
        this.inputId = input.getInputId();
        this.input = input;
        this.chunkConsumer = buildChunkConsumer(Optional.ofNullable(chunkType).orElse(ChunkType.NONE));
        JMLog.info(log, "InputPublisher", inputId, input, chunkType);
    }

    private Consumer<Transfer<String>> buildChunkConsumer(ChunkType chunkType) {
        switch (chunkType) {
            case LINES:
                return buildTransferConsumer(data -> Arrays.stream(data.split(JMString.LINE_SEPARATOR)));
            case JSON_LIST:
                return buildTransferConsumer(
                        data -> JMJson.getInstance().toList(data).stream().map(JMJson.getInstance()::toJsonString));
            default:
                return this.transferBulkSubmissionPublisher::submitSingle;
        }
    }

    private Consumer<Transfer<String>> buildTransferConsumer(Function<String, Stream<String>> stringStreamFunction) {
        return transfer -> this.transferBulkSubmissionPublisher
                .submit(JMOptional.getOptional(transfer.getData()).stream().flatMap(stringStreamFunction)
                        .map(transfer::newWith).toArray(Transfer[]::new));
    }

    public InputPublisher start() {
        JMLog.info(log, "start", inputId);
        input.start(this.chunkConsumer);
        return this;
    }

    @Override
    public void close() {
        JMLog.info(log, "close", inputId);
        try {
            input.close();
            this.transferBulkSubmissionPublisher.close();
        } catch (Exception e) {
            JMException.handleException(log, e, "close");
        }
    }

    @SafeVarargs
    @Override
    public final InputPublisher subscribeWith(Flow.Subscriber<List<Transfer<String>>>... subscribers) {
        this.transferBulkSubmissionPublisher.subscribeWith(subscribers);
        return this;
    }

    @SafeVarargs
    @Override
    public final InputPublisher consumeWith(Consumer<List<Transfer<String>>>... consumers) {
        this.transferBulkSubmissionPublisher.consumeWith(consumers);
        return this;
    }

    public void testInput(String data) {
        submit(this.inputId, List.of(data));
    }

    public void testInput(List<String> dataList) {
        submit(this.inputId, dataList);
        this.transferBulkSubmissionPublisher.flush();
    }

    @Override
    public void subscribe(Flow.Subscriber<? super List<Transfer<String>>> subscriber) {
        this.transferBulkSubmissionPublisher.subscribe(subscriber);
    }

    @Override
    public int submit(List<Transfer<String>> transferList) {
        transferList.forEach(this.chunkConsumer);
        return transferList.size();
    }
}