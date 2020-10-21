package kr.jm.metric.output;

import com.fasterxml.jackson.core.type.TypeReference;
import kr.jm.metric.config.output.ElasticsearchOutputConfig;
import kr.jm.metric.data.Transfer;
import kr.jm.utils.*;
import kr.jm.utils.elasticsearch.JMElasticsearchClient;
import kr.jm.utils.elasticsearch.JMEmbeddedElasticsearch;
import kr.jm.utils.helper.JMJson;
import kr.jm.utils.helper.JMPath;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
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
import java.util.UUID;
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
        JMPath.getInstance().deleteDirOnExist(JMPath.getInstance().getPath("data"));
        // Embedded Elasticsearch Node Start
        this.jmEmbeddedElasticsearch = new JMEmbeddedElasticsearch();
        this.jmEmbeddedElasticsearch.start();
        jmElasticsearchClient = new JMElasticsearchClient(
                this.jmEmbeddedElasticsearch.getTransportIpPortPair());

        // JMElasticsearchClient Init
        ElasticsearchOutputConfig outputConfig = JMJson.getInstance().transform(
                Map.of("elasticsearchConnect", this.jmEmbeddedElasticsearch.getTransportIpPortPair(), "idField", "@id",
                        "indexField", "requestMethod", "indexSuffixDateFormatMap",
                        Map.of("POST", "yyyy.MM", "n_a", "yyyy.ww")),
                ElasticsearchOutputConfig.class);
        this.elasticsearchOutput = new ElasticsearchOutput(outputConfig);
        System.out.println(JMJson.getInstance().toJsonString(this.elasticsearchOutput.getConfig()));
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
        JMPath.getInstance().deleteDirOnExist(JMPath.getInstance().getPath("data"));
    }


    @Test
    public void writeData() {

        List<Transfer<List<Map<String, Object>>>> dataList =
                JMResources.readLines("testTransferData.txt").stream()
                        .map(line -> JMJson.getInstance().withJsonString(line,
                                new TypeReference<Transfer<List<Map<String, Object>>>>() {}))
                        .collect(Collectors.toList());
        String uuid = UUID.randomUUID().toString();
        dataList.get(0).getData().get(10).put("@id", uuid);
        dataList.get(0).getData().get(11).put("@id", "");
        dataList.get(0).getData().get(13).put("@id", "   ");
        elasticsearchOutput.writeData(dataList.stream().flatMap(transfer -> transfer.newStreamWith(transfer.getData()))
                .collect(Collectors.toList()));
        JMThread.sleep(3500);

        Set<String> allIndices = jmElasticsearchClient.getAllIndices();
        System.out.println(allIndices);
        Assert.assertEquals("[jm-metric-n_a-2018.20, jm-metric-post-2018.05, jm-metric-get-2018.05.15]",
                allIndices.toString());
        SearchResponse searchResponse = jmElasticsearchClient.searchAllWithTargetCount(JMArrays.toArray(allIndices));
        System.out.println(searchResponse);
        SearchHits hits = searchResponse.getHits();
        Assert.assertEquals(200, hits.getTotalHits().value);
        List<SearchHit> uuidList =
                kr.jm.utils.JMStream.buildStream(hits.getHits())
                        .filter(searchHit -> searchHit.getSourceAsMap().containsKey("@id"))
                        .collect(Collectors.toList());
        Assert.assertEquals(3, uuidList.size());
        uuidList = JMStream.buildStream(hits.getHits()).filter(searchHit -> searchHit.getId().equals(uuid))
                .collect(Collectors.toList());
        Assert.assertEquals(1, uuidList.size());
        Assert.assertEquals(uuid, uuidList.get(0).getSourceAsMap().get("@id"));
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
        ElasticsearchOutputConfig outputConfig = JMJson.getInstance().transform(
                Map.of("elasticsearchConnect", this.jmEmbeddedElasticsearch.getTransportIpPortPair(),
                        "indexSuffixDateFormatMap", Map.of("POST", "yyyy.MM", "n_a", "yyyy.ww")),
                ElasticsearchOutputConfig.class);
        this.elasticsearchOutput = new ElasticsearchOutput(outputConfig);
        System.out.println(JMJson.getInstance().toJsonString(this.elasticsearchOutput.getConfig()));

        ZonedDateTime minusOneDay = zonedDateTime.minusDays(1);
        System.out.println(elasticsearchOutput.buildIndex(Map.of(), minusOneDay.toInstant().toEpochMilli()));
        Assert.assertEquals("jm-metric-2019.08.16", elasticsearchOutput.buildIndex(Map.of(),
                minusOneDay.toInstant().toEpochMilli()));
        Assert.assertEquals("jm-metric-2019.08.18", elasticsearchOutput.buildIndex(Map.of(),
                minusOneDay.plusDays(2).toInstant().toEpochMilli()));
    }
}