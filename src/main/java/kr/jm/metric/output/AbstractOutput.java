package kr.jm.metric.output;

import kr.jm.metric.config.output.OutputConfigInterface;
import org.slf4j.Logger;

import java.util.Map;

public abstract class AbstractOutput<T> implements OutputInterface<T> {

    protected Logger log = org.slf4j.LoggerFactory.getLogger(getClass());
    protected OutputConfigInterface outputConfig;

    /**
     * Instantiates a new Abstract output.
     *
     * @param outputConfig the output properties
     */
    public AbstractOutput(OutputConfigInterface outputConfig) {
        this.outputConfig = outputConfig;
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
