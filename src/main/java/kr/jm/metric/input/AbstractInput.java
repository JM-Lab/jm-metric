package kr.jm.metric.input;

import kr.jm.metric.config.PropertiesConfigInterface;
import kr.jm.metric.config.input.InputConfigInterface;
import kr.jm.metric.data.Transfer;
import lombok.Getter;
import org.slf4j.Logger;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;


public abstract class AbstractInput<C extends InputConfigInterface> implements
        InputInterface {

    protected Logger log = org.slf4j.LoggerFactory.getLogger(getClass());
    @Getter
    protected C inputConfig;
    @Getter
    protected String inputId;

    public AbstractInput(C inputConfig) {
        this.inputConfig = inputConfig;
        this.inputId = inputConfig.getInputId();
    }

    @Override
    public void start(Consumer<Transfer<String>> inputConsumer) {

        startImpl(inputConsumer);
        log.info("Input Start - {}", toString());
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

    protected abstract void startImpl(Consumer<Transfer<String>> inputConsumer);

    protected abstract void closeImpl();


}
