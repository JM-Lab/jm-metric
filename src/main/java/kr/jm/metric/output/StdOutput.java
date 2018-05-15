package kr.jm.metric.output;

import lombok.extern.slf4j.Slf4j;

/**
 * The type Std output.
 */
@Slf4j
public class StdOutput<T> extends AbstractStringOutput<T> {

    /**
     * Instantiates a new Std output.
     */
    public StdOutput() {
        this(false);
    }

    /**
     * Instantiates a new Std output.
     *
     * @param enableJsonString the enable json string
     */
    public StdOutput(boolean enableJsonString) {
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
