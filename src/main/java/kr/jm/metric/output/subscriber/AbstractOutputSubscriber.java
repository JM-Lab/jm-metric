package kr.jm.metric.output.subscriber;

import kr.jm.utils.flow.subscriber.JMSubscriberBuilder;
import lombok.experimental.Delegate;

import java.util.concurrent.Flow;

/**
 * The type Abstract output subscriber.
 *
 * @param <T> the type parameter
 */
public abstract class AbstractOutputSubscriber<T> implements
        Flow.Subscriber<T> {
    @Delegate
    private Flow.Subscriber<T> subscriber;

    /**
     * Instantiates a new Abstract output subscriber.
     */
    public AbstractOutputSubscriber() {
        this.subscriber = JMSubscriberBuilder.build(this::consume);
    }

    /**
     * Consume.
     *
     * @param data the data
     */
    protected abstract void consume(T data);
}
