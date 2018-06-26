package kr.jm.metric.output.subscriber;

import kr.jm.metric.config.output.OutputConfigInterface;
import kr.jm.metric.data.ConfigIdTransfer;
import kr.jm.metric.data.FieldMap;
import kr.jm.metric.output.FileOutput;
import kr.jm.metric.output.OutputInterface;
import kr.jm.metric.output.StdOutput;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * The type Output subscriber builder.
 */
public class OutputSubscriberBuilder {

    /**
     * Build file to json string output subscriber.
     *
     * @param filePath the file path
     * @return the output subscriber
     */
    public static OutputSubscriber buildFileOutput(String filePath) {
        return buildFileOutput(true, filePath);
    }

    public static OutputSubscriber buildFileOutput(
            boolean enableJsonString, String filePath) {
        return buildFileOutput(enableJsonString, filePath, null);
    }

    public static OutputSubscriber buildFileOutput(String filePath,
            Function<List<ConfigIdTransfer<FieldMap>>, List<Object>> transformOutputObjectFunction) {
        return buildFileOutput(true, filePath,
                transformOutputObjectFunction);
    }


    public static OutputSubscriber buildFileOutput(boolean enableJsonString,
            String filePath,
            Function<List<ConfigIdTransfer<FieldMap>>, List<Object>> transformOutputObjectFunction) {
        return build(new FileOutput(enableJsonString, filePath,
                transformOutputObjectFunction));
    }

    /**
     * Build std out to json string output subscriber.
     *
     * @return the output subscriber
     */
    public static OutputSubscriber buildStdOut() {
        return buildStdOut(true);
    }

    /**
     * Build std out abstract string output.
     *
     * @return the abstract string output
     */
    public static OutputSubscriber buildStdOut(boolean enableJsonString) {
        return buildStdOut(enableJsonString, null);
    }

    public static OutputSubscriber buildStdOut(
            Function<List<ConfigIdTransfer<FieldMap>>, List<Object>> transformOutputObjectFunction) {
        return buildStdOut(true, transformOutputObjectFunction);
    }


    public static OutputSubscriber buildStdOut(boolean enableJsonString,
            Function<List<ConfigIdTransfer<FieldMap>>, List<Object>> transformOutputObjectFunction) {
        return build(
                new StdOutput(enableJsonString, transformOutputObjectFunction));
    }


    public static OutputSubscriber buildOutput(String outputId,
            Consumer<List<ConfigIdTransfer<FieldMap>>> outputConsumer) {
        return new OutputSubscriber(new OutputInterface() {
            @Override
            public String getOutputId() {
                return outputId;
            }

            @Override
            public void writeData(List<ConfigIdTransfer<FieldMap>> data) {
                outputConsumer.accept(data);
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
