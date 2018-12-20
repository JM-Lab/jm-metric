package kr.jm.metric.config.mutator;

import kr.jm.metric.config.mutator.field.FieldConfig;
import kr.jm.metric.mutator.DelimiterMutator;
import lombok.ToString;

@ToString(callSuper = true)
public class DelimiterMutatorConfig extends AbstractMutatorConfig {
    private String delimiterRegex;
    private String discardRegex;

    protected DelimiterMutatorConfig() {
    }

    public DelimiterMutatorConfig(String mutatorId) {
        this(mutatorId, "");
    }

    public DelimiterMutatorConfig(String mutatorId,
            String delimiterRegex) {
        this(mutatorId, delimiterRegex, "");
    }

    public DelimiterMutatorConfig(String mutatorId, String[] fields) {
        this(mutatorId, "", fields);
    }

    public DelimiterMutatorConfig(String mutatorId, FieldConfig fieldConfig,
            String... fields) {
        this(mutatorId, null, fieldConfig, fields);
    }

    public DelimiterMutatorConfig(String mutatorId, String delimiterRegex,
            String[] fields) {
        this(mutatorId, delimiterRegex, "", fields);
    }

    public DelimiterMutatorConfig(String mutatorId, String delimiterRegex,
            FieldConfig fieldConfig, String... fields) {
        this(mutatorId, delimiterRegex, null, fieldConfig, fields);
    }

    public DelimiterMutatorConfig(String mutatorId, String delimiterRegex,
            String discardRegex) {
        this(mutatorId, delimiterRegex, discardRegex, null);
    }

    public DelimiterMutatorConfig(String mutatorId, String delimiterRegex,
            String discardRegex, String[] fields) {
        this(mutatorId, delimiterRegex, discardRegex, null, fields);
    }

    public DelimiterMutatorConfig(String mutatorId, String delimiterRegex,
            String discardRegex, FieldConfig fieldConfig,
            String... fields) {
        this(mutatorId, MutatorConfigType.DELIMITER, delimiterRegex,
                discardRegex, fieldConfig, fields);
    }

    protected DelimiterMutatorConfig(String mutatorId,
            MutatorConfigType mutatorConfigType, String delimiterRegex,
            String discardRegex, FieldConfig fieldConfig, String... fields) {
        super(mutatorId, mutatorConfigType, fieldConfig, fields);
        this.delimiterRegex = delimiterRegex;
        this.discardRegex = discardRegex;
    }

    public void setFields(String... fields) {
        this.fields = fields;
    }

    public String getDelimiterRegex() {return this.delimiterRegex;}

    public String getDiscardRegex() {return this.discardRegex;}

    @Override
    public DelimiterMutator buildMutator() {
        return new DelimiterMutator(this);
    }
}
