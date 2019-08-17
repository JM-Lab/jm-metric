package kr.jm.metric.output;

import com.fasterxml.jackson.core.type.TypeReference;
import kr.jm.metric.config.output.ElasticsearchOutputConfig;
import kr.jm.metric.data.Transfer;
import kr.jm.utils.datastructure.JMArrays;
import kr.jm.utils.elasticsearch.JMElasticsearchClient;
import kr.jm.utils.elasticsearch.JMEmbeddedElasticsearch;
import kr.jm.utils.helper.*;
import org.elasticsearch.action.search.SearchResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ElasticsearchOutputTest {

    private JMEmbeddedElasticsearch jmEmbeddedElasticsearch;
    private ElasticsearchOutput elasticsearchOutput;
    private JMElasticsearchClient jmElasticsearchClient;

    /**
     * Sets the up.
     */
    @Before
    public void setUp() {
        JMPathOperation.deleteDirOnExist(JMPath.getPath("data"));
        // Embedded Elasticsearch Node Start
        this.jmEmbeddedElasticsearch = new JMEmbeddedElasticsearch();
        this.jmEmbeddedElasticsearch.start();
        jmElasticsearchClient = new JMElasticsearchClient(
                this.jmEmbeddedElasticsearch.getTransportIpPortPair());

        // JMElasticsearchClient Init
        ElasticsearchOutputConfig outputConfig = JMJson.transform(
                Map.of("elasticsearchConnect", this.jmEmbeddedElasticsearch.getTransportIpPortPair(), "indexField",
                        "requestMethod", "indexSuffixDateFormatMap", Map.of("POST", "yyyy.MM", "n_a", "yyyy.ww")),
                ElasticsearchOutputConfig.class);
        this.elasticsearchOutput = new ElasticsearchOutput(outputConfig);
        System.out.println(JMJson.toJsonString(this.elasticsearchOutput.getConfig()));
    }

    /**
     * Tear down.
     *
     * @throws Exception the exception
     */
    @After
    public void tearDown() throws Exception {
        JMOptional.getOptional(jmElasticsearchClient.getAllIndices())
                .ifPresent(indices -> jmElasticsearchClient.deleteIndices(indices.toArray(new String[indices.size()])));
        while (jmElasticsearchClient.getAllIndices().size() > 0)
            JMThread.sleep(1000);
        jmElasticsearchClient.close();
        jmEmbeddedElasticsearch.close();
        JMPathOperation.deleteDirOnExist(JMPath.getPath("data"));
    }


    @Test
    public void writeData() {

        List<Transfer<List<Map<String, Object>>>> dataList =
                JMResources.readLines("testTransferData.txt").stream().map(line -> JMJson.withJsonString(line,
                        new TypeReference<Transfer<List<Map<String, Object>>>>() {})).collect(Collectors.toList());
        elasticsearchOutput.writeData(dataList.stream().flatMap(transfer -> transfer.newStreamWith(transfer.getData()))
                .collect(Collectors.toList()));
        JMThread.sleep(3500);
        Set<String> allIndices = jmElasticsearchClient.getAllIndices();
        System.out.println(allIndices);
        Assert.assertEquals("[jm-metric-n_a-2018.20, jm-metric-post-2018.05, jm-metric-get-2018.05.15]",
                allIndices.toString());
        SearchResponse searchResponse = jmElasticsearchClient.searchAll(JMArrays.toArray(allIndices));
        System.out.println(searchResponse);
        Assert.assertEquals(200, searchResponse.getHits().getTotalHits());
        elasticsearchOutput.close();
    }

    @Test
    public void buildIndex() {
        long epochMilli = 1566027760265l;
        ZonedDateTime zonedDateTime = Instant.ofEpochMilli(epochMilli).atZone(ZoneId.of("UTC"));
        System.out.println(elasticsearchOutput.buildIndex(Map.of(), epochMilli));
        Assert.assertEquals("jm-metric-n_a-2019.33", elasticsearchOutput.buildIndex(Map.of(),
                zonedDateTime.toInstant().toEpochMilli()));
        Assert.assertEquals("jm-metric-n_a-2019.35", elasticsearchOutput.buildIndex(Map.of(),
                zonedDateTime.plusWeeks(2).toInstant().toEpochMilli()));

        Assert.assertEquals("jm-metric-post-2019.08", elasticsearchOutput
                .buildIndex(Map.of("requestMethod", "POST"), zonedDateTime.toInstant().toEpochMilli()));
        Assert.assertEquals("jm-metric-post-2020.01", elasticsearchOutput
                .buildIndex(Map.of("requestMethod", "POST"), zonedDateTime.plusMonths(5).toInstant().toEpochMilli()));

        // JMElasticsearchClient Init
        this.elasticsearchOutput.close();
        ElasticsearchOutputConfig outputConfig = JMJson.transform(
                Map.of("elasticsearchConnect", this.jmEmbeddedElasticsearch.getTransportIpPortPair(),
                        "indexSuffixDateFormatMap", Map.of("POST", "yyyy.MM", "n_a", "yyyy.ww")),
                ElasticsearchOutputConfig.class);
        this.elasticsearchOutput = new ElasticsearchOutput(outputConfig);
        System.out.println(JMJson.toJsonString(this.elasticsearchOutput.getConfig()));

        ZonedDateTime minusOneDay = zonedDateTime.minusDays(1);
        System.out.println(elasticsearchOutput.buildIndex(Map.of(), minusOneDay.toInstant().toEpochMilli()));
        Assert.assertEquals("jm-metric-2019.08.16", elasticsearchOutput.buildIndex(Map.of(),
                minusOneDay.toInstant().toEpochMilli()));
        Assert.assertEquals("jm-metric-2019.08.18", elasticsearchOutput.buildIndex(Map.of(),
                minusOneDay.plusDays(2).toInstant().toEpochMilli()));
    }
}