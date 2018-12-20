package kr.jm.metric.output;

import kr.jm.metric.data.FieldMap;
import kr.jm.metric.data.Transfer;

import java.util.List;


public interface OutputInterface extends AutoCloseable {
    String getOutputId();

    void writeData(List<Transfer<FieldMap>> transferList);
}
