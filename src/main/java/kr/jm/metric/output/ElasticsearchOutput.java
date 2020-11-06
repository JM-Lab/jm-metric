package kr.jm.metric.output;

import kr.jm.metric.config.output.ElasticsearchOutputConfig;
import kr.jm.metric.data.Transfer;
import kr.jm.utils.JMOptional;
import kr.jm.utils.JMString;
import kr.jm.utils.elasticsearch.JMElasticsearchClient;
import kr.jm.utils.helper.JM2DepthMap;
import kr.jm.utils.time.JMTime;
import lombok.Getter;
import lombok.ToString;
import org.elasticsearch.common.settings.Settings;

import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.function.Predicate.not;

@ToString(callSuper = true)
@Getter
public class ElasticsearchOutput extends AbstractOutput {

    private final ZoneId zoneId;
    private final String indexPrefix;
    private final Optional<String> indexFieldOptional;
    private final Optional<String> idFieldOptional;
    // default suffixDateFormat
    private final String indexSuffixDateFormat;
    // dynamic suffixDateFormat by indexField's value
    private final Map<String, String> indexSuffixDateFormatMap;

    private final JM2DepthMap<Object, String, String> indexCache;

    protected JMElasticsearchClient elasticsearchClient;

    public ElasticsearchOutput() {
        this(new ElasticsearchOutputConfig());
    }

    public ElasticsearchOutput(ElasticsearchOutputConfig outputConfig) {
        super(outputConfig);
        this.zoneId = ZoneId.of(outputConfig.getZoneId());
        this.indexPrefix = outputConfig.getIndexPrefix();
        this.indexFieldOptional = JMOptional.getOptional(outputConfig.getIndexField());
        this.idFieldOptional = JMOptional.getOptional(outputConfig.getIdField());
        this.indexSuffixDateFormat = outputConfig.getIndexSuffixDateFormat();
        this.indexSuffixDateFormatMap = outputConfig.getIndexSuffixDateFormatMap();
        this.elasticsearchClient = new JMElasticsearchClient(outputConfig.getElasticsearchConnect(), buildSettings(
                JMElasticsearchClient
                        .getSettingsBuilder(outputConfig.getNodeName(), outputConfig.isClientTransportSniff(),
                                outputConfig.getClusterName()), outputConfig.getProperties()));
        this.elasticsearchClient.setBulkProcessor(outputConfig.getBulkActions(), outputConfig.getBulkSizeKB(),
                outputConfig.getFlushIntervalSeconds());
        this.indexCache = new JM2DepthMap<>(true);
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
        this.elasticsearchClient.sendWithBulkProcessorAndObjectMapper(data, buildIndex(data, timestamp),
                idFieldOptional.map(data::get).map(Object::toString).filter(not(String::isBlank)).orElse(null));
    }

    String buildIndex(Map<String, Object> data, long timestamp) {
        return buildIndex(timestamp, indexFieldOptional
                .map(indexField -> JMOptional.getOptional(data, indexField).map(Object::toString).orElse("n_a")));
    }

    private String buildIndex(long timestamp, Optional<String> indexFieldValueOptional) {
        return buildIndex(indexFieldValueOptional, buildIndexSuffixDate(timestamp, indexFieldValueOptional));
    }

    private String buildIndexSuffixDate(long timestamp, Optional<String> indexFieldValueOptional) {
        return JMTime.getInstance().getTime(timestamp, indexFieldValueOptional
                .flatMap(indexFieldValue -> JMOptional.getOptional(indexSuffixDateFormatMap, indexFieldValue))
                .orElse(indexSuffixDateFormat), this.zoneId);
    }

    private String buildIndex(Optional<String> indexFieldValueOptional, String indexSuffixDate) {
        return indexCache.getOrPutGetNew(indexFieldValueOptional.orElse(JMString.EMPTY), indexSuffixDate,
                () -> indexPrefix +
                        indexFieldValueOptional.map(indexFieldValue -> JMString.HYPHEN + indexFieldValue.toLowerCase())
                                .orElse(JMString.EMPTY) + JMString.HYPHEN + indexSuffixDate);
    }
}
