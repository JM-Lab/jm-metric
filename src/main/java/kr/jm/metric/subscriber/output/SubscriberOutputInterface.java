package kr.jm.metric.subscriber.output;

/**
 * The interface Subscriber output interface.
 *
 * @param <T> the type parameter
 */
public interface SubscriberOutputInterface<T> extends AutoCloseable {
    /**
     * Write data.
     *
     * @param data the data
     */
    void writeData(T data);
}
