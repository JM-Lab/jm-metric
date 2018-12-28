package kr.jm.metric.mutator;

import kr.jm.metric.config.mutator.AbstractMutatorConfig;
import kr.jm.metric.config.mutator.field.FieldConfig;
import kr.jm.utils.exception.JMExceptionManager;
import kr.jm.utils.helper.JMLambda;
import lombok.Getter;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static kr.jm.metric.config.mutator.field.FieldConfig.RAW_DATA;

public abstract class AbstractMutator<C extends AbstractMutatorConfig> implements
        MutatorInterface {

    protected Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

    @Getter
    protected String mutatorId;
    protected C mutatorConfig;
    protected FieldConfig fieldConfig;
    @Getter
    protected Map<String, Object> fieldMeta;

    private FieldConfigHandler fieldConfigHandler;

    public AbstractMutator(C mutatorConfig) {
        this.mutatorConfig = mutatorConfig;
        this.mutatorId = mutatorConfig.getMutatorId();
        this.fieldConfig = this.mutatorConfig.getFieldConfig();
        Optional.ofNullable(this.fieldConfig).ifPresent(
                fieldConfig -> this.fieldMeta =
                        fieldConfig.extractFieldMetaMap());
    }

    public Map<String, Object> getConfig() {
        return mutatorConfig.extractConfigMap();
    }

    @Override
    public Map<String, Object> mutate(String targetString) {
        try {
            return buildDataWithRawData(buildFieldObjectMap(targetString),
                    targetString);
        } catch (Exception e) {
            return JMExceptionManager
                    .handleExceptionAndReturnNull(log, e, "mutate",
                            mutatorConfig, targetString);
        }
    }

    private Map<String, Object> buildDataWithRawData(
            Map<String, Object> fieldObjectMap, String targetString) {
        if (Objects.nonNull(this.fieldConfig)) {
            fieldObjectMap.put(RAW_DATA, targetString);
            fieldObjectMap = JMLambda.supplierIfNull(this.fieldConfigHandler,
                    () -> this.fieldConfigHandler =
                            new FieldConfigHandler(this.mutatorId,
                                    this.fieldConfig))
                    .applyFieldConfig(fieldObjectMap);
        }
        return new HashMap<>(fieldObjectMap);
    }

    @Override
    public String toString() {
        return "AbstractMutator{" + "mutatorId='" + mutatorId + '\'' +
                ", mutatorConfig=" + mutatorConfig + ", fieldConfig=" +
                fieldConfig + ", fieldMeta=" + fieldMeta + '}';
    }
}
