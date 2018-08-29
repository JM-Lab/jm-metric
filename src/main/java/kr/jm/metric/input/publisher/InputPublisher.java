package kr.jm.metric.input.publisher;

import kr.jm.metric.config.input.ChunkType;
import kr.jm.metric.data.Transfer;
import kr.jm.metric.input.InputInterface;
import kr.jm.utils.exception.JMExceptionManager;
import kr.jm.utils.flow.publisher.BulkSubmissionPublisher;
import kr.jm.utils.flow.publisher.JMSubmissionPublisherInterface;
import kr.jm.utils.helper.JMJson;
import kr.jm.utils.helper.JMLog;
import kr.jm.utils.helper.JMString;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Flow;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The type Input publisher.
 */
@Slf4j
public class InputPublisher implements
        JMSubmissionPublisherInterface<List<Transfer<String>>>, AutoCloseable {

    private BulkSubmissionPublisher<Transfer<String>> submissionPublisher;

    /**
     * The Input id.
     */
    @Getter
    protected String inputId;
    /**
     * The Input.
     */
    protected InputInterface input;

    private Consumer<Transfer<String>> submitSingleConsumer;

    public InputPublisher(
            BulkSubmissionPublisher<Transfer<String>> submissionPublisher,
            InputInterface input) {
        this(submissionPublisher, input, ChunkType.NONE);
    }

    /**
     * Instantiates a new Input publisher.
     *
     * @param submissionPublisher the submission publisher
     * @param input               the input
     */
    public InputPublisher(
            BulkSubmissionPublisher<Transfer<String>> submissionPublisher,
            InputInterface input, ChunkType chunkType) {
        this.submissionPublisher = submissionPublisher;
        this.inputId = input.getInputId();
        this.input = input;
        this.submitSingleConsumer =
                initSubmitSingleConsumer(
                        Optional.ofNullable(chunkType).orElse(ChunkType.NONE));
        JMLog.info(log, "InputPublisher", inputId, input, chunkType);
    }

    private Consumer<Transfer<String>> initSubmitSingleConsumer(
            ChunkType chunkType) {
        switch (chunkType) {
            case LINES:
                return data -> Arrays
                        .asList(data.getData().split(JMString.LINE_SEPARATOR))
                        .stream().map(data::newWith)
                        .forEach(this.submissionPublisher::submitSingle);
            case JSON_LIST:
                return data -> JMJson.toList(data.getData()).stream()
                        .map(JMJson::toJsonString).map(data::newWith)
                        .forEach(this.submissionPublisher::submitSingle);
            default:
                return this.submissionPublisher::submitSingle;
        }
    }

    /**
     * Start input publisher.
     *
     * @return the input publisher
     */
    public InputPublisher start() {
        JMLog.info(log, "start", inputId);
        input.start(this.submitSingleConsumer);
        return this;
    }

    @Override
    public void close() {
        JMLog.info(log, "close", inputId);
        try {
            input.close();
            this.submissionPublisher.close();
        } catch (Exception e) {
            JMExceptionManager.logException(log, e, "close");
        }
    }

    @Override
    public InputPublisher subscribeWith(
            Flow.Subscriber<List<Transfer<String>>>... subscribers) {
        this.submissionPublisher.subscribeWith(subscribers);
        return this;
    }

    @Override
    public InputPublisher consumeWith(
            Consumer<List<Transfer<String>>>... consumers) {
        this.submissionPublisher.consumeWith(consumers);
        return this;
    }

    /**
     * Test input.
     *
     * @param data the data
     */
    public void testInput(String data) {
        testInput(Stream.of(data));
    }

    /**
     * Test input.
     *
     * @param dataList the data list
     */
    public void testInput(List<String> dataList) {
        testInput(dataList.stream());
    }

    /**
     * Test input.
     *
     * @param dataStream the data stream
     */
    public void testInput(Stream<String> dataStream) {
        submit(dataStream.map(data -> new Transfer<>(this.inputId, data))
                .collect(Collectors.toList()));
        this.submissionPublisher.flush();
    }

    @Override
    public int submit(List<Transfer<String>> item) {
        item.stream().forEach(submitSingleConsumer);
        return item.size();
    }

    @Override
    public void subscribe(
            Flow.Subscriber<? super List<Transfer<String>>> subscriber) {
        this.submissionPublisher.subscribe(subscriber);
    }
}
