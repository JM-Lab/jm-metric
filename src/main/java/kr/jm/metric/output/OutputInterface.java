package kr.jm.metric.output;

import kr.jm.metric.data.ConfigIdTransfer;
import kr.jm.metric.output.config.OutputConfigInterface;

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

    <C extends OutputConfigInterface> C getOutputConfig();
}
