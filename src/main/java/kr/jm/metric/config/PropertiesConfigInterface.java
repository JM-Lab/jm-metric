package kr.jm.metric.config;

import java.util.Map;

/**
 * The interface Properties config interface.
 */
public interface PropertiesConfigInterface extends ConfigInterface {

    /**
     * Gets properties.
     *
     * @return the properties
     */
    Map<String, Object> getProperties();

}
