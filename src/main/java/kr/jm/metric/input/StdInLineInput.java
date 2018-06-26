package kr.jm.metric.input;

import kr.jm.metric.config.input.StdInLineInputConfig;
import kr.jm.metric.data.Transfer;
import kr.jm.utils.StdInLineConsumer;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * The type Abstract string output.
 */
public class StdInLineInput extends AbstractInput<StdInLineInputConfig> {
    private StdInLineConsumer stdInLineConsumer;

    public StdInLineInput(String inputId) {
        this(new StdInLineInputConfig(inputId));
    }

    public StdInLineInput(StdInLineInputConfig inputConfig) {
        super(inputConfig);
    }

    @Override
    protected void startImpl(Consumer<Transfer<String>> inputConsumer) {
        this.stdInLineConsumer =
                new StdInLineConsumer(s -> inputConsumer.accept(newTransfer(s)))
                        .consumeStdIn();
    }

    @Override
    protected void closeImpl() {
        Optional.ofNullable(this.stdInLineConsumer)
                .ifPresent(StdInLineConsumer::close);
    }

}
