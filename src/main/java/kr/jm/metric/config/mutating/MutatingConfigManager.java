package kr.jm.metric.config.mutating;

import kr.jm.metric.config.ConfigManager;
import kr.jm.utils.datastructure.JMCollections;
import kr.jm.utils.datastructure.JMMap;
import kr.jm.utils.exception.JMExceptionManager;
import kr.jm.utils.helper.JMLog;
import kr.jm.utils.helper.JMOptional;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * The type Metric properties manager.
 */
@Slf4j
public class MutatingConfigManager {
    private static final String INPUT_CONFIG_TYPE = "mutatingConfigType";
    private static final String METRIC_CONFIG_FILENAME = "MutatingConfig.json";

    private Map<String, MutatingConfig> metricConfigMap;
    private Map<String, Set<String>> dataIdConfigIdSetMap;

    /**
     * Instantiates a new Metric properties manager.
     *
     * @param configMaps the properties maps
     */
    @SafeVarargs
    public MutatingConfigManager(Map<String, Object>... configMaps) {
        this(Optional.ofNullable(configMaps).map(Arrays::stream)
                .map(stream -> stream
                        .map(MutatingConfigManager::transformToConfig)
                        .collect(Collectors.toList()))
                .orElseGet(Collections::emptyList));
    }

    /**
     * Instantiates a new Metric properties manager.
     *
     * @param configList the properties list
     */
    public MutatingConfigManager(List<MutatingConfig> configList) {
        this.dataIdConfigIdSetMap = new HashMap<>();
        loadConfig(ConfigManager.buildAllConfigMapList(METRIC_CONFIG_FILENAME));
        insertConfigList(configList);
    }

    /**
     * Insert properties list metric properties manager.
     *
     * @param mutatingConfigList the metric properties list
     * @return the metric properties manager
     */
    public MutatingConfigManager insertConfigList(
            List<MutatingConfig> mutatingConfigList) {
        JMLog.info(log, "insertConfigList",
                mutatingConfigList.stream().map(this::insertConfig)
                        .map(MutatingConfig::getConfigId)
                        .collect(Collectors.toList()));
        return this;
    }

    /**
     * Insert properties map list metric properties manager.
     *
     * @param metricConfigMapList the metric properties map list
     * @return the metric properties manager
     */
    public MutatingConfigManager insertConfigMapList(
            List<Map<String, Object>> metricConfigMapList) {
        insertConfigList(metricConfigMapList.stream()
                .map(MutatingConfigManager::transformToConfig)
                .collect(Collectors.toList()));
        return this;
    }

    /**
     * Transform to properties metric properties.
     *
     * @param metricConfigMap the metric properties map
     * @return the metric properties
     */
    public static MutatingConfig transformToConfig(
            Map<String, Object> metricConfigMap) {
        try {
            return getConfigType(metricConfigMap)
                    .transform(metricConfigMap);
        } catch (Exception e) {
            return JMExceptionManager.handleExceptionAndReturnNull(log, e,
                    "transformToConfig", metricConfigMap);
        }
    }

    private static MutatingConfigType getConfigType(
            Map<String, Object> metricConfigMap) {
        return JMOptional.getOptional(metricConfigMap, INPUT_CONFIG_TYPE)
                .map(Object::toString)
                .map(MutatingConfigType::valueOf)
                .orElseGet(() -> JMExceptionManager.throwRunTimeException(
                        "Wrong MutatingConfigType !!! : " + INPUT_CONFIG_TYPE +
                                "=" + metricConfigMap.get(INPUT_CONFIG_TYPE)));
    }

    /**
     * Bind data id to properties id metric properties manager.
     *
     * @param dataId   the data id
     * @param configId the properties id
     * @return the metric properties manager
     */
    public MutatingConfigManager bindDataIdToConfigId(String dataId,
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

    private Map<String, MutatingConfig> getOrNewConfigMap() {
        return Objects.requireNonNullElseGet(this.metricConfigMap,
                () -> this.metricConfigMap = new ConcurrentHashMap<>());
    }

    /**
     * Gets properties list with data id.
     *
     * @param dataId the data id
     * @return the properties list with data id
     */
    public List<MutatingConfig> getConfigListWithDataId(String dataId) {
        return getConfigIdList(dataId).stream().map(this::getMutatingConfig)
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
    public MutatingConfig getMutatingConfig(String configId) {
        return getOrNewConfigMap().get(configId);
    }

    /**
     * Remove data id metric properties manager.
     *
     * @param dataId the data id
     * @return the metric properties manager
     */
    public MutatingConfigManager removeDataId(String dataId) {
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
    public List<MutatingConfig> getConfigList() {
        return Collections
                .unmodifiableList(
                        new ArrayList<>(getOrNewConfigMap().values()));
    }

    /**
     * Insert properties metric properties.
     *
     * @param mutatingConfig the metric properties
     * @return the metric properties
     */
    public MutatingConfig insertConfig(MutatingConfig mutatingConfig) {
        JMLog.info(log, "insertConfig", mutatingConfig);
        return insertConfigAndBindDataIdConfigId(mutatingConfig,
                mutatingConfig.getConfigId());
    }

    private MutatingConfig insertConfigAndBindDataIdConfigId(
            MutatingConfig mutatingConfig, String configId) {
        getOrNewConfigMap().put(configId, mutatingConfig);
        mutatingConfig.getBindDataIds().forEach(
                dataId -> bindDataIdToConfigId(dataId, configId));
        return mutatingConfig;
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
    public Map<String, MutatingConfig> getConfigMap() {
        return Collections.unmodifiableMap(this.metricConfigMap);
    }

    /**
     * Load properties metric properties manager.
     *
     * @param jmMetricConfigUrl the jm metric properties url
     * @return the metric properties manager
     */
    public MutatingConfigManager loadConfig(String jmMetricConfigUrl) {
        return loadConfig(ConfigManager.buildConfigMapList(jmMetricConfigUrl));
    }

    /**
     * Load properties metric properties manager.
     *
     * @param metricConfigMapList the metric properties map list
     * @return the metric properties manager
     */
    public MutatingConfigManager loadConfig(
            List<Map<String, Object>> metricConfigMapList) {
        JMLog.info(log, "loadConfig", metricConfigMapList.size());
        return insertConfigMapList(metricConfigMapList);
    }
}
