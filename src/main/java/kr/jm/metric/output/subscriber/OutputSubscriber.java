package kr.jm.metric.output.subscriber;

import kr.jm.metric.data.Transfer;
import kr.jm.metric.output.OutputInterface;
import kr.jm.utils.JMString;
import kr.jm.utils.exception.JMException;
import kr.jm.utils.flow.subscriber.JMSubscriber;
import kr.jm.utils.helper.JMLog;
import lombok.Getter;

import java.util.List;
import java.util.Map;

public class OutputSubscriber extends JMSubscriber<List<Transfer<Map<String, Object>>>> implements AutoCloseable {

    @Getter
    protected String outputId;
    private final OutputInterface output;

    public OutputSubscriber(OutputInterface output) {
        super();
        this.outputId = output.getOutputId();
        this.output = output;
        setDataConsumer(this::output);
        JMLog.debug(log, "OutputSubscriber", outputId, output);
    }

    @Override
    public void close() {
        JMLog.info(log, "close", outputId);
        try {
            output.close();
        } catch (Exception e) {
            JMException.handleException(log, e, "close", outputId);
        }
    }

    private void output(List<Transfer<Map<String, Object>>> dataList) {
        JMLog.debug(log, "output", dataList.size() > 0 ? dataList.get(0).getInputId() : JMString.EMPTY, outputId,
                dataList.size());
        try {
            this.output.writeData(dataList);
        } catch (Exception e) {
            JMException.handleException(log, e, "output", outputId, dataList);
        }
    }

}
