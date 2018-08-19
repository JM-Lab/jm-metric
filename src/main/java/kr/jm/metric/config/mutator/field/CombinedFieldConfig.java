package kr.jm.metric.config.mutator.field;

import kr.jm.utils.helper.JMOptional;
import kr.jm.utils.helper.JMString;
import lombok.*;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The type Combined field config.
 */
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CombinedFieldConfig {
    /**
     * The constant DEFAULT_DELIMITER.
     */
    public static final String DEFAULT_DELIMITER = JMString.UNDERSCORE;
    /**
     * The constant N_A.
     */
    public static final String N_A = "[N/A]";
    private String[] targetFields;
    private String combinedFieldName;
    private String delimiter;

    /**
     * Gets combined field name.
     *
     * @return the combined field name
     */
    public String getCombinedFieldName() {
        return JMOptional.getOptional(this.combinedFieldName).orElseGet(
                () -> this.combinedFieldName =
                        JMString.joiningWithDelimiter(getDelimiter(),
                                this.targetFields));
    }

    /**
     * Gets delimiter.
     *
     * @return the delimiter
     */
    public String getDelimiter() {
        return Objects.requireNonNullElseGet(this.delimiter,
                () -> this.delimiter = DEFAULT_DELIMITER);
    }

    /**
     * Build value object.
     *
     * @param fieldObjectMap the field object map
     * @return the object
     */
    public Object buildValue(Map<String, Object> fieldObjectMap) {
        return Stream.of(this.targetFields)
                .map(field -> JMOptional.getOptional(fieldObjectMap, field)
                        .map(Object::toString).orElse(N_A))
                .collect(Collectors.joining(getDelimiter()));
    }
}
