package kr.jm.metric.input.publisher;

import kr.jm.metric.config.input.InputConfigInterface;
import kr.jm.metric.input.InputInterface;
import kr.jm.metric.publisher.StringBulkTransferSubmissionPublisher;
import kr.jm.utils.exception.JMExceptionManager;
import kr.jm.utils.helper.JMLog;
import lombok.Getter;

import java.util.Optional;

import static kr.jm.utils.flow.publisher.BulkSubmissionPublisher.DEFAULT_BULK_SIZE;
import static kr.jm.utils.flow.publisher.BulkSubmissionPublisher.DEFAULT_FLUSH_INTERVAL_SECONDS;

public class InputPublisher extends
        StringBulkTransferSubmissionPublisher implements AutoCloseable {

    @Getter
    protected String dataId;
    protected InputInterface input;

    public InputPublisher(InputConfigInterface inputConfig,
            InputInterface input) {
        this(inputConfig.getDataId(), inputConfig.getBulkSize(),
                inputConfig.getFlushIntervalSeconds(), input);
    }

    public InputPublisher(String dataId, Integer bulkSize,
            Integer flushIntervalSeconds, InputInterface input) {
        super(Optional.ofNullable(bulkSize).orElse(DEFAULT_BULK_SIZE),
                Optional.ofNullable(flushIntervalSeconds)
                        .orElse(DEFAULT_FLUSH_INTERVAL_SECONDS));
        this.dataId = dataId;
        this.input = input;
    }

    public void start() {
        JMLog.info(log, "start");
        input.start(data -> submit(dataId, data));
    }

    @Override
    public void close() {
        JMLog.info(log, "close");
        try {
            input.close();
        } catch (Exception e) {
            JMExceptionManager.logException(log, e, "close");
        }
    }
}
