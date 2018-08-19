package kr.jm.metric.output;

import kr.jm.metric.config.output.OutputConfigInterface;
import lombok.Getter;
import org.slf4j.Logger;

import java.util.Map;

/**
 * The type Abstract output.
 */
public abstract class AbstractOutput implements OutputInterface {

    /**
     * The Log.
     */
    protected Logger log = org.slf4j.LoggerFactory.getLogger(getClass());
    /**
     * The Output id.
     */
    @Getter
    protected String outputId;
    /**
     * The Output config.
     */
    protected OutputConfigInterface outputConfig;

    /**
     * Instantiates a new Abstract output.
     *
     * @param outputConfig the output config
     */
    public AbstractOutput(OutputConfigInterface outputConfig) {
        this.outputConfig = outputConfig;
        this.outputId = outputConfig.getOutputId();
    }

    /**
     * Gets config.
     *
     * @return the config
     */
    public Map<String, Object> getConfig() {
        return outputConfig.extractConfigMap();
    }

    @Override
    public void close() {
        log.info("Start Output Closing - {}", toString());
        closeImpl();
        log.info("Finish Output Closing - {}", toString());
    }

    /**
     * Close.
     */
    protected abstract void closeImpl();

    @Override
    public String toString() {
        return "AbstractOutput{" + "outputId='" + outputId + '\'' +
                ", outputConfig=" + outputConfig + '}';
    }
}
