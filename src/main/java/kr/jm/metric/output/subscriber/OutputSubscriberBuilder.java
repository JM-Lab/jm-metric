package kr.jm.metric.output.subscriber;

import kr.jm.metric.output.AbstractStringOutput;
import kr.jm.metric.output.FileOutput;
import kr.jm.metric.output.OutputInterface;
import kr.jm.metric.output.StdOutput;

import java.util.function.Consumer;

/**
 * The type Output subscriber builder.
 */
public class OutputSubscriberBuilder {

    /**
     * Build file output subscriber.
     *
     * @param filePath the file path
     * @return the output subscriber
     */
    public static OutputSubscriber buildFile(String filePath) {
        return build(new FileOutput(filePath));
    }

    /**
     * Build file to json string output subscriber.
     *
     * @param filePath the file path
     * @return the output subscriber
     */
    public static OutputSubscriber buildFileToJsonString(String filePath) {
        return build(new FileOutput(filePath, true));
    }

    /**
     * Build std out to json string output subscriber.
     *
     * @return the output subscriber
     */
    public static OutputSubscriber buildStdOutToJsonString() {
        return build(new StdOutput(true));
    }

    /**
     * Build std out abstract string output.
     *
     * @return the abstract string output
     */
    public static AbstractStringOutput buildStdOut() {
        return new StdOutput();
    }

    /**
     * Build output subscriber.
     *
     * @param <T>             the type parameter
     * @param writingConsumer the writing consumer
     * @return the output subscriber
     */
    public static <T> OutputSubscriber<T> build(Consumer<T> writingConsumer) {
        return build(new OutputInterface<>() {
            @Override
            public void writeData(T data) {
                writingConsumer.accept(data);
            }

            @Override
            public void close() {
            }
        });
    }

    /**
     * Build output subscriber.
     *
     * @param <T>             the type parameter
     * @param outputInterface the output interface
     * @return the output subscriber
     */
    public static <T> OutputSubscriber<T> build(
            OutputInterface<T> outputInterface) {
        return new OutputSubscriber<>(outputInterface);
    }
}
