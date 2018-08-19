package kr.jm.metric.mutator;

import kr.jm.metric.config.mutator.AbstractMutatorConfig;
import kr.jm.metric.data.FieldMap;

import java.util.Map;

/**
 * The interface Field map mutator interface.
 *
 * @param <C> the type parameter
 */
public interface FieldMapMutatorInterface<C extends AbstractMutatorConfig> {

    /**
     * Build field object map map.
     *
     * @param inputConfig  the input config
     * @param targetString the target string
     * @return the map
     */
    Map<String, Object> buildFieldObjectMap(C inputConfig,
            String targetString);

    /**
     * Build field map field map.
     *
     * @param targetString the target string
     * @return the field map
     */
    FieldMap buildFieldMap(String targetString);

}
