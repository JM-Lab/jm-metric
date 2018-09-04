package kr.jm.metric.config.mutator.field;

import kr.jm.utils.exception.JMExceptionManager;
import kr.jm.utils.helper.JMString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum DataType {

    WORD, NUMBER, LONG;

    public Object transform(String data) {
        switch (this) {
            case NUMBER:
                return transformToNumber(data);
            case LONG:
                return transformToNumber(data).longValue();
            default:
                return data;
        }
    }

    private Double transformToNumber(String data) {
        return JMString.isNumber(data) ? Double
                .valueOf(data) : JMExceptionManager
                .handleExceptionAndReturn(log,
                        new RuntimeException("Wrong Number Format Occur !!!"),
                        "transformToNumber", () -> 0D, data);
    }
}
