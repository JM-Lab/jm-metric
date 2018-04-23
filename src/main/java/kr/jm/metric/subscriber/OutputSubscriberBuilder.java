package kr.jm.metric.subscriber;

import kr.jm.metric.subscriber.output.AbstractStringSubscriberOutput;
import kr.jm.metric.subscriber.output.FileSubscriberOutput;
import kr.jm.metric.subscriber.output.StdSubscriberOutput;
import kr.jm.metric.subscriber.output.SubscriberOutputInterface;

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
        return build(new FileSubscriberOutput(filePath));
    }

    /**
     * Build file to json string output subscriber.
     *
     * @param filePath the file path
     * @return the output subscriber
     */
    public static OutputSubscriber buildFileToJsonString(String filePath) {
        return build(new FileSubscriberOutput(filePath, true));
    }

    /**
     * Build std out to json string output subscriber.
     *
     * @return the output subscriber
     */
    public static OutputSubscriber buildStdOutToJsonString() {
        return build(new StdSubscriberOutput(true));
    }

    /**
     * Build std out abstract string subscriber output.
     *
     * @return the abstract string subscriber output
     */
    public static AbstractStringSubscriberOutput buildStdOut() {
        return new StdSubscriberOutput();
    }

    /**
     * Build output subscriber.
     *
     * @param <T>             the type parameter
     * @param writingConsumer the writing consumer
     * @return the output subscriber
     */
    public static <T> OutputSubscriber<T> build(
            Consumer<T> writingConsumer) {
        return build(new SubscriberOutputInterface<T>() {
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
     * @param <T>              the type parameter
     * @param subscriberOutput the subscriber output
     * @return the output subscriber
     */
    public static <T> OutputSubscriber<T> build(
            SubscriberOutputInterface<T> subscriberOutput) {
        return new OutputSubscriber<>(subscriberOutput);
    }
}
