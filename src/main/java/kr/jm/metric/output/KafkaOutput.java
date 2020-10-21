package kr.jm.metric.output;

import kr.jm.metric.config.output.KafkaOutputConfig;
import kr.jm.metric.data.Transfer;
import kr.jm.utils.JMOptional;
import kr.jm.utils.kafka.client.JMKafkaProducer;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.Map;
import java.util.Properties;

@ToString(callSuper = true)
public class KafkaOutput extends AbstractOutput {

    private final JMKafkaProducer kafkaProducer;
    @Getter
    private final String topic;
    @Getter
    private final String keyField;

    public KafkaOutput(KafkaOutputConfig outputConfig) {
        super(outputConfig);
        this.topic = outputConfig.getTopic();
        this.keyField = JMOptional.getOptional(outputConfig.getKeyField())
                .orElse(null);
        this.kafkaProducer = new JMKafkaProducer(buildProperties(outputConfig))
                .withDefaultTopic(topic);
    }

    private Properties buildProperties(KafkaOutputConfig outputConfig) {
        Properties properties = JMKafkaProducer
                .buildProperties(outputConfig.getBootstrapServers(),
                        outputConfig.getProducerId(), outputConfig.getRetries(),
                        outputConfig.getBatchSize(),
                        outputConfig.getBufferMemory(),
                        outputConfig.getLingerMs());
        properties.putAll(outputConfig.getProperties());
        return properties;
    }

    @Override
    protected void closeImpl() {
        this.kafkaProducer.close();
    }

    @Override
    public void writeData(List<Transfer<Map<String, Object>>> transferList) {
        transferList.stream().map(Transfer::getData).forEach(this::writeData);
    }

    private void writeData(Map<String, Object> fieldMap) {
        this.kafkaProducer.sendJsonString(
                JMOptional.getOptional(keyField).map(fieldMap::get)
                        .map(Object::toString).orElse(null), fieldMap);
    }
}
