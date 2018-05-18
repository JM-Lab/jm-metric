package kr.jm.metric.output;

import kr.jm.metric.data.ConfigIdTransfer;
import kr.jm.metric.output.config.StdOutputConfig;
import kr.jm.utils.helper.JMJson;
import kr.jm.utils.helper.JMStream;

import java.util.function.Function;

/**
 * The type Abstract string output.
 */
public class StdOutput<T> extends AbstractOutput<StdOutputConfig, T> {

    private Function<Object, String> toStringFunction;

    /**
     * Instantiates a new Abstract string output.
     *
     * @param outputConfig the output properties
     */
    public StdOutput(StdOutputConfig outputConfig) {
        super(outputConfig);
        this.toStringFunction = outputConfig
                .isEnableJsonString() ? JMJson::toJsonString : Object::toString;
    }

    @Override
    protected void closeImpl() {

    }

    /**
     * Instantiates a new Abstract string output.
     */
    public StdOutput() {
        this(false);
    }

    /**
     * Instantiates a new Abstract string output.
     *
     * @param enableJsonString the enable json string
     */
    public StdOutput(boolean enableJsonString) {
        this(new StdOutputConfig(enableJsonString));
    }

    /**
     * Is enable json string boolean.
     *
     * @return the boolean
     */
    public boolean isEnableJsonString() {
        return outputConfig.isEnableJsonString();
    }

    @Override
    public void writeData(ConfigIdTransfer<T> data) {
        JMStream.buildStream(data.getData()).map(toStringFunction::apply)
                .forEach(this::writeString);
    }

    protected void writeString(String string) {
        System.out.println(string);
    }

}
