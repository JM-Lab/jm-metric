package kr.jm.metric.config.mutator;

import kr.jm.metric.config.mutator.field.FieldConfig;
import kr.jm.metric.mutator.FormattedFieldMapMutator;
import kr.jm.utils.datastructure.JMArrays;
import lombok.Getter;
import lombok.ToString;

import java.util.Collections;
import java.util.Map;

/**
 * The type Formatted mutator config.
 */
@Getter
@ToString(callSuper = true)
public class FormattedMutatorConfig extends AbstractMutatorConfig {
    /**
     * The Is word value regex.
     */
    protected boolean isWordValueRegex;
    /**
     * The Format.
     */
    protected String format;
    /**
     * The Field name map.
     */
    protected Map<String, String> fieldNameMap;

    /**
     * Instantiates a new Formatted mutator config.
     */
    protected FormattedMutatorConfig() {
    }

    /**
     * Instantiates a new Formatted mutator config.
     *
     * @param mutatorId the mutator id
     * @param format    the format
     */
    public FormattedMutatorConfig(String mutatorId, String format) {
        this(mutatorId, true, format, Collections.emptyMap());
    }

    /**
     * Instantiates a new Formatted mutator config.
     *
     * @param mutatorId        the mutator id
     * @param isWordValueRegex the is word value regex
     * @param format           the format
     */
    public FormattedMutatorConfig(String mutatorId, boolean isWordValueRegex,
            String format) {
        this(mutatorId, isWordValueRegex, format, Collections.emptyMap());
    }

    /**
     * Instantiates a new Formatted mutator config.
     *
     * @param mutatorId        the mutator id
     * @param isWordValueRegex the is word value regex
     * @param format           the format
     * @param fieldConfig      the field config
     */
    public FormattedMutatorConfig(String mutatorId, boolean isWordValueRegex,
            String format, FieldConfig fieldConfig) {
        this(mutatorId, isWordValueRegex, format, Collections.emptyMap(),
                fieldConfig);
    }

    /**
     * Instantiates a new Formatted mutator config.
     *
     * @param mutatorId        the mutator id
     * @param isWordValueRegex the is word value regex
     * @param format           the format
     * @param fieldNameMap     the field name map
     */
    public FormattedMutatorConfig(String mutatorId, boolean isWordValueRegex,
            String format, Map<String, String> fieldNameMap) {
        this(mutatorId, isWordValueRegex, format, fieldNameMap, null);
    }

    /**
     * Instantiates a new Formatted mutator config.
     *
     * @param mutatorId        the mutator id
     * @param isWordValueRegex the is word value regex
     * @param format           the format
     * @param fieldNameMap     the field name map
     * @param fieldConfig      the field config
     */
    public FormattedMutatorConfig(String mutatorId, boolean isWordValueRegex,
            String format,
            Map<String, String> fieldNameMap, FieldConfig fieldConfig) {
        this(mutatorId, MutatorConfigType.FORMATTED, isWordValueRegex, format,
                fieldNameMap, fieldConfig);
    }

    /**
     * Instantiates a new Formatted mutator config.
     *
     * @param mutatorId         the mutator id
     * @param mutatorConfigType the mutator config type
     * @param isWordValueRegex  the is word value regex
     * @param format            the format
     * @param fieldConfig       the field config
     */
    public FormattedMutatorConfig(String mutatorId,
            MutatorConfigType mutatorConfigType, boolean isWordValueRegex,
            String format, FieldConfig fieldConfig) {
        this(mutatorId, mutatorConfigType, isWordValueRegex, format,
                Collections.emptyMap(), fieldConfig);
    }

    /**
     * Instantiates a new Formatted mutator config.
     *
     * @param mutatorId         the mutator id
     * @param mutatorConfigType the mutator config type
     * @param isWordValueRegex  the is word value regex
     * @param format            the format
     * @param fieldNameMap      the field name map
     * @param fieldConfig       the field config
     */
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
    public FormattedFieldMapMutator buildMutator() {
        return new FormattedFieldMapMutator(this);
    }

    @Override
    public String[] getFields() {
        return JMArrays.toArray(buildMutator().getFieldList(getMutatorId()));
    }

}
