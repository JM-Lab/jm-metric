package kr.jm.metric.config.output;

import kr.jm.metric.config.PropertiesConfigInterface;
import kr.jm.metric.output.OutputInterface;

/**
 * The interface Output config interface.
 */
public interface OutputConfigInterface extends PropertiesConfigInterface {

    /**
     * Gets output id.
     *
     * @return the output id
     */
    String getOutputId();

    /**
     * Gets output config type.
     *
     * @return the output config type
     */
    OutputConfigType getOutputConfigType();

    /**
     * Build output o.
     *
     * @param <O> the type parameter
     * @return the o
     */
    <O extends OutputInterface> O buildOutput();
}
