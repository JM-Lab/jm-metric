package kr.jm.metric.transformer;

import kr.jm.metric.config.ChunkType;
import kr.jm.metric.config.MetricConfig;
import kr.jm.metric.config.field.FieldMeta;
import kr.jm.metric.data.ConfigIdTransfer;
import kr.jm.metric.data.Transfer;
import kr.jm.utils.flow.TransformerInterface;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The interface Config id transfer list transformer interface 2.
 *
 * @param <I> the type parameter
 * @param <O> the type parameter
 */
public interface ConfigIdTransferListTransformerInterface2<I, O> extends
        TransformerInterface<Transfer<I>, List<ConfigIdTransfer<O>>> {

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
    default List<ConfigIdTransfer<O>> transform(Transfer<I> transfer) {
        return transform(transfer, getInputConfigList(transfer
                .getDataId()), Optional.ofNullable(transfer.getMeta())
                .orElseGet(Collections::emptyMap));
    }

    private List<ConfigIdTransfer<O>> transform(Transfer<I> transfer,
            List<MetricConfig> metricConfigList, Map<String, Object> meta) {
        return metricConfigList.stream().flatMap(
                inputConfig -> buildConfigTransferStream(transfer, meta,
                        inputConfig, inputConfig.getFieldConfig()))
                .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    private Stream<ConfigIdTransfer<O>> buildConfigTransferStream(
            Transfer<I> transfer, Map<String, Object> meta,
            MetricConfig metricConfig, FieldMeta fieldMeta) {
        return buildDataStream(transfer.getData(), metricConfig.getChunkType())
                .map(data -> transform(metricConfig, data))
                .filter(Objects::nonNull).map(o -> transfer.newWith(o, meta))
                .map(t -> new ConfigIdTransfer<>(metricConfig.getConfigId(),
                        fieldMeta, t));
    }

    private Stream<I> buildDataStream(I data, ChunkType metricConfig) {
        return Optional.ofNullable(metricConfig).map(chunkType -> chunkType
                .buildChunk(data.toString()))
                .map(list -> list.stream().map(o -> (I) o))
                .orElseGet(() -> Stream.of(data));
    }

}
