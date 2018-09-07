package kr.jm.metric.output;

import kr.jm.metric.config.output.StdOutLineOutputConfig;
import kr.jm.metric.data.FieldMap;
import kr.jm.metric.data.Transfer;
import kr.jm.utils.helper.JMJson;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The type Std out line output.
 */
@ToString(callSuper = true)
public class StdOutLineOutput extends AbstractOutput {

    @Getter
    private boolean enableJsonString;
    private Function<List<Transfer<FieldMap>>, List<Object>>
            transformOutputObjectFunction;
    private Function<Object, String> toStringFunction;

    /**
     * Instantiates a new Std out line output.
     */
    public StdOutLineOutput() {
        this(false);
    }

    /**
     * Instantiates a new Std out line output.
     *
     * @param enableJsonString the enable json string
     */
    public StdOutLineOutput(boolean enableJsonString) {
        this(enableJsonString, null);
    }

    /**
     * Instantiates a new Std out line output.
     *
     * @param enableJsonString              the enable json string
     * @param transformOutputObjectFunction the transform output object function
     */
    public StdOutLineOutput(boolean enableJsonString,
            Function<List<Transfer<FieldMap>>, List<Object>> transformOutputObjectFunction) {
        this(new StdOutLineOutputConfig(enableJsonString),
                transformOutputObjectFunction);
    }

    /**
     * Instantiates a new Std out line output.
     *
     * @param outputConfig the output config
     */
    public StdOutLineOutput(StdOutLineOutputConfig outputConfig) {
        this(outputConfig, null);
    }

    /**
     * Instantiates a new Std out line output.
     *
     * @param outputConfig                  the output config
     * @param transformOutputObjectFunction the transform output object function
     */
    public StdOutLineOutput(StdOutLineOutputConfig outputConfig,
            Function<List<Transfer<FieldMap>>, List<Object>>
                    transformOutputObjectFunction) {
        super(outputConfig);
        this.enableJsonString = outputConfig.isEnableJsonString();
        this.transformOutputObjectFunction =
                Optional.ofNullable(transformOutputObjectFunction)
                        .orElseGet(() -> list -> list.stream()
                                .map(Transfer::getData)
                                .collect(Collectors.toList()));
        this.toStringFunction = outputConfig
                .isEnableJsonString() ? JMJson::toJsonString : Object::toString;
    }

    @Override
    protected void closeImpl() {

    }

    /**
     * Write string.
     *
     * @param string the string
     */
    protected void writeString(String string) {
        System.out.println(string);
    }

    @Override
    public void writeData(List<Transfer<FieldMap>> transferList) {
        transformOutputObjectFunction.apply(transferList).stream()
                .map(toStringFunction).forEach(this::writeString);
    }
}
