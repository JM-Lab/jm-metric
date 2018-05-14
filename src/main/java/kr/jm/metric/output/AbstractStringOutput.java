package kr.jm.metric.output;

import kr.jm.metric.output.config.StringOutputConfig;
import kr.jm.utils.helper.JMJson;
import kr.jm.utils.helper.JMStream;

import java.util.function.Function;

/**
 * The type Abstract string output.
 */
public abstract class AbstractStringOutput extends
        AbstractOutput<StringOutputConfig, Object> {

    /**
     * Instantiates a new Abstract string output.
     *
     * @param outputConfig the output config
     */
    public AbstractStringOutput(StringOutputConfig outputConfig) {
        super(outputConfig);
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
    public void writeData(Object data) {
        if (isEnableJsonString())
            writeToString(data, JMJson::toJsonString);
        else
            writeToString(data, Object::toString);
    }

    private void writeToString(Object data, Function<Object, String>
            toStringFunction) {
        JMStream.buildStream(data).map(toStringFunction::apply)
                .forEach(this::writeString);
    }

    /**
     * Write string.
     *
     * @param string the string
     */
    protected abstract void writeString(String string);
}
