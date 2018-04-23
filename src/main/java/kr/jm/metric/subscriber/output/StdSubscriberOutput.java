package kr.jm.metric.subscriber.output;

import lombok.extern.slf4j.Slf4j;

/**
 * The type Std subscriber output.
 */
@Slf4j
public class StdSubscriberOutput extends AbstractStringSubscriberOutput {

    /**
     * Instantiates a new Std subscriber output.
     */
    public StdSubscriberOutput() {
        this(false);
    }

    /**
     * Instantiates a new Std subscriber output.
     *
     * @param enableJsonString the enable json string
     */
    public StdSubscriberOutput(boolean enableJsonString) {
        super(enableJsonString);
    }

    @Override
    protected void writeString(String string) {
        System.out.println(string);
    }

    @Override
    public void close() {
    }
}
