package kr.jm.metric.config;

public interface ConfigTypeInterface {
    <C extends ConfigInterface> Class<C> getConfigClass();
}
