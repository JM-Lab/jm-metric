package kr.jm.metric.output;

import kr.jm.metric.config.output.KafkaOutputConfig;
import kr.jm.metric.data.FieldMap;
import kr.jm.metric.data.Transfer;
import kr.jm.utils.helper.JMOptional;
import kr.jm.utils.kafka.client.JMKafkaProducer;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.Properties;

@ToString(callSuper = true)
public class KafkaOutput extends AbstractOutput {

    private JMKafkaProducer kafkaProducer;
    @Getter
    private String topic;
    @Getter
    private String keyField;

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
    public void writeData(List<Transfer<FieldMap>> transferList) {
        transferList.stream().map(Transfer::getData).forEach(this::writeData);
    }

    private void writeData(FieldMap fieldMap) {
        this.kafkaProducer.sendJsonString(
                JMOptional.getOptional(keyField).map(fieldMap::get)
                        .map(Object::toString).orElse(null), fieldMap);
    }
}
