package kr.jm.metric.config.mutator;

import kr.jm.metric.config.mutator.field.FieldConfig;
import kr.jm.metric.mutator.KeyValueDelimiterMutator;
import kr.jm.utils.helper.JMOptional;
import lombok.ToString;

/**
 * The type Key value delimiter mutator config.
 */
@ToString(callSuper = true)
public class KeyValueDelimiterMutatorConfig extends DelimiterMutatorConfig {
    /**
     * The constant DefaultKeyValueDelimiterRegex.
     */
    public static final String DefaultKeyValueDelimiterRegex = "=";
    private String keyValueDelimiterRegex;

    /**
     * Instantiates a new Key value delimiter mutator config.
     */
    protected KeyValueDelimiterMutatorConfig() {
    }

    /**
     * Instantiates a new Key value delimiter mutator config.
     *
     * @param mutatorId the mutator id
     */
    public KeyValueDelimiterMutatorConfig(String mutatorId) {
        this(mutatorId, DefaultKeyValueDelimiterRegex);
    }

    /**
     * Instantiates a new Key value delimiter mutator config.
     *
     * @param mutatorId   the mutator id
     * @param fieldConfig the field config
     */
    public KeyValueDelimiterMutatorConfig(String mutatorId,
            FieldConfig fieldConfig) {
        this(mutatorId, DefaultKeyValueDelimiterRegex, fieldConfig);
    }

    /**
     * Instantiates a new Key value delimiter mutator config.
     *
     * @param mutatorId              the mutator id
     * @param keyValueDelimiterRegex the key value delimiter regex
     */
    public KeyValueDelimiterMutatorConfig(String mutatorId,
            String keyValueDelimiterRegex) {
        this(mutatorId, keyValueDelimiterRegex, null, null);
    }

    /**
     * Instantiates a new Key value delimiter mutator config.
     *
     * @param mutatorId              the mutator id
     * @param keyValueDelimiterRegex the key value delimiter regex
     * @param fieldConfig            the field config
     */
    public KeyValueDelimiterMutatorConfig(String mutatorId,
            String keyValueDelimiterRegex, FieldConfig fieldConfig) {
        this(mutatorId, keyValueDelimiterRegex, null, null, fieldConfig);
    }

    /**
     * Instantiates a new Key value delimiterRegex mutator config.
     *
     * @param mutatorId              the mutator id
     * @param keyValueDelimiterRegex the key value delimiterRegex regex
     * @param delimiterRegex         the delimiterRegex
     */
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

    /**
     * Instantiates a new Key value delimiterRegex mutator config.
     *
     * @param mutatorId              the mutator id
     * @param keyValueDelimiterRegex the key value delimiterRegex regex
     * @param delimiterRegex         the delimiterRegex
     * @param discardRegex           the discard regex
     * @param fieldConfig            the field config
     */
    public KeyValueDelimiterMutatorConfig(String mutatorId,
            String keyValueDelimiterRegex, String delimiterRegex,
            String discardRegex, FieldConfig fieldConfig) {
        super(mutatorId, MutatorConfigType.KEY_VALUE_DELIMITER, delimiterRegex,
                discardRegex, fieldConfig);
        this.keyValueDelimiterRegex =
                JMOptional.getOptional(keyValueDelimiterRegex)
                        .orElse(DefaultKeyValueDelimiterRegex);
    }

    /**
     * Gets key value delimiter regex.
     *
     * @return the key value delimiter regex
     */
    public String getKeyValueDelimiterRegex() {return this.keyValueDelimiterRegex;}

    @Override
    public KeyValueDelimiterMutator buildMutator() {
        return new KeyValueDelimiterMutator(this);
    }
}
