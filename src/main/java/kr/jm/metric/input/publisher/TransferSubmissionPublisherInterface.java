package kr.jm.metric.input.publisher;

import kr.jm.metric.data.Transfer;
import kr.jm.utils.flow.publisher.JMPublisherInterface;
import kr.jm.utils.helper.JMOptional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The interface Transfer submission publisher interface.
 *
 * @param <T> the type parameter
 */
public interface TransferSubmissionPublisherInterface<T> extends
        JMPublisherInterface<List<Transfer<T>>> {

    default int submit(String inputId, List<T> dataList) {
        return submit(inputId, dataList, null);
    }

    default int submit(String inputId, List<T> dataList,
            Map<String, Object> meta) {
        return submit(inputId, dataList, System.currentTimeMillis(), meta);
    }

    default int submit(String inputId, List<T> dataList, long timestamp) {
        return submit(inputId, dataList, timestamp, null);
    }

    default int submit(String inputId, List<T> dataList, long timestamp,
            Map<String, Object> meta) {
        return submit(
                JMOptional.getOptional(dataList).stream().flatMap(List::stream)
                        .map(data -> new Transfer<>(inputId, data, timestamp,
                                meta)).collect(Collectors.toList()));
    }

    int submit(List<Transfer<T>> transferList);

}
