package kr.jm.metric.mutating;

import kr.jm.metric.config.mutating.MutatingConfig;
import kr.jm.metric.data.FieldMap;
import kr.jm.utils.exception.JMExceptionManager;
import kr.jm.utils.helper.JMOptional;
import lombok.extern.slf4j.Slf4j;

/**
 * The type Field map properties id transfer list transformer.
 */
@Slf4j
public class FieldMapConfigIdTransferListMutating implements
        ConfigIdTransferListMutatingInterface<String, FieldMap> {

    private MutatingConfig mutatingConfig;

    /**
     * Instantiates a new Field map properties id transfer list transformer.
     *
     * @param mutatingConfig the metric properties manager
     */
    public FieldMapConfigIdTransferListMutating(
            MutatingConfig mutatingConfig) {
        this.mutatingConfig = mutatingConfig;
    }

    @Override
    public MutatingConfig getMutatingConfig() {
        return this.mutatingConfig;
    }

    @SuppressWarnings("unchecked")
    @Override
    public FieldMap transform(String data) {
        try {
            return JMOptional.getOptional(
                    mutatingConfig.getMutatingConfigType().getFieldMapBuilder()
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
