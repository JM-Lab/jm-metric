package kr.jm.metric.config.mutator.field;

import kr.jm.metric.config.ConfigInterface;
import kr.jm.metric.config.mutator.MutatorConfigInterface;
import kr.jm.metric.config.mutator.MutatorConfigType;
import kr.jm.utils.datastructure.JMMap;
import kr.jm.utils.helper.JMOptional;
import kr.jm.utils.helper.JMString;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.*;

/**
 * The type Field config.
 */
@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FieldConfig extends FieldMeta {
    /**
     * The constant RAW_DATA.
     */
    public static final String RAW_DATA = "rawData";

    /**
     * The Format.
     */
    protected Map<String, Map<String, Object>> format;

    private Map<String, MutatorConfigInterface> formatMutatorConfigMap;
    /**
     * The Raw data.
     */
    protected boolean rawData;
    /**
     * The Ignore.
     */
    protected List<String> ignore;
    /**
     * The Combined fields.
     */
    protected CombinedFieldConfig[] combinedFields;

    /**
     * The Formula fields.
     */
    protected FormulaFieldConfig[] formulaFields;

    /**
     * The Data type.
     */
    protected Map<String, DataType> dataType;
    /**
     * The Date format.
     */
    protected Map<String, DateFormatConfig> dateFormat;

    /**
     * Instantiates a new Field config.
     *
     * @param format         the format
     * @param rawData        the raw data
     * @param ignore         the ignore
     * @param combinedFields the combined fields
     * @param dataType       the data type
     * @param dateFormat     the date format
     * @param unit           the unit
     * @param custom         the custom
     */
    public FieldConfig(Map<String, Map<String, Object>> format, boolean rawData,
            List<String> ignore, CombinedFieldConfig[] combinedFields,
            Map<String, DataType> dataType,
            Map<String, DateFormatConfig> dateFormat,
            Map<String, String> unit, Map<String, Object> custom) {
        super(unit, custom);
        this.dateFormat = dateFormat;
        this.rawData = rawData;
        this.format = format;
        this.ignore = ignore;
        this.dataType = dataType;
        this.combinedFields = combinedFields;
    }

    /**
     * Apply config map.
     *
     * @param fieldObjectMap the field object map
     * @return the map
     */
    public Map<String, Object> applyConfig(Map<String, Object> fieldObjectMap) {
        if (Objects.nonNull(format))
            format.forEach((field, fieldConfigMap) -> JMOptional
                    .getOptional(fieldObjectMap, field).map(Object::toString)
                    .map(targetString -> buildNestedFieldStringMap(
                            getFormatMutatorConfig(field, fieldConfigMap),
                            targetString)).ifPresent(fieldObjectMap::putAll));
        if (!rawData)
            fieldObjectMap.remove(RAW_DATA);
        JMOptional.getOptional(combinedFields).stream().flatMap(Arrays::stream)
                .forEach(combinedFieldConfig -> fieldObjectMap
                        .put(combinedFieldConfig.getCombinedFieldName(),
                                combinedFieldConfig
                                        .buildValue(fieldObjectMap)));
        JMOptional.getOptional(formulaFields).stream().flatMap(Arrays::stream)
                .forEach(formulaFieldConfig -> Optional.ofNullable(
                        formulaFieldConfig.buildValue(fieldObjectMap))
                        .ifPresent(value -> fieldObjectMap
                                .put(formulaFieldConfig.getCombinedFieldName(),
                                        value)));
        JMOptional.getOptional(ignore).ifPresent(ignoreList -> ignoreList
                .forEach(fieldObjectMap::remove));
        return JMMap.newChangedValueWithEntryMap(fieldObjectMap,
                entry -> transformValue(entry.getKey(), entry.getValue()));
    }

    private MutatorConfigInterface getFormatMutatorConfig(String field,
            Map<String, Object> fieldConfigMap) {
        return JMMap.getOrPutGetNew(
                Optional.ofNullable(this.formatMutatorConfigMap).orElseGet(
                        () -> this.formatMutatorConfigMap = new HashMap<>()),
                field, () -> buildFieldConfig(fieldConfigMap));
    }

    private MutatorConfigInterface buildFieldConfig(
            Map<String, Object> fieldConfigMap) {
        return ConfigInterface.transformConfig(fieldConfigMap, MutatorConfigType
                .valueOf(fieldConfigMap.get("mutatorConfigType").toString())
                .getConfigClass());
    }

    private Object transformValue(String field, Object value) {
        if (Objects.nonNull(dateFormat) && dateFormat.containsKey(field))
            value = dateFormat.get(field).change(value);
        return Objects.nonNull(dataType) && dataType.containsKey(field) ?
                transformWithDataType(dataType.get(field),
                        value.toString()) : value;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> buildNestedFieldStringMap(
            MutatorConfigInterface mutatorConfig, String targetString) {
        return mutatorConfig.buildMutator().mutate(targetString);
    }

    private Object transformWithDataType(DataType dataType, String data) {
        return DataType.NUMBER.equals(dataType) &&
                JMString.isNumber(data) ? Double.valueOf(data) : data;
    }

}
