package kr.jm.metric.input;

import kr.jm.metric.config.input.StdinLineInputConfig;
import kr.jm.metric.data.Transfer;
import kr.jm.utils.StdInLineConsumer;
import lombok.ToString;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * The type Std in line input.
 */
@ToString(callSuper = true)
public class StdinLineInput extends AbstractInput<StdinLineInputConfig> {
    private StdInLineConsumer stdInLineConsumer;

    /**
     * Instantiates a new Std in line input.
     *
     * @param inputId the input id
     */
    public StdinLineInput(String inputId) {
        this(new StdinLineInputConfig(inputId));
    }

    /**
     * Instantiates a new Std in line input.
     *
     * @param inputConfig the input config
     */
    public StdinLineInput(StdinLineInputConfig inputConfig) {
        super(inputConfig);
    }

    @Override
    protected void startImpl(Consumer<Transfer<String>> inputConsumer) {
        this.stdInLineConsumer =
                new StdInLineConsumer(s -> inputConsumer.accept(newTransfer
                        (s))).consumeStdIn();
    }

    @Override
    protected void closeImpl() {
        Optional.ofNullable(this.stdInLineConsumer)
                .ifPresent(StdInLineConsumer::close);
    }

}