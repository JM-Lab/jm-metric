package kr.jm.metric.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.jm.utils.datastructure.JMCollections;
import kr.jm.utils.enums.OS;
import kr.jm.utils.exception.JMExceptionManager;
import kr.jm.utils.helper.JMJson;
import kr.jm.utils.helper.JMOptional;
import kr.jm.utils.helper.JMResources;
import kr.jm.utils.helper.JMRestfulResource;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The type Metric properties manager.
 */
@Slf4j
public class ConfigManager {
    private static ObjectMapper ConfigObjectMapper = new ObjectMapper()
            .configure(JsonParser.Feature.ALLOW_COMMENTS, true);
    public static final String CONFIG_DIR = Optional.ofNullable(
            JMResources.getSystemProperty("jm.metric.configDir"))
            .orElse("config");

    public static List<Map<String, Object>> buildAllConfigMapList(
            String configFilename) {
        return JMCollections.buildMergedList(buildConfigMapList(configFilename),
                buildConfigMapList(buildDefaultConfigPath(configFilename)));
    }

    private static String buildDefaultConfigPath(String configFilename) {
        return CONFIG_DIR + OS.getFileSeparator() + configFilename;
    }


    public static List<Map<String, Object>> buildConfigMapList(
            String jmMetricConfigUrl) {
        try {
            return ConfigObjectMapper.readValue(JMOptional.getOptional(
                    JMRestfulResource.getStringWithRestOrFilePathOrClasspath(
                            jmMetricConfigUrl))
                            .orElseThrow(NullPointerException::new),
                    JMJson.LIST_MAP_TYPE_REFERENCE);
        } catch (Exception e) {
            return JMExceptionManager.handleExceptionAndReturn(log, e,
                    "buildConfigMapList", Collections::emptyList,
                    jmMetricConfigUrl);
        }
    }

}
