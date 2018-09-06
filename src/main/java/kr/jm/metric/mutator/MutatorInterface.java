package kr.jm.metric.mutator;

import kr.jm.metric.data.FieldMap;
import kr.jm.metric.data.Transfer;
import kr.jm.utils.helper.JMOptional;
import kr.jm.utils.time.JMTimeUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * The interface Mutator interface.
 */
public interface MutatorInterface extends
        Function<Transfer<String>, Transfer<FieldMap>> {

    @Override
    default Transfer<FieldMap> apply(Transfer<String> inputTransfer) {
        return inputTransfer.newWith(
                JMOptional.getOptional(mutate(inputTransfer.getData()))
                        .map(map -> buildFinalFieldMap(map,
                                buildMeta(inputTransfer))).orElse(null));
    }

    private FieldMap buildFinalFieldMap(Map<String, Object> fieldObjectMap,
            Map<String, Object> meta) {
        fieldObjectMap.put("meta", meta);
        return new FieldMap(fieldObjectMap);
    }

    private Map<String, Object> buildMeta(Transfer<String> inputTransfer) {
        Map<String, Object> meta = new HashMap<>();
        JMOptional.getOptional(inputTransfer.getMeta())
                .ifPresent(meta::putAll);
        JMOptional.getOptional(getFieldMeta())
                .ifPresent(fieldMeta -> meta.put("field", fieldMeta));
        meta.put("inputId", inputTransfer.getInputId());
        meta.put("processTimestamp",
                JMTimeUtil.getTimeAsDefaultUtcFormat(inputTransfer
                        .getTimestamp()));
        return meta;
    }

    /**
     * Mutate map.
     *
     * @param data the data
     * @return the map
     */
    Map<String, Object> mutate(String data);

    /**
     * Gets mutator id.
     *
     * @return the mutator id
     */
    String getMutatorId();

    /**
     * Build field object map map.
     *
     * @param targetString the target string
     * @return the map
     */
    Map<String, Object> buildFieldObjectMap(String targetString);

    /**
     * Gets field meta.
     *
     * @return the field meta
     */
    Map<String, Object> getFieldMeta();

}
