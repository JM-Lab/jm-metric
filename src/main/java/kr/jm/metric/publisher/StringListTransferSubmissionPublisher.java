package kr.jm.metric.publisher;

import kr.jm.metric.data.Transfer;

import java.util.List;
import java.util.concurrent.Flow;

/**
 * The type String list transfer submission publisher.
 */
public class StringListTransferSubmissionPublisher implements
        StringListTransferSubmissionPublisherInterface {
    private TransferSubmissionPublisher<List<String>>
            transferSubmissionPublisher;

    /**
     * Instantiates a new String list transfer submission publisher.
     */
    public StringListTransferSubmissionPublisher() {
        this(new TransferSubmissionPublisher<>());
    }

    /**
     * Instantiates a new String list transfer submission publisher.
     *
     * @param transferSubmissionPublisher the transfer submission publisher
     */
    public StringListTransferSubmissionPublisher(
            TransferSubmissionPublisher<List<String>> transferSubmissionPublisher) {
        this.transferSubmissionPublisher = transferSubmissionPublisher;
    }

    @Override
    public int submit(Transfer<List<String>> item) {
        return item.getData().size() > 0 ? transferSubmissionPublisher
                .submit(item) : 0;
    }

    @Override
    public void subscribe(
            Flow.Subscriber<? super Transfer<List<String>>> subscriber) {
        transferSubmissionPublisher.subscribe(subscriber);
    }


}
