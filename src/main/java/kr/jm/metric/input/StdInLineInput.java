package kr.jm.metric.input;

import kr.jm.metric.config.input.StdInLineInputConfig;
import kr.jm.metric.data.Transfer;
import kr.jm.utils.StdInLineConsumer;
import lombok.ToString;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * The type Std in line input.
 */
@ToString(callSuper = true)
public class StdInLineInput extends AbstractInput<StdInLineInputConfig> {
    private StdInLineConsumer stdInLineConsumer;

    /**
     * Instantiates a new Std in line input.
     *
     * @param inputId the input id
     */
    public StdInLineInput(String inputId) {
        this(new StdInLineInputConfig(inputId));
    }

    /**
     * Instantiates a new Std in line input.
     *
     * @param inputConfig the input config
     */
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
