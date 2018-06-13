package kr.jm.metric.input;

import kr.jm.metric.config.input.StdInLineInputConfig;
import kr.jm.utils.StdInLineConsumer;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * The type Abstract string output.
 */
public class StdLineInput extends AbstractInput {
    private StdInLineConsumer stdInLineConsumer;

    public StdLineInput(String inputId) {
        this(new StdInLineInputConfig(inputId));
    }

    public StdLineInput(StdInLineInputConfig inputConfig) {
        super(inputConfig);
    }

    @Override
    protected void startImpl(Consumer<List<String>> inputConsumer) {
        this.stdInLineConsumer =
                new StdInLineConsumer(s -> inputConsumer.accept(List.of(s)))
                        .consumeStdIn();
    }

    @Override
    protected void closeImpl() {
        Optional.ofNullable(this.stdInLineConsumer)
                .ifPresent(StdInLineConsumer::close);
    }

}
