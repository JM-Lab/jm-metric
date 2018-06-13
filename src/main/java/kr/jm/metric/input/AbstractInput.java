package kr.jm.metric.input;

import kr.jm.metric.config.PropertiesConfigInterface;
import kr.jm.metric.config.input.InputConfigInterface;
import org.slf4j.Logger;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;


public abstract class AbstractInput implements
        InputInterface {

    protected Logger log = org.slf4j.LoggerFactory.getLogger(getClass());
    protected InputConfigInterface inputConfig;

    public AbstractInput(InputConfigInterface inputConfig) {
        this.inputConfig = inputConfig;
    }

    @Override
    public void start(Consumer<List<String>> inputConsumer) {
        startImpl(inputConsumer);
        log.info("Input Start - {}", toString());
    }

    @Override
    public String getInputId() {
        return inputConfig.getInputId();
    }

    public Map<String, Object> getConfig() {
        return Optional.ofNullable(inputConfig)
                .map(PropertiesConfigInterface::extractConfigMap)
                .orElseGet(Collections::emptyMap);
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
