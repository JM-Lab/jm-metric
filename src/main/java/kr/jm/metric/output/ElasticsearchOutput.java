package kr.jm.metric.output;

import kr.jm.metric.config.output.ElasticsearchOutputConfig;
import kr.jm.metric.data.Transfer;
import kr.jm.utils.collections.JMNestedMap;
import kr.jm.utils.elasticsearch.JMElasticsearchClient;
import kr.jm.utils.helper.JMJson;
import kr.jm.utils.helper.JMOptional;
import kr.jm.utils.helper.JMString;
import kr.jm.utils.time.JMTimeUtil;
import lombok.Getter;
import lombok.ToString;
import org.elasticsearch.common.settings.Settings;

import java.util.List;
import java.util.Map;

@ToString(callSuper = true)
@Getter
public class ElasticsearchOutput extends AbstractOutput {

    private static final String TYPE = "doc";
    private String zoneId;
    private String indexField;
    private String indexPrefix;
    private String indexSuffixDate;
    private String indexSuffixDateFormat;

    private JMNestedMap<Object, String, String> indexCache;

    protected JMElasticsearchClient elasticsearchClient;

    public ElasticsearchOutput() {
        this(new ElasticsearchOutputConfig());
    }

    public ElasticsearchOutput(ElasticsearchOutputConfig outputConfig) {
        super(outputConfig);
        this.zoneId = outputConfig.getZoneId();
        this.indexField = outputConfig.getIndexField();
        this.indexPrefix = outputConfig.getIndexPrefix();
        this.indexSuffixDateFormat = outputConfig.getIndexSuffixDateFormat();
        this.elasticsearchClient = new JMElasticsearchClient(
                outputConfig.getElasticsearchConnect(), buildSettings(
                JMElasticsearchClient
                        .getSettingsBuilder(outputConfig.getNodeName(),
                                outputConfig.isClientTransportSniff(),
                                outputConfig.getClusterName()),
                outputConfig.getProperties()));
        this.elasticsearchClient.setBulkProcessor(outputConfig.getBulkActions(),
                outputConfig.getBulkSizeKB(),
                outputConfig.getFlushIntervalSeconds());
        this.indexCache = new JMNestedMap<>(true);
    }

    private static Settings buildSettings(Settings.Builder settingsBuilder,
            Map<String, Object> esClientConfig) {
        esClientConfig.forEach(
                (key, value) -> settingsBuilder.put(key, value.toString()));
        return settingsBuilder.build();
    }

    private String buildInputSuffixDate(long timestamp) {
        return JMTimeUtil
                .getTime(timestamp, this.indexSuffixDateFormat, this.zoneId);
    }

    @Override
    protected void closeImpl() {
        this.elasticsearchClient.close();
    }

    @Override
    public void writeData(List<Transfer<Map<String, Object>>> transferList) {
        for (Transfer<Map<String, Object>> inputIdTransfer : transferList)
            writeData(inputIdTransfer.getData(),
                    inputIdTransfer.getTimestamp());
    }

    private void writeData(Map<String, Object> data, long timestamp) {
        // elasticsearch 자체 json parser xcontent가 잘 안되는 경우가 있음
        this.elasticsearchClient
                .sendWithBulkProcessor(JMJson.transformToMap(data),
                        buildIndex(data, buildInputSuffixDate(timestamp)),
                        TYPE);
    }

    private String buildIndex(Map<String, Object> data, String indexSuffix) {
        return JMString.isNullOrEmpty(indexField) ? indexCache
                .getOrPutGetNew(JMString.EMPTY, indexSuffix,
                        () -> indexPrefix + JMString.HYPHEN +
                                indexSuffix) : buildIndex(
                JMOptional.getOptional(data, this.indexField).orElse("n_a"),
                indexSuffix);
    }

    private String buildIndex(Object indexValue, String indexSuffix) {
        return indexCache.getOrPutGetNew(indexValue, indexSuffix,
                () -> indexPrefix + JMString.HYPHEN +
                        indexValue.toString().toLowerCase() +
                        JMString.HYPHEN + indexSuffix);
    }

}
