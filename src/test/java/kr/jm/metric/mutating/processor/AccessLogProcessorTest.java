package kr.jm.metric.mutating.processor;

import kr.jm.utils.JMWordSplitter;
import kr.jm.utils.flow.processor.JMConcurrentTransformProcessor;
import kr.jm.utils.flow.subscriber.JMSubscriber;
import kr.jm.utils.flow.subscriber.JMSubscriberBuilder;
import kr.jm.utils.helper.JMResources;
import kr.jm.utils.helper.JMThread;
import kr.jm.utils.stats.collector.WordNumberCollector;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.atomic.LongAdder;

public class AccessLogProcessorTest {
    private static final String FileName = "webAccessLogSample.txt";
    private JMConcurrentTransformProcessor<List<String>, WordNumberCollector>
            singleTransformWithThreadPoolProcessor;
    private LongAdder count;

    @Before
    public void setUp() {
        this.count = new LongAdder();
        this.singleTransformWithThreadPoolProcessor =
                new JMConcurrentTransformProcessor<>(
                        list -> {
                            WordNumberCollector wordNumberCollector = new
                                    WordNumberCollector(FileName);
                            for (int i = 0; i < list.size(); i++) {
                                String key = String.valueOf(i);
                                String data = list.get(i);
                                wordNumberCollector.addData(key, data);
                            }
                            count.increment();
                            return wordNumberCollector;
                        });
    }

    @After
    public void tearDown() {
        this.singleTransformWithThreadPoolProcessor.close();
    }

    @Test
    public void testAccessLogProcessor() {
        JMSubscriber<WordNumberCollector> finalSubscriber =
                JMSubscriberBuilder.build(wordNumberCollector -> {
                    System.out.println(wordNumberCollector);
                    count.increment();
                });
        singleTransformWithThreadPoolProcessor.subscribe(finalSubscriber);
        JMResources.readLines(FileName).stream()
                .map(JMWordSplitter::splitAsList)
                .forEach(singleTransformWithThreadPoolProcessor::onNext);

        JMThread.sleep(1000);
        Assert.assertEquals(2048, count.intValue());
    }
}
