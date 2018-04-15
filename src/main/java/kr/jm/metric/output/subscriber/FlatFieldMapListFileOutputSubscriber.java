package kr.jm.metric.output.subscriber;

import kr.jm.metric.data.ConfigIdTransfer;
import kr.jm.metric.data.FieldMap;
import kr.jm.utils.helper.JMPath;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Flat field map list file output subscriber.
 */
public class FlatFieldMapListFileOutputSubscriber extends
        ListFileOutputSubscriber<ConfigIdTransfer<List<FieldMap>>> {

    /**
     * Instantiates a new Flat field map list file output subscriber.
     *
     * @param filePath the file path
     */
    public FlatFieldMapListFileOutputSubscriber(String filePath) {
        this(JMPath.getPath(filePath));
    }

    /**
     * Instantiates a new Flat field map list file output subscriber.
     *
     * @param filePath the file path
     */
    public FlatFieldMapListFileOutputSubscriber(Path filePath) {
        super(filePath,
                configIdDataTransfer -> configIdDataTransfer
                        .newStreamWith(configIdDataTransfer.getData())
                        .map(ConfigIdTransfer::buildFieldMapWithMeta)
                        .collect(Collectors.toList()));
    }
}
