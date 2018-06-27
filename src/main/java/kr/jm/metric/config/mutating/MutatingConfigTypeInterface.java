package kr.jm.metric.config.mutating;

import kr.jm.metric.config.ConfigTypeInterface;
import kr.jm.metric.mutating.builder.FieldMapBuilderInterface;

/**
 * The interface Metric properties type interface.
 */
public interface MutatingConfigTypeInterface extends ConfigTypeInterface {
    /**
     * Gets field map builder.
     *
     * @param <P> the type parameter
     * @return the field map builder
     */
    <P extends FieldMapBuilderInterface> P getFieldMapBuilder();

}
