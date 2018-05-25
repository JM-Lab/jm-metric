package kr.jm.metric.config.field;

import kr.jm.utils.helper.JMJson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

public class FieldConfigTest {

    private FieldConfig fieldConfig;

    @Before
    public void setUp() {
        this.fieldConfig =
                JMJson.withJsonResource("fieldConfigTest.json",
                        FieldConfig.class);
    }

    @Test
    public void testFieldConfig() {
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
                        "         \"timestamp\":\"08/Jun/2015:17:00:00 +0900\",\n" +
                        "         \"statusCode\":\"200\"\n" +
                        "      }", JMJson.getMapOrListTypeReference());

        Map<String, Object> stringObjectMap =
                this.fieldConfig.applyConfig(fieldObjectMap);
        System.out.println(stringObjectMap);
        Assert.assertFalse(fieldObjectMap.containsKey("remoteUser"));
        Assert.assertEquals("172.22.206.86|/app/5104", stringObjectMap.get
                ("remoteHost|requestUrl"));
        Assert.assertEquals("2015-06-08T18:00:00+1000", stringObjectMap.get
                ("timestamp"));
        Assert.assertEquals(448d, stringObjectMap.get
                ("sizeByte"));
        Assert.assertEquals(167434d, stringObjectMap.get
                ("requestTime"));
        Assert.assertEquals(167434d / 1000, stringObjectMap.get
                ("requestTimeInMicro"));
        Assert.assertNull(stringObjectMap.get("requestTime_sizeByte"));

        Map<String, Object> fieldMetaMap = fieldConfig.extractFieldMetaMap();
        System.out.println(fieldMetaMap);
        Assert.assertEquals("{unit={requestTime=MicroSecond}, " +
                "custom={customKey=customValue, customObject={bool=false}, " +
                "customList=[hello, world]}}", fieldMetaMap.toString());

    }
}