package kr.jm.metric.transformer;

import kr.jm.metric.config.MetricConfig;
import kr.jm.metric.config.field.FieldMeta;
import kr.jm.metric.data.ConfigIdTransfer;
import kr.jm.metric.data.Transfer;

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
     * @param metricConfig the metric config
     * @param data         the data
     * @return the o
     */
    O transform(MetricConfig metricConfig, I data);

    /**
     * Gets input config list.
     *
     * @param dataId the data id
     * @return the input config list
     */
    List<MetricConfig> getInputConfigList(String dataId);

    @Override
    default List<ConfigIdTransfer<O>> apply(Transfer<I> transfer) {
        return apply(transfer, getInputConfigList(transfer
                .getDataId()), Optional.ofNullable(transfer.getMeta())
                .orElseGet(Collections::emptyMap));
    }

    private List<ConfigIdTransfer<O>> apply(Transfer<I> transfer,
            List<MetricConfig> metricConfigList, Map<String, Object> meta) {
        return metricConfigList.stream()
                .map(inputConfig -> newListConfigTransfer(transfer, meta,
                        inputConfig, inputConfig.getFieldConfig()))
                .filter(Objects::nonNull).collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    private ConfigIdTransfer<O> newListConfigTransfer(
            Transfer<I> transfer, Map<String, Object> meta,
            MetricConfig metricConfig, FieldMeta fieldMeta) {
        return Optional.ofNullable(transform(metricConfig, transfer.getData()))
                .map(o -> transfer.newWith(o, meta))
                .map(t -> new ConfigIdTransfer<>(metricConfig.getConfigId(),
                        fieldMeta, t)).orElse(null);
    }

}
