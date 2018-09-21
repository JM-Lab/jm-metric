package kr.jm.metric.config.input;

import kr.jm.metric.config.PropertiesConfigInterface;
import kr.jm.metric.input.InputInterface;

/**
 * The interface Input config interface.
 */
public interface InputConfigInterface extends PropertiesConfigInterface {

    Integer getBulkSize();

    Long getFlushIntervalMillis();

    Long getWaitingMillis();

    Integer getMaxBufferCapacity();

    String getInputId();

    ChunkType getChunkType();

    InputConfigType getInputConfigType();

    /**
     * Build input o.
     *
     * @param <O> the type parameter
     * @return the o
     */
    <O extends InputInterface> O buildInput();
}