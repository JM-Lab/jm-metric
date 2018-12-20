package kr.jm.metric.config.output;


import kr.jm.metric.output.KafkaOutput;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class KafkaOutputConfig extends AbstractOutputConfig {

    private String bootstrapServers;
    private String keyField;
    private String producerId;
    private String topic;
    private Integer retries, batchSize, bufferMemory, lingerMs;

    public KafkaOutputConfig(String outputId, String bootstrapServers,
            String keyField, String topic) {
        this(outputId, bootstrapServers, keyField,
                "KafkaOutput-" + System.currentTimeMillis(), topic);
    }

    public KafkaOutputConfig(String outputId, String bootstrapServers,
            String keyField, String producerId, String topic) {
        this(outputId, bootstrapServers, keyField, producerId, topic, null,
                null, null, null);
    }

    public KafkaOutputConfig(String outputId, String bootstrapServers,
            String keyField, String producerId, String topic, Integer retries,
            Integer batchSize, Integer bufferMemory, Integer lingerMs) {
        super(outputId);
        this.bootstrapServers = bootstrapServers;
        this.keyField = keyField;
        this.producerId = producerId;
        this.topic = topic;
        this.retries = retries;
        this.batchSize = batchSize;
        this.bufferMemory = bufferMemory;
        this.lingerMs = lingerMs;
    }

    @Override
    public OutputConfigType getOutputConfigType() {
        return OutputConfigType.KAFKA;
    }

    @Override
    public KafkaOutput buildOutput() {
        return new KafkaOutput(this);
    }
}
