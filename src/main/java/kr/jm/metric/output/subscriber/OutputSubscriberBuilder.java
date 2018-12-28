package kr.jm.metric.output.subscriber;

import kr.jm.metric.config.output.OutputConfigInterface;
import kr.jm.metric.data.Transfer;
import kr.jm.metric.output.FileOutput;
import kr.jm.metric.output.OutputInterface;
import kr.jm.metric.output.StdoutLineOutput;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class OutputSubscriberBuilder {

    public static OutputSubscriber buildFileOutput(String filePath) {
        return buildFileOutput(true, filePath);
    }

    public static OutputSubscriber buildFileOutput(
            boolean enableJsonString, String filePath) {
        return buildFileOutput(enableJsonString, filePath, null);
    }

    public static OutputSubscriber buildFileOutput(String filePath,
            Function<List<Transfer<Map<String, Object>>>, List<Object>> transformOutputObjectFunction) {
        return buildFileOutput(true, filePath,
                transformOutputObjectFunction);
    }


    public static OutputSubscriber buildFileOutput(boolean enableJsonString,
            String filePath,
            Function<List<Transfer<Map<String, Object>>>, List<Object>> transformOutputObjectFunction) {
        return build(new FileOutput(enableJsonString, filePath,
                transformOutputObjectFunction));
    }

    public static OutputSubscriber buildStdout() {
        return buildStdout(true);
    }

    public static OutputSubscriber buildStdout(boolean enableJsonString) {
        return buildStdout(enableJsonString, null);
    }

    public static OutputSubscriber buildStdout(
            Function<List<Transfer<Map<String, Object>>>, List<Object>> transformOutputObjectFunction) {
        return buildStdout(true, transformOutputObjectFunction);
    }


    public static OutputSubscriber buildStdout(boolean enableJsonString,
            Function<List<Transfer<Map<String, Object>>>, List<Object>> transformOutputObjectFunction) {
        return build(new StdoutLineOutput(enableJsonString,
                transformOutputObjectFunction));

    }


    public static OutputSubscriber build(String outputId,
            Consumer<List<Transfer<Map<String, Object>>>> outputConsumer) {
        return build(new OutputInterface() {
            @Override
            public String getOutputId() {
                return outputId;
            }

            @Override
            public void writeData(
                    List<Transfer<Map<String, Object>>> transferList) {
                outputConsumer.accept(transferList);
            }

            @Override
            public void close() {

            }
        });
    }

    public static OutputSubscriber build(OutputConfigInterface outputConfig) {
        return new OutputSubscriber(outputConfig.buildOutput());
    }

    public static OutputSubscriber build(OutputInterface output) {
        return new OutputSubscriber(output);
    }
}
