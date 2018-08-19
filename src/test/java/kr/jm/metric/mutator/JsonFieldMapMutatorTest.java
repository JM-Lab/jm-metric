package kr.jm.metric.mutator;

import kr.jm.metric.data.FieldMap;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class JsonFieldMapMutatorTest {
    JsonFieldMapMutator jsonMetricBuilder = new JsonFieldMapMutator();

    @Test
    public void testBuildFieldMap() {
        String targetString =
                "{\"dataType\":{\"statusCode\":\"WORD\",\"userAgent\":\"WORD\",\"sizeByte\":\"NUMBER\"},\"dateFormat\":{\"timestamp\":{\"dateFormatType\":\"CUSTOM\",\"format\":\"dd/MMM/yyyy:HH:mm:ss Z\",\"timezone\":\"\"}},\"unit\":{\"requestTime\":\"MicroSecond\"},\"ignore\":[\"remoteUser\",\"remoteLogName\"]}";
        FieldMap fieldMap = jsonMetricBuilder.mutate(targetString);
        System.out.println(fieldMap);
        assertEquals("dd/MMM/yyyy:HH:mm:ss Z",
                fieldMap.get("dateFormat.timestamp.format"));
        Map<String, Object> fieldObjectMap = jsonMetricBuilder.mutate(
                targetString);
        System.out.println(fieldObjectMap);
    }
}