package kr.jm.metric.config;

import kr.jm.utils.exception.JMExceptionManager;
import kr.jm.utils.helper.JMJson;
import kr.jm.utils.helper.JMLog;
import kr.jm.utils.helper.JMOptional;
import kr.jm.utils.helper.JMResources;

import java.util.*;
import java.util.stream.Stream;

/**
 * The type Abstract config manager.
 *
 * @param <C> the type parameter
 */
public abstract class AbstractListConfigManager<C extends ConfigInterface>
        extends AbstractConfigManager {

    /**
     * The Config map.
     */
    protected Map<String, C> configMap;

    /**
     * Instantiates a new Abstract config manager.
     */
    public AbstractListConfigManager() {
        this.configMap = new HashMap<>();
    }

    /**
     * Instantiates a new Abstract config manager.
     *
     * @param configFilename the config filename
     */
    public AbstractListConfigManager(String configFilename) {
        this();
        loadConfig(configFilename);
    }

    /**
     * Instantiates a new Abstract config manager.
     *
     * @param configList the config list
     */
    public AbstractListConfigManager(List<C> configList) {
        this();
        loadConfig(configList.stream());
    }

    /**
     * Instantiates a new Abstract config manager.
     *
     * @param configs the configs
     */
    public AbstractListConfigManager(C... configs) {
        this();
        loadConfig(Arrays.stream(configs));
    }

    /**
     * Load config abstract config manager.
     *
     * @param configFilePath the config filename
     * @return the abstract config manager
     */
    public AbstractListConfigManager<C> loadConfig(String configFilePath) {
        return loadConfig(
                buildConfigStream(buildConfigMapList(configFilePath)));
    }

    private AbstractListConfigManager<C> loadConfig(Stream<C> configStream) {
        configStream.forEach(this::insertConfig);
        return this;
    }

    /**
     * Insert config list abstract config manager.
     *
     * @param configList the config list
     * @return the abstract config manager
     */
    public AbstractListConfigManager<C> insertConfigList(List<C> configList) {
        return loadConfig(configList.stream());
    }

    /**
     * Insert config map list abstract config manager.
     *
     * @param configMapList the config map list
     * @return the abstract config manager
     */
    public AbstractListConfigManager<C> insertConfigMapList(
            List<Map<String, Object>> configMapList) {
        return loadConfig(buildConfigStream(configMapList));
    }

    private Stream<C> buildConfigStream(
            List<Map<String, Object>> configMapList) {
        return configMapList.stream().map(this::transform)
                .filter(Objects::nonNull);
    }

    public C transform(Map<String, Object> configMap) {
        try {
            return getConfigTypeStringAsOpt(configMap)
                    .map(this::extractConfigClass)
                    .map(configClass -> ConfigInterface
                            .transformConfig(configMap, configClass))
                    .orElseGet(() -> JMExceptionManager
                            .handleExceptionAndReturnNull(log,
                                    JMExceptionManager.newRunTimeException(
                                            "Config Error Occur !!!"),
                                    "transformToConfig", configMap));
        } catch (Exception e) {
            return JMExceptionManager
                    .handleExceptionAndReturnNull(log, e, "mutate",
                            this.configMap);
        }
    }

    /**
     * Insert config abstract config manager.
     *
     * @param inputConfig the input config
     * @return the abstract config manager
     */
    public AbstractListConfigManager<C> insertConfig(C inputConfig) {
        this.configMap.put(extractConfigId(inputConfig), inputConfig);
        JMLog.info(log, "insertOutputConfig", inputConfig);
        return this;
    }

    /**
     * Extract config class class.
     *
     * @param configTypeString the config type string
     * @return the class
     */
    protected abstract Class<C> extractConfigClass(String configTypeString);

    /**
     * Gets config type string as opt.
     *
     * @param configMap the config map
     * @return the config type string as opt
     */
    protected Optional<String> getConfigTypeStringAsOpt(
            Map<String, Object> configMap) {
        return JMOptional.getOptional(configMap, getConfigTypeKey())
                .map(Object::toString);
    }

    /**
     * Gets config type key.
     *
     * @return the config type key
     */
    protected abstract String getConfigTypeKey();

    /**
     * Extract mutator id string.
     *
     * @param inputConfig the input config
     * @return the string
     */
    protected abstract String extractConfigId(C inputConfig);

    private List<Map<String, Object>> buildConfigMapList(
            String jmMetricConfigUrl) {
        try {
            return ConfigObjectMapper.readValue(JMOptional.getOptional(
                    JMResources.getStringWithFilePathOrClasspath(
                            jmMetricConfigUrl))
                            .orElseThrow(NullPointerException::new),
                    JMJson.LIST_MAP_TYPE_REFERENCE);
        } catch (Exception e) {
            return JMExceptionManager.handleExceptionAndReturn(log, e,
                    "buildConfigMapList", Collections::emptyList,
                    jmMetricConfigUrl);
        }
    }

    /**
     * Gets config map.
     *
     * @return the config map
     */
    public Map<String, C> getConfigMap() {
        return Collections.unmodifiableMap(this.configMap);
    }

    /**
     * Gets config.
     *
     * @param configId the mutator id
     * @return the config
     */
    public C getConfig(String configId) {
        return JMOptional.getOptional(this.configMap, configId).orElseGet(
                () -> JMExceptionManager.handleExceptionAndReturnNull(log,
                        JMExceptionManager.newRunTimeException("No Config !!!"),
                        "getConfig", configId));
    }

    /**
     * Remove config c.
     *
     * @param configId the mutator id
     * @return the c
     */
    public C removeConfig(String configId) {
        return this.configMap.remove(configId);
    }

}
