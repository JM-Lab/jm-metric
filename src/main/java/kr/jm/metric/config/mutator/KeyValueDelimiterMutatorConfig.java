package kr.jm.metric.config.mutator;

import kr.jm.metric.config.mutator.field.FieldConfig;
import kr.jm.metric.mutator.KeyValueDelimiterMutator;
import kr.jm.utils.JMOptional;
import lombok.ToString;

@ToString(callSuper = true)
public class KeyValueDelimiterMutatorConfig extends DelimiterMutatorConfig {
    public static final String DefaultKeyValueDelimiterRegex = "=";
    private String keyValueDelimiterRegex;

    protected KeyValueDelimiterMutatorConfig() {
    }

    public KeyValueDelimiterMutatorConfig(String mutatorId) {
        this(mutatorId, DefaultKeyValueDelimiterRegex);
    }

    public KeyValueDelimiterMutatorConfig(String mutatorId,
            FieldConfig fieldConfig) {
        this(mutatorId, DefaultKeyValueDelimiterRegex, fieldConfig);
    }

    public KeyValueDelimiterMutatorConfig(String mutatorId,
            String keyValueDelimiterRegex) {
        this(mutatorId, keyValueDelimiterRegex, null, null);
    }

    public KeyValueDelimiterMutatorConfig(String mutatorId,
            String keyValueDelimiterRegex, FieldConfig fieldConfig) {
        this(mutatorId, keyValueDelimiterRegex, null, null, fieldConfig);
    }

    public KeyValueDelimiterMutatorConfig(String mutatorId,
            String keyValueDelimiterRegex, String delimiterRegex) {
        this(mutatorId, keyValueDelimiterRegex, delimiterRegex, null);
    }

    public KeyValueDelimiterMutatorConfig(String mutatorId,
            String keyValueDelimiterRegex, String delimiterRegex,
            String discardRegex) {
        this(mutatorId, keyValueDelimiterRegex, delimiterRegex, discardRegex,
                null);
    }

    public KeyValueDelimiterMutatorConfig(String mutatorId,
            String keyValueDelimiterRegex, String delimiterRegex,
            String discardRegex, FieldConfig fieldConfig) {
        super(mutatorId, MutatorConfigType.KEY_VALUE_DELIMITER, delimiterRegex,
                discardRegex, fieldConfig);
        this.keyValueDelimiterRegex =
                JMOptional.getOptional(keyValueDelimiterRegex)
                        .orElse(DefaultKeyValueDelimiterRegex);
    }

    public String getKeyValueDelimiterRegex() {return this.keyValueDelimiterRegex;}

    @Override
    public KeyValueDelimiterMutator buildMutator() {
        return new KeyValueDelimiterMutator(this);
    }
}
