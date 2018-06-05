package kr.jm.metric.transformer;

import kr.jm.metric.config.mutating.MutatingConfig;
import kr.jm.metric.config.mutating.MutatingConfigManager;
import kr.jm.metric.data.FieldMap;
import kr.jm.utils.exception.JMExceptionManager;
import kr.jm.utils.helper.JMOptional;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * The type Field map properties id transfer list transformer.
 */
@Slf4j
public class FieldMapConfigIdTransferListTransformer implements
        ConfigIdTransferListTransformerInterface<String, FieldMap> {

    private MutatingConfigManager mutatingConfigManager;

    /**
     * Instantiates a new Field map properties id transfer list transformer.
     *
     * @param mutatingConfigManager the metric properties manager
     */
    public FieldMapConfigIdTransferListTransformer(
            MutatingConfigManager mutatingConfigManager) {
        this.mutatingConfigManager = mutatingConfigManager;
    }

    @Override
    public List<MutatingConfig> getInputConfigList(String dataId) {
        return mutatingConfigManager.getConfigListWithDataId(dataId);
    }

    @SuppressWarnings("unchecked")
    @Override
    public FieldMap transform(MutatingConfig mutatingConfig, String data) {
        try {
            return JMOptional.getOptional(
                    mutatingConfig
                            .getMetricBuilder()
                            .buildFieldMap(mutatingConfig, data))
                    .orElseThrow(() -> JMExceptionManager
                            .newRunTimeException("Can't Transform !!!"));
        } catch (Exception e) {
            return JMExceptionManager
                    .handleExceptionAndReturnNull(log, e, "transform",
                            mutatingConfig, data);
        }
    }

}
