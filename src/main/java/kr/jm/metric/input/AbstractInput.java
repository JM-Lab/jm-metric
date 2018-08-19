package kr.jm.metric.input;

import kr.jm.metric.config.input.InputConfigInterface;
import kr.jm.metric.data.Transfer;
import lombok.Getter;
import org.slf4j.Logger;

import java.util.Map;
import java.util.function.Consumer;


/**
 * The type Abstract input.
 *
 * @param <C> the type parameter
 */
public abstract class AbstractInput<C extends InputConfigInterface> implements
        InputInterface {

    /**
     * The Log.
     */
    protected Logger log = org.slf4j.LoggerFactory.getLogger(getClass());
    /**
     * The Input id.
     */
    @Getter
    protected String inputId;
    /**
     * The Input config.
     */
    @Getter
    protected C inputConfig;

    /**
     * Instantiates a new Abstract input.
     *
     * @param inputConfig the input config
     */
    public AbstractInput(C inputConfig) {
        this.inputConfig = inputConfig;
        this.inputId = inputConfig.getInputId();
    }

    @Override
    public void start(Consumer<Transfer<String>> inputConsumer) {
        startImpl(inputConsumer);
        log.info("Input Start - {}", toString());
    }

    /**
     * Gets config.
     *
     * @return the config
     */
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

    /**
     * Start.
     *
     * @param inputConsumer the input consumer
     */
    protected abstract void startImpl(Consumer<Transfer<String>> inputConsumer);

    /**
     * Close.
     */
    protected abstract void closeImpl();


}
