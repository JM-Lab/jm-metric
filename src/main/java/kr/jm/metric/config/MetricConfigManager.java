package kr.jm.metric.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.jm.utils.datastructure.JMCollections;
import kr.jm.utils.datastructure.JMMap;
import kr.jm.utils.enums.OS;
import kr.jm.utils.exception.JMExceptionManager;
import kr.jm.utils.helper.*;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * The type Metric properties manager.
 */
@Slf4j
public class MetricConfigManager {
    private static final String INPUT_CONFIG_TYPE = "metricConfigType";
    private static ObjectMapper ConfigObjectMapper = new ObjectMapper()
            .configure(JsonParser.Feature.ALLOW_COMMENTS, true);
    public static final String CONFIG_DIR = Optional.ofNullable(
            JMResources.getSystemProperty("jm.metric.configDir"))
            .orElse("config");
    private static final String METRIC_CONFIG_FILENAME = "JMMetricConfig.json";

    private Map<String, MetricConfig> metricConfigMap;
    private Map<String, Set<String>> dataIdConfigIdSetMap;

    /**
     * Instantiates a new Metric properties manager.
     *
     * @param configMaps the properties maps
     */
    @SafeVarargs
    public MetricConfigManager(Map<String, Object>... configMaps) {
        this(Optional.ofNullable(configMaps).map(Arrays::stream)
                .map(stream -> stream
                        .map(MetricConfigManager::transformToConfig)
                        .collect(Collectors.toList()))
                .orElseGet(Collections::emptyList));
    }

    /**
     * Instantiates a new Metric properties manager.
     *
     * @param configList the properties list
     */
    public MetricConfigManager(List<MetricConfig> configList) {
        this.dataIdConfigIdSetMap = new HashMap<>();
        loadConfig(buildAllConfigMapList(METRIC_CONFIG_FILENAME));
        insertConfigList(configList);
    }

    public static List<Map<String, Object>> buildAllConfigMapList(
            String configFilename) {
        return JMCollections.buildMergedList(buildConfigMapList(configFilename),
                buildConfigMapList(buildDefaultConfigPath(configFilename)));
    }

    private static String buildDefaultConfigPath(String configFilename) {
        return CONFIG_DIR + OS.getFileSeparator() + configFilename;
    }

    /**
     * Insert properties list metric properties manager.
     *
     * @param metricConfigList the metric properties list
     * @return the metric properties manager
     */
    public MetricConfigManager insertConfigList(
            List<MetricConfig> metricConfigList) {
        JMLog.info(log, "insertConfigList",
                metricConfigList.stream().map(this::insertConfig)
                        .map(MetricConfig::getConfigId)
                        .collect(Collectors.toList()));
        return this;
    }

    /**
     * Insert properties map list metric properties manager.
     *
     * @param metricConfigMapList the metric properties map list
     * @return the metric properties manager
     */
    public MetricConfigManager insertConfigMapList(
            List<Map<String, Object>> metricConfigMapList) {
        insertConfigList(metricConfigMapList.stream()
                .map(MetricConfigManager::transformToConfig)
                .collect(Collectors.toList()));
        return this;
    }

    /**
     * Transform to properties metric properties.
     *
     * @param metricConfigMap the metric properties map
     * @return the metric properties
     */
    public static MetricConfig transformToConfig(
            Map<String, Object> metricConfigMap) {
        try {
            return getConfigType(metricConfigMap)
                    .transform(metricConfigMap);
        } catch (Exception e) {
            return JMExceptionManager.handleExceptionAndReturnNull(log, e,
                    "transformToConfig", metricConfigMap);
        }
    }

    private static MetricConfigType getConfigType(
            Map<String, Object> metricConfigMap) {
        return JMOptional.getOptional(metricConfigMap, INPUT_CONFIG_TYPE)
                .map(Object::toString)
                .map(MetricConfigType::valueOf)
                .orElseGet(() -> JMExceptionManager.throwRunTimeException(
                        "Wrong MetricConfigType !!! : " + INPUT_CONFIG_TYPE +
                                "=" + metricConfigMap.get(INPUT_CONFIG_TYPE)));
    }

    /**
     * Bind data id to properties id metric properties manager.
     *
     * @param dataId   the data id
     * @param configId the properties id
     * @return the metric properties manager
     */
    public MetricConfigManager bindDataIdToConfigId(String dataId,
            String configId) {
        JMLog.info(log, "bindDataIdToConfigId", dataId, configId);
        Optional.ofNullable(getOrNewConfigMap().get(configId))
                .map(metricConfig -> metricConfig.withBindDataIds(dataId))
                .ifPresentOrElse(metricConfig -> JMMap
                        .getOrPutGetNew(this.dataIdConfigIdSetMap, dataId,
                                () -> new HashSet<>()).add(configId), () -> log
                        .warn("No ConfigId Occur !!! - inputSingle configId = {}",
                                configId));
        return this;
    }

    private Map<String, MetricConfig> getOrNewConfigMap() {
        return Objects.requireNonNullElseGet(this.metricConfigMap,
                () -> this.metricConfigMap = new ConcurrentHashMap<>());
    }

    /**
     * Gets properties list with data id.
     *
     * @param dataId the data id
     * @return the properties list with data id
     */
    public List<MetricConfig> getConfigListWithDataId(String dataId) {
        return getConfigIdList(dataId).stream().map(this::getConfig)
                .collect(Collectors.toList());
    }

    /**
     * Gets properties id list.
     *
     * @param dataId the data id
     * @return the properties id list
     */
    public List<String> getConfigIdList(String dataId) {
        return JMOptional.getOptional(this.dataIdConfigIdSetMap, dataId)
                .map(JMCollections::newList)
                .map(Collections::unmodifiableList)
                .orElseGet(() -> warnEmptyList(dataId));
    }

    private List<String> warnEmptyList(String dataId) {
        log.warn("No ConfigIdList Occur !!! - dataId = {}", dataId);
        return Collections.emptyList();
    }

    /**
     * Gets properties id set.
     *
     * @return the properties id set
     */
    public Set<String> getConfigIdSet() {
        return Collections.unmodifiableSet(this.metricConfigMap.keySet());
    }

    /**
     * Gets properties.
     *
     * @param configId the properties id
     * @return the properties
     */
    public MetricConfig getConfig(String configId) {
        return getOrNewConfigMap().get(configId);
    }

    /**
     * Remove data id metric properties manager.
     *
     * @param dataId the data id
     * @return the metric properties manager
     */
    public MetricConfigManager removeDataId(String dataId) {
        Optional.ofNullable(this.dataIdConfigIdSetMap.remove(dataId)).stream()
                .flatMap(Set::stream).map(getOrNewConfigMap()::get)
                .forEach(
                        inputConfig -> inputConfig.removeBindDataId(dataId));
        return this;
    }

    /**
     * Gets properties list.
     *
     * @return the properties list
     */
    public List<MetricConfig> getConfigList() {
        return Collections
                .unmodifiableList(
                        new ArrayList<>(getOrNewConfigMap().values()));
    }

    /**
     * Insert properties metric properties.
     *
     * @param metricConfig the metric properties
     * @return the metric properties
     */
    public MetricConfig insertConfig(MetricConfig metricConfig) {
        JMLog.info(log, "insertConfig", metricConfig);
        return insertConfigAndBindDataIdConfigId(metricConfig,
                metricConfig.getConfigId());
    }

    private MetricConfig insertConfigAndBindDataIdConfigId(
            MetricConfig metricConfig, String configId) {
        getOrNewConfigMap().put(configId, metricConfig);
        metricConfig.getBindDataIds().forEach(
                dataId -> bindDataIdToConfigId(dataId, configId));
        return metricConfig;
    }

    /**
     * Gets data id properties id set map.
     *
     * @return the data id properties id set map
     */
    public Map<String, Set<String>> getDataIdConfigIdSetMap() {
        return Collections.unmodifiableMap(this.dataIdConfigIdSetMap);
    }

    /**
     * Gets properties map.
     *
     * @return the properties map
     */
    public Map<String, MetricConfig> getConfigMap() {
        return Collections.unmodifiableMap(this.metricConfigMap);
    }

    /**
     * Load properties metric properties manager.
     *
     * @param jmMetricConfigUrl the jm metric properties url
     * @return the metric properties manager
     */
    public MetricConfigManager loadConfig(String jmMetricConfigUrl) {
        return loadConfig(buildConfigMapList(jmMetricConfigUrl));
    }

    public static List<Map<String, Object>> buildConfigMapList(
            String jmMetricConfigUrl) {
        try {
            return ConfigObjectMapper.readValue(JMOptional.getOptional(
                    JMRestfulResource.getStringWithRestOrFilePathOrClasspath(
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
     * Load properties metric properties manager.
     *
     * @param metricConfigMapList the metric properties map list
     * @return the metric properties manager
     */
    public MetricConfigManager loadConfig(
            List<Map<String, Object>> metricConfigMapList) {
        JMLog.info(log, "loadConfig", metricConfigMapList.size());
        return insertConfigMapList(metricConfigMapList);
    }
}
