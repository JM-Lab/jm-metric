package kr.jm.metric.config.mutator.field;

import kr.jm.utils.JMOptional;
import kr.jm.utils.JMString;
import lombok.*;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CombinedFieldConfig {
    public static final String DEFAULT_DELIMITER = JMString.UNDERSCORE;
    private static final String N_A = "[N/A]";
    private String[] targetFields;
    private String combinedFieldName;
    private String delimiter;

    public String getCombinedFieldName() {
        return JMOptional.getOptional(this.combinedFieldName).orElseGet(
                () -> this.combinedFieldName =
                        JMString.joiningWith(getDelimiter(),
                                this.targetFields));
    }

    public String getDelimiter() {
        return Objects.requireNonNullElseGet(this.delimiter,
                () -> this.delimiter = DEFAULT_DELIMITER);
    }

    public Object buildValue(Map<String, Object> fieldObjectMap) {
        return Stream.of(this.targetFields)
                .map(field -> JMOptional.getOptional(fieldObjectMap, field)
                        .map(Object::toString).orElse(N_A))
                .collect(Collectors.joining(getDelimiter()));
    }
}
