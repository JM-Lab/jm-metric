package kr.jm.metric.output;

import kr.jm.utils.helper.JMJson;
import kr.jm.utils.helper.JMStream;
import lombok.Getter;

import java.util.function.Function;

/**
 * The type Abstract string output.
 */
public abstract class AbstractStringOutput implements
        OutputInterface<Object> {

    /**
     * The String output config.
     */
    @Getter
    protected StringOutputConfig stringOutputConfig;

    /**
     * Instantiates a new Abstract string output.
     *
     * @param stringOutputConfig the string output config
     */
    public AbstractStringOutput(StringOutputConfig stringOutputConfig) {
        this.stringOutputConfig = stringOutputConfig;
    }

    /**
     * Instantiates a new Abstract string output.
     *
     * @param configId the config id
     */
    public AbstractStringOutput(String configId) {
        this(configId, false);
    }

    /**
     * Instantiates a new Abstract string output.
     *
     * @param configId         the config id
     * @param enableJsonString the enable json string
     */
    public AbstractStringOutput(String configId, boolean enableJsonString) {
        this(new StringOutputConfig(configId, enableJsonString));
    }

    /**
     * Is enable json string boolean.
     *
     * @return the boolean
     */
    public boolean isEnableJsonString() {
        return stringOutputConfig.isEnableJsonString();
    }

    /**
     * Gets config id.
     *
     * @return the config id
     */
    public String getConfigId() {return stringOutputConfig.getConfigId();}

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
