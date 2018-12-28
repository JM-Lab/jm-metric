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
        this.elasticsearchOutput =
                new ElasticsearchOutput(new ElasticsearchOutputConfig(
                        this.jmEmbeddedElasticsearch.getTransportIpPortPair()));
        System.out.println(JMJson.toJsonString(
                this.elasticsearchOutput.getConfig()));
    }

    /**
     * Tear down.
     *
     * @throws Exception the exception
     */
    @After
    public void tearDown() throws Exception {
        JMOptional.getOptional(jmElasticsearchClient.getAllIndices())
                .ifPresent(indices -> jmElasticsearchClient.deleteIndices(
                        indices.toArray(new String[indices.size()])));
        while (jmElasticsearchClient.getAllIndices().size() > 0)
            JMThread.sleep(1000);
        jmElasticsearchClient.close();
        jmEmbeddedElasticsearch.close();
        JMPathOperation.deleteDirOnExist(JMPath.getPath("data"));
    }


    @Test
    public void writeData() {
        List<Transfer<List<Map<String, Object>>>> dataList =
                JMResources.readLines("testTransferData.txt").stream()
                        .map(line -> JMJson.withJsonString(line,
                                new TypeReference<Transfer<List<Map<String, Object>>>>() {}))
                        .collect(Collectors.toList());
        elasticsearchOutput
                .writeData(dataList.stream().flatMap(
                        transfer -> transfer.newStreamWith(transfer.getData()))
                        .collect(Collectors.toList()));
        JMThread.sleep(3000);
        Set<String> allIndices = jmElasticsearchClient.getAllIndices();
        System.out.println(allIndices);
        String index = elasticsearchOutput.getIndexPreSuf();
        Assert.assertTrue(allIndices.contains(index));
        SearchResponse searchResponse =
                jmElasticsearchClient.searchAll(JMArrays.toArray(allIndices));
        System.out.println(searchResponse);
        Assert.assertEquals(200, searchResponse.getHits().getTotalHits());
        elasticsearchOutput.close();
    }
}