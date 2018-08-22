package kr.jm.metric.input.publisher;

import kr.jm.metric.config.input.FileInputConfig;
import kr.jm.metric.config.input.InputConfigInterface;
import kr.jm.metric.config.input.StdInLineInputConfig;
import kr.jm.metric.data.Transfer;
import kr.jm.metric.input.InputInterface;
import kr.jm.utils.flow.publisher.BulkSubmissionPublisher;
import kr.jm.utils.helper.JMResources;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * The type Input publisher builder.
 */
public class InputPublisherBuilder {

    /**
     * Build file input input publisher.
     *
     * @param filePath the file path
     * @return the input publisher
     */
    public static InputPublisher buildFileInput(String filePath) {
        return build(new FileInputConfig(filePath));
    }

    /**
     * Build file input input publisher.
     *
     * @param inputId  the input id
     * @param filePath the file path
     * @return the input publisher
     */
    public static InputPublisher buildFileInput(String inputId,
            String filePath) {
        return build(new FileInputConfig(filePath));
    }

    /**
     * Build input publisher.
     *
     * @param <T>      the type parameter
     * @param inputId  the input id
     * @param dataList the data list
     * @return the input publisher
     */
    public static <T> InputPublisher build(String inputId, List<T> dataList) {
        return build(inputId, dataList,
                data -> new Transfer(inputId, data));
    }

    /**
     * Build std input input publisher.
     *
     * @param inputId the input id
     * @return the input publisher
     */
    public static InputPublisher buildStdInput(String inputId) {
        return build(new StdInLineInputConfig(inputId));
    }

    /**
     * Build input publisher.
     *
     * @param inputConfig the input config
     * @return the input publisher
     */
    public static InputPublisher build(InputConfigInterface inputConfig) {
        return new InputPublisher(
                new StringTransferWaitingBulkSubmissionPublisher(
                        new StringTransferWaitingSubmissionPublisher(
                                inputConfig.getWaitingMillis(),
                                inputConfig.getQueueSizeLimit()),
                        inputConfig.getBulkSize(),
                        inputConfig.getFlushIntervalSeconds()),
                inputConfig.buildInput());
    }


    /**
     * Build resource input input publisher.
     *
     * @param inputId      the input id
     * @param resourceName the resource name
     * @return the input publisher
     */
    public static InputPublisher buildResourceInput(String inputId,
            String resourceName) {
        return InputPublisherBuilder
                .build(inputId, JMResources.readLines(resourceName));
    }

    /**
     * Build resource input input publisher.
     *
     * @param resourceName the resource name
     * @return the input publisher
     */
    public static InputPublisher buildResourceInput(String resourceName) {
        return buildResourceInput(resourceName, resourceName);
    }

    /**
     * Build test input input publisher.
     *
     * @param inputId the input id
     * @return the input publisher
     */
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

    /**
     * Build input publisher.
     *
     * @param input the input
     * @return the input publisher
     */
    public static InputPublisher build(InputInterface input) {
        return new InputPublisher(new BulkSubmissionPublisher<>(), input);
    }

    /**
     * Build input publisher.
     *
     * @param <T>               the type parameter
     * @param inputId           the input id
     * @param dataList          the data list
     * @param transformFunction the transform function
     * @return the input publisher
     */
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
