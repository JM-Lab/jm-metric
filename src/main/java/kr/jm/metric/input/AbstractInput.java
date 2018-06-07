package kr.jm.metric.input;

import org.slf4j.Logger;

import java.util.List;
import java.util.function.Consumer;


public abstract class AbstractInput implements
        InputInterface {

    protected Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

    @Override
    public void start(Consumer<List<String>> inputConsumer) {
        startImpl(inputConsumer);
        log.info("Input Start - {}", toString());
    }

    @Override
    public void close() {
        log.info("Start Input Closing - {}", toString());
        closeImpl();
        log.info("Finish Input Closing - {}", toString());
    }

    protected abstract void startImpl(Consumer<List<String>> inputConsumer);

    protected abstract void closeImpl();


}
