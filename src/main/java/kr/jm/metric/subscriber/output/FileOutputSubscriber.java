package kr.jm.metric.subscriber.output;

import kr.jm.metric.subscriber.AbstractOutputSubscriber;
import kr.jm.utils.JMFileAppender;
import kr.jm.utils.helper.JMPath;
import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.nio.file.Path;
import java.util.function.Function;

/**
 * The type File output subscriber.
 *
 * @param <T> the type parameter
 */
@Slf4j
public class FileOutputSubscriber<T> extends
        AbstractOutputSubscriber<T> implements Closeable {
    private JMFileAppender fileAppender;
    /**
     * The Transform function.
     */
    protected Function<T, ?> transformFunction;

    /**
     * Instantiates a new File output subscriber.
     *
     * @param filePath          the file path
     * @param transformFunction the transform function
     */
    public FileOutputSubscriber(String filePath,
            Function<T, ?> transformFunction) {
        this(JMPath.getPath(filePath), transformFunction);
    }

    /**
     * Instantiates a new File output subscriber.
     *
     * @param path              the path
     * @param transformFunction the transform function
     */
    public FileOutputSubscriber(Path path, Function<T, ?> transformFunction) {
        this.fileAppender = new JMFileAppender(path);
        this.transformFunction = transformFunction;
    }

    @Override
    public void close() {
        fileAppender.close();
    }

    @Override
    protected void consume(T data) {
        appendData(transformFunction.apply(data));
    }

    /**
     * Append data.
     *
     * @param data the data
     */
    protected void appendData(Object data) {
        fileAppender.appendLine(transformToString(data));
    }

    /**
     * Transform to string string.
     *
     * @param data the data
     * @return the string
     */
    protected String transformToString(Object data) {
        return data.toString();
    }
}
