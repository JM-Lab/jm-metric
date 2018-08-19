package kr.jm.metric.config.mutator;

import kr.jm.metric.config.mutator.field.FieldConfig;
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
        this(mutatorId, fieldConfig, DefaultKeyValueDelimiterRegex);
    }

    /**
     * Instantiates a new Key value delimiter mutator config.
     *
     * @param mutatorId              the mutator id
     * @param keyValueDelimiterRegex the key value delimiter regex
     */
    public KeyValueDelimiterMutatorConfig(String mutatorId,
            String keyValueDelimiterRegex) {
        this(mutatorId, null, keyValueDelimiterRegex, null);
    }

    /**
     * Instantiates a new Key value delimiter mutator config.
     *
     * @param mutatorId              the mutator id
     * @param fieldConfig            the field config
     * @param keyValueDelimiterRegex the key value delimiter regex
     */
    public KeyValueDelimiterMutatorConfig(String mutatorId,
            FieldConfig fieldConfig, String keyValueDelimiterRegex) {
        this(mutatorId, fieldConfig, keyValueDelimiterRegex, null);
    }

    /**
     * Instantiates a new Key value delimiter mutator config.
     *
     * @param mutatorId              the mutator id
     * @param keyValueDelimiterRegex the key value delimiter regex
     * @param delimiter              the delimiter
     */
    public KeyValueDelimiterMutatorConfig(String mutatorId,
            String keyValueDelimiterRegex, String delimiter) {
        this(mutatorId, null, keyValueDelimiterRegex, delimiter);
    }

    /**
     * Instantiates a new Key value delimiter mutator config.
     *
     * @param mutatorId              the mutator id
     * @param fieldConfig            the field config
     * @param keyValueDelimiterRegex the key value delimiter regex
     * @param delimiter              the delimiter
     */
    public KeyValueDelimiterMutatorConfig(String mutatorId,
            FieldConfig fieldConfig, String keyValueDelimiterRegex,
            String delimiter) {
        this(mutatorId, fieldConfig, keyValueDelimiterRegex, delimiter, null);
    }

    /**
     * Instantiates a new Key value delimiter mutator config.
     *
     * @param mutatorId              the mutator id
     * @param fieldConfig            the field config
     * @param keyValueDelimiterRegex the key value delimiter regex
     * @param delimiter              the delimiter
     * @param discardRegex           the discard regex
     */
    public KeyValueDelimiterMutatorConfig(String mutatorId,
            FieldConfig fieldConfig, String keyValueDelimiterRegex,
            String delimiter, String discardRegex) {
        super(mutatorId, MutatorConfigType.KEY_VALUE_DELIMITER, delimiter,
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

}
