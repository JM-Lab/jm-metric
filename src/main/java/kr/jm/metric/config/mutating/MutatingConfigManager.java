package kr.jm.metric.config.mutating;

import com.fasterxml.jackson.core.type.TypeReference;
import kr.jm.metric.config.AbstractConfigManager;
import kr.jm.utils.helper.JMLambda;
import kr.jm.utils.helper.JMLog;
import kr.jm.utils.helper.JMOptional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The type Metric properties manager.
 */
public class MutatingConfigManager extends
        AbstractConfigManager<MutatingConfig> {

    private static final String MUTATING_CONFIG_TYPE = "mutatingConfigType";
    private Map<String, String> inputIdConfigIdMap;

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
    protected TypeReference<MutatingConfig> extractConfigTypeReference(
            String configTypeString) {
        return MutatingConfigType.valueOf(configTypeString).getTypeReference();
    }

    @Override
    protected String getConfigTypeKey() {
        return MUTATING_CONFIG_TYPE;
    }

    @Override
    protected String extractConfigId(MutatingConfig inputConfig) {
        return inputConfig.getConfigId();
    }

    /**
     * Bind data id to properties id metric properties manager.
     *
     * @param inputId          the data id
     * @param mutatingConfigId the properties id
     * @return the metric properties manager
     */
    public MutatingConfigManager bindInputIdToMutatingConfigId(String inputId,
            String mutatingConfigId) {
        JMOptional.getOptional(this.configMap, mutatingConfigId)
                .map(metricConfig -> metricConfig.bindInputId(inputId))
                .ifPresentOrElse(
                        config -> putInputIdMutatingConfigId(inputId,
                                mutatingConfigId),
                        () -> log
                                .warn("No ConfigId Occur !!! - mutatingConfigId = {}",
                                        mutatingConfigId));
        return this;
    }

    private void putInputIdMutatingConfigId(String inputId,
            String mutatingConfigId) {
        Optional.ofNullable(
                getInputIdMutatingConfigIdMap().put(inputId, mutatingConfigId))
                .ifPresentOrElse(oldConfigId -> log
                                .warn("Change ConfigId Occur !!! - " +
                                                "oldConfigId = {}, " +
                                                "newConfigId = {}, inputId = {}",
                                        oldConfigId, mutatingConfigId, inputId),
                        () -> JMLog
                                .info(log, "bindInputIdToMutatingConfigId",
                                        inputId, mutatingConfigId));
    }


    /**
     * Gets properties list with data id.
     *
     * @param inputId the data id
     * @return the properties list with data id
     */
    public Optional<MutatingConfig> getMutatingConfigAsOpt(String inputId) {
        return getMutatingConfigIdAsOpt(inputId).map(this::getMutatingConfig);
    }

    /**
     * Gets properties id list.
     *
     * @param inputId the data id
     * @return the properties id list
     */
    public Optional<String> getMutatingConfigIdAsOpt(String inputId) {
        return JMOptional.getOptional(getInputIdMutatingConfigIdMap(), inputId);
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
     * @param inputId the data id
     * @return the metric properties manager
     */
    public MutatingConfigManager removeInputId(String inputId) {
        this.configMap.values().stream().filter(mutatingConfig -> inputId
                .equals(mutatingConfig.bindInputId))
                .forEach(MutatingConfig::clearBindInputId);
        getInputIdMutatingConfigIdMap().remove(inputId);
        return this;
    }

    @Override
    public MutatingConfigManager insertConfig(MutatingConfig mutatingConfig) {
        super.insertConfig(mutatingConfig);
        Optional.ofNullable(mutatingConfig.getBindInputId()).ifPresent
                (bindInputId -> bindInputIdToMutatingConfigId(bindInputId,
                        mutatingConfig.getConfigId()));
        return this;
    }

    /**
     * Gets data id properties id set map.
     *
     * @return the data id properties id set map
     */
    public Map<String, String> getInputIdMutatingConfigIdMap() {
        return JMLambda.supplierIfNull(this.inputIdConfigIdMap,
                () -> this.inputIdConfigIdMap = new HashMap<>());
    }

}
