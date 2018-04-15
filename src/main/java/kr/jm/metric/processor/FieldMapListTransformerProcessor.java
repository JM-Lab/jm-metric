package kr.jm.metric.processor;

import kr.jm.metric.data.ConfigIdTransfer;
import kr.jm.metric.data.FieldMap;
import kr.jm.utils.flow.processor.JMTransformProcessor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Field map list transformer processor.
 */
public class FieldMapListTransformerProcessor extends
        JMTransformProcessor<ConfigIdTransfer<List<FieldMap>>, List<FieldMap>> {

    /**
     * Instantiates a new Field map list transformer processor.
     */
    public FieldMapListTransformerProcessor() {
        super(configIdTransfer -> configIdTransfer
                .newStreamWith(configIdTransfer.getData())
                .map(ConfigIdTransfer::buildFieldMapWithMeta)
                .collect(Collectors.toList()));
    }

}
