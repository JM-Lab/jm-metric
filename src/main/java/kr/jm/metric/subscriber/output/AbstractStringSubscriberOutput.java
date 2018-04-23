package kr.jm.metric.subscriber.output;

import kr.jm.utils.helper.JMJson;
import kr.jm.utils.helper.JMStream;
import lombok.Getter;

import java.util.function.Function;

/**
 * The type Abstract string subscriber output.
 */
public abstract class AbstractStringSubscriberOutput implements
        SubscriberOutputInterface<Object> {

    @Getter
    private boolean enableJsonString;

    /**
     * Instantiates a new Abstract string subscriber output.
     */
    public AbstractStringSubscriberOutput() {
        this(false);
    }

    /**
     * Instantiates a new Abstract string subscriber output.
     *
     * @param enableJsonString the enable json string
     */
    public AbstractStringSubscriberOutput(boolean enableJsonString) {
        this.enableJsonString = enableJsonString;
    }


    @Override
    public void writeData(Object data) {
        if (enableJsonString)
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
