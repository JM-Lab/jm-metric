package kr.jm.metric.output;

import java.util.Map;

/**
 * The interface Output config interface.
 */
public interface OutputConfigInterface {
    /**
     * Gets config with all.
     *
     * @return the config with all
     */
    Map<String, Object> getConfigWithAll();

    /**
     * Gets config.
     *
     * @return the config
     */
    Map<String, Object> getConfig();

    /**
     * Gets config id.
     *
     * @return the config id
     */
    String getConfigId();
}
