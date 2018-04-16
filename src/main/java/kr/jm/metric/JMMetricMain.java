package kr.jm.metric;

import kr.jm.metric.publisher.input.StdInLineInputPublisher;
import kr.jm.utils.enums.OS;
import kr.jm.utils.exception.JMExceptionManager;
import kr.jm.utils.flow.processor.JMTransformProcessorBuilder;
import kr.jm.utils.flow.subscriber.JMSubscriberBuilder;
import kr.jm.utils.helper.JMJson;
import kr.jm.utils.helper.JMOptional;
import kr.jm.utils.helper.JMString;
import kr.jm.utils.helper.object.ABCObjects;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

/**
 * The type Jm metric main.
 */
@Slf4j
public class JMMetricMain {

    private static final int DEFAULT_BULK_SIZE = 10;

    /**
     * Main.
     *
     * @param args the args
     */
    public void main(String... args) {
        ABCObjects<String, Integer, String> argsObjects = buildArgsObject(args);
        String dataId = "StdIn";
        StdInLineInputPublisher
                stdInLineInputPublisher =
                new StdInLineInputPublisher(dataId,
                        argsObjects.getB());
        JMMetric jmMetric =
                new JMMetric(stdInLineInputPublisher);
        JMOptional.getOptional(argsObjects.getC())
                .ifPresent(jmMetric::loadConfig);
        jmMetric.bindDataIdToConfigId(dataId, argsObjects.getA());
        jmMetric.subscribeAndReturnSubcriber(
                JMTransformProcessorBuilder.buildCollectionEach())
                .subscribe(JMSubscriberBuilder.getJsonStringSOPLSubscriber());
        stdInLineInputPublisher.start();
        log.info("==== Config List ====");
        log.info(JMJson.toPrettyJsonString(jmMetric.getConfigList()));
        log.info("==== DataId-ConfigIds ====");
        log.info(JMJson.toPrettyJsonString(jmMetric.getDataIdConfigIdSetMap()));
        OS.addShutdownHook(jmMetric::close);
    }

    private ABCObjects<String, Integer, String> buildArgsObject(String[] args) {
        if (args.length < 1) {
            String message =
                    "Wrong Args !!! - Args: <configId> [bulkSize] [configPath]";
            System.err.println(message);
            JMExceptionManager.handleExceptionAndThrowRuntimeEx(log,
                    JMExceptionManager.newRunTimeException(message),
                    Arrays.toString(args));
        }
        String configId = args[0];
        boolean isNumber = false;
        String arg1 = null;
        if (args.length >= 2) {
            arg1 = args[1];
            isNumber = JMString.isNumber(arg1);
        }
        return new ABCObjects<>(configId,
                isNumber ? Integer.valueOf(arg1) : DEFAULT_BULK_SIZE,
                !isNumber && args.length >= 3 ? args[2] : arg1);
    }
}
