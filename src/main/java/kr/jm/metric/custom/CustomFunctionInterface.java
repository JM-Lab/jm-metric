package kr.jm.metric.custom;

import kr.jm.metric.data.Transfer;

import java.util.Map;
import java.util.function.Function;

public interface CustomFunctionInterface extends
        Function<Transfer<Map<String, Object>>, Map<String, Object>> {
    String N_A = "N/A";
}
