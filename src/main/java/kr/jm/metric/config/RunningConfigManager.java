package kr.jm.metric.config;

import kr.jm.metric.config.input.InputConfigInterface;
import kr.jm.metric.config.input.InputConfigManager;
import kr.jm.metric.config.mutator.MutatorConfigInterface;
import kr.jm.metric.config.mutator.MutatorConfigManager;
import kr.jm.metric.config.output.OutputConfigInterface;
import kr.jm.metric.config.output.OutputConfigManager;
import kr.jm.utils.exception.JMExceptionManager;
import kr.jm.utils.helper.JMOptional;
import kr.jm.utils.helper.JMRestfulResource;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Getter
@ToString
public class RunningConfigManager extends AbstractConfigManager<RunningConfig> {

    private RunningConfig runningConfig;
    private InputConfigInterface inputConfig;
    private MutatorConfigInterface mutatorConfig;
    private OutputConfigInterface outputConfig;

    public RunningConfigManager(String runningConfigFilename,
            InputConfigManager inputConfigManager,
            MutatorConfigManager mutatorConfigManager,
            OutputConfigManager outputConfigManager) {
        loadConfigRunningConfig(runningConfigFilename);
        if (Objects.nonNull(this.runningConfig)) {
            buildCombinedConfigAsOpt(inputConfigManager,
                    this.runningConfig.getInput())
                    .ifPresent(inputConfig -> inputConfigManager
                            .insertConfig(this.inputConfig = inputConfig));
            buildCombinedConfigAsOpt(mutatorConfigManager,
                    this.runningConfig.getMutator())
                    .ifPresent(mutatorConfig -> mutatorConfigManager
                            .insertConfig(this.mutatorConfig = mutatorConfig));
            buildCombinedConfigAsOpt(outputConfigManager,
                    this.runningConfig.getOutput())
                    .ifPresent(outputConfig -> outputConfigManager
                            .insertConfig(this.outputConfig = outputConfig));
        }
    }

    private void loadConfigRunningConfig(String runningConfigFilename) {
        Optional.ofNullable(Optional.ofNullable(transformRunningConfig(
                buildDefaultConfigPath(runningConfigFilename))).orElseGet(()
                -> transformRunningConfig(runningConfigFilename)))
                .ifPresent(runningConfig -> this.runningConfig = runningConfig);
    }

    private RunningConfig transformRunningConfig(String jmMetricConfigUrl) {
        try {
            return ConfigObjectMapper.readValue(JMOptional.getOptional(
                    JMRestfulResource.getStringWithRestOrFilePathOrClasspath(
                            jmMetricConfigUrl))
                            .orElseThrow(NullPointerException::new),
                    RunningConfig.class);
        } catch (Exception e) {
            return JMExceptionManager.handleExceptionAndReturnNull(log, e,
                    "transformRunningConfig", jmMetricConfigUrl);
        }
    }

    private <C extends ConfigInterface> Optional<C> buildCombinedConfigAsOpt(
            AbstractListConfigManager<C> configManager,
            Map<String, Object> configMap) {
        return Optional.ofNullable(configManager.transform(configMap))
                .flatMap(config -> Optional.ofNullable(configManager
                        .getConfig(configManager.extractConfigId(config)))
                        .map(ConfigInterface::extractConfigMap)
                        .map(oldConfigMap -> buildCombinedConfigMap(
                                configMap, oldConfigMap))
                        .map(configManager::transform));
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
