package kr.jm.metric.output;

import kr.jm.metric.config.output.KafkaOutputConfig;
import kr.jm.metric.data.ConfigIdTransfer;
import kr.jm.metric.data.FieldMap;
import kr.jm.utils.helper.JMOptional;
import kr.jm.utils.kafka.client.JMKafkaProducer;
import lombok.Getter;

import java.util.List;
import java.util.Properties;

public class KafkaOutput extends AbstractOutput {

    private JMKafkaProducer kafkaProducer;
    @Getter
    private String topic;
    @Getter
    private String keyField;

    /**
     * Instantiates a new Abstract output.
     *
     * @param outputConfig the output properties
     */
    public KafkaOutput(KafkaOutputConfig outputConfig) {
        super(outputConfig);
        this.topic = outputConfig.getTopic();
        this.keyField = outputConfig.getKeyField();
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
    public void writeData(List<ConfigIdTransfer<FieldMap>> data) {
        data.stream().map(ConfigIdTransfer::getData).forEach(this::writeData);
    }

    private void writeData(FieldMap fieldMap) {
        this.kafkaProducer.sendJsonString(
                JMOptional.getOptional(fieldMap, keyField).map(Object::toString)
                        .orElseGet(() -> logNoKeyFieldAndNull(fieldMap)),
                fieldMap);
    }

    private String logNoKeyFieldAndNull(FieldMap fieldMap) {
        log.warn("No KeyField !!! - keyField = {}, " +
                "fieldMap = {}", keyField, fieldMap);
        return null;
    }
}
