package kr.jm.metric.output;

import kr.jm.metric.config.output.StdOutputConfig;
import kr.jm.metric.data.ConfigIdTransfer;
import kr.jm.metric.data.FieldMap;
import kr.jm.utils.helper.JMJson;
import lombok.Getter;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The type Abstract string output.
 */
public class StdOutLineOutput extends AbstractOutput {

    @Getter
    private boolean enableJsonString;
    private Function<List<ConfigIdTransfer<FieldMap>>, List<Object>>
            transformOutputObjectFunction;
    private Function<Object, String> toStringFunction;

    /**
     * Instantiates a new Abstract string output.
     */
    public StdOutLineOutput() {
        this(false);
    }

    public StdOutLineOutput(boolean enableJsonString) {
        this(enableJsonString, null);
    }

    public StdOutLineOutput(boolean enableJsonString,
            Function<List<ConfigIdTransfer<FieldMap>>, List<Object>> transformOutputObjectFunction) {
        this(new StdOutputConfig(enableJsonString),
                transformOutputObjectFunction);
    }

    /**
     * Instantiates a new Abstract string output.
     *
     * @param outputConfig the output properties
     */
    public StdOutLineOutput(StdOutputConfig outputConfig) {
        this(outputConfig, null);
    }

    public StdOutLineOutput(StdOutputConfig outputConfig,
            Function<List<ConfigIdTransfer<FieldMap>>, List<Object>>
                    transformOutputObjectFunction) {
        super(outputConfig);
        this.enableJsonString = outputConfig.isEnableJsonString();
        this.transformOutputObjectFunction =
                Optional.ofNullable(transformOutputObjectFunction)
                        .orElseGet(() -> list -> list.stream()
                                .map(ConfigIdTransfer::getData)
                                .collect(Collectors.toList()));
        this.toStringFunction = outputConfig
                .isEnableJsonString() ? JMJson::toJsonString : Object::toString;
    }

    @Override
    protected void closeImpl() {

    }

    protected void writeString(String string) {
        System.out.println(string);
    }

    @Override
    public void writeData(List<ConfigIdTransfer<FieldMap>> data) {
        transformOutputObjectFunction.apply(data).stream()
                .map(toStringFunction::apply).forEach(this::writeString);
    }
}
