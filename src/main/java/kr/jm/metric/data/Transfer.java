package kr.jm.metric.data;

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

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Transfer<T> {
    public static final String META = "meta";
    public static final String DATA = "data";
    public static final String INPUT_ID = "inputId";
    public static final String TIMESTAMP = "timestamp";
    protected String inputId;
    protected T data;
    protected long timestamp;
    protected Map<String, Object> meta;

    public Transfer(String inputId, T data) {
        this(inputId, data, null);
    }

    public Transfer(String inputId, T data, long timestamp) {
        this(inputId, data, timestamp, null);
    }

    public Transfer(String inputId, T data, Map<String, Object> meta) {
        this(inputId, data, System.currentTimeMillis(), meta);
    }

    public Transfer(String inputId, T data, long timestamp,
            Map<String, Object> meta) {
        this.inputId = inputId;
        this.data = data;
        this.timestamp = timestamp;
        this.meta = meta;
    }

    public Map<String, Object> getMeta() {
        return Objects.requireNonNullElseGet(this.meta,
                () -> this.meta = new HashMap<>());
    }

    public Transfer<T> putMeta(Map<String, Object> meta) {
        getMeta().putAll(meta);
        return this;
    }

    public Transfer<T> putMeta(String key, Object value) {
        getMeta().put(key, value);
        return this;
    }


    public <D> Transfer<D> newWith(D data) {
        return newWith(data, this.meta);
    }

    public <D> Transfer<D> newWith(D data, long timestamp) {
        return newWith(data, timestamp, this.meta);
    }

    public <D> Transfer<D> newWith(D data, Map<String, Object> meta) {
        return newWith(data, this.timestamp, meta);
    }

    public <D> Transfer<D> newWith(D data, long timestamp,
            Map<String, Object> meta) {
        return new Transfer<>(this.inputId, data, timestamp, meta);
    }

    public <D> List<Transfer<D>> newListWith(List<D> dataList) {
        return newStreamWith(dataList).collect(Collectors.toList());
    }

    public <D> Stream<Transfer<D>> newStreamWith(
            List<D> dataList) {
        return dataList.stream().map(this::newWith);
    }

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

    protected Map<String, Object> buildMetaForFieldMap() {
        Map<String, Object> meta = getMeta();
        meta.put(INPUT_ID, inputId);
        meta.put(TIMESTAMP, timestamp);
        return meta;
    }

}
