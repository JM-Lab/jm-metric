package kr.jm.metric.input.publisher;

import kr.jm.metric.config.input.InputConfigInterface;
import kr.jm.metric.input.AbstractInput;
import kr.jm.metric.input.FileInput;
import kr.jm.metric.input.InputInterface;
import kr.jm.metric.input.StdLineInput;

import java.util.List;
import java.util.function.Consumer;

/**
 * The type Output subscriber builder.
 */
public class InputPublisherBuilder {

    public static InputPublisher buildFile(String filePath) {
        return build(filePath, new FileInput(filePath));
    }

    public static InputPublisher buildFile(String dataId, String filePath) {
        return build(dataId, new FileInput(filePath));
    }

    public static InputPublisher buildStdInput() {
        return buildStdInput("StdIn");
    }

    public static InputPublisher buildStdInput(String dataId) {
        return build(dataId, new StdLineInput());
    }

    public static InputPublisher build(String dataId, List<String> data) {
        return build(dataId, new AbstractInput() {
            @Override
            protected void startImpl(Consumer<List<String>> inputConsumer) {
                inputConsumer.accept(data);
            }

            @Override
            protected void closeImpl() {

            }
        });
    }


    public static InputPublisher build(String dataId, InputInterface input) {
        return build(dataId, null, input);
    }

    public static InputPublisher build(String dataId, Integer bulkSize,
            InputInterface input) {
        return build(dataId, bulkSize, null, input);
    }

    public static InputPublisher build(String dataId, Integer bulkSize,
            Integer flushIntervalSeconds, InputInterface input) {
        return new InputPublisher(dataId, bulkSize, flushIntervalSeconds,
                input);
    }

    public static InputPublisher build(InputConfigInterface inputConfig,
            InputInterface input) {
        return new InputPublisher(inputConfig, input);
    }


}
