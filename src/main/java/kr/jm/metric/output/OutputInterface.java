package kr.jm.metric.output;

/**
 * The interface Output interface.
 *
 * @param <T> the type parameter
 */
public interface OutputInterface<T> extends AutoCloseable {
    /**
     * Write data.
     *
     * @param data the data
     */
    void writeData(T data);
}
