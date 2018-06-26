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
        Function<List<Transfer<I>>, List<ConfigIdTransfer<O>>> {

    O transform(I data);

    MutatingConfig getMutatingConfig();

    @Override
    default List<ConfigIdTransfer<O>> apply(List<Transfer<I>> transferList) {
        return transferList.stream().map(this::buildConfigIdTransferAsOpt)
                .flatMap(Optional::stream).collect(Collectors.toList());
    }

    private Optional<ConfigIdTransfer<O>> buildConfigIdTransferAsOpt(
            Transfer<I> transfer) {
        return Optional.ofNullable(transform(transfer.getData()))
                .map(o -> transfer.newWith(o, buildConfigTransferMeta(
                        new HashMap<>(Optional.ofNullable(transfer.getMeta())
                                .orElseGet(Collections::emptyMap)),
                        getMutatingConfig().getFieldConfig())))
                .map(t -> new ConfigIdTransfer<>(transfer.getInputId(), t));
    }

    private Map<String, Object> buildConfigTransferMeta(
            Map<String, Object> meta, FieldMeta fieldMeta) {
        Optional.ofNullable(fieldMeta).map(FieldMeta::extractFieldMetaMap)
                .filter(JMPredicate.getGreaterMapSize(0))
                .ifPresent(extractFlatMap -> meta.put("field", extractFlatMap));
        return meta;
    }

}
