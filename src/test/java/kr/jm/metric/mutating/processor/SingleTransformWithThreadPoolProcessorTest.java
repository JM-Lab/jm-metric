package kr.jm.metric.mutating.processor;

import kr.jm.utils.flow.processor.JMConcurrentTransformProcessor;
import kr.jm.utils.flow.subscriber.JMSubscriberBuilder;
import kr.jm.utils.helper.JMThread;
import org.junit.Assert;
import org.junit.Test;

public class SingleTransformWithThreadPoolProcessorTest {

    @Test
    public void testTransformerProcessor() {
        StringBuilder result = new StringBuilder();
        JMConcurrentTransformProcessor<String, String>
                singleTransformWithThreadPoolProcessor1 =
                new JMConcurrentTransformProcessor<>(
                        s -> s + " step 1 -> ");
        JMConcurrentTransformProcessor<String, String>
                singleTransformWithThreadPoolProcessor2 =
                new JMConcurrentTransformProcessor<>(
                        s -> s + " step 2 -> ");
        singleTransformWithThreadPoolProcessor1
                .subscribe(singleTransformWithThreadPoolProcessor2);
        singleTransformWithThreadPoolProcessor2
                .subscribe(JMSubscriberBuilder
                        .getSOPLSubscriber(
                                s -> result.append(s + "complete !!! ")));
        singleTransformWithThreadPoolProcessor1.onNext("test1");
        singleTransformWithThreadPoolProcessor1.onNext("test2");
        JMThread.sleep(1000);
        Assert.assertEquals(
                "test1 step 1 ->  step 2 -> complete !!! test2 step 1 -> " +
                        " step 2 -> complete !!! ", result.toString());
    }
}