package kr.jm.metric.config.output;

import kr.jm.metric.config.PropertiesConfigInterface;
import kr.jm.metric.output.OutputInterface;

public interface OutputConfigInterface extends PropertiesConfigInterface {

    String getOutputId();

    OutputConfigType getOutputConfigType();

    <O extends OutputInterface> O buildOutput();
}
