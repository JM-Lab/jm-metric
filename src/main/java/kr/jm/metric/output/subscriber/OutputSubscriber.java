package kr.jm.metric.output.subscriber;

import kr.jm.metric.data.ConfigIdTransfer;
import kr.jm.metric.output.OutputInterface;
import kr.jm.utils.exception.JMExceptionManager;
import kr.jm.utils.flow.subscriber.JMSubscriber;
import kr.jm.utils.helper.JMLog;

import java.util.Objects;

/**
 * The type Output subscriber.
 *
 * @param <T> the type parameter
 */
public class OutputSubscriber<T> extends JMSubscriber<ConfigIdTransfer<T>>
        implements AutoCloseable {

    private OutputInterface<T> subscriberOutput;

    /**
     * Instantiates a new Output subscriber.
     *
     * @param subscriberOutput the subscriber output
     */
    public OutputSubscriber(OutputInterface<T> subscriberOutput) {
        this.subscriberOutput = subscriberOutput;
        setDataConsumer(this::write);
    }

    @Override
    public void close() {
        JMLog.info(log, "close");
        try {
            subscriberOutput.close();
        } catch (Exception e) {
            JMExceptionManager.logException(log, e, "close");
        }
    }

    private void write(ConfigIdTransfer<T> data) {
        if (Objects.isNull(data)) {
            JMLog.debug(log, "write", data);
            return;
        }
        try {
            this.subscriberOutput.writeData(data);
        } catch (Exception e) {
            JMExceptionManager.logException(log, e, "write", data);
        }
    }

}
