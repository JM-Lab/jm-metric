package kr.jm.metric.config.mutating;

import kr.jm.utils.helper.JMJson;
import kr.jm.utils.helper.JMString;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The enum Chunk type.
 */
public enum ChunkType {
    /**
     * Lines chunk type.
     */
    LINES,
    /**
     * Json list chunk type.
     */
    JSON_LIST;

    /**
     * Build chunk list.
     *
     * @param data the data
     * @return the list
     */
    public List<String> buildChunk(String data) {
        switch (this) {
            case LINES:
                return Arrays.asList(data.split(JMString.LINE_SEPARATOR));
            case JSON_LIST:
                return JMJson.toList(data).stream().map(JMJson::toJsonString)
                        .collect(Collectors.toList());
            default:
                return List.of(data);
        }
    }
}