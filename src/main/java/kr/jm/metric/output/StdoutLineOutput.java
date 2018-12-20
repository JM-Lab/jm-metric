package kr.jm.metric.output;

import kr.jm.metric.config.output.StdoutLineOutputConfig;
import kr.jm.metric.data.FieldMap;
import kr.jm.metric.data.Transfer;
import kr.jm.utils.helper.JMJson;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@ToString(callSuper = true)
public class StdoutLineOutput extends AbstractOutput {

    @Getter
    private boolean enableJsonString;
    private Function<List<Transfer<FieldMap>>, List<Object>>
            transformOutputObjectFunction;
    private Function<Object, String> toStringFunction;

    public StdoutLineOutput() {
        this(false);
    }

    public StdoutLineOutput(boolean enableJsonString) {
        this(enableJsonString, null);
    }

    public StdoutLineOutput(boolean enableJsonString,
            Function<List<Transfer<FieldMap>>, List<Object>> transformOutputObjectFunction) {
        this(new StdoutLineOutputConfig(enableJsonString),
                transformOutputObjectFunction);
    }

    public StdoutLineOutput(StdoutLineOutputConfig outputConfig) {
        this(outputConfig, null);
    }

    public StdoutLineOutput(StdoutLineOutputConfig outputConfig,
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

    protected void writeString(String string) {
        System.out.println(string);
    }

    @Override
    public void writeData(List<Transfer<FieldMap>> transferList) {
        transformOutputObjectFunction.apply(transferList).stream()
                .map(toStringFunction).forEach(this::writeString);
    }
}
