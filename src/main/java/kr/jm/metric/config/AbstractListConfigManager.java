package kr.jm.metric.config;

import kr.jm.utils.exception.JMExceptionManager;
import kr.jm.utils.helper.JMJson;
import kr.jm.utils.helper.JMLog;
import kr.jm.utils.helper.JMOptional;
import kr.jm.utils.helper.JMResources;

import java.util.*;
import java.util.stream.Stream;

public abstract class AbstractListConfigManager<C extends ConfigInterface>
        extends AbstractConfigManager {

    protected Map<String, C> configMap;

    public AbstractListConfigManager() {
        this.configMap = new HashMap<>();
    }

    public AbstractListConfigManager(String configFilename) {
        this();
        loadConfig(configFilename);
    }

    public AbstractListConfigManager(List<C> configList) {
        this();
        loadConfig(configList.stream());
    }

    public AbstractListConfigManager(C... configs) {
        this();
        loadConfig(Arrays.stream(configs));
    }

    public AbstractListConfigManager<C> loadConfig(String configFilePath) {
        return loadConfig(
                buildConfigStream(buildConfigMapList(configFilePath)));
    }

    private AbstractListConfigManager<C> loadConfig(Stream<C> configStream) {
        configStream.forEach(this::insertConfig);
        return this;
    }

    public AbstractListConfigManager<C> insertConfigList(List<C> configList) {
        return loadConfig(configList.stream());
    }

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

    public AbstractListConfigManager<C> insertConfig(C inputConfig) {
        this.configMap.put(extractConfigId(inputConfig), inputConfig);
        JMLog.info(log, "insertOutputConfig", inputConfig);
        return this;
    }

    protected abstract Class<C> extractConfigClass(String configTypeString);

    protected Optional<String> getConfigTypeStringAsOpt(
            Map<String, Object> configMap) {
        return JMOptional.getOptional(configMap, getConfigTypeKey())
                .map(Object::toString);
    }

    protected abstract String getConfigTypeKey();

    protected abstract String extractConfigId(C inputConfig);

    private List<Map<String, Object>> buildConfigMapList(
            String jmMetricConfigUrl) {
        try {
            return ConfigObjectMapper.readValue(JMOptional.getOptional(
                    JMResources.getStringWithFilePathOrClasspath(
                            jmMetricConfigUrl))
                            .orElseThrow(NullPointerException::new),
                    JMJson.MAP_LIST_TYPE_REFERENCE);
        } catch (Exception e) {
            return JMExceptionManager.handleExceptionAndReturn(log, e,
                    "buildConfigMapList", Collections::emptyList,
                    jmMetricConfigUrl);
        }
    }

    public Map<String, C> getConfigMap() {
        return Collections.unmodifiableMap(this.configMap);
    }

    public C getConfig(String configId) {
        return this.configMap.get(configId);
    }

    public C removeConfig(String configId) {
        return this.configMap.remove(configId);
    }

}
