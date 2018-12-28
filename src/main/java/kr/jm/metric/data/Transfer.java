package kr.jm.metric.data;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Transfer<T> {
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
        this.meta = Optional.ofNullable(meta).orElseGet(Map::of);
    }

    public Map<String, Object> getMeta() {
        return Objects.requireNonNullElseGet(this.meta,
                () -> this.meta = new HashMap<>());
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

}
