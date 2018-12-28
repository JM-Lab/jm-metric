package kr.jm.metric.output;

import kr.jm.metric.data.Transfer;

import java.util.List;
import java.util.Map;


public interface OutputInterface extends AutoCloseable {
    String getOutputId();

    void writeData(List<Transfer<Map<String, Object>>> transferList);
}
