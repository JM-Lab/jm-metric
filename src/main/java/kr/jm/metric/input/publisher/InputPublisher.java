package kr.jm.metric.input.publisher;

import kr.jm.metric.config.input.ChunkType;
import kr.jm.metric.data.Transfer;
import kr.jm.metric.input.InputInterface;
import kr.jm.utils.exception.JMExceptionManager;
import kr.jm.utils.flow.publisher.BulkSubmissionPublisher;
import kr.jm.utils.helper.JMJson;
import kr.jm.utils.helper.JMLog;
import kr.jm.utils.helper.JMOptional;
import kr.jm.utils.helper.JMString;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Flow;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * The type Input publisher.
 */
@Slf4j
public class InputPublisher implements
        TransferSubmissionPublisherInterface<String>, AutoCloseable {

    private BulkSubmissionPublisher<Transfer<String>>
            transferBulkSubmissionPublisher;

    /**
     * The Input id.
     */
    @Getter
    protected String inputId;
    /**
     * The Input.
     */
    protected InputInterface input;

    private Consumer<Transfer<String>> chunkConsumer;

    public InputPublisher(
            BulkSubmissionPublisher<Transfer<String>> transferBulkSubmissionPublisher,
            InputInterface input) {
        this(transferBulkSubmissionPublisher, input, ChunkType.NONE);
    }

    /**
     * Instantiates a new Input publisher.
     *
     * @param transferBulkSubmissionPublisher the submission publisher
     * @param input                           the input
     */
    public InputPublisher(
            BulkSubmissionPublisher<Transfer<String>> transferBulkSubmissionPublisher,
            InputInterface input, ChunkType chunkType) {
        this.transferBulkSubmissionPublisher = transferBulkSubmissionPublisher;
        this.inputId = input.getInputId();
        this.input = input;
        this.chunkConsumer = buildChunkConsumer(
                Optional.ofNullable(chunkType).orElse(ChunkType.NONE));
        JMLog.info(log, "InputPublisher", inputId, input, chunkType);
    }

    private Consumer<Transfer<String>> buildChunkConsumer(
            ChunkType chunkType) {
        switch (chunkType) {
            case LINES:
                return buildTransferConsumer(data -> Arrays
                        .stream(data.split(JMString.LINE_SEPARATOR)));
            case JSON_LIST:
                return buildTransferConsumer(
                        data -> JMJson.toList(data).stream()
                                .map(JMJson::toJsonString));
            default:
                return this.transferBulkSubmissionPublisher::submitSingle;
        }
    }

    private Consumer<Transfer<String>> buildTransferConsumer(
            Function<String, Stream<String>> stringStreamFunction) {
        return transfer -> this.transferBulkSubmissionPublisher
                .submit(JMOptional.getOptional(transfer.getData()).stream()
                        .flatMap(stringStreamFunction).map(transfer::newWith)
                        .toArray(Transfer[]::new));
    }

    /**
     * Start input publisher.
     *
     * @return the input publisher
     */
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
            JMExceptionManager.handleException(log, e, "close");
        }
    }

    @Override
    public InputPublisher subscribeWith(
            Flow.Subscriber<List<Transfer<String>>>... subscribers) {
        this.transferBulkSubmissionPublisher.subscribeWith(subscribers);
        return this;
    }

    @Override
    public InputPublisher consumeWith(
            Consumer<List<Transfer<String>>>... consumers) {
        this.transferBulkSubmissionPublisher.consumeWith(consumers);
        return this;
    }

    /**
     * Test input.
     *
     * @param data the data
     */
    public void testInput(String data) {
        submit(this.inputId, List.of(data));
    }

    /**
     * Test input.
     *
     * @param dataList the data list
     */
    public void testInput(List<String> dataList) {
        submit(this.inputId, dataList);
        this.transferBulkSubmissionPublisher.flush();
    }

    @Override
    public void subscribe(
            Flow.Subscriber<? super List<Transfer<String>>> subscriber) {
        this.transferBulkSubmissionPublisher.subscribe(subscriber);
    }

    @Override
    public int submit(List<Transfer<String>> transferList) {
        transferList.forEach(this.chunkConsumer);
        return transferList.size();
    }
}
