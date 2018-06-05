package kr.jm.metric.config.mutating;

import com.fasterxml.jackson.core.type.TypeReference;
import kr.jm.metric.builder.FieldMapBuilderInterface;
import kr.jm.utils.helper.JMJson;

/**
 * The interface Metric properties type interface.
 */
public interface MutatingConfigTypeInterface {
    /**
     * Gets field map builder.
     *
     * @param <P> the type parameter
     * @return the field map builder
     */
    <P extends FieldMapBuilderInterface> P getFieldMapBuilder();

    /**
     * Gets type reference.
     *
     * @param <T> the type parameter
     * @return the type reference
     */
    <T extends MutatingConfig> TypeReference<T> getTypeReference();

    /**
     * Transform t.
     *
     * @param <T>          the type parameter
     * @param metricConfig the metric properties
     * @return the t
     */
    default <T extends MutatingConfig> T transform(Object metricConfig) {
        return JMJson.transform(metricConfig, getTypeReference());
    }
}
