package kr.jm.metric.output;

import kr.jm.metric.output.config.OutputConfigInterface;
import lombok.Getter;
import org.slf4j.Logger;

/**
 * The type Abstract output.
 *
 * @param <C> the type parameter
 * @param <T> the type parameter
 */
public abstract class AbstractOutput<C extends OutputConfigInterface, T> implements
        OutputInterface<T> {

    protected Logger log = org.slf4j.LoggerFactory.getLogger(getClass());
    /**
     * The Output properties.
     */
    @Getter
    protected C outputConfig;

    /**
     * Instantiates a new Abstract output.
     *
     * @param outputConfig the output properties
     */
    public AbstractOutput(C outputConfig) {
        this.outputConfig = outputConfig;
    }

    /**
     * Gets properties id.
     *
     * @return the properties id
     */
    public String getConfigId() {return outputConfig.getConfigId();}

    @Override
    public void close() {
        log.info("Start Closing - {}", toString());
        closeImpl();
        log.info("Finish Closing - {}", toString());
    }

    protected abstract void closeImpl();
}
