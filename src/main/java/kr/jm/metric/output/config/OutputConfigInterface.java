package kr.jm.metric.output.config;

import kr.jm.metric.output.OutputInterface;
import kr.jm.metric.output.config.type.OutputConfigType;

import java.util.Map;

/**
 * The interface Output properties interface.
 */
public interface OutputConfigInterface {
    /**
     * Gets properties with all.
     *
     * @return the properties with all
     */
    Map<String, Object> extractConfigMap();

    /**
     * Gets properties.
     *
     * @return the properties
     */
    Map<String, Object> getProperties();

    /**
     * Gets properties id.
     *
     * @return the properties id
     */
    String getConfigId();

    OutputConfigType getOutputConfigType();

    <O extends OutputInterface> O buildOutput();
}
