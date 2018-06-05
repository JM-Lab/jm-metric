package kr.jm.metric.transformer;

import kr.jm.metric.config.mutating.ChunkType;
import kr.jm.metric.config.mutating.MutatingConfig;
import kr.jm.metric.config.mutating.MutatingConfigManager;
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
     * @param mutatingConfigManager the metric properties manager
     */
    public FieldMapListConfigIdTransferListTransformer(
            MutatingConfigManager mutatingConfigManager) {
        this.inputTransformer =
                new FieldMapConfigIdTransferListTransformer(
                        mutatingConfigManager);
    }

    @Override
    public List<MutatingConfig> getInputConfigList(String dataId) {
        return this.inputTransformer.getInputConfigList(dataId);
    }

    @Override
    public List<FieldMap> transform(MutatingConfig mutatingConfig,
            List<String> dataList) {
        try {
            return dataList.stream().flatMap(
                    data -> buildDataStream(data,
                            mutatingConfig.getChunkType()))
                    .map(data -> inputTransformer
                            .transform(mutatingConfig, data))
                    .filter(Objects::nonNull).collect(Collectors.toList());
        } catch (Exception e) {
            return JMExceptionManager
                    .handleExceptionAndReturn(log, e, "transform",
                            Collections::emptyList, mutatingConfig,
                            dataList.size());
        }
    }

    private Stream<String> buildDataStream(String data, ChunkType chunkType) {
        return Optional.ofNullable(chunkType)
                .map(cType -> cType.buildChunk(data))
                .map(List::stream).orElseGet(() -> Stream.of(data));
    }

}
