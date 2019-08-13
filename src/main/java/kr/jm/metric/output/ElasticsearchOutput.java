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
    private Optional<String> indexSuffixDateFormatAsOpt;
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
        this.indexSuffixDateFormatAsOpt = JMOptional.getOptional(outputConfig.getIndexSuffixDateFormat());
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

    private String buildIndexSuffixDate(long timestamp, String dateFormat) {
        return JMTimeUtil.getTime(timestamp, dateFormat, this.zoneId);
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
                buildIndexWithSuffix(data, timestamp), TYPE);
    }

    private String buildIndexWithSuffix(Map<String, Object> data, long timestamp) {
        return indexFieldAsOpt
                .map(indexField -> buildIndexWithIndexField(timestamp,
                        JMOptional.getOptional(data, indexField).map(Object::toString).orElse("n_a")))
                .orElseGet(() -> buildIndexWithoutIndexField(timestamp));
    }

    private String buildIndexWithoutIndexField(long timestamp) {
        return this.indexSuffixDateFormatAsOpt.map(suffixDateFormat -> indexCache
                .getOrPutGetNew(JMString.EMPTY, suffixDateFormat,
                        () -> indexPrefix + JMString.HYPHEN + buildIndexSuffixDate(timestamp, suffixDateFormat)))
                .orElse(indexPrefix);
    }

    private String buildIndexWithIndexField(long timestamp, String indexFieldValue) {
        return indexCache.getOrPutGetNew(indexFieldValue, indexFieldValue,
                () -> indexPrefix + JMString.HYPHEN + indexFieldValue.toLowerCase() +
                        extractSuffixDateFormat(timestamp, indexFieldValue));
    }

    private String extractSuffixDateFormat(long timestamp, String indexFieldValue) {
        return JMOptional.getOptional(indexSuffixDateFormatMap, indexFieldValue)
                .map(dateFormat -> JMString.HYPHEN + buildIndexSuffixDate(timestamp, dateFormat)).orElseGet(
                        () -> this.indexSuffixDateFormatAsOpt.map(indexSuffixDateFormat -> JMString.HYPHEN +
                                buildIndexSuffixDate(timestamp, indexSuffixDateFormat)).orElse(JMString.EMPTY));
    }

}
