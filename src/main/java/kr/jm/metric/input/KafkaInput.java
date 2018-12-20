package kr.jm.metric.input;

import kr.jm.metric.config.input.KafkaInputConfig;
import kr.jm.metric.data.Transfer;
import kr.jm.utils.kafka.client.JMKafkaConsumer;
import lombok.ToString;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.Properties;
import java.util.function.Consumer;

@ToString(callSuper = true)
public class KafkaInput extends AbstractInput<KafkaInputConfig> {

    private JMKafkaConsumer kafkaConsumer;
    private Consumer<Transfer<String>> inputConsumer;

    public KafkaInput(KafkaInputConfig inputConfig) {
        super(inputConfig);
    }

    private Properties buildProperties(KafkaInputConfig inputConfig) {
        Properties properties = JMKafkaConsumer
                .buildProperties(inputConfig.isLatest(),
                        inputConfig.getBootstrapServers(),
                        inputConfig.getGroupId(), 1000);
        properties.putAll(inputConfig.getProperties());
        return properties;
    }

    @Override
    protected void startImpl(Consumer<Transfer<String>> inputConsumer) {
        this.inputConsumer = inputConsumer;
        this.kafkaConsumer =
                new JMKafkaConsumer(buildProperties(inputConfig),
                        this::convertTransfer, inputConfig.getTopics()).start();

    }

    private void convertTransfer(
            ConsumerRecord<String, String> consumerRecord) {
        this.inputConsumer.accept(newTransfer(consumerRecord.value(),
                consumerRecord.timestamp()));
    }

    @Override
    protected void closeImpl() {
        this.kafkaConsumer.shutdown();
    }
}
