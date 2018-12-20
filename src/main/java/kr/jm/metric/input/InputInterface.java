package kr.jm.metric.input;

import kr.jm.metric.data.Transfer;

import java.util.Map;
import java.util.function.Consumer;

public interface InputInterface extends AutoCloseable {

    String getInputId();

    void start(Consumer<Transfer<String>> inputConsumer);

    default Transfer<String> newTransfer(String data, long timestamp,
            Map<String, Object> meta) {
        return new Transfer<>(getInputId(), data, timestamp, meta);
    }

    default Transfer<String> newTransfer(String data) {
        return newTransfer(data, null);
    }

    default Transfer<String> newTransfer(String data,
            Map<String, Object> meta) {
        return newTransfer(data, System.currentTimeMillis(), meta);
    }

    default Transfer<String> newTransfer(String data, long timestamp) {
        return newTransfer(data, timestamp, null);
    }
}
