package kr.jm.metric.output;

import kr.jm.metric.config.output.StdOutputConfig;
import kr.jm.metric.data.ConfigIdTransfer;
import kr.jm.utils.helper.JMJson;
import kr.jm.utils.helper.JMStream;
import lombok.Getter;

import java.util.function.Function;

/**
 * The type Abstract string output.
 */
public class StdOutput<T> extends AbstractOutput<T> {

    private Function<Object, String> toStringFunction;
    @Getter
    private boolean enableJsonString;

    /**
     * Instantiates a new Abstract string output.
     *
     * @param outputConfig the output properties
     */
    public StdOutput(StdOutputConfig outputConfig) {
        super(outputConfig);
        this.enableJsonString = outputConfig.isEnableJsonString();
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

    @Override
    public void writeData(ConfigIdTransfer<T> data) {
        JMStream.buildStream(data.getData()).map(toStringFunction::apply)
                .forEach(this::writeString);
    }

    protected void writeString(String string) {
        System.out.println(string);
    }

}
