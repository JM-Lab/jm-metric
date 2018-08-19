package kr.jm.metric.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.jm.utils.datastructure.JMCollections;
import kr.jm.utils.enums.OS;
import kr.jm.utils.exception.JMExceptionManager;
import kr.jm.utils.helper.*;
import org.slf4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

/**
 * The type Abstract config manager.
 *
 * @param <C> the type parameter
 */
public abstract class AbstractConfigManager<C extends ConfigInterface> {
    /**
     * The Log.
     */
    protected Logger log = org.slf4j.LoggerFactory.getLogger(getClass());
    private static ObjectMapper ConfigObjectMapper = new ObjectMapper()
            .configure(JsonParser.Feature.ALLOW_COMMENTS, true);
    /**
     * The constant CONFIG_DIR.
     */
    public static final String CONFIG_DIR = Optional.ofNullable(
            JMResources.getSystemProperty("jm.metric.configDir"))
            .orElse("config");

    /**
     * The Config map.
     */
    protected Map<String, C> configMap;

    /**
     * Instantiates a new Abstract config manager.
     */
    public AbstractConfigManager() {
        this.configMap = new ConcurrentHashMap<>();
    }

    /**
     * Instantiates a new Abstract config manager.
     *
     * @param configFilename the config filename
     */
    public AbstractConfigManager(String configFilename) {
        this();
        loadConfig(configFilename);
    }

    /**
     * Instantiates a new Abstract config manager.
     *
     * @param configList the config list
     */
    public AbstractConfigManager(List<C> configList) {
        this();
        loadConfig(configList.stream());
    }

    /**
     * Instantiates a new Abstract config manager.
     *
     * @param configs the configs
     */
    public AbstractConfigManager(C... configs) {
        this();
        loadConfig(Arrays.stream(configs));
    }

    /**
     * Load config abstract config manager.
     *
     * @param configFilename the config filename
     * @return the abstract config manager
     */
    public AbstractConfigManager<C> loadConfig(String configFilename) {
        return loadConfig(
                buildConfigStream(buildAllConfigMapList(configFilename)));
    }

    private AbstractConfigManager<C> loadConfig(Stream<C> configStream) {
        configStream.forEach(this::insertConfig);
        return this;
    }

    /**
     * Insert config list abstract config manager.
     *
     * @param configList the config list
     * @return the abstract config manager
     */
    public AbstractConfigManager<C> insertConfigList(List<C> configList) {
        return loadConfig(configList.stream());
    }

    /**
     * Insert config map list abstract config manager.
     *
     * @param configMapList the config map list
     * @return the abstract config manager
     */
    public AbstractConfigManager<C> insertConfigMapList(
            List<Map<String, Object>> configMapList) {
        return loadConfig(buildConfigStream(configMapList));
    }

    private Stream<C> buildConfigStream(
            List<Map<String, Object>> configMapList) {
        return configMapList.stream().map(this::transform)
                .filter(Objects::nonNull);
    }

    private C transform(Map<String, Object> configMap) {
        try {
            return transformToConfig(configMap);
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
    public AbstractConfigManager<C> insertConfig(C inputConfig) {
        this.configMap.put(extractMutatorId(inputConfig), inputConfig);
        JMLog.info(log, "insertOutputConfig", inputConfig);
        return this;
    }

    private C transformToConfig(
            Map<String, Object> configMap) {
        return getConfigTypeStringAsOpt(configMap)
                .map(this::extractConfigClass)
                .map(typeReference -> ConfigInterface
                        .transformConfig(configMap, typeReference))
                .orElseGet(() -> JMExceptionManager
                        .handleExceptionAndReturnNull(log, JMExceptionManager
                                        .newRunTimeException("Config Error Occur !!!"),
                                "transformToConfig", configMap));
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
    protected abstract String extractMutatorId(C inputConfig);

    private List<Map<String, Object>> buildAllConfigMapList(
            String configFilename) {
        return JMCollections
                .buildMergedList(buildConfigMapList(configFilename),
                        buildConfigMapList(
                                buildDefaultConfigPath(configFilename)));
    }

    private String buildDefaultConfigPath(String configFilename) {
        return CONFIG_DIR + OS.getFileSeparator() + configFilename;
    }


    private List<Map<String, Object>> buildConfigMapList(
            String jmMetricConfigUrl) {
        try {
            return ConfigObjectMapper.readValue(JMOptional.getOptional(
                    JMRestfulResource
                            .getStringWithRestOrFilePathOrClasspath(
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
     * @param mutatorId the mutator id
     * @return the config
     */
    public C getConfig(String mutatorId) {
        return JMOptional.getOptional(this.configMap, mutatorId).orElseGet(
                () -> JMExceptionManager.handleExceptionAndReturnNull(log,
                        JMExceptionManager.newRunTimeException("No Config !!!"),
                        "getConfig", mutatorId));
    }

    /**
     * Extract number config number.
     *
     * @param mutatorId     the mutator id
     * @param key           the key
     * @param defaultNumber the default number
     * @return the number
     */
    public Number extractNumberConfig(String mutatorId, String key,
            Number defaultNumber) {
        return JMOptional.getOptional(extractStringConfig(mutatorId, key))
                .map(Double::valueOf).map(d -> (Number) d)
                .orElse(defaultNumber);
    }

    /**
     * Extract string config string.
     *
     * @param mutatorId the mutator id
     * @param key       the key
     * @return the string
     */
    public String extractStringConfig(String mutatorId, String key) {
        return JMOptional.getOptional(this.configMap, mutatorId)
                .map(ConfigInterface::extractConfigMap)
                .flatMap(map -> JMOptional.getOptional(map, key))
                .map(Object::toString).orElse(JMString.EMPTY);
    }

    /**
     * Remove config c.
     *
     * @param mutatorId the mutator id
     * @return the c
     */
    public C removeConfig(String mutatorId) {
        return this.configMap.remove(mutatorId);
    }

}
