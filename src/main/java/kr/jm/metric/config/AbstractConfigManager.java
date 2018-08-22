package kr.jm.metric.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.jm.utils.enums.OS;
import kr.jm.utils.helper.JMResources;
import org.slf4j.Logger;

import java.util.Optional;

/**
 * The type Abstract config manager.
 *
 * @param <C> the type parameter
 */
public abstract class AbstractConfigManager<C extends ConfigInterface> {
    /**
     * The constant CONFIG_DIR.
     */
    public static final String CONFIG_DIR = Optional.ofNullable(
            JMResources.getSystemProperty("jm.metric.configDir"))
            .orElse("config");
    /**
     * The Log.
     */
    protected Logger log = org.slf4j.LoggerFactory.getLogger(getClass());
    protected static ObjectMapper ConfigObjectMapper = new ObjectMapper()
            .configure(JsonParser.Feature.ALLOW_COMMENTS, true);

    protected String buildDefaultConfigPath(String configFilename) {
        return CONFIG_DIR + OS.getFileSeparator() + configFilename;
    }

}
