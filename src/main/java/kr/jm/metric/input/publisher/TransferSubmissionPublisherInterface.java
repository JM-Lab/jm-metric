package kr.jm.metric.input.publisher;

import kr.jm.metric.data.Transfer;
import kr.jm.utils.flow.publisher.JMSubmissionPublisherInterface;

import java.util.Map;
import java.util.Optional;

/**
 * The interface Transfer submission publisher interface.
 *
 * @param <T> the type parameter
 */
public interface TransferSubmissionPublisherInterface<T> extends
        JMSubmissionPublisherInterface<Transfer<T>> {

    /**
     * Submit int.
     *
     * @param inputId the data id
     * @param data   the data
     * @return the int
     */
    default int submit(String inputId, T data) {
        return submit(inputId, data, null);
    }

    /**
     * Submit int.
     *
     * @param inputId the data id
     * @param data   the data
     * @param meta   the meta
     * @return the int
     */
    default int submit(String inputId, T data, Map<String, Object> meta) {
        return submit(inputId, data, System.currentTimeMillis(), meta);
    }

    /**
     * Submit int.
     *
     * @param inputId    the data id
     * @param data      the data
     * @param timestamp the timestamp
     * @return the int
     */
    default int submit(String inputId, T data, long timestamp) {
        return submit(inputId, data, timestamp, null);
    }

    /**
     * Submit int.
     *
     * @param inputId    the data id
     * @param data      the data
     * @param timestamp the timestamp
     * @param meta      the meta
     * @return the int
     */
    default int submit(String inputId, T data, long timestamp,
            Map<String, Object> meta) {
        return Optional.ofNullable(data)
                .map(d -> new Transfer<>(inputId, data, timestamp, meta))
                .map(this::submit).orElse(0);
    }

}
