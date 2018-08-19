package kr.jm.metric.output;

import kr.jm.metric.config.output.ElasticsearchOutputConfig;
import kr.jm.metric.data.FieldMap;
import kr.jm.metric.data.Transfer;
import kr.jm.utils.elasticsearch.JMElasticsearchClient;
import kr.jm.utils.helper.JMString;
import kr.jm.utils.time.JMTimeUtil;
import lombok.Getter;
import lombok.ToString;
import org.elasticsearch.common.settings.Settings;

import java.util.List;
import java.util.Map;

/**
 * The type Elasticsearch output.
 */
@ToString(callSuper = true)
@Getter
public class ElasticsearchOutput extends AbstractOutput {

    private String zoneId;
    private String indexSuffixDateFormat;
    private String indexPrefix;
    private String indexSuffixDate;
    private String index;

    /**
     * The Elasticsearch client.
     */
    protected JMElasticsearchClient elasticsearchClient;

    /**
     * Instantiates a new Elasticsearch output.
     */
    public ElasticsearchOutput() {
        this(new ElasticsearchOutputConfig());
    }

    /**
     * Instantiates a new Elasticsearch output.
     *
     * @param outputConfig the output config
     */
    public ElasticsearchOutput(ElasticsearchOutputConfig outputConfig) {
        super(outputConfig);
        this.indexPrefix = outputConfig.getIndexPrefix();
        this.indexSuffixDateFormat = outputConfig.getIndexSuffixDateFormat();
        this.zoneId = outputConfig.getZoneId();
        this.indexSuffixDate = buildInputSuffixDate(System.currentTimeMillis());
        this.index = buildIndex(this.indexSuffixDate);
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
    }

    private static Settings buildSettings(Settings.Builder settingsBuilder,
            Map<String, Object> esClientConfig) {
        esClientConfig.forEach(
                (key, value) -> settingsBuilder.put(key, value.toString()));
        return settingsBuilder.build();
    }

    private String buildIndex(long timestamp) {
        return getIndex(buildInputSuffixDate(timestamp));
    }

    private String buildInputSuffixDate(long timestamp) {
        return JMTimeUtil
                .getTime(timestamp, this.indexSuffixDateFormat, this.zoneId);
    }

    private String getIndex(String indexSuffixDate) {
        return indexSuffixDate
                .equals(this.indexSuffixDate) ? this.index : buildIndex(
                indexSuffixDate);
    }

    private String buildIndex(String indexSuffixDate) {
        synchronized (this.indexSuffixDate) {
            this.indexSuffixDate = indexSuffixDate;
            return this.index =
                    this.indexPrefix + JMString.HYPHEN + indexSuffixDate;
        }
    }

    @Override
    protected void closeImpl() {
        this.elasticsearchClient.close();
    }

    @Override
    public void writeData(List<Transfer<FieldMap>> transferList) {
        for (Transfer<FieldMap> inputIdTransfer : transferList)
            this.elasticsearchClient
                    .sendWithBulkProcessor(inputIdTransfer.getData(),
                            buildIndex(inputIdTransfer.getTimestamp()),
                            inputIdTransfer.getInputId());
    }

}
