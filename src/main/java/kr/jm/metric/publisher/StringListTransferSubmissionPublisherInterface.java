package kr.jm.metric.publisher;

import kr.jm.utils.helper.JMFiles;
import kr.jm.utils.helper.JMOptional;
import kr.jm.utils.helper.JMPath;
import kr.jm.utils.helper.JMResources;

import java.io.File;
import java.util.List;

/**
 * The interface String list transfer submission publisher interface.
 */
public interface StringListTransferSubmissionPublisherInterface extends
        TransferSubmissionPublisherInterface<List<String>> {

    /**
     * Input file path.
     *
     * @param filePath the file path
     */
    default void inputFilePath(String filePath) {
        inputFilePath(filePath, filePath);
    }

    /**
     * Input file path.
     *
     * @param dataId   the data id
     * @param filePath the file path
     */
    default void inputFilePath(String dataId, String filePath) {
        inputFile(dataId, JMPath.getPath(filePath).toFile());
    }

    /**
     * Input file.
     *
     * @param file the file
     */
    default void inputFile(File file) {
        inputFile(file.getAbsolutePath(), file);
    }

    /**
     * Input file.
     *
     * @param dataId the data id
     * @param file   the file
     */
    default void inputFile(String dataId, File file) {
        submit(dataId, JMFiles.readLines(file));
    }

    /**
     * Input classpath.
     *
     * @param resourceClasspath the resource classpath
     */
    default void inputClasspath(String resourceClasspath) {
        submit(resourceClasspath, JMResources.readLines(resourceClasspath));
    }

    /**
     * Input classpath.
     *
     * @param dataId            the data id
     * @param resourceClasspath the resource classpath
     */
    default void inputClasspath(String dataId,
            String resourceClasspath) {
        submit(dataId, JMResources.readLines(resourceClasspath));
    }

    /**
     * Input file path or classpath.
     *
     * @param filePathOrResourceClasspath the file path or resource classpath
     */
    default void inputFilePathOrClasspath(
            String filePathOrResourceClasspath) {
        inputFilePathOrClasspath(filePathOrResourceClasspath,
                filePathOrResourceClasspath);
    }

    /**
     * Input file path or classpath.
     *
     * @param dataId                      the data id
     * @param filePathOrResourceClasspath the file path or resource classpath
     */
    default void inputFilePathOrClasspath(String dataId,
            String filePathOrResourceClasspath) {
        submit(dataId, JMOptional
                .getOptional(JMFiles.readLines(filePathOrResourceClasspath))
                .orElseGet(() -> JMResources
                        .readLines(filePathOrResourceClasspath)));
    }

}
