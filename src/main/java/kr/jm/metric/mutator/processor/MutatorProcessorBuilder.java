package kr.jm.metric.mutator.processor;

import kr.jm.metric.config.mutator.MutatorConfigInterface;
import kr.jm.metric.config.mutator.field.FieldConfig;
import kr.jm.metric.config.mutator.field.FilterConfig;
import kr.jm.metric.mutator.MutatorInterface;
import kr.jm.metric.mutator.RawMutator;
import kr.jm.utils.enums.OS;
import kr.jm.utils.helper.JMOptional;

import java.util.Map;
import java.util.Optional;

public class MutatorProcessorBuilder {

    public static MutatorProcessor buildRaw() {
        return build(new RawMutator());
    }

    public static MutatorProcessor build(MutatorConfigInterface mutatorConfig) {
        return build(mutatorConfig.getWorkers(), mutatorConfig.buildMutator(),
                Optional.ofNullable(mutatorConfig.getFieldConfig())
                        .map(FieldConfig::getFilter).orElse(null));
    }

    public static MutatorProcessor build(int workers, MutatorInterface mutator,
            Map<String, FilterConfig> filterConfigMap) {
        return new MutatorProcessor(workers, mutator,
                JMOptional.getOptional(filterConfigMap)
                        .map(map -> new MatchFilter(mutator.getMutatorId(),
                                map)).orElse(null));
    }

    public static MutatorProcessor build(int workers,
            MutatorInterface mutator) {
        return build(workers, mutator, null);
    }

    public static MutatorProcessor build(MutatorInterface mutator) {
        return build(OS.getAvailableProcessors(), mutator);
    }

}
