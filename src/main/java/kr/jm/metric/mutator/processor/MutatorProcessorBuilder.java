package kr.jm.metric.mutator.processor;

import kr.jm.metric.config.mutator.MutatorConfigInterface;
import kr.jm.metric.mutator.MutatorInterface;
import kr.jm.metric.mutator.RawFieldMapMutator;

import java.util.concurrent.Executor;

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
     * @param executor the executor
     * @param mutator  the mutator
     * @return the mutator processor
     */
    public static MutatorProcessor build(Executor executor,
            MutatorInterface mutator) {
        return new MutatorProcessor(executor, mutator);
    }

    /**
     * Build mutator processor.
     *
     * @param executor          the executor
     * @param maxBufferCapacity the max buffer capacity
     * @param mutator           the mutator
     * @return the mutator processor
     */
    public static MutatorProcessor build(Executor executor,
            int maxBufferCapacity, MutatorInterface mutator) {
        return new MutatorProcessor(executor, maxBufferCapacity, mutator);
    }

    /**
     * Build mutator processor.
     *
     * @param executor      the executor
     * @param mutatorConfig the mutator config
     * @return the mutator processor
     */
    public static MutatorProcessor build(Executor executor,
            MutatorConfigInterface mutatorConfig) {
        return build(executor, mutatorConfig.buildMutator());
    }

    /**
     * Build mutator processor.
     *
     * @param executor          the executor
     * @param maxBufferCapacity the max buffer capacity
     * @param mutatorConfig     the mutator config
     * @return the mutator processor
     */
    public static MutatorProcessor build(Executor executor,
            int maxBufferCapacity, MutatorConfigInterface mutatorConfig) {
        return build(executor, maxBufferCapacity, mutatorConfig.buildMutator());
    }

    /**
     * Build mutator processor.
     *
     * @param mutatorConfig the mutator config
     * @return the mutator processor
     */
    public static MutatorProcessor build(MutatorConfigInterface mutatorConfig) {
        return build(mutatorConfig.buildMutator());
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
