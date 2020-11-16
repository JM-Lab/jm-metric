package kr.jm.metric.mutator;

import kr.jm.metric.config.mutator.AbstractMutatorConfig;
import kr.jm.metric.config.mutator.field.FieldConfig;
import kr.jm.utils.JMOptional;
import lombok.Getter;
import org.slf4j.Logger;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static kr.jm.metric.config.mutator.field.FieldConfig.RAW_DATA;

public abstract class AbstractMutator<C extends AbstractMutatorConfig> implements MutatorInterface {

    protected final Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

    @Getter
    protected String mutatorId;
    protected final C mutatorConfig;
    protected final FieldConfig fieldConfig;
    @Getter
    protected Map<String, Object> fieldMeta;

    private FieldConfigHandler fieldConfigHandler;

    public AbstractMutator(C mutatorConfig) {
        this.mutatorConfig = mutatorConfig;
        this.mutatorId = mutatorConfig.getMutatorId();
        this.fieldConfig = this.mutatorConfig.getFieldConfig();
        Optional.ofNullable(this.fieldConfig)
                .ifPresent(fieldConfig -> this.fieldMeta = fieldConfig.extractFieldMetaMap());
    }

    public Map<String, Object> getConfig() {
        return mutatorConfig.extractConfigMap();
    }

    @Override
    public Map<String, Object> mutate(String targetString) {
        return JMOptional.getOptional(buildFieldObjectMap(targetString))
                .map(fieldObjectMap -> buildDataWithRawData(fieldObjectMap, targetString))
                .orElseGet(Collections::emptyMap);
    }

    private Map<String, Object> buildDataWithRawData(Map<String, Object> fieldObjectMap, String targetString) {
        if (Objects.nonNull(this.fieldConfig)) {
            if (this.fieldConfig.isRawData())
                fieldObjectMap.put(RAW_DATA, targetString);
            fieldObjectMap = Objects.requireNonNullElseGet(this.fieldConfigHandler,
                    () -> this.fieldConfigHandler = new FieldConfigHandler(this.mutatorId, this.fieldConfig))
                    .applyFieldConfig(fieldObjectMap);
        }
        return fieldObjectMap;
    }

    @Override
    public String toString() {
        return "AbstractMutator{" + "mutatorId='" + mutatorId + '\'' +
                ", mutatorConfig=" + mutatorConfig + ", fieldConfig=" +
                fieldConfig + ", fieldMeta=" + fieldMeta + '}';
    }
}
