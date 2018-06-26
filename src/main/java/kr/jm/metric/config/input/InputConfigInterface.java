package kr.jm.metric.config.input;

import kr.jm.metric.config.PropertiesConfigInterface;
import kr.jm.metric.input.InputInterface;

public interface InputConfigInterface extends PropertiesConfigInterface {

    String getInputId();

    Integer getBulkSize();

    Integer getFlushIntervalSeconds();

    Long getWaitingMillis();

    Integer getQueueSizeLimit();

    InputConfigType getInputConfigType();

    <O extends InputInterface> O buildInput();
}