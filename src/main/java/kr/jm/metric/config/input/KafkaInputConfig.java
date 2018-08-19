package kr.jm.metric.config.input;


import kr.jm.metric.input.KafkaInput;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * The type Kafka input config.
 */
@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class KafkaInputConfig extends AbstractInputConfig {

    private String bootstrapServers;
    private String groupId;
    private boolean isLatest;
    private String[] topics;

    /**
     * Instantiates a new Kafka input config.
     *
     * @param inputId          the input id
     * @param bootstrapServers the bootstrap servers
     * @param topics           the topics
     */
    public KafkaInputConfig(String inputId, String bootstrapServers,
            String... topics) {
        this(inputId, bootstrapServers, true, topics);
    }

    /**
     * Instantiates a new Kafka input config.
     *
     * @param inputId          the input id
     * @param bootstrapServers the bootstrap servers
     * @param isLatest         the is latest
     * @param topics           the topics
     */
    public KafkaInputConfig(String inputId, String bootstrapServers,
            boolean isLatest, String... topics) {
        this(inputId, bootstrapServers, "KafkaInputGroup", isLatest, topics);
    }


    /**
     * Instantiates a new Kafka input config.
     *
     * @param inputId          the input id
     * @param bootstrapServers the bootstrap servers
     * @param groupId          the group id
     * @param isLatest         the is latest
     * @param topics           the topics
     */
    public KafkaInputConfig(String inputId, String bootstrapServers,
            String groupId, boolean isLatest, String... topics) {
        super(inputId);
        this.bootstrapServers = bootstrapServers;
        this.groupId = groupId;
        this.isLatest = isLatest;
        this.topics = topics;
    }

    @Override
    public InputConfigType getInputConfigType() {
        return InputConfigType.KAFKA;
    }

    @Override
    public KafkaInput buildInput() {
        return new KafkaInput(this);
    }
}
