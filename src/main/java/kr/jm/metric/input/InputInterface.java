package kr.jm.metric.input;

import java.util.List;
import java.util.function.Consumer;

public interface InputInterface extends AutoCloseable {

    void start(Consumer<List<String>> inputConsumer);
}
