package kr.jm.metric.config.mutator;

import kr.jm.metric.config.mutator.field.FieldConfig;
import kr.jm.metric.mutator.DelimiterFieldMapMutator;
import lombok.ToString;

/**
 * The type Delimiter mutator config.
 */
@ToString(callSuper = true)
public class DelimiterMutatorConfig extends AbstractMutatorConfig {
    private String delimiterRegex;
    private String discardRegex;

    /**
     * Instantiates a new Delimiter mutator config.
     */
    protected DelimiterMutatorConfig() {
    }

    /**
     * Instantiates a new Delimiter mutator config.
     *
     * @param mutatorId the mutator id
     */
    public DelimiterMutatorConfig(String mutatorId) {
        this(mutatorId, "");
    }

    /**
     * Instantiates a new Delimiter mutator config.
     *
     * @param mutatorId      the mutator id
     * @param delimiterRegex the delimiter regex
     */
    public DelimiterMutatorConfig(String mutatorId,
            String delimiterRegex) {
        this(mutatorId, delimiterRegex, "");
    }

    /**
     * Instantiates a new Delimiter mutator config.
     *
     * @param mutatorId the mutator id
     * @param fields    the fields
     */
    public DelimiterMutatorConfig(String mutatorId, String[] fields) {
        this(mutatorId, "", fields);
    }

    /**
     * Instantiates a new Delimiter mutator config.
     *
     * @param mutatorId   the mutator id
     * @param fieldConfig the field config
     * @param fields      the fields
     */
    public DelimiterMutatorConfig(String mutatorId, FieldConfig fieldConfig,
            String... fields) {
        this(mutatorId, null, fieldConfig, fields);
    }

    /**
     * Instantiates a new Delimiter mutator config.
     *
     * @param mutatorId      the mutator id
     * @param delimiterRegex the delimiter regex
     * @param fields         the fields
     */
    public DelimiterMutatorConfig(String mutatorId, String delimiterRegex,
            String[] fields) {
        this(mutatorId, delimiterRegex, "", fields);
    }

    /**
     * Instantiates a new Delimiter mutator config.
     *
     * @param mutatorId      the mutator id
     * @param delimiterRegex the delimiter regex
     * @param fieldConfig    the field config
     * @param fields         the fields
     */
    public DelimiterMutatorConfig(String mutatorId, String delimiterRegex,
            FieldConfig fieldConfig, String... fields) {
        this(mutatorId, delimiterRegex, null, fieldConfig, fields);
    }

    /**
     * Instantiates a new Delimiter mutator config.
     *
     * @param mutatorId      the mutator id
     * @param delimiterRegex the delimiter regex
     * @param discardRegex   the discard regex
     */
    public DelimiterMutatorConfig(String mutatorId, String delimiterRegex,
            String discardRegex) {
        this(mutatorId, delimiterRegex, discardRegex, null);
    }

    /**
     * Instantiates a new Delimiter mutator config.
     *
     * @param mutatorId      the mutator id
     * @param delimiterRegex the delimiter regex
     * @param discardRegex   the discard regex
     * @param fields         the fields
     */
    public DelimiterMutatorConfig(String mutatorId, String delimiterRegex,
            String discardRegex, String[] fields) {
        this(mutatorId, delimiterRegex, discardRegex, null, fields);
    }

    /**
     * Instantiates a new Delimiter mutator config.
     *
     * @param mutatorId      the mutator id
     * @param delimiterRegex the delimiter regex
     * @param discardRegex   the discard regex
     * @param fieldConfig    the field config
     * @param fields         the fields
     */
    public DelimiterMutatorConfig(String mutatorId, String delimiterRegex,
            String discardRegex, FieldConfig fieldConfig,
            String... fields) {
        this(mutatorId, MutatorConfigType.DELIMITER, delimiterRegex,
                discardRegex, fieldConfig, fields);
    }

    /**
     * Instantiates a new Delimiter mutator config.
     *
     * @param mutatorId         the mutator id
     * @param mutatorConfigType the mutator config type
     * @param delimiterRegex    the delimiter regex
     * @param discardRegex      the discard regex
     * @param fieldConfig       the field config
     * @param fields            the fields
     */
    protected DelimiterMutatorConfig(String mutatorId,
            MutatorConfigType mutatorConfigType, String delimiterRegex,
            String discardRegex, FieldConfig fieldConfig, String... fields) {
        super(mutatorId, mutatorConfigType, fieldConfig, fields);
        this.delimiterRegex = delimiterRegex;
        this.discardRegex = discardRegex;
    }

    /**
     * Sets fields.
     *
     * @param fields the fields
     */
    public void setFields(String... fields) {
        this.fields = fields;
    }

    /**
     * Gets delimiter regex.
     *
     * @return the delimiter regex
     */
    public String getDelimiterRegex() {return this.delimiterRegex;}

    /**
     * Gets discard regex.
     *
     * @return the discard regex
     */
    public String getDiscardRegex() {return this.discardRegex;}

    @Override
    public DelimiterFieldMapMutator buildMutator() {
        return new DelimiterFieldMapMutator(this);
    }
}
