package kr.jm.metric.mutator.processor;

import kr.jm.metric.config.mutator.MutatorConfigInterface;
import kr.jm.metric.mutator.MutatorInterface;
import kr.jm.metric.mutator.RawFieldMapMutator;

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
        return build(mutatorConfig.getWorkers(), mutatorConfig.buildMutator());
    }

    public static MutatorProcessor build(int workers,
            MutatorInterface mutator) {
        return new MutatorProcessor(workers, mutator);
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
