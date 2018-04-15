package kr.jm.metric.builder;

import kr.jm.metric.config.MetricConfig;
import kr.jm.metric.config.field.FieldConfig;
import kr.jm.metric.data.FieldMap;
import org.slf4j.Logger;

import java.util.Map;
import java.util.Optional;

import static java.util.Optional.ofNullable;
import static kr.jm.metric.config.field.FieldConfig.RAW_DATA;

/**
 * The type Abstract field map builder.
 *
 * @param <C> the type parameter
 */
public abstract class AbstractFieldMapBuilder<C extends MetricConfig> implements
        FieldMapBuilderInterface<C> {

    /**
     * The Log.
     */
    protected final Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

    public FieldMap buildFieldMap(C inputConfig, String targetString) {
        return buildFieldMapWithRawData(
                buildWithFieldConfig(ofNullable(inputConfig.getFieldConfig()),
                        buildFieldObjectMap(inputConfig, targetString)),
                targetString);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private Map<String, Object> buildWithFieldConfig(
            Optional<FieldConfig> fieldDataConfigAsOpt,
            Map<String, Object> fieldObjectMap) {
        return fieldDataConfigAsOpt.map(fieldDataConfig -> fieldDataConfig
                .applyConfig(fieldObjectMap)).orElse(fieldObjectMap);
    }

    private FieldMap buildFieldMapWithRawData(
            Map<String, Object> fieldObjectMap, String targetString) {
        fieldObjectMap.put(RAW_DATA, targetString);
        return new FieldMap(fieldObjectMap);
    }
}
