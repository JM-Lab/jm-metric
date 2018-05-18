package kr.jm.metric.transformer;

import kr.jm.metric.config.MetricConfig;
import kr.jm.metric.config.MetricConfigManager;
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

    private MetricConfigManager metricConfigManager;

    /**
     * Instantiates a new Field map properties id transfer list transformer.
     *
     * @param metricConfigManager the metric properties manager
     */
    public FieldMapConfigIdTransferListTransformer(
            MetricConfigManager metricConfigManager) {
        this.metricConfigManager = metricConfigManager;
    }

    @Override
    public List<MetricConfig> getInputConfigList(String dataId) {
        return metricConfigManager.getConfigListWithDataId(dataId);
    }

    @SuppressWarnings("unchecked")
    @Override
    public FieldMap transform(MetricConfig metricConfig, String data) {
        try {
            return JMOptional.getOptional(
                    metricConfig.getMetricBuilder().buildFieldMap(metricConfig, data))
                    .orElseThrow(() -> JMExceptionManager
                            .newRunTimeException("Can't Transform !!!"));
        } catch (Exception e) {
            return JMExceptionManager
                    .handleExceptionAndReturnNull(log, e, "transform",
                            metricConfig, data);
        }
    }

}
