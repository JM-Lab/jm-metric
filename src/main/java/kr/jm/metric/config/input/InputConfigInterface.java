package kr.jm.metric.config.input;

import kr.jm.metric.config.PropertiesConfigInterface;
import kr.jm.metric.input.InputInterface;

public interface InputConfigInterface extends PropertiesConfigInterface {

    Integer getBulkSize();

    Long getFlushIntervalMillis();

    Long getWaitingMillis();

    Integer getMaxBufferCapacity();

    String getInputId();

    ChunkType getChunkType();

    InputConfigType getInputConfigType();

    <O extends InputInterface> O buildInput();
}