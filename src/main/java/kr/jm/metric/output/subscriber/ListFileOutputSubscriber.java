package kr.jm.metric.output.subscriber;

import kr.jm.utils.helper.JMPath;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Function;

/**
 * The type List file output subscriber.
 *
 * @param <T> the type parameter
 */
@Slf4j
public class ListFileOutputSubscriber<T> extends
        FileOutputSubscriber<T> {

    /**
     * Instantiates a new List file output subscriber.
     *
     * @param filePath              the file path
     * @param listTransformFunction the list transform function
     */
    public ListFileOutputSubscriber(String filePath,
            Function<T, List<?>> listTransformFunction) {
        this(JMPath.getPath(filePath), listTransformFunction);
    }

    /**
     * Instantiates a new List file output subscriber.
     *
     * @param path              the path
     * @param transformFunction the transform function
     */
    public ListFileOutputSubscriber(Path path,
            Function<T, List<?>> transformFunction) {
        super(path, transformFunction);
    }

    @Override
    protected void consume(T data) {
        ((List<?>) transformFunction.apply(data))
                .forEach(this::appendData);
    }

}
