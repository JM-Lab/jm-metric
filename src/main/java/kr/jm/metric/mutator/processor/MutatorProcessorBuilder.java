package kr.jm.metric.mutator.processor;

import kr.jm.metric.config.mutator.MutatorConfigInterface;
import kr.jm.metric.config.mutator.field.FieldConfig;
import kr.jm.metric.config.mutator.field.FilterConfig;
import kr.jm.metric.mutator.MutatorInterface;
import kr.jm.metric.mutator.RawFieldMapMutator;
import kr.jm.utils.helper.JMOptional;

import java.util.Map;
import java.util.Optional;

/**
 * The type Mutator processor builder.
 */
public class MutatorProcessorBuilder {

    /**
     * Build raw mutator processor.
     *
     * @return the mutator processor
     */
    public static MutatorProcessor buildRaw() {
        return build(new RawFieldMapMutator());
    }

    /**
     * Build mutator processor.
     *
     * @param mutatorConfig the mutator config
     * @return the mutator processor
     */
    public static MutatorProcessor build(MutatorConfigInterface mutatorConfig) {
        return build(mutatorConfig.getWorkers(), mutatorConfig.buildMutator(),
                Optional.ofNullable(mutatorConfig.getFieldConfig())
                        .map(FieldConfig::getFilter).orElse(null));
    }

    public static MutatorProcessor build(int workers,
            MutatorInterface mutator) {
        return new MutatorProcessor(workers, mutator);
    }

    public static MutatorProcessor build(int workers, MutatorInterface mutator,
            Map<String, FilterConfig> filterConfigMap) {
        return new MutatorProcessor(workers, mutator,
                JMOptional.getOptional(filterConfigMap)
                        .map(map -> new MatchFilter(mutator.getMutatorId(),
                                map)).orElse(null));
    }

    /**
     * Build mutator processor.
     *
     * @param mutator the mutator
     * @return the mutator processor
     */
    public static MutatorProcessor build(MutatorInterface mutator) {
        return new MutatorProcessor(mutator);
    }

}
