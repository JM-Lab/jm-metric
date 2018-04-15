package kr.jm.metric.input.publisher;

import kr.jm.metric.data.Transfer;
import kr.jm.utils.flow.publisher.JMSubmissionPublisher;
import kr.jm.utils.flow.publisher.JMSubmissionPublisherInterface;
import org.slf4j.Logger;

import java.util.concurrent.Flow;

/**
 * The type Transfer submission publisher.
 *
 * @param <T> the type parameter
 */
public class TransferSubmissionPublisher<T> implements
        TransferSubmissionPublisherInterface<T> {

    /**
     * The Log.
     */
    protected final Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

    private JMSubmissionPublisherInterface<Transfer<T>> jmSubmissionPublisher;

    /**
     * Instantiates a new Transfer submission publisher.
     */
    public TransferSubmissionPublisher() {
        this.jmSubmissionPublisher = new JMSubmissionPublisher<>();
    }

    @Override
    public int submit(Transfer<T> item) {
        return this.jmSubmissionPublisher.submit(item);
    }

    @Override
    public void subscribe(Flow.Subscriber<? super Transfer<T>> subscriber) {
        this.jmSubmissionPublisher.subscribe(subscriber);
    }

}
