package kr.jm.metric.input.publisher;

import kr.jm.metric.config.input.FileInputConfig;
import kr.jm.metric.config.input.InputConfigInterface;
import kr.jm.metric.config.input.StdinLineInputConfig;
import kr.jm.metric.data.Transfer;
import kr.jm.metric.input.InputInterface;
import kr.jm.utils.flow.publisher.BulkSubmissionPublisher;
import kr.jm.utils.helper.JMResources;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class InputPublisherBuilder {

    public static InputPublisher buildFileInput(String filePath) {
        return build(new FileInputConfig(filePath));
    }

    public static InputPublisher buildFileInput(String inputId,
            String filePath) {
        return build(new FileInputConfig(filePath));
    }

    public static <T> InputPublisher build(String inputId, List<T> dataList) {
        return build(inputId, dataList,
                data -> new Transfer(inputId, data));
    }

    public static InputPublisher buildStdinLineInput(String inputId) {
        return build(new StdinLineInputConfig(inputId));
    }

    public static InputPublisher build(InputConfigInterface inputConfig) {
        return new InputPublisher(
                new StringTransferBulkSubmissionPublisher(
                        new TransferSubmissionPublisher<>(1,
                                inputConfig.getMaxBufferCapacity(),
                                inputConfig.getWaitingMillis()),
                        inputConfig.getBulkSize(),
                        inputConfig.getFlushIntervalMillis()),
                inputConfig.buildInput(), inputConfig.getChunkType());
    }


    public static InputPublisher buildResourceInput(String inputId,
            String resourceName) {
        return InputPublisherBuilder
                .build(inputId, JMResources.readLines(resourceName));
    }

    public static InputPublisher buildResourceInput(String resourceName) {
        return buildResourceInput(resourceName, resourceName);
    }

    public static InputPublisher buildTestInput(String inputId) {
        return build(new InputInterface() {
            @Override
            public void close() {
            }

            @Override
            public String getInputId() {
                return inputId;
            }

            @Override
            public void start(
                    Consumer<Transfer<String>> inputConsumer) {
            }
        });
    }

    public static InputPublisher build(InputInterface input) {
        return new InputPublisher(new BulkSubmissionPublisher<>(), input);
    }

    public static <T> InputPublisher build(String inputId, List<T>
            dataList, Function<T, Transfer<String>> transformFunction) {
        return build(new InputInterface() {
            @Override
            public String getInputId() {
                return inputId;
            }

            @Override
            public void start(Consumer<Transfer<String>> inputConsumer) {
                dataList.stream().map(transformFunction)
                        .forEach(inputConsumer);
            }

            @Override
            public void close() {

            }
        });
    }
}
