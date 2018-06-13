package kr.jm.metric.config.mutating;

import kr.jm.metric.config.AbstractConfigManager;
import kr.jm.utils.datastructure.JMCollections;
import kr.jm.utils.datastructure.JMMap;
import kr.jm.utils.exception.JMExceptionManager;
import kr.jm.utils.helper.JMLambda;
import kr.jm.utils.helper.JMLog;
import kr.jm.utils.helper.JMOptional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The type Metric properties manager.
 */
public class MutatingConfigManager extends
        AbstractConfigManager<MutatingConfig> {

    private static final String MUTATING_CONFIG_TYPE = "mutatingConfigType";
    private Map<String, Set<String>> dataIdConfigIdSetMap;

    public MutatingConfigManager(String configFilename) {
        super(configFilename);
    }

    /**
     * Instantiates a new Metric properties manager.
     *
     * @param configList the properties list
     */
    public MutatingConfigManager(List<MutatingConfig> configList) {
        super(configList);
    }

    @Override
    protected MutatingConfig transformToConfig(
            Map<String, Object> metricConfigMap) {
        try {
            return getConfigType(metricConfigMap).transform(metricConfigMap);
        } catch (Exception e) {
            return JMExceptionManager.handleExceptionAndReturnNull(log, e,
                    "transformToConfig", metricConfigMap);
        }
    }

    private MutatingConfigType getConfigType(
            Map<String, Object> metricConfigMap) {
        return JMOptional.getOptional(metricConfigMap, MUTATING_CONFIG_TYPE)
                .map(Object::toString)
                .map(MutatingConfigType::valueOf)
                .orElseGet(() -> JMExceptionManager.throwRunTimeException(
                        "Wrong MutatingConfigType !!! : " +
                                MUTATING_CONFIG_TYPE +
                                "=" +
                                metricConfigMap.get(MUTATING_CONFIG_TYPE)));
    }

    @Override
    protected String extractConfigId(MutatingConfig inputConfig) {
        return inputConfig.getConfigId();
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
        Optional.ofNullable(this.configMap.get(configId))
                .map(metricConfig -> metricConfig.withBindDataIds(dataId))
                .ifPresentOrElse(metricConfig -> JMMap
                        .getOrPutGetNew(getDataIdConfigIdSetMap(), dataId,
                                () -> new HashSet<>()).add(configId), () -> log
                        .warn("No ConfigId Occur !!! - inputSingle configId = {}",
                                configId));
        return this;
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
        return JMOptional.getOptional(getDataIdConfigIdSetMap(), dataId)
                .map(JMCollections::newList)
                .orElseGet(() -> warnEmptyList(dataId));
    }

    private List<String> warnEmptyList(String dataId) {
        log.warn("No ConfigIdList Occur !!! - inputId = {}", dataId);
        return Collections.emptyList();
    }

    /**
     * Gets properties.
     *
     * @param configId the properties id
     * @return the properties
     */
    public MutatingConfig getMutatingConfig(String configId) {
        return this.configMap.get(configId);
    }

    /**
     * Remove data id metric properties manager.
     *
     * @param dataId the data id
     * @return the metric properties manager
     */
    public MutatingConfigManager removeDataId(String dataId) {
        Optional.ofNullable(getDataIdConfigIdSetMap().remove(dataId)).stream()
                .flatMap(Set::stream).map(this.configMap::get)
                .forEach(
                        inputConfig -> inputConfig.removeBindDataId(dataId));
        return this;
    }

    @Override
    public MutatingConfigManager insertConfig(MutatingConfig mutatingConfig) {
        super.insertConfig(mutatingConfig);
        insertConfigAndBindDataIdConfigId(mutatingConfig,
                mutatingConfig.getConfigId());
        return this;
    }

    private void insertConfigAndBindDataIdConfigId(
            MutatingConfig mutatingConfig, String configId) {
        mutatingConfig.getBindDataIds()
                .forEach(dataId -> bindDataIdToConfigId(dataId, configId));
    }

    /**
     * Gets data id properties id set map.
     *
     * @return the data id properties id set map
     */
    public Map<String, Set<String>> getDataIdConfigIdSetMap() {
        return JMLambda.supplierIfNull(this.dataIdConfigIdSetMap,
                () -> this.dataIdConfigIdSetMap = new HashMap<>());
    }

}
