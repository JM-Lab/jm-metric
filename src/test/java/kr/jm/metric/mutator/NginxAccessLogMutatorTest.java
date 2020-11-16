package kr.jm.metric.mutator;

import kr.jm.metric.config.mutator.NginxAccessLogMutatorConfig;
import kr.jm.metric.config.mutator.field.FieldConfig;
import kr.jm.utils.helper.JMJson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

public class NginxAccessLogMutatorTest {

    private NginxAccessLogMutator nginxAccessLogMutator;

    @Before
    public void setUp() {
        this.nginxAccessLogMutator = new NginxAccessLogMutator(new NginxAccessLogMutatorConfig("NginxLogMutator",
                JMJson.getInstance()
                        .transform(Map.of("dataType", Map.of("bytesReceived", "NUMBER", "requestTime", "NUMBER")),
                                FieldConfig.class),
                "$remote_addr - $remote_user [$time_local] \"$request\" $status $body_bytes_sent \"$http_referer\" " +
                        "\"$http_user_agent\" $request_length $request_time.*"));
    }

    @Test
    public void testParser() {
        System.out.println(nginxAccessLogMutator);
        String targetString =
                "192.168.64.1 - - [05/Aug/2020:04:35:54 +0000] \"GET /_nodes HTTP/1.1\" 200 3593 \"http://head.dev.com/\" \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.105 Safari/537.36\" 446 0.017 [default-elasticsearch-es-http] [] 172.17.0.11:9200 3593 0.016 200 cf6a50bff4exception96ca363565cd807108226b";
        Map<String, Object> fieldObjectMap = nginxAccessLogMutator.mutate(targetString);
        System.out.println(fieldObjectMap);
        Assert.assertEquals(
                "{remoteUser=-, requestTime=0.017, request=GET /_nodes HTTP/1.1, referer=http://head.dev.com/, bytesReceived=446.0, sizeByte=3593, userAgent=Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.105 Safari/537.36, remoteAddr=192.168.64.1, timestamp=05/Aug/2020:04:35:54 +0000, statusCode=200}",
                fieldObjectMap.toString());
    }


}