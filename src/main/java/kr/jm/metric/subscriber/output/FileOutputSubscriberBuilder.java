package kr.jm.metric.subscriber.output;

import kr.jm.utils.helper.JMJson;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The type File output subscriber builder.
 */
public class FileOutputSubscriberBuilder {

    /**
     * Build file output subscriber.
     *
     * @param <T>               the type parameter
     * @param filePath          the file path
     * @param transformFunction the transform function
     * @return the file output subscriber
     */
    public static <T> FileOutputSubscriber<T> build(String filePath,
            Function<T, ?> transformFunction) {
        return new FileOutputSubscriber<>(filePath, transformFunction);
    }

    /**
     * Build json string file output subscriber.
     *
     * @param <T>               the type parameter
     * @param filePath          the file path
     * @param transformFunction the transform function
     * @return the file output subscriber
     */
    public static <T> FileOutputSubscriber<T> buildJsonString(String filePath,
            Function<T, ?> transformFunction) {
        return build(filePath,
                o -> JMJson.toJsonString(transformFunction.apply(o)));
    }

    /**
     * Build string list file output subscriber.
     *
     * @param <T>                   the type parameter
     * @param filePath              the file path
     * @param listTransformFunction the list transform function
     * @return the file output subscriber
     */
    public static <T> FileOutputSubscriber<T> buildStringList(
            String filePath, Function<T, List<?>> listTransformFunction) {
        return new ListFileOutputSubscriber<>(filePath, listTransformFunction);
    }

    /**
     * Build json string list file output subscriber.
     *
     * @param <T>                   the type parameter
     * @param filePath              the file path
     * @param listTransformFunction the list transform function
     * @return the file output subscriber
     */
    public static <T> FileOutputSubscriber<T> buildJsonStringList(
            String filePath, Function<T, List<?>> listTransformFunction) {
        return buildStringList(filePath,
                o -> listTransformFunction.apply(o).stream()
                        .map(JMJson::toJsonString)
                        .collect(Collectors.toList()));
    }

    /**
     * Build json string file output subscriber.
     *
     * @param <T>      the type parameter
     * @param filePath the file path
     * @return the file output subscriber
     */
    public static <T> FileOutputSubscriber<T> buildJsonString(String filePath) {
        return buildJsonString(filePath, Function.identity());
    }

    /**
     * Build string file output subscriber.
     *
     * @param <T>      the type parameter
     * @param filePath the file path
     * @return the file output subscriber
     */
    public static <T> FileOutputSubscriber<T> buildString(String filePath) {
        return build(filePath, Function.identity());
    }

}
