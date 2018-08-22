package kr.jm.metric.config.mutator;

import kr.jm.metric.config.ConfigInterface;
import kr.jm.metric.config.mutator.field.FieldConfig;
import kr.jm.metric.mutator.MutatorInterface;

/**
 * The interface Mutator config interface.
 */
public interface MutatorConfigInterface extends ConfigInterface {

    /**
     * Build mutator mutator interface.
     *
     * @return the mutator interface
     */
    MutatorInterface buildMutator();

    /**
     * Get fields string [ ].
     *
     * @return the string [ ]
     */
    String[] getFields();

    /**
     * Gets mutator id.
     *
     * @return the mutator id
     */
    String getMutatorId();

    /**
     * Gets mutator config type.
     *
     * @return the mutator config type
     */
    MutatorConfigType getMutatorConfigType();

    /**
     * Gets field config.
     *
     * @return the field config
     */
    FieldConfig getFieldConfig();

    /**
     * Gets chunk type.
     *
     * @return the chunk type
     */
    ChunkType getChunkType();

    int getWorkers();
}
