package kr.jm.metric;

import kr.jm.metric.output.subscriber.OutputSubscriberBuilder;
import kr.jm.metric.publisher.input.StdInLineInputPublisher;
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
    private StdInLineInputPublisher stdInLineInputPublisher;

    /**
     * Main.
     *
     * @param args the args
     */
    public void main(String... args) {
        ABCObjects<String, String, Integer> argsObjects = buildArgsObject(args);
        String dataId = "StdIn";
        this.stdInLineInputPublisher =
                new StdInLineInputPublisher(dataId, argsObjects.getC());
        this.jmMetric = new JMMetric(stdInLineInputPublisher);
        jmMetric.bindDataIdToConfigId(dataId, argsObjects.getA());
        jmMetric.subscribe(OutputSubscriberBuilder
                .build(jmMetric.getOutput(argsObjects.getB())));
        stdInLineInputPublisher.start();
        log.info("==== Config List ====");
        log.info(JMJson.toPrettyJsonString(jmMetric.getConfigList()));
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
