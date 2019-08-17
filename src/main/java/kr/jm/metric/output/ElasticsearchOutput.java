package kr.jm.metric.output;

import kr.jm.metric.config.output.ElasticsearchOutputConfig;
import kr.jm.metric.data.Transfer;
import kr.jm.utils.collections.JMNestedMap;
import kr.jm.utils.elasticsearch.JMElasticsearchClient;
import kr.jm.utils.helper.JMOptional;
import kr.jm.utils.helper.JMString;
import kr.jm.utils.time.JMTimeUtil;
import lombok.Getter;
import lombok.ToString;
import org.elasticsearch.common.settings.Settings;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@ToString(callSuper = true)
@Getter
public class ElasticsearchOutput extends AbstractOutput {

    private static final String TYPE = "doc";
    private String zoneId;
    private String indexPrefix;
    private Optional<String> indexFieldAsOpt;
    // default suffixDateFormat
    private String indexSuffixDateFormat;
    // dynamic suffixDateFormat by indexField's value
    private Map<String, String> indexSuffixDateFormatMap;

    private JMNestedMap<Object, String, String> indexCache;

    protected JMElasticsearchClient elasticsearchClient;

    public ElasticsearchOutput() {
        this(new ElasticsearchOutputConfig());
    }

    public ElasticsearchOutput(ElasticsearchOutputConfig outputConfig) {
        super(outputConfig);
        this.zoneId = outputConfig.getZoneId();
        this.indexPrefix = outputConfig.getIndexPrefix();
        this.indexFieldAsOpt = JMOptional.getOptional(outputConfig.getIndexField());
        this.indexSuffixDateFormat = outputConfig.getIndexSuffixDateFormat();
        this.indexSuffixDateFormatMap = outputConfig.getIndexSuffixDateFormatMap();
        this.elasticsearchClient = new JMElasticsearchClient(outputConfig.getElasticsearchConnect(), buildSettings(
                JMElasticsearchClient
                        .getSettingsBuilder(outputConfig.getNodeName(), outputConfig.isClientTransportSniff(),
                                outputConfig.getClusterName()), outputConfig.getProperties()));
        this.elasticsearchClient.setBulkProcessor(outputConfig.getBulkActions(), outputConfig.getBulkSizeKB(),
                outputConfig.getFlushIntervalSeconds());
        this.indexCache = new JMNestedMap<>(true);
    }

    private static Settings buildSettings(Settings.Builder settingsBuilder, Map<String, Object> esClientConfig) {
        esClientConfig.forEach((key, value) -> settingsBuilder.put(key, value.toString()));
        return settingsBuilder.build();
    }

    @Override
    protected void closeImpl() {
        this.elasticsearchClient.close();
    }

    @Override
    public void writeData(List<Transfer<Map<String, Object>>> transferList) {
        for (Transfer<Map<String, Object>> inputIdTransfer : transferList)
            writeData(inputIdTransfer.getData(), inputIdTransfer.getTimestamp());
    }

    private void writeData(Map<String, Object> data, long timestamp) {
        this.elasticsearchClient.sendWithBulkProcessorAndObjectMapper(data,
                buildIndex(data, timestamp), TYPE);
    }

    String buildIndex(Map<String, Object> data, long timestamp) {
        return buildIndex(timestamp, indexFieldAsOpt
                .map(indexField -> JMOptional.getOptional(data, indexField).map(Object::toString).orElse("n_a")));
    }

    private String buildIndex(long timestamp, Optional<String> indexFieldValueAsOpt) {
        return buildIndex(indexFieldValueAsOpt, buildIndexSuffixDate(timestamp, indexFieldValueAsOpt));
    }

    private String buildIndexSuffixDate(long timestamp, Optional<String> indexFieldValueAsOpt) {
        return JMTimeUtil.getTime(timestamp, indexFieldValueAsOpt
                .flatMap(indexFieldValue -> JMOptional.getOptional(indexSuffixDateFormatMap, indexFieldValue))
                .orElse(indexSuffixDateFormat), this.zoneId);
    }

    private String buildIndex(Optional<String> indexFieldValueAsOpt, String indexSuffixDate) {
        return indexCache.getOrPutGetNew(indexFieldValueAsOpt.orElse(JMString.EMPTY), indexSuffixDate,
                () -> indexPrefix +
                        indexFieldValueAsOpt.map(indexFieldValue -> JMString.HYPHEN + indexFieldValue.toLowerCase())
                                .orElse(JMString.EMPTY) + JMString.HYPHEN + indexSuffixDate);
    }
}
