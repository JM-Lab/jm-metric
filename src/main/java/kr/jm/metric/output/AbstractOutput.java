package kr.jm.metric.output;

import kr.jm.metric.config.output.OutputConfigInterface;
import lombok.Getter;
import org.slf4j.Logger;

import java.util.Map;

public abstract class AbstractOutput implements OutputInterface {

    protected Logger log = org.slf4j.LoggerFactory.getLogger(getClass());
    @Getter
    protected String outputId;
    protected OutputConfigInterface outputConfig;

    /**
     * Instantiates a new Abstract output.
     *
     * @param outputConfig the output properties
     */
    public AbstractOutput(OutputConfigInterface outputConfig) {
        this.outputConfig = outputConfig;
        this.outputId = outputConfig.getOutputId();
    }

    public Map<String, Object> getConfig() {
        return outputConfig.extractConfigMap();
    }

    @Override
    public void close() {
        log.info("Start Output Closing - {}", toString());
        closeImpl();
        log.info("Finish Output Closing - {}", toString());
    }

    protected abstract void closeImpl();
}
