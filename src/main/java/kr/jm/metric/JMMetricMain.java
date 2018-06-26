package kr.jm.metric;

import kr.jm.utils.enums.OS;
import kr.jm.utils.exception.JMExceptionManager;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

/**
 * The type Jm metric main.
 */
@Getter
@Slf4j
public class JMMetricMain {

    private JMMetric jmMetric;
    private String inputId = "StdIn";
    private String mutatingId;
    private String[] outputIds = {"StdOut"};

    /**
     * Main.
     *
     * @param args the args
     */
    public void main(String... args) {
        initArgs(args);

        this.jmMetric = new JMMetric(inputId, mutatingId, outputIds);
        this.jmMetric.printAllConfig();
        this.jmMetric.start();
        OS.addShutdownHook(jmMetric::close);
    }

    private void initArgs(String[] args) {
        if (args.length < 1) {
            String message =
                    "Wrong Args !!! - Args: [inputId=:default=StdIn] " +
                            "mutatingId> [outputIds...:default=StdOut]";
            System.err.println(message);
            JMExceptionManager.handleExceptionAndThrowRuntimeEx(log,
                    JMExceptionManager.newRunTimeException(message),
                    Arrays.toString(args));
        }
        if (args.length == 1) {
            this.mutatingId = args[0];
        } else if (args.length == 2) {
            this.inputId = args[0];
            this.mutatingId = args[1];
        }
        if (args.length > 2)
            this.outputIds = Arrays.stream(args).skip(2)
                    .toArray(value -> new String[value - 2]);
    }
}
