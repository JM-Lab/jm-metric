package kr.jm.metric.config.output;

import kr.jm.metric.output.ElasticsearchOutput;
import kr.jm.utils.enums.OS;
import kr.jm.utils.helper.JMString;
import kr.jm.utils.time.JMTimeUtil;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

@Getter
@ToString(callSuper = true)
public class ElasticsearchOutputConfig extends AbstractOutputConfig {

    public static final int DEFAULT_BULK_ACTIONS = 128;
    public static final long DEFAULT_BULK_SIZE_KB = 1024;
    public static final int DEFAULT_FLUSH_INTERVAL_SECONDS = 1;
    public static final String DEFAULT_ZONE_ID = JMTimeUtil.UTC_ZONE_ID;
    public static final String DEFAULT_INDEX_SUFFIX_DATE_FORMAT = "yyyy.MM.dd";
    public static final String DEFAULT_INDEX_PREFIX = "jm-metric";
    private static final String DEFAULT_NODE_NAME = OS.getHostname();
    public static final String DEFAULT_ELASTICSEARCH_CONNECT =
            JMString.buildIpOrHostnamePortPair(DEFAULT_NODE_NAME, 9300);

    private String elasticsearchConnect;
    private String nodeName;
    private boolean clientTransportSniff;
    private String clusterName;

    private String indexPrefix;
    private String indexField;
    private String indexSuffixDateFormat;
    private Map<String, String> indexSuffixDateFormatMap;
    private String zoneId;

    private int bulkActions;
    private long bulkSizeKB;
    private int flushIntervalSeconds;

    public ElasticsearchOutputConfig() {
        this(DEFAULT_ELASTICSEARCH_CONNECT);
    }

    public ElasticsearchOutputConfig(String elasticsearchConnect) {
        this(elasticsearchConnect, DEFAULT_NODE_NAME);
    }

    public ElasticsearchOutputConfig(String elasticsearchConnect,
            String nodeName) {
        this(elasticsearchConnect, nodeName, false);
    }

    public ElasticsearchOutputConfig(String elasticsearchConnect,
            boolean clientTransportSniff) {
        this(elasticsearchConnect, DEFAULT_NODE_NAME, clientTransportSniff);
    }


    public ElasticsearchOutputConfig(String elasticsearchConnect,
            String nodeName, boolean clientTransportSniff) {
        this(elasticsearchConnect, nodeName, clientTransportSniff, null);
    }

    public ElasticsearchOutputConfig(String elasticsearchConnect,
            String nodeName, boolean clientTransportSniff, String clusterName) {
        this(elasticsearchConnect, nodeName, clientTransportSniff, clusterName,
                null);
    }

    public ElasticsearchOutputConfig(String elasticsearchConnect,
            String nodeName, boolean clientTransportSniff, String
            clusterName, String indexField) {
        this(elasticsearchConnect, nodeName, clientTransportSniff,
                clusterName, indexField, DEFAULT_INDEX_PREFIX,
                DEFAULT_INDEX_SUFFIX_DATE_FORMAT, DEFAULT_ZONE_ID);
    }

    public ElasticsearchOutputConfig(String elasticsearchConnect,
            String nodeName, boolean clientTransportSniff, String clusterName,
            String indexField, String indexPrefix, String indexSuffixDateFormat,
            String zoneId) {
        this(elasticsearchConnect, nodeName, clientTransportSniff, clusterName,
                indexField, indexPrefix, indexSuffixDateFormat, zoneId,
                DEFAULT_BULK_ACTIONS, DEFAULT_BULK_SIZE_KB,
                DEFAULT_FLUSH_INTERVAL_SECONDS);
    }

    public ElasticsearchOutputConfig(String elasticsearchConnect,
            String nodeName, boolean clientTransportSniff, String clusterName,
            String indexField, String indexPrefix, String indexSuffixDateFormat,
            String zoneId, int bulkActions, long bulkSizeKB,
            int flushIntervalSeconds) {
        this("ESOutput-" + nodeName, elasticsearchConnect, nodeName,
                clientTransportSniff, clusterName, indexField, indexPrefix,
                indexSuffixDateFormat, zoneId, bulkActions, bulkSizeKB,
                flushIntervalSeconds);
    }

    public ElasticsearchOutputConfig(String outputId,
            String elasticsearchConnect, String nodeName,
            boolean clientTransportSniff, String clusterName, String indexField,
            String indexPrefix, String indexSuffixDateFormat, String zoneId,
            int bulkActions, long bulkSizeKB, int flushIntervalSeconds) {
        super(outputId);
        this.elasticsearchConnect = elasticsearchConnect;
        this.nodeName = nodeName;
        this.clientTransportSniff = clientTransportSniff;
        this.clusterName = clusterName;
        this.indexField = indexField;
        this.indexPrefix = indexPrefix;
        this.indexSuffixDateFormat = indexSuffixDateFormat;
        this.zoneId = zoneId;
        this.bulkActions = bulkActions;
        this.bulkSizeKB = bulkSizeKB;
        this.flushIntervalSeconds = flushIntervalSeconds;
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
