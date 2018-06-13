package kr.jm.metric.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.jm.utils.datastructure.JMCollections;
import kr.jm.utils.enums.OS;
import kr.jm.utils.exception.JMExceptionManager;
import kr.jm.utils.helper.*;
import org.slf4j.Logger;

import java.util.*;
import java.util.stream.Stream;


public abstract class AbstractConfigManager<C extends ConfigInterface> {
    protected Logger log = org.slf4j.LoggerFactory.getLogger(getClass());
    private static ObjectMapper ConfigObjectMapper = new ObjectMapper()
            .configure(JsonParser.Feature.ALLOW_COMMENTS, true);
    public static final String CONFIG_DIR = Optional.ofNullable(
            JMResources.getSystemProperty("jm.metric.configDir"))
            .orElse("config");

    protected Map<String, C> configMap;

    public AbstractConfigManager() {
        this.configMap = new HashMap<>();
    }

    public AbstractConfigManager(String configFilename) {
        this();
        loadConfig(configFilename);
    }

    public AbstractConfigManager(List<C> configList) {
        this();
        loadConfig(configList.stream());
    }

    public AbstractConfigManager(C... configs) {
        this();
        loadConfig(Arrays.stream(configs));
    }

    public AbstractConfigManager<C> loadConfig(String configFilename) {
        return loadConfig(
                buildConfigStream(buildAllConfigMapList(configFilename)));
    }

    private AbstractConfigManager<C> loadConfig(Stream<C> configStream) {
        configStream.forEach(this::insertConfig);
        return this;
    }

    public AbstractConfigManager<C> insertConfigList(List<C> configList) {
        return loadConfig(configList.stream());
    }

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
                    .handleExceptionAndReturnNull(log, e, "transform",
                            this.configMap);
        }
    }

    public AbstractConfigManager<C> insertConfig(C inputConfig) {
        this.configMap.put(extractConfigId(inputConfig), inputConfig);
        JMLog.info(log, "insertConfig", inputConfig);
        return this;
    }

    protected abstract C transformToConfig(Map<String, Object> configMap);

    protected abstract String extractConfigId(C inputConfig);

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

    public Map<String, C> getConfigMap() {
        return Collections.unmodifiableMap(this.configMap);
    }

    public Optional<C> getConfigAsOpt(String configId) {
        return JMOptional.getOptional(this.configMap, configId);
    }

    public Number extractNumberConfig(String configId, String key,
            Number defaultNumber) {
        return JMOptional.getOptional(extractStringConfig(configId, key))
                .map(Double::valueOf).map(d -> (Number) d)
                .orElse(defaultNumber);
    }

    public String extractStringConfig(String configId, String key) {
        return JMOptional.getOptional(this.configMap, configId)
                .map(ConfigInterface::extractConfigMap)
                .flatMap(map -> JMOptional.getOptional(map, key))
                .map(Object::toString).orElse(JMString.EMPTY);
    }

}
