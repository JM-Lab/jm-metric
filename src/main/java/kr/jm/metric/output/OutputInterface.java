package kr.jm.metric.output;

import kr.jm.metric.data.ConfigIdTransfer;

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
    void writeData(ConfigIdTransfer<T> data);
}
