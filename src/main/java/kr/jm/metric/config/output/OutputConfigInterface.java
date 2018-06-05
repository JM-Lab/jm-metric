package kr.jm.metric.config.output;

import kr.jm.metric.config.MapConfigInterface;
import kr.jm.metric.output.OutputInterface;

public interface OutputConfigInterface extends MapConfigInterface {

    OutputConfigType getOutputConfigType();

    <O extends OutputInterface> O buildOutput();
}
