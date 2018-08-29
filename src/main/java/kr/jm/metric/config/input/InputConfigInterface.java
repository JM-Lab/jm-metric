package kr.jm.metric.config.input;

import kr.jm.metric.config.PropertiesConfigInterface;
import kr.jm.metric.input.InputInterface;

/**
 * The interface Input config interface.
 */
public interface InputConfigInterface extends PropertiesConfigInterface {

    /**
     * Gets input id.
     *
     * @return the input id
     */
    String getInputId();

    /**
     * Gets bulk size.
     *
     * @return the bulk size
     */
    Integer getBulkSize();

    /**
     * Gets flush interval seconds.
     *
     * @return the flush interval seconds
     */
    Integer getFlushIntervalSeconds();

    /**
     * Gets waiting millis.
     *
     * @return the waiting millis
     */
    Long getWaitingMillis();

    /**
     * Gets queue size limit.
     *
     * @return the queue size limit
     */
    Integer getQueueSizeLimit();

    /**
     * Gets input config type.
     *
     * @return the input config type
     */
    InputConfigType getInputConfigType();

    /**
     * Gets chunk type.
     *
     * @return the chunk type
     */
    ChunkType getChunkType();

    /**
     * Build input o.
     *
     * @param <O> the type parameter
     * @return the o
     */
    <O extends InputInterface> O buildInput();
}