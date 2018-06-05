package kr.jm.metric.builder;

import kr.jm.metric.config.mutating.MutatingConfig;
import kr.jm.metric.data.FieldMap;

import java.util.Map;

/**
 * The interface Field map builder interface.
 *
 * @param <C> the type parameter
 */
public interface FieldMapBuilderInterface<C extends MutatingConfig> {

    /**
     * Build field object map map.
     *
     * @param inputConfig  the input properties
     * @param targetString the target string
     * @return the map
     */
    Map<String, Object> buildFieldObjectMap(C inputConfig,
            String targetString);

    /**
     * Build field map field map.
     *
     * @param inputConfig  the input properties
     * @param targetString the target string
     * @return the field map
     */
    FieldMap buildFieldMap(C inputConfig, String targetString);

}
