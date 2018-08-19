package kr.jm.metric.output.subscriber;

import kr.jm.metric.config.output.OutputConfigInterface;
import kr.jm.metric.data.FieldMap;
import kr.jm.metric.data.Transfer;
import kr.jm.metric.output.FileOutput;
import kr.jm.metric.output.OutputInterface;
import kr.jm.metric.output.StdOutLineOutput;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * The type Output subscriber builder.
 */
public class OutputSubscriberBuilder {

    /**
     * Build file output output subscriber.
     *
     * @param filePath the file path
     * @return the output subscriber
     */
    public static OutputSubscriber buildFileOutput(String filePath) {
        return buildFileOutput(true, filePath);
    }

    /**
     * Build file output output subscriber.
     *
     * @param enableJsonString the enable json string
     * @param filePath         the file path
     * @return the output subscriber
     */
    public static OutputSubscriber buildFileOutput(
            boolean enableJsonString, String filePath) {
        return buildFileOutput(enableJsonString, filePath, null);
    }

    /**
     * Build file output output subscriber.
     *
     * @param filePath                      the file path
     * @param transformOutputObjectFunction the transform output object function
     * @return the output subscriber
     */
    public static OutputSubscriber buildFileOutput(String filePath,
            Function<List<Transfer<FieldMap>>, List<Object>> transformOutputObjectFunction) {
        return buildFileOutput(true, filePath,
                transformOutputObjectFunction);
    }


    /**
     * Build file output output subscriber.
     *
     * @param enableJsonString              the enable json string
     * @param filePath                      the file path
     * @param transformOutputObjectFunction the transform output object function
     * @return the output subscriber
     */
    public static OutputSubscriber buildFileOutput(boolean enableJsonString,
            String filePath,
            Function<List<Transfer<FieldMap>>, List<Object>> transformOutputObjectFunction) {
        return build(new FileOutput(enableJsonString, filePath,
                transformOutputObjectFunction));
    }

    /**
     * Build std out output subscriber.
     *
     * @return the output subscriber
     */
    public static OutputSubscriber buildStdOut() {
        return buildStdOut(true);
    }

    /**
     * Build std out output subscriber.
     *
     * @param enableJsonString the enable json string
     * @return the output subscriber
     */
    public static OutputSubscriber buildStdOut(boolean enableJsonString) {
        return buildStdOut(enableJsonString, null);
    }

    /**
     * Build std out output subscriber.
     *
     * @param transformOutputObjectFunction the transform output object function
     * @return the output subscriber
     */
    public static OutputSubscriber buildStdOut(
            Function<List<Transfer<FieldMap>>, List<Object>> transformOutputObjectFunction) {
        return buildStdOut(true, transformOutputObjectFunction);
    }


    /**
     * Build std out output subscriber.
     *
     * @param enableJsonString              the enable json string
     * @param transformOutputObjectFunction the transform output object function
     * @return the output subscriber
     */
    public static OutputSubscriber buildStdOut(boolean enableJsonString,
            Function<List<Transfer<FieldMap>>, List<Object>> transformOutputObjectFunction) {
        return build(
                new StdOutLineOutput(enableJsonString,
                        transformOutputObjectFunction));
    }


    /**
     * Build output subscriber.
     *
     * @param outputId       the output id
     * @param outputConsumer the output consumer
     * @return the output subscriber
     */
    public static OutputSubscriber build(String outputId,
            Consumer<List<Transfer<FieldMap>>> outputConsumer) {
        return new OutputSubscriber(new OutputInterface() {
            @Override
            public String getOutputId() {
                return outputId;
            }

            @Override
            public void writeData(
                    List<Transfer<FieldMap>> transferList) {
                outputConsumer.accept(transferList);
            }

            @Override
            public void close() {

            }
        });
    }

    /**
     * Build output subscriber.
     *
     * @param outputConfig the output config
     * @return the output subscriber
     */
    public static OutputSubscriber build(OutputConfigInterface outputConfig) {
        return new OutputSubscriber(outputConfig.buildOutput());
    }

    /**
     * Build output subscriber.
     *
     * @param output the output
     * @return the output subscriber
     */
    public static OutputSubscriber build(OutputInterface output) {
        return new OutputSubscriber(output);
    }
}
