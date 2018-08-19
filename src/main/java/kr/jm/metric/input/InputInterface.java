package kr.jm.metric.input;

import kr.jm.metric.data.Transfer;

import java.util.Map;
import java.util.function.Consumer;

/**
 * The interface Input interface.
 */
public interface InputInterface extends AutoCloseable {

    /**
     * Gets input id.
     *
     * @return the input id
     */
    String getInputId();

    /**
     * Start.
     *
     * @param inputConsumer the input consumer
     */
    void start(Consumer<Transfer<String>> inputConsumer);

    /**
     * New transfer transfer.
     *
     * @param data      the data
     * @param timestamp the timestamp
     * @param meta      the meta
     * @return the transfer
     */
    default Transfer<String> newTransfer(String data, long timestamp,
            Map<String, Object> meta) {
        return new Transfer<>(getInputId(), data, timestamp, meta);
    }

    /**
     * New transfer transfer.
     *
     * @param data the data
     * @return the transfer
     */
    default Transfer<String> newTransfer(String data) {
        return newTransfer(data, null);
    }

    /**
     * New transfer transfer.
     *
     * @param data the data
     * @param meta the meta
     * @return the transfer
     */
    default Transfer<String> newTransfer(String data,
            Map<String, Object> meta) {
        return newTransfer(data, System.currentTimeMillis(), meta);
    }

    /**
     * New transfer transfer.
     *
     * @param data      the data
     * @param timestamp the timestamp
     * @return the transfer
     */
    default Transfer<String> newTransfer(String data, long timestamp) {
        return newTransfer(data, timestamp, null);
    }
}
