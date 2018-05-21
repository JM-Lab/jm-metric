package kr.jm.metric.data;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The type Config id transfer.
 *
 * @param <T> the type parameter
 */
@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ConfigIdTransfer<T> extends Transfer<T> {

    /**
     * The constant CONFIG_ID.
     */
    public static final String CONFIG_ID = "configId";
    private String configId;

    /**
     * Instantiates a new Config id transfer.
     *
     * @param configId  the properties id
     * @param transfer  the transfer
     */
    public ConfigIdTransfer(String configId, Transfer<T> transfer) {
        super(transfer.getDataId(), transfer.getData(), transfer
                .getTimestamp(), transfer.getMeta());
        this.configId = configId;
    }

    @Override
    public <D> ConfigIdTransfer<D> newWith(D data) {
        return newWith(super.newWith(data));
    }

    /**
     * New with properties id transfer.
     *
     * @param <D>      the type parameter
     * @param transfer the transfer
     * @return the properties id transfer
     */
    public <D> ConfigIdTransfer<D> newWith(Transfer<D> transfer) {
        return new ConfigIdTransfer<>(this.configId, transfer);
    }

    @Override
    public <D> List<ConfigIdTransfer<D>> newListWith(List<D> dataList) {
        return newStreamWith(dataList).collect(Collectors.toList());
    }

    @Override
    public <D> Stream<ConfigIdTransfer<D>> newStreamWith(List<D> dataList) {
        return super.newStreamWith(dataList).map(this::newWith);
    }

    @Override
    protected Map<String, Object> buildMetaForFieldMap() {
        Map<String, Object> meta = super.buildMetaForFieldMap();
        meta.put(CONFIG_ID, configId);
        return meta;
    }
}
