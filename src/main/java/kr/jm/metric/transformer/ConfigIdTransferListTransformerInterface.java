package kr.jm.metric.transformer;

import kr.jm.metric.config.mutating.MutatingConfig;
import kr.jm.metric.config.mutating.field.FieldMeta;
import kr.jm.metric.data.ConfigIdTransfer;
import kr.jm.metric.data.Transfer;
import kr.jm.utils.helper.JMPredicate;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The interface Config id transfer list transformer interface.
 *
 * @param <I> the type parameter
 * @param <O> the type parameter
 */
public interface ConfigIdTransferListTransformerInterface<I, O> extends
        Function<Transfer<I>, List<ConfigIdTransfer<O>>> {

    /**
     * Transform o.
     *
     * @param mutatingConfig the metric properties
     * @param data         the data
     * @return the o
     */
    O transform(MutatingConfig mutatingConfig, I data);

    /**
     * Gets input properties list.
     *
     * @param dataId the data id
     * @return the input properties list
     */
    List<MutatingConfig> getInputConfigList(String dataId);

    @Override
    default List<ConfigIdTransfer<O>> apply(Transfer<I> transfer) {
        return apply(transfer, getInputConfigList(transfer
                .getDataId()), Optional.ofNullable(transfer.getMeta())
                .orElseGet(Collections::emptyMap));
    }

    private List<ConfigIdTransfer<O>> apply(Transfer<I> transfer,
            List<MutatingConfig> mutatingConfigList, Map<String, Object> meta) {
        return mutatingConfigList.stream()
                .map(inputConfig -> newListConfigTransfer(transfer, meta,
                        inputConfig, inputConfig.getFieldConfig()))
                .filter(Objects::nonNull).collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    private ConfigIdTransfer<O> newListConfigTransfer(
            Transfer<I> transfer, Map<String, Object> meta,
            MutatingConfig mutatingConfig, FieldMeta fieldMeta) {
        return Optional
                .ofNullable(transform(mutatingConfig, transfer.getData()))
                .map(o -> transfer.newWith(o,
                        buildConfigTransferMeta(new HashMap<>(meta),
                                fieldMeta)))
                .map(t -> new ConfigIdTransfer<>(mutatingConfig.getConfigId(),
                        t))
                .orElse(null);
    }

    private Map<String, Object> buildConfigTransferMeta(
            Map<String, Object> meta, FieldMeta fieldMeta) {
        Optional.ofNullable(fieldMeta).map(FieldMeta::extractFieldMetaMap)
                .filter(JMPredicate.getGreaterMapSize(0))
                .ifPresent(extractFlatMap -> meta.put("field", extractFlatMap));
        return meta;
    }

}
