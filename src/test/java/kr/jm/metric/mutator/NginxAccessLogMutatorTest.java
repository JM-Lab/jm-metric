package kr.jm.metric.mutator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

public class NginxAccessLogMutatorTest {

    private NginxAccessLogMutator nginxAccessLogMutator;

    @Before
    public void setUp() {
        this.nginxAccessLogMutator = new NginxAccessLogMutator();
    }

    @Test
    public void testParser() {
        System.out.println(nginxAccessLogMutator);
        String targetString =
                "127.0.0.1 - frank [10/Oct/2000:13:55:36 -0700] \"GET /apache_pb.gif HTTP/1.0\" 200 2326 \"http://www.example.com/start.html\" \"Mozilla/4.08 [en] (Win98; I ;Nav)\"";
        Map<String, Object> fieldObjectMap =
                nginxAccessLogMutator.mutate(targetString);
        System.out.println(fieldObjectMap);
        Assert.assertEquals(
                "{remoteUser=frank, request=GET /apache_pb.gif HTTP/1.0, referer=http://www.example.com/start.html, sizeByte=2326, userAgent=Mozilla/4.08 [en] (Win98; I ;Nav), remoteAddr=127.0.0.1, timestamp=10/Oct/2000:13:55:36 -0700, statusCode=200}",
                fieldObjectMap.toString());
    }


}