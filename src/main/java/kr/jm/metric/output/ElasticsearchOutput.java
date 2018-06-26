package kr.jm.metric.output;

import kr.jm.metric.config.output.ElasticsearchOutputConfig;
import kr.jm.metric.data.ConfigIdTransfer;
import kr.jm.metric.data.FieldMap;
import kr.jm.utils.elasticsearch.JMElasticsearchClient;
import kr.jm.utils.helper.JMString;
import kr.jm.utils.time.JMTimeUtil;
import lombok.Getter;
import org.elasticsearch.common.settings.Settings;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Getter
public class ElasticsearchOutput extends AbstractOutput {

    private String zoneId;
    private String indexSuffixDateFormat;
    private String indexPrefix;
    private String indexSuffixDate;
    private String index;

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
     * @param elasticsearchConnect the elasticsearch connect
     */
    public ElasticsearchOutput(String elasticsearchConnect) {
        this(new ElasticsearchOutputConfig(elasticsearchConnect));
    }

    /**
     * Instantiates a new Elasticsearch output.
     *
     * @param elasticsearchConnect the elasticsearch connect
     * @param nodeName             the node name
     */
    public ElasticsearchOutput(String elasticsearchConnect,
            String nodeName) {
        this(new ElasticsearchOutputConfig(elasticsearchConnect, nodeName));
    }

    /**
     * Instantiates a new Elasticsearch output.
     *
     * @param elasticsearchConnect the elasticsearch connect
     * @param clientTransportSniff the client transport sniff
     */
    public ElasticsearchOutput(String elasticsearchConnect,
            boolean clientTransportSniff) {
        this(new ElasticsearchOutputConfig(elasticsearchConnect,
                clientTransportSniff));
    }

    /**
     * Instantiates a new Elasticsearch output.
     *
     * @param elasticsearchConnect the elasticsearch connect
     * @param nodeName             the node name
     * @param clientTransportSniff the client transport sniff
     */
    public ElasticsearchOutput(String elasticsearchConnect,
            String nodeName, boolean clientTransportSniff) {
        this(new ElasticsearchOutputConfig(elasticsearchConnect, nodeName,
                clientTransportSniff));
    }

    /**
     * Instantiates a new Elasticsearch output.
     *
     * @param elasticsearchConnect the elasticsearch connect
     * @param nodeName             the node name
     * @param clientTransportSniff the client transport sniff
     * @param clusterName          the cluster name
     */
    public ElasticsearchOutput(String elasticsearchConnect,
            String nodeName, boolean clientTransportSniff, String clusterName) {
        this(new ElasticsearchOutputConfig(elasticsearchConnect, nodeName,
                clientTransportSniff, clusterName));
    }

    /**
     * Instantiates a new Elasticsearch output.
     *
     * @param elasticsearchConnect  the elasticsearch connect
     * @param nodeName              the node name
     * @param clientTransportSniff  the client transport sniff
     * @param clusterName           the cluster name
     * @param indexPrefix           the index prefix
     * @param indexSuffixDateFormat the index suffix date format
     * @param zoneId                the zone id
     */
    public ElasticsearchOutput(String elasticsearchConnect,
            String nodeName, boolean clientTransportSniff, String clusterName,
            String indexPrefix, String indexSuffixDateFormat, String zoneId) {
        this(new ElasticsearchOutputConfig(elasticsearchConnect, nodeName,
                clientTransportSniff, clusterName, indexPrefix,
                indexSuffixDateFormat, zoneId));
    }

    /**
     * Instantiates a new Elasticsearch output.
     *
     * @param elasticsearchConnect  the elasticsearch connect
     * @param nodeName              the node name
     * @param clientTransportSniff  the client transport sniff
     * @param clusterName           the cluster name
     * @param indexPrefix           the index prefix
     * @param indexSuffixDateFormat the index suffix date format
     * @param zoneId                the zone id
     * @param bulkActions           the bulk actions
     * @param bulkSizeKB            the bulk size kb
     * @param flushIntervalSeconds  the flush interval seconds
     */
    public ElasticsearchOutput(
            String elasticsearchConnect, String nodeName,
            boolean clientTransportSniff, String clusterName,
            String indexPrefix, String indexSuffixDateFormat, String zoneId,
            int bulkActions, long bulkSizeKB, int flushIntervalSeconds) {
        this(new ElasticsearchOutputConfig(elasticsearchConnect, nodeName,
                clientTransportSniff, clusterName, indexPrefix,
                indexSuffixDateFormat, zoneId, bulkActions, bulkSizeKB,
                flushIntervalSeconds));
    }

    /**
     * Instantiates a new Elasticsearch output.
     *
     * @param outputConfig the output properties
     */
    public ElasticsearchOutput(
            ElasticsearchOutputConfig outputConfig) {
        super(outputConfig);
        this.index = JMString.EMPTY;
        this.indexPrefix = Optional.ofNullable(outputConfig.getIndexPrefix())
                .orElse("jm-metric");
        this.indexSuffixDateFormat = outputConfig.getIndexSuffixDateFormat();
        this.zoneId = outputConfig.getZoneId();
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
        return getIndex(JMTimeUtil
                .getTime(timestamp, this.indexSuffixDateFormat, this.zoneId));
    }

    private String getIndex(String indexSuffixDate) {
        if (!indexSuffixDate.equals(this.indexSuffixDate))
            synchronized (this.index) {
                this.indexSuffixDate = indexSuffixDate;
                return this.index =
                        this.indexPrefix + JMString.HYPHEN + indexSuffixDate;
            }
        return this.index;
    }

    @Override
    protected void closeImpl() {
        this.elasticsearchClient.close();
    }

    @Override
    public void writeData(List<ConfigIdTransfer<FieldMap>> data) {
        for (ConfigIdTransfer<FieldMap> configIdTransfer : data)
            writeConfigIdTransfer(configIdTransfer.getData(),
                    configIdTransfer.getTimestamp());
    }

    private void writeConfigIdTransfer(FieldMap fieldMap, long timestamp) {
        this.elasticsearchClient
                .sendWithBulkProcessor(fieldMap, buildIndex(timestamp),
                        this.indexPrefix);
    }

}
