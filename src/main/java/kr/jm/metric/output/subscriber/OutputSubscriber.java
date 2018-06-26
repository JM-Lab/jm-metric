package kr.jm.metric.output.subscriber;

import kr.jm.metric.data.ConfigIdTransfer;
import kr.jm.metric.data.FieldMap;
import kr.jm.metric.output.OutputInterface;
import kr.jm.utils.exception.JMExceptionManager;
import kr.jm.utils.flow.subscriber.JMSubscriber;
import kr.jm.utils.helper.JMLog;
import kr.jm.utils.helper.JMOptional;
import lombok.Getter;

import java.util.List;

public class OutputSubscriber extends
        JMSubscriber<List<ConfigIdTransfer<FieldMap>>>
        implements AutoCloseable {

    @Getter
    protected String outputId;
    private OutputInterface output;

    /**
     * Instantiates a new Output subscriber.
     *
     * @param output the subscriber output
     */
    public OutputSubscriber(OutputInterface output) {
        super();
        this.outputId = output.getOutputId();
        this.output = output;
        setDataConsumer(this::write);
    }

    @Override
    public void close() {
        JMLog.info(log, "close", outputId);
        try {
            output.close();
        } catch (Exception e) {
            JMExceptionManager.logException(log, e, "close", outputId);
        }
    }

    private void write(List<ConfigIdTransfer<FieldMap>> data) {
        try {
            JMOptional.getOptional(data).ifPresentOrElse(this.output::writeData,
                    () -> JMLog.debug(log, "write", outputId, data));
        } catch (Exception e) {
            JMExceptionManager.logException(log, e, "write", outputId, data);
        }
    }

}
