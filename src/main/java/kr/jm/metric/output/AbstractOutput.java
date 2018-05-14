package kr.jm.metric.output;

import kr.jm.metric.output.config.OutputConfigInterface;
import lombok.Getter;

/**
 * The type Abstract output.
 *
 * @param <C> the type parameter
 * @param <T> the type parameter
 */
public abstract class AbstractOutput<C extends OutputConfigInterface, T> implements
        OutputInterface<T> {

    /**
     * The Output config.
     */
    @Getter
    protected C outputConfig;

    /**
     * Instantiates a new Abstract output.
     *
     * @param outputConfig the output config
     */
    public AbstractOutput(C outputConfig) {
        this.outputConfig = outputConfig;
    }

    /**
     * Gets config id.
     *
     * @return the config id
     */
    public String getConfigId() {return outputConfig.getConfigId();}
}
