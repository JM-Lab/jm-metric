package kr.jm.metric.output;

import kr.jm.metric.data.ConfigIdTransfer;
import kr.jm.metric.data.FieldMap;

import java.util.List;


public interface OutputInterface extends AutoCloseable {
    String getOutputId();
    /**
     * Write data.
     *
     * @param data the data
     */
    void writeData(List<ConfigIdTransfer<FieldMap>> data);
}
