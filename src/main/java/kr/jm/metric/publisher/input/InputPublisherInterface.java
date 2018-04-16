package kr.jm.metric.publisher.input;

import kr.jm.metric.publisher.TransferSubmissionPublisherInterface;

import java.util.List;

public interface InputPublisherInterface extends
        TransferSubmissionPublisherInterface<List<String>>, AutoCloseable {
    void start();
}
