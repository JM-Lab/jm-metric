package kr.jm.metric.config;

import kr.jm.metric.config.input.InputConfigInterface;
import kr.jm.metric.config.input.InputConfigManager;
import kr.jm.metric.config.mutator.MutatorConfigInterface;
import kr.jm.metric.config.mutator.MutatorConfigManager;
import kr.jm.metric.config.output.OutputConfigInterface;
import kr.jm.metric.config.output.OutputConfigManager;
import kr.jm.utils.exception.JMExceptionManager;
import kr.jm.utils.helper.JMOptional;
import kr.jm.utils.helper.JMResources;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@ToString
public class RunningConfigManager extends AbstractConfigManager {

    private RunningConfig runningConfig;
    private InputConfigInterface inputConfig;
    private MutatorConfigInterface mutatorConfig;
    private List<OutputConfigInterface> outputConfigs;

    public RunningConfigManager(String runningConfigFilename,
            InputConfigManager inputConfigManager,
            MutatorConfigManager mutatorConfigManager,
            OutputConfigManager outputConfigManager) {

        this.runningConfig = transformRunningConfig(runningConfigFilename);
        if (Objects.nonNull(this.runningConfig)) {
            this.runningConfig.getInputIdAsOpt().flatMap(inputId ->
                    buildCombinedConfigAsOpt(inputConfigManager, inputId,
                            this.runningConfig.getInput()))
                    .ifPresent(inputConfig -> inputConfigManager
                            .insertConfig(
                                    this.inputConfig = inputConfig));
            this.runningConfig.getMutatorIdAsOpt().flatMap(mutatorId
                    -> buildCombinedConfigAsOpt(mutatorConfigManager, mutatorId,
                    this.runningConfig.getMutator()))
                    .ifPresent(mutatorConfig -> mutatorConfigManager
                            .insertConfig(this.mutatorConfig = mutatorConfig));
            this.outputConfigs =
                    runningConfig.getOutputIdMap().entrySet().stream()
                            .map(entry -> buildCombinedConfigAsOpt(
                                    outputConfigManager, entry.getKey(),
                                    entry.getValue()))
                            .filter(Optional::isPresent).map(Optional::get)
                            .peek(outputConfigManager::insertConfig)
                            .collect(Collectors.toList());
        }
    }

    private RunningConfig transformRunningConfig(String jmMetricConfigUrl) {
        try {
            return ConfigObjectMapper.readValue(
                    JMResources.getStringAsOptWithFilePath(jmMetricConfigUrl)
                            .orElseThrow(NullPointerException::new),
                    RunningConfig.class);
        } catch (Exception e) {
            return JMExceptionManager.handleExceptionAndReturnNull(log, e,
                    "transformRunningConfig", jmMetricConfigUrl);
        }
    }

    private <C extends ConfigInterface> Optional<C> buildCombinedConfigAsOpt(
            AbstractListConfigManager<C> configManager, String configId,
            Map<String, Object> configMap) {
        try {
            return JMOptional.getOptional(configId)
                    .map(configManager::getConfig)
                    .map(ConfigInterface::extractConfigMap)
                    .map(oldConfigMap -> buildCombinedConfigMap(configMap,
                            oldConfigMap))
                    .map(configManager::transform);
        } catch (Exception e) {
            throw JMExceptionManager.handleExceptionAndReturnRuntimeEx(log, e,
                    "buildCombinedConfigAsOpt", configManager, configMap);
        }
    }

    private Map<String, Object> buildCombinedConfigMap(
            Map<String, Object> newConfigMap,
            Map<String, Object> oldConfigMap) {
        newConfigMap.entrySet().stream()
                .filter(entry -> Objects.nonNull(entry.getValue())).forEach(
                entry -> oldConfigMap.put(entry.getKey(),
                        entry.getValue()));
        return oldConfigMap;
    }

}
