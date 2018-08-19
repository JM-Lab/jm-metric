package kr.jm.metric.mutator;

import kr.jm.metric.config.mutator.AbstractMutatorConfig;
import kr.jm.metric.config.mutator.field.FieldConfig;
import kr.jm.metric.data.FieldMap;
import kr.jm.utils.exception.JMExceptionManager;
import lombok.Getter;
import org.slf4j.Logger;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static kr.jm.metric.config.mutator.field.FieldConfig.RAW_DATA;

/**
 * The type Abstract field map mutator.
 *
 * @param <C> the type parameter
 */
public abstract class AbstractFieldMapMutator<C extends AbstractMutatorConfig> implements
        MutatorInterface {

    /**
     * The Log.
     */
    protected Logger log = org.slf4j.LoggerFactory.getLogger(getClass());
    /**
     * The Mutator id.
     */
    @Getter
    protected String mutatorId;
    /**
     * The Mutator config.
     */
    protected C mutatorConfig;
    /**
     * The Field config.
     */
    protected FieldConfig fieldConfig;
    /**
     * The Field meta.
     */
    @Getter
    protected Map<String, Object> fieldMeta;

    /**
     * Instantiates a new Abstract field map mutator.
     *
     * @param mutatorConfig the mutator config
     */
    public AbstractFieldMapMutator(C mutatorConfig) {
        this.mutatorConfig = mutatorConfig;
        this.mutatorId = mutatorConfig.getMutatorId();
        this.fieldConfig = this.mutatorConfig.getFieldConfig();
        Optional.ofNullable(this.fieldConfig).ifPresent(
                fieldConfig -> this.fieldMeta =
                        fieldConfig.extractFieldMetaMap());
    }

    /**
     * Gets config.
     *
     * @return the config
     */
    public Map<String, Object> getConfig() {
        return mutatorConfig.extractConfigMap();
    }

    @Override
    public FieldMap mutate(String targetString) {
        try {
            return buildFieldMapWithRawData(buildFieldObjectMap(targetString),
                    targetString);
        } catch (Exception e) {
            return JMExceptionManager
                    .handleExceptionAndReturnNull(log, e, "mutate",
                            mutatorConfig, targetString);
        }
    }

    private FieldMap buildFieldMapWithRawData(
            Map<String, Object> fieldObjectMap, String targetString) {
        if (Objects.nonNull(this.fieldConfig)) {
            fieldObjectMap.put(RAW_DATA, targetString);
            fieldObjectMap = this.fieldConfig.applyConfig(fieldObjectMap);
        }
        return new FieldMap(fieldObjectMap);
    }

    @Override
    public String toString() {
        return "AbstractFieldMapMutator{" + "mutatorId='" + mutatorId + '\'' +
                ", mutatorConfig=" + mutatorConfig + ", fieldConfig=" +
                fieldConfig + ", fieldMeta=" + fieldMeta + '}';
    }
}
