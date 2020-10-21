package kr.jm.metric.config.mutator.field;

import kr.jm.utils.JMOptional;
import kr.jm.utils.JMString;
import kr.jm.utils.exception.JMException;
import kr.jm.utils.helper.JMScriptEvaluator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;

@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class FormulaFieldConfig extends CombinedFieldConfig {

    private String formula;
    private Number defaultResult;
    private String[] sortedTargetFields;
    private JMScriptEvaluator jmScriptEvaluator;

    public FormulaFieldConfig(String[] targetFields, String combinedFieldName, String delimiter, String formula,
            Number defaultResult) {
        super(targetFields, combinedFieldName, delimiter);
        this.formula = formula;
        this.defaultResult = defaultResult;
        this.jmScriptEvaluator = JMScriptEvaluator.getInstance();
    }

    @Override
    public Object buildValue(Map<String, Object> fieldObjectMap) {
        try {
            return JMScriptEvaluator.getInstance().eval(buildFormula(fieldObjectMap));
        } catch (Exception e) {
            return JMException
                    .handleExceptionAndReturn(log, e, "buildValue", () -> defaultResult, fieldObjectMap, formula);
        }
    }

    private String buildFormula(Map<String, Object> fieldObjectMap) {
        String formula = this.formula;
        for (String field : getSortedTargetFields())
            formula = buildFormula(fieldObjectMap, formula, field);
        return formula;
    }

    private String buildFormula(Map<String, Object> fieldObjectMap, String formula, String field) {
        return JMOptional.getOptional(fieldObjectMap, field).map(Object::toString).filter(JMString::isNumber)
                .map(number -> formula.replaceAll(field, number))
                .orElseThrow(() -> new RuntimeException("Fail To Build Formula !!!"));
    }

    public String[] getSortedTargetFields() {
        return Objects.requireNonNullElseGet(this.sortedTargetFields,
                () -> this.sortedTargetFields = buildSortedTargetFields());
    }

    private String[] buildSortedTargetFields() {
        return JMOptional.getOptional(getTargetFields()).stream().flatMap(Arrays::stream)
                .sorted(Comparator.reverseOrder()).toArray(String[]::new);
    }
}
