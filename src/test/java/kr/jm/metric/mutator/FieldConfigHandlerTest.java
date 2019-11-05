package kr.jm.metric.mutator;

import kr.jm.metric.config.mutator.field.DateFormatConfigBuilder;
import kr.jm.metric.config.mutator.field.DateFormatType;
import kr.jm.metric.config.mutator.field.FieldConfig;
import kr.jm.metric.config.mutator.field.FieldConfigBuilder;
import kr.jm.utils.helper.JMJson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

public class FieldConfigHandlerTest {

    private FieldConfig fieldConfig;
    private FieldConfigHandler fieldConfigHandler;

    @Before
    public void setUp() {
        this.fieldConfig = JMJson.withJsonResource("fieldConfigTest.json", FieldConfig.class);
        this.fieldConfigHandler = new FieldConfigHandler("testMutatorId", this.fieldConfig);
    }

    @Test
    public void testApplyFieldConfig() {
        System.out.println(fieldConfig.isRawData());
        Assert.assertFalse(fieldConfig.isRawData());
        Map<String, Object> fieldObjectMap = JMJson.withJsonString(
                "{      \"requestTime\":\"167434\",\n" +
                        "         \"request\":\"POST /app/5104 HTTP/1.1\",\n" +
                        "         \"remoteUser\":\"jm\"," + "\n" +
                        "         \"referer\":\"-\",\n" +
                        "         \"remoteHost\":\"172.22.206.86\",\n" +
                        "         \"requestUrl\":\"/app/5104\",\n" +
                        "         \"requestMethod\":\"POST\",\n" +
                        "         \"userAgent\":\"Jakarta Commons-HttpClient/3.1\",\n" +
                        "         \"sizeByte\":\"448\",\n" +
                        "         \"requestProtocol\":\"HTTP/1.1\",\n" +
                        "         \"receivedTimestamp\":\"08/Jun/2015:17:00:00 +0900\",\n" +
                        "         \"statusCode\":\"200\"\n" +
                        "      }", JMJson.getMapOrListTypeReference());

        Map<String, Object> stringObjectMap = this.fieldConfigHandler.applyFieldConfig(fieldObjectMap);
        System.out.println(stringObjectMap);
        Assert.assertFalse(stringObjectMap.containsKey("remoteUser"));
        Assert.assertEquals("172.22.206.86|/app/5104", stringObjectMap.get("remoteHost|requestUrl"));
        Assert.assertEquals("2015-06-08T18:00:00+1000", stringObjectMap.get("receivedTimestamp"));
        Assert.assertEquals(448d, stringObjectMap.get("sizeByte"));
        Assert.assertEquals(167434d, stringObjectMap.get("aa"));
        Assert.assertEquals(167434d / 1000, stringObjectMap.get("requestTimeInMicro"));
        Assert.assertNull(stringObjectMap.get("requestTime_sizeByte"));

        Map<String, Object> fieldMetaMap = fieldConfig.extractFieldMetaMap();
        System.out.println(fieldMetaMap);
        Assert.assertEquals("{unit={requestTime=MicroSecond}}", fieldMetaMap.toString());

        this.fieldConfig = new FieldConfigBuilder().setRawData(false)
                .setDateFormat(Map.of("receivedTimestamp", new DateFormatConfigBuilder().setDateFormatType(
                        DateFormatType.CUSTOM).setTimeUnit(null).setFormat("dd/MMM/yyyy:HH:mm:ss Z").setZoneOffset("")
                        .setNewFieldName("@timestamp").setChangeDateConfig(new DateFormatConfigBuilder()
                                .setDateFormatType(DateFormatType.ISO).setTimeUnit(null)
                                .setFormat("yyyy-MM-dd'T'HH:mm:ssZ").setZoneOffset("+1000").setNewFieldName(null)
                                .setChangeDateConfig(null).createDateFormatConfig()).createDateFormatConfig()))
                .createFieldConfig();
        System.out.println(JMJson.toJsonString(fieldConfig));

        stringObjectMap = new FieldConfigHandler("testMutatorId", this.fieldConfig).applyFieldConfig(fieldObjectMap);
        System.out.println(stringObjectMap);
        Assert.assertEquals("08/Jun/2015:17:00:00 +0900", stringObjectMap.get("receivedTimestamp"));
        Assert.assertEquals("2015-06-08T08:00:00.000Z", stringObjectMap.get("@timestamp"));

    }
}