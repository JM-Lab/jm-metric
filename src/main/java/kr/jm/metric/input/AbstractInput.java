package kr.jm.metric.input;

import kr.jm.metric.config.input.InputConfigInterface;
import kr.jm.metric.data.Transfer;
import lombok.Getter;
import org.slf4j.Logger;

import java.util.Map;
import java.util.function.Consumer;


public abstract class AbstractInput<C extends InputConfigInterface> implements
        InputInterface {

    protected final Logger log = org.slf4j.LoggerFactory.getLogger(getClass());
    @Getter
    protected String inputId;
    @Getter
    protected C inputConfig;

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
        return inputConfig.extractConfigMap();
    }

    @Override
    public void close() {
        log.info("Start Input Closing - {}", toString());
        closeImpl();
        log.info("Finish Input Closing - {}", toString());
    }

    @Override
    public String toString() {
        return "AbstractInput{" + "inputId='" + inputId + '\'' +
                ", inputConfig=" + inputConfig + '}';
    }

    protected abstract void startImpl(Consumer<Transfer<String>> inputConsumer);

    protected abstract void closeImpl();


}
