package kr.jm.metric.config.input;


import kr.jm.metric.input.KafkaInput;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class KafkaInputConfig extends AbstractInputConfig {

    private String bootstrapServers;
    private String groupId;
    private boolean isLatest;
    private String[] topics;

    public KafkaInputConfig(String inputId, String bootstrapServers,
            String... topics) {
        this(inputId, bootstrapServers, true, topics);
    }

    public KafkaInputConfig(String inputId, String bootstrapServers,
            boolean isLatest, String... topics) {
        this(inputId, null, bootstrapServers, "KafkaInputGroup", isLatest,
                topics);
    }


    public KafkaInputConfig(String inputId, ChunkType chunkType, String
            bootstrapServers,
            String groupId, boolean isLatest, String... topics) {
        super(inputId, chunkType);
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
