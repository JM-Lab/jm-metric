package kr.jm.metric.mutator;

import kr.jm.metric.data.Transfer;
import kr.jm.utils.JMOptional;
import kr.jm.utils.time.JMTime;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public interface MutatorInterface extends Function<Transfer<String>, Transfer<Map<String, Object>>> {

    String META = "@meta";
    String PROCESS_TIMESTAMP = "@processTimestamp";
    String INPUT_ID = "inputId";

    @Override
    default Transfer<Map<String, Object>> apply(Transfer<String> inputTransfer) {
        return inputTransfer.newWith(JMOptional.getOptional(mutate(inputTransfer.getData()))
                .map(map -> buildFinalData(map, buildMeta(inputTransfer))).map(HashMap::new).orElse(null));
    }

    private Map<String, Object> buildFinalData(Map<String, Object> fieldObjectMap, Map<String, Object> meta) {
        fieldObjectMap.put(META, meta);
        return fieldObjectMap;
    }

    private Map<String, Object> buildMeta(Transfer<String> inputTransfer) {
        Map<String, Object> meta = new HashMap<>();
        JMOptional.getOptional(inputTransfer.getMeta()).ifPresent(meta::putAll);
        JMOptional.getOptional(getFieldMeta()).ifPresent(fieldMeta -> meta.put("field", fieldMeta));
        meta.put(INPUT_ID, inputTransfer.getInputId());
        meta.put(PROCESS_TIMESTAMP, JMTime.getInstance().getTime(inputTransfer.getTimestamp()));
        return meta;
    }

    Map<String, Object> mutate(String data);

    String getMutatorId();

    Map<String, Object> buildFieldObjectMap(String targetString);

    Map<String, Object> getFieldMeta();

}
