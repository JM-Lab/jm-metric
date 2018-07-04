package kr.jm.metric;

import kr.jm.metric.data.ConfigIdTransfer;
import kr.jm.metric.data.FieldMap;
import kr.jm.utils.flow.subscriber.JMSubscriberBuilder;
import kr.jm.utils.helper.JMThread;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class JMMetricMainTest {
    static {
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "DEBUG");
    }

    private PrintWriter printWriter;

    @Before
    public void setUp() throws Exception {
        PipedInputStream pin = new PipedInputStream();
        this.printWriter = new PrintWriter(new PipedOutputStream(pin));
        System.setIn(pin);
    }

    @After
    public void tearDown() {
        printWriter.close();
    }

    @Test
    public void testMain1() {
        JMMetricMain jmMetricMain = new JMMetricMain();
        jmMetricMain.main();
    }

    @Test
    public void testMain2() {
        JMMetricMain jmMetricMain = new JMMetricMain();
        jmMetricMain.main("-h", "-m", "CombinedLogFormat");
    }

    @Test
    public void testMain3() {
        JMMetricMain jmMetricMain = new JMMetricMain();
        jmMetricMain.main("CombinedLogFormat");
    }

    @Test
    public void testMain() {
        JMMetricMain jmMetricMain = new JMMetricMain();
        jmMetricMain.main("-m", "CombinedLogFormat");
        List<List<ConfigIdTransfer<FieldMap>>> resultList = new ArrayList<>();
        jmMetricMain.getJmMetric()
                .subscribe(JMSubscriberBuilder.build(resultList::add));
        printWriter
                .println("141.248.111.36 - - [09/Apr/2018:18:03:52 +0900] " +
                        "\"POST /wp-content HTTP/1.0\" 200 4968 \"http://www.mccann.com/explore/about/\" \"Mozilla/5.0 (compatible; MSIE 7.0; Windows NT 6.0; Trident/5.0)\"");
        printWriter.flush();
        JMThread.sleep(2000);
        printWriter.println(
                "223.62.219.101 - - [08/Jun/2015:16:59:59 +0900] \"POST /app/5315 HTTP/1.1\" 200 1100 \"-\" \"Dalvik/1.6.0 (Linux; U; Android 4.4.2; SHV-E330S Build/KOT49H)\"");
        printWriter.flush();
        JMThread.sleep(2000);
        Assert.assertEquals(2, resultList.size());
        System.out.println(resultList);
    }
}