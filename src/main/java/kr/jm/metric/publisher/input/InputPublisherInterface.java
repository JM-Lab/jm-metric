package kr.jm.metric.publisher.input;

import kr.jm.metric.publisher.TransferSubmissionPublisherInterface;

import java.util.List;

/**
 * The interface Input publisher interface.
 */
public interface InputPublisherInterface extends
        TransferSubmissionPublisherInterface<List<String>>, AutoCloseable {
    /**
     * Start.
     */
    void start();
}
