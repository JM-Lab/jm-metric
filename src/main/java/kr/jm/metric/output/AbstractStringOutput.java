package kr.jm.metric.output;

import kr.jm.metric.data.ConfigIdTransfer;
import kr.jm.metric.output.config.StringOutputConfig;
import kr.jm.utils.helper.JMJson;
import kr.jm.utils.helper.JMStream;

import java.util.function.Function;

/**
 * The type Abstract string output.
 */
public abstract class AbstractStringOutput<T> extends
        AbstractOutput<StringOutputConfig, T> {

    private Function<Object, String> toStringFunction;

    /**
     * Instantiates a new Abstract string output.
     *
     * @param outputConfig the output config
     */
    public AbstractStringOutput(StringOutputConfig outputConfig) {
        super(outputConfig);
        this.toStringFunction = outputConfig
                .isEnableJsonString() ? JMJson::toJsonString : Object::toString;
    }

    /**
     * Instantiates a new Abstract string output.
     */
    public AbstractStringOutput() {
        this(false);
    }

    /**
     * Instantiates a new Abstract string output.
     *
     * @param enableJsonString the enable json string
     */
    public AbstractStringOutput(boolean enableJsonString) {
        this(new StringOutputConfig(enableJsonString));
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

    /**
     * Write string.
     *
     * @param string the string
     */
    protected abstract void writeString(String string);
}
