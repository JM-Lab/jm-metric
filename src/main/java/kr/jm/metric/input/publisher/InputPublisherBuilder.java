package kr.jm.metric.input.publisher;

import kr.jm.metric.config.input.InputConfigInterface;
import kr.jm.metric.data.Transfer;
import kr.jm.metric.input.FileInput;
import kr.jm.metric.input.InputInterface;
import kr.jm.metric.input.StdLineInput;
import kr.jm.utils.helper.JMResources;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * The type Output subscriber builder.
 */
public class InputPublisherBuilder {

    public static InputPublisher buildFileInput(String filePath) {
        return build(new FileInput(filePath));
    }

    public static InputPublisher buildFileInput(String inputId,
            String filePath) {
        return build(new FileInput(filePath));
    }

    public static <T> InputPublisher buildInput(String inputId, List<T>
            dataList, Function<T, Transfer<String>> transformFunction) {
        return build(new InputInterface() {
            @Override
            public String getInputId() {
                return inputId;
            }

            @Override
            public void start(Consumer<Transfer<String>> inputConsumer) {
                dataList.stream().map(transformFunction)
                        .forEach(inputConsumer::accept);
            }

            @Override
            public void close() {

            }
        });
    }

    public static <T> InputPublisher buildInput(String inputId,
            List<T> dataList) {
        return buildInput(inputId, dataList,
                data -> new Transfer(inputId, data));
    }

    public static InputPublisher buildStdInput(String inputId) {
        return build(new StdLineInput(inputId));
    }

    public static InputPublisher build(InputInterface input) {
        return build(input.getInputId(), null, null, input);
    }

    public static InputPublisher build(String inputId, Integer bulkSize,
            Integer flushIntervalSeconds, InputInterface input) {
        return new InputPublisher(inputId, bulkSize, flushIntervalSeconds,
                input);
    }

    public static InputPublisher build(InputConfigInterface inputConfig) {
        return new InputPublisher(inputConfig);
    }


    public static InputPublisher buildResourceInput(String inputId,
            String resourceName) {
        return InputPublisherBuilder
                .buildInput(inputId, JMResources.readLines(resourceName));
    }

    public static InputPublisher buildResourceInput(String resourceName) {
        return buildResourceInput(resourceName, resourceName);
    }
}
