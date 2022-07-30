package kr.jm.metric.config;

import kr.jm.metric.config.input.InputConfigManager;
import kr.jm.metric.config.mutator.MutatorConfigManager;
import kr.jm.metric.config.output.OutputConfigManager;
import kr.jm.utils.JMOptional;
import kr.jm.utils.JMResources;
import kr.jm.utils.exception.JMException;
import lombok.Getter;
import lombok.ToString;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Getter
@ToString
public class RunningConfigManager extends AbstractConfigManager {

    private BindingConfig bindingConfig;

    public RunningConfigManager(String runningConfigFilename,
            InputConfigManager inputConfigManager,
            MutatorConfigManager mutatorConfigManager,
            OutputConfigManager outputConfigManager) {
        Optional.ofNullable(transformRunningConfig(runningConfigFilename))
                .ifPresent(
                        runningConfig -> init(runningConfig, inputConfigManager,
                                mutatorConfigManager, outputConfigManager));
    }

    private void init(RunningConfig runningConfig,
            InputConfigManager inputConfigManager,
            MutatorConfigManager mutatorConfigManager,
            OutputConfigManager outputConfigManager) {
        this.bindingConfig = runningConfig.getBinding();
        addConfigList(inputConfigManager, runningConfig.getInputs(), "inputId");
        addConfigList(mutatorConfigManager, runningConfig.getMutators(),
                "mutatorId");
        addConfigList(outputConfigManager, runningConfig.getOutputs(),
                "outputId");
    }

    private <C extends ConfigInterface> void addConfigList(
            AbstractListConfigManager<C> listConfigManager,
            Map<String, Object>[] configMaps, String idKey) {
        JMOptional.getOptional(configMaps).stream().flatMap(Arrays::stream)
                .map(map -> buildCombinedConfig(listConfigManager,
                        map.get(idKey).toString(), map))
                .forEach(listConfigManager::insertConfig);
    }


    private RunningConfig transformRunningConfig(String jmMetricConfigUrl) {
        try {
            return ConfigObjectMapper.readValue(
                    JMResources.getStringOptionalWithFilePath(jmMetricConfigUrl)
                            .orElseThrow(NullPointerException::new),
                    RunningConfig.class);
        } catch (Exception e) {
            return JMException.handleExceptionAndReturnNull(log, e,
                    "transformRunningConfig", jmMetricConfigUrl);
        }
    }

    private <C extends ConfigInterface> C buildCombinedConfig(
            AbstractListConfigManager<C> configManager, String configId,
            Map<String, Object> configMap) {
        try {
            return configManager.transform(JMOptional.getOptional(configId)
                    .map(configManager::getConfig)
                    .map(ConfigInterface::extractConfigMap)
                    .map(oldConfigMap -> buildCombinedConfigMap(oldConfigMap,
                            configMap)).orElse(configMap));
        } catch (Exception e) {
            throw JMException.handleExceptionAndReturnRuntimeEx(log, e,
                    "buildCombinedConfig", configManager, configMap);
        }
    }

    private Map<String, Object> buildCombinedConfigMap(
            Map<String, Object> oldConfigMap,
            Map<String, Object> newConfigMap) {
        newConfigMap.entrySet().stream()
                .filter(entry -> Objects.nonNull(entry.getValue())).forEach(
                entry -> oldConfigMap.put(entry.getKey(),
                        buildCombinedValue(oldConfigMap.get(entry.getKey()),
                                entry.getValue())));
        return oldConfigMap;
    }

    private Object buildCombinedValue(Object oldValue, Object newValue) {
        return Objects.nonNull(oldValue) &&
                oldValue instanceof Map ? buildCombinedConfigMap(
                (Map<String, Object>) oldValue,
                (Map<String, Object>) newValue) : newValue;
    }

}
