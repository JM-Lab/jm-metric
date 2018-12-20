package kr.jm.metric.config.mutator;

import kr.jm.metric.config.mutator.field.FieldConfig;
import kr.jm.metric.mutator.FormattedMutator;
import kr.jm.utils.datastructure.JMArrays;
import lombok.Getter;
import lombok.ToString;

import java.util.Collections;
import java.util.Map;

@Getter
@ToString(callSuper = true)
public class FormattedMutatorConfig extends AbstractMutatorConfig {
    protected boolean isWordValueRegex;
    protected String format;
    protected Map<String, String> fieldNameMap;

    protected FormattedMutatorConfig() {
    }

    public FormattedMutatorConfig(String mutatorId, String format) {
        this(mutatorId, true, format, Collections.emptyMap());
    }

    public FormattedMutatorConfig(String mutatorId, boolean isWordValueRegex,
            String format) {
        this(mutatorId, isWordValueRegex, format, Collections.emptyMap());
    }

    public FormattedMutatorConfig(String mutatorId, boolean isWordValueRegex,
            String format, FieldConfig fieldConfig) {
        this(mutatorId, isWordValueRegex, format, Collections.emptyMap(),
                fieldConfig);
    }

    public FormattedMutatorConfig(String mutatorId, boolean isWordValueRegex,
            String format, Map<String, String> fieldNameMap) {
        this(mutatorId, isWordValueRegex, format, fieldNameMap, null);
    }

    public FormattedMutatorConfig(String mutatorId, boolean isWordValueRegex,
            String format,
            Map<String, String> fieldNameMap, FieldConfig fieldConfig) {
        this(mutatorId, MutatorConfigType.FORMATTED, isWordValueRegex, format,
                fieldNameMap, fieldConfig);
    }

    public FormattedMutatorConfig(String mutatorId,
            MutatorConfigType mutatorConfigType, boolean isWordValueRegex,
            String format, FieldConfig fieldConfig) {
        this(mutatorId, mutatorConfigType, isWordValueRegex, format,
                Collections.emptyMap(), fieldConfig);
    }

    public FormattedMutatorConfig(String mutatorId,
            MutatorConfigType mutatorConfigType, boolean isWordValueRegex,
            String format, Map<String, String> fieldNameMap,
            FieldConfig fieldConfig) {
        super(mutatorId, mutatorConfigType, fieldConfig);
        this.isWordValueRegex = isWordValueRegex;
        this.format = format;
        this.fieldNameMap = fieldNameMap;
    }

    @Override
    public FormattedMutator buildMutator() {
        return new FormattedMutator(this);
    }

    @Override
    public String[] getFields() {
        return JMArrays.toArray(buildMutator().getFieldList());
    }

}
