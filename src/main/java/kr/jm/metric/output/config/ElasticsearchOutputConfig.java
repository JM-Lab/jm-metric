package kr.jm.metric.output.config;

import kr.jm.metric.output.ElasticsearchOutput;
import kr.jm.metric.output.config.type.OutputConfigType;
import kr.jm.utils.enums.OS;
import kr.jm.utils.helper.JMString;
import kr.jm.utils.time.JMTimeUtil;
import lombok.Getter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

/**
 * The type Elasticsearch output properties.
 */
@Getter
@ToString(callSuper = true)
public class ElasticsearchOutputConfig extends AbstractOutputConfig {

    /**
     * The constant DEFAULT_BULK_ACTIONS.
     */
    public static final int DEFAULT_BULK_ACTIONS = 128;
    /**
     * The constant DEFAULT_BULK_SIZE_KB.
     */
    public static final long DEFAULT_BULK_SIZE_KB = 1 * 1024;
    /**
     * The constant DEFAULT_FLUSH_INTERVAL_SECONDS.
     */
    public static final int DEFAULT_FLUSH_INTERVAL_SECONDS = 1;
    /**
     * The constant DEFAULT_ZONE_ID.
     */
    public static final String DEFAULT_ZONE_ID = JMTimeUtil.UTC_ZONE_ID;
    /**
     * The constant DEFAULT_INDEX_SUFFIX_DATE_FORMAT.
     */
    public static final String DEFAULT_INDEX_SUFFIX_DATE_FORMAT = "yyyy.MM.dd";
    /**
     * The constant DEFAULT_INDEX_PREFIX.
     */
    public static final String DEFAULT_INDEX_PREFIX = "jm-metric";
    private static final String DEFAULT_NODE_NAME = OS.getHostname();
    /**
     * The constant DEFAULT_ELASTICSEARCH_CONNECT.
     */
    public static final String DEFAULT_ELASTICSEARCH_CONNECT =
            JMString.buildIpOrHostnamePortPair(DEFAULT_NODE_NAME, 9300);

    private String elasticsearchConnect;
    private String nodeName;
    private boolean clientTransportSniff;
    private String clusterName;

    private String indexPrefix;
    private String indexSuffixDateFormat;
    private String zoneId;

    private int bulkActions;
    private long bulkSizeKB;
    private int flushIntervalSeconds;

    /**
     * Instantiates a new Elasticsearch output properties.
     */
    public ElasticsearchOutputConfig() {
        this(DEFAULT_ELASTICSEARCH_CONNECT);
    }

    /**
     * Instantiates a new Elasticsearch output properties.
     *
     * @param elasticsearchConnect the elasticsearch connect
     */
    public ElasticsearchOutputConfig(String elasticsearchConnect) {
        this(elasticsearchConnect, DEFAULT_NODE_NAME);
    }

    /**
     * Instantiates a new Elasticsearch output properties.
     *
     * @param elasticsearchConnect the elasticsearch connect
     * @param nodeName             the node name
     */
    public ElasticsearchOutputConfig(String elasticsearchConnect,
            String nodeName) {
        this(elasticsearchConnect, nodeName, false);
    }

    /**
     * Instantiates a new Elasticsearch output properties.
     *
     * @param elasticsearchConnect the elasticsearch connect
     * @param clientTransportSniff the client transport sniff
     */
    public ElasticsearchOutputConfig(String elasticsearchConnect,
            boolean clientTransportSniff) {
        this(elasticsearchConnect, DEFAULT_NODE_NAME, clientTransportSniff);
    }


    /**
     * Instantiates a new Elasticsearch output properties.
     *
     * @param elasticsearchConnect the elasticsearch connect
     * @param nodeName             the node name
     * @param clientTransportSniff the client transport sniff
     */
    public ElasticsearchOutputConfig(String elasticsearchConnect,
            String nodeName, boolean clientTransportSniff) {
        this(elasticsearchConnect, nodeName, clientTransportSniff, null);
    }

    /**
     * Instantiates a new Elasticsearch output properties.
     *
     * @param elasticsearchConnect the elasticsearch connect
     * @param nodeName             the node name
     * @param clientTransportSniff the client transport sniff
     * @param clusterName          the cluster name
     */
    public ElasticsearchOutputConfig(String elasticsearchConnect,
            String nodeName, boolean clientTransportSniff, String clusterName) {
        this(elasticsearchConnect, nodeName, clientTransportSniff,
                clusterName, DEFAULT_INDEX_PREFIX,
                DEFAULT_INDEX_SUFFIX_DATE_FORMAT, DEFAULT_ZONE_ID);
    }

    /**
     * Instantiates a new Elasticsearch output properties.
     *
     * @param elasticsearchConnect  the elasticsearch connect
     * @param nodeName              the node name
     * @param clientTransportSniff  the client transport sniff
     * @param clusterName           the cluster name
     * @param indexPrefix           the index prefix
     * @param indexSuffixDateFormat the index suffix date format
     * @param zoneId                the zone id
     */
    public ElasticsearchOutputConfig(String elasticsearchConnect,
            String nodeName, boolean clientTransportSniff, String clusterName,
            String indexPrefix, String indexSuffixDateFormat, String zoneId) {
        this(elasticsearchConnect, nodeName, clientTransportSniff,
                clusterName, indexPrefix, indexSuffixDateFormat, zoneId,
                DEFAULT_BULK_ACTIONS, DEFAULT_BULK_SIZE_KB,
                DEFAULT_FLUSH_INTERVAL_SECONDS);
    }

    /**
     * Instantiates a new Elasticsearch output properties.
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
    public ElasticsearchOutputConfig(
            String elasticsearchConnect, String nodeName,
            boolean clientTransportSniff, String clusterName,
            String indexPrefix, String indexSuffixDateFormat, String zoneId,
            int bulkActions, long bulkSizeKB, int flushIntervalSeconds) {
        this.elasticsearchConnect = elasticsearchConnect;
        this.nodeName = nodeName;
        this.clientTransportSniff = clientTransportSniff;
        this.clusterName = clusterName;
        this.indexPrefix = indexPrefix;
        this.indexSuffixDateFormat = indexSuffixDateFormat;
        this.zoneId = zoneId;
        this.bulkActions = bulkActions;
        this.bulkSizeKB = bulkSizeKB;
        this.flushIntervalSeconds = flushIntervalSeconds;
    }

    @Override
    protected Map<String, Object> buildChildConfig() {
        return new HashMap<>() {{
            put("elasticsearchConnect", elasticsearchConnect);
            put("nodeName", nodeName);
            put("clientTransportSniff", clientTransportSniff);
            put("clusterName", clusterName);
            put("indexPrefix", indexPrefix);
            put("indexSuffixDateFormat", indexSuffixDateFormat);
            put("zoneId", zoneId);
            put("bulkActions", bulkActions);
            put("bulkSizeKB", bulkSizeKB);
            put("flushIntervalSeconds", flushIntervalSeconds);
        }};
    }

    @Override
    public OutputConfigType getOutputConfigType() {
        return OutputConfigType.ELASTICSEARCH;
    }

    @Override
    public ElasticsearchOutput buildOutput() {
        return new ElasticsearchOutput(this);
    }
}
