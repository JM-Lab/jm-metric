package kr.jm.metric.config.mutator.field;

import kr.jm.utils.JavascriptEvaluator;
import kr.jm.utils.exception.JMExceptionManager;
import kr.jm.utils.helper.JMOptional;
import kr.jm.utils.helper.JMString;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * The type Formula field config.
 */
@Getter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class FormulaFieldConfig extends CombinedFieldConfig {

    private String numberType;
    private String formula;
    private Number defaultResult;

    @Override
    public Object buildValue(Map<String, Object> fieldObjectMap) {
        try {
            return JavascriptEvaluator.eval(buildFormula(fieldObjectMap));
        } catch (Exception e) {
            return JMExceptionManager.handleExceptionAndReturn(log, e,
                    "buildValue", () -> defaultResult, fieldObjectMap, formula);
        }
    }

    private String buildFormula(Map<String, Object> fieldObjectMap) {
        String formula = this.formula;
        for (String field : getTargetFields())
            formula = buildFormula(fieldObjectMap, formula, field);
        return formula;
    }

    private String buildFormula(Map<String, Object> fieldObjectMap,
            String formula, String field) {
        return JMOptional.getOptional(fieldObjectMap, field)
                .map(Object::toString).filter(JMString::isNumber)
                .map(number -> formula.replaceAll(field, number))
                .orElseThrow(() -> new RuntimeException(
                        "Fail To Build Formula !!!"));
    }


}
