package kr.jm.metric.output;

import kr.jm.metric.data.FieldMap;
import kr.jm.metric.data.Transfer;

import java.util.List;


/**
 * The interface Output interface.
 */
public interface OutputInterface extends AutoCloseable {
    /**
     * Gets output id.
     *
     * @return the output id
     */
    String getOutputId();

    /**
     * Write data.
     *
     * @param transferList the transfer list
     */
    void writeData(List<Transfer<FieldMap>> transferList);
}
