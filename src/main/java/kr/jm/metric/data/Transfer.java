package kr.jm.metric.data;

import kr.jm.utils.helper.JMOptional;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The type Transfer.
 *
 * @param <T> the type parameter
 */
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Transfer<T> {
    /**
     * The constant META.
     */
    public static final String META = "meta";
    /**
     * The constant DATA.
     */
    public static final String DATA = "data";
    /**
     * The constant INPUT_ID.
     */
    public static final String INPUT_ID = "inputId";
    /**
     * The constant TIMESTAMP.
     */
    public static final String TIMESTAMP = "timestamp";
    /**
     * The Input id.
     */
    protected String inputId;
    /**
     * The Data.
     */
    protected T data;
    /**
     * The Timestamp.
     */
    protected long timestamp;
    /**
     * The Meta.
     */
    protected Map<String, Object> meta;

    /**
     * Instantiates a new Transfer.
     *
     * @param inputId the input id
     * @param data    the data
     */
    public Transfer(String inputId, T data) {
        this(inputId, data, null);
    }

    /**
     * Instantiates a new Transfer.
     *
     * @param inputId   the input id
     * @param data      the data
     * @param timestamp the timestamp
     */
    public Transfer(String inputId, T data, long timestamp) {
        this(inputId, data, timestamp, null);
    }

    /**
     * Instantiates a new Transfer.
     *
     * @param inputId the input id
     * @param data    the data
     * @param meta    the meta
     */
    public Transfer(String inputId, T data, Map<String, Object> meta) {
        this(inputId, data, System.currentTimeMillis(), meta);
    }

    /**
     * Instantiates a new Transfer.
     *
     * @param inputId   the input id
     * @param data      the data
     * @param timestamp the timestamp
     * @param meta      the meta
     */
    public Transfer(String inputId, T data, long timestamp,
            Map<String, Object> meta) {
        this.inputId = inputId;
        this.data = data;
        this.timestamp = timestamp;
        this.meta = meta;
    }

    /**
     * Gets meta.
     *
     * @return the meta
     */
    public Map<String, Object> getMeta() {
        return Objects.requireNonNullElseGet(this.meta,
                () -> this.meta = new HashMap<>());
    }

    /**
     * Put meta transfer.
     *
     * @param meta the meta
     * @return the transfer
     */
    public Transfer<T> putMeta(Map<String, Object> meta) {
        getMeta().putAll(meta);
        return this;
    }

    /**
     * Put meta transfer.
     *
     * @param key   the key
     * @param value the value
     * @return the transfer
     */
    public Transfer<T> putMeta(String key, Object value) {
        getMeta().put(key, value);
        return this;
    }


    /**
     * New with transfer.
     *
     * @param <D>  the type parameter
     * @param data the data
     * @return the transfer
     */
    public <D> Transfer<D> newWith(D data) {
        return newWith(data, this.meta);
    }

    /**
     * New with transfer.
     *
     * @param <D>       the type parameter
     * @param data      the data
     * @param timestamp the timestamp
     * @return the transfer
     */
    public <D> Transfer<D> newWith(D data, long timestamp) {
        return newWith(data, timestamp, this.meta);
    }

    /**
     * New with transfer.
     *
     * @param <D>  the type parameter
     * @param data the data
     * @param meta the meta
     * @return the transfer
     */
    public <D> Transfer<D> newWith(D data, Map<String, Object> meta) {
        return newWith(data, this.timestamp, meta);
    }

    /**
     * New with transfer.
     *
     * @param <D>       the type parameter
     * @param data      the data
     * @param timestamp the timestamp
     * @param meta      the meta
     * @return the transfer
     */
    public <D> Transfer<D> newWith(D data, long timestamp,
            Map<String, Object> meta) {
        return new Transfer<>(this.inputId, data, timestamp,
                JMOptional.getOptional(meta).map(HashMap::new)
                        .orElseGet(HashMap::new));
    }

    /**
     * New list with list.
     *
     * @param <D>      the type parameter
     * @param dataList the data list
     * @return the list
     */
    public <D> List<Transfer<D>> newListWith(List<D> dataList) {
        return newStreamWith(dataList).collect(Collectors.toList());
    }

    /**
     * New stream with stream.
     *
     * @param <D>      the type parameter
     * @param dataList the data list
     * @return the stream
     */
    public <D> Stream<Transfer<D>> newStreamWith(
            List<D> dataList) {
        return dataList.stream().map(this::newWith);
    }

    /**
     * Build field map with meta field map.
     *
     * @return the field map
     */
    public FieldMap buildFieldMapWithMeta() {
        return new FieldMap(buildFieldMapWithMeta(new HashMap<>()));
    }

    private Map<String, Object> buildFieldMapWithMeta(
            Map<String, Object> newFieldMap) {
        if (this.data instanceof Map)
            newFieldMap.putAll((Map<String, Object>) data);
        else
            newFieldMap.put(DATA, data);
        newFieldMap.put(META, buildMetaForFieldMap());
        return newFieldMap;
    }

    /**
     * Build meta for field map map.
     *
     * @return the map
     */
    protected Map<String, Object> buildMetaForFieldMap() {
        Map<String, Object> meta = getMeta();
        meta.put(INPUT_ID, inputId);
        meta.put(TIMESTAMP, timestamp);
        return meta;
    }

}
