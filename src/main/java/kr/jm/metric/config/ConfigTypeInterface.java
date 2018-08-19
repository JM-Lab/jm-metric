package kr.jm.metric.config;

/**
 * The interface Config type interface.
 */
public interface ConfigTypeInterface {
    /**
     * Gets config class.
     *
     * @param <C> the type parameter
     * @return the config class
     */
    <C extends ConfigInterface> Class<C> getConfigClass();
}
