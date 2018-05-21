package kr.jm.metric.transformer;

import kr.jm.metric.config.ChunkType;
import kr.jm.metric.config.MetricConfig;
import kr.jm.metric.config.MetricConfigManager;
import kr.jm.metric.data.FieldMap;
import kr.jm.utils.exception.JMExceptionManager;
import org.slf4j.Logger;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The type Field map list properties id transfer list transformer.
 */
public class FieldMapListConfigIdTransferListTransformer implements
        ConfigIdTransferListTransformerInterface<List<String>, List<FieldMap>> {

    private static final Logger log = org.slf4j.LoggerFactory
            .getLogger(FieldMapListConfigIdTransferListTransformer.class);
    private FieldMapConfigIdTransferListTransformer inputTransformer;

    /**
     * Instantiates a new Field map list properties id transfer list transformer.
     *
     * @param metricConfigManager the metric properties manager
     */
    public FieldMapListConfigIdTransferListTransformer(
            MetricConfigManager metricConfigManager) {
        this.inputTransformer =
                new FieldMapConfigIdTransferListTransformer(
                        metricConfigManager);
    }

    @Override
    public List<MetricConfig> getInputConfigList(String dataId) {
        return this.inputTransformer.getInputConfigList(dataId);
    }

    @Override
    public List<FieldMap> transform(MetricConfig metricConfig,
            List<String> dataList) {
        try {
            return dataList.stream().flatMap(
                    data -> buildDataStream(data, metricConfig.getChunkType()))
                    .map(data -> inputTransformer.transform(metricConfig, data))
                    .filter(Objects::nonNull).collect(Collectors.toList());
        } catch (Exception e) {
            return JMExceptionManager
                    .handleExceptionAndReturn(log, e, "transform",
                            Collections::emptyList, metricConfig,
                            dataList.size());
        }
    }

    private Stream<String> buildDataStream(String data, ChunkType chunkType) {
        return Optional.ofNullable(chunkType)
                .map(cType -> cType.buildChunk(data))
                .map(List::stream).orElseGet(() -> Stream.of(data));
    }

}
