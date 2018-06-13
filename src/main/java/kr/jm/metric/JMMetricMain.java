package kr.jm.metric;

import kr.jm.metric.input.StdLineInput;
import kr.jm.metric.input.publisher.InputPublisher;
import kr.jm.metric.input.publisher.InputPublisherBuilder;
import kr.jm.utils.enums.OS;
import kr.jm.utils.exception.JMExceptionManager;
import kr.jm.utils.helper.JMJson;
import kr.jm.utils.helper.JMString;
import kr.jm.utils.helper.object.ABCObjects;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

/**
 * The type Jm metric main.
 */
@Slf4j
public class JMMetricMain {

    private static final int DEFAULT_BULK_SIZE = 10;
    @Getter
    private JMMetric jmMetric;
    @Getter
    private InputPublisher stdLineInputPublisher;

    /**
     * Main.
     *
     * @param args the args
     */
    public void main(String... args) {
        ABCObjects<String, String, Integer> argsObjects = buildArgsObject(args);
        String inputId = "StdIn";
        this.stdLineInputPublisher =
                InputPublisherBuilder.build(inputId, argsObjects.getC(), null,
                        new StdLineInput(inputId));
        this.jmMetric =
                new JMMetric(stdLineInputPublisher, argsObjects.getB());
        jmMetric.bindDataIdToConfigId(stdLineInputPublisher.getDataId(),
                argsObjects.getA());
        stdLineInputPublisher.start();
        log.info("==== Config List ====");
        log.info(JMJson.toPrettyJsonString(
                jmMetric.getInputConfigManager().getConfigMap()));
        log.info("==== DataId-ConfigIds ====");
        log.info(JMJson.toPrettyJsonString(jmMetric.getDataIdConfigIdSetMap()));
        log.info("==== OutputConfigMap ====");
        log.info(JMJson.toPrettyJsonString(jmMetric.getOutputConfigMap()));
        OS.addShutdownHook(jmMetric::close);
    }

    private ABCObjects<String, String, Integer> buildArgsObject(String[] args) {
        if (args.length < 1) {
            String message =
                    "Wrong Args !!! - Args: <configId> [outputConfigId:default=StdOut] [bulkSize]";
            System.err.println(message);
            JMExceptionManager.handleExceptionAndThrowRuntimeEx(log,
                    JMExceptionManager.newRunTimeException(message),
                    Arrays.toString(args));
        }
        String configId = args[0];
        String outputConfigId = "StdOut";
        int bulkSize = DEFAULT_BULK_SIZE;
        if (args.length == 2) {
            if (JMString.isNumber(args[1]))
                bulkSize = Integer.valueOf(args[1]);
            else
                outputConfigId = args[1];
        } else if (args.length >= 3) {
            outputConfigId = args[1];
            bulkSize = JMString.isNumber(args[2]) ? Integer
                    .valueOf(args[2]) : bulkSize;
        }
        return new ABCObjects<>(configId, outputConfigId, bulkSize);
    }
}
