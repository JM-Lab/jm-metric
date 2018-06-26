package kr.jm.metric.config.mutating.field;

import kr.jm.metric.config.ConfigInterface;
import kr.jm.metric.config.mutating.MutatingConfig;
import kr.jm.utils.datastructure.JMMap;
import kr.jm.utils.helper.JMOptional;
import kr.jm.utils.helper.JMString;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The type Field properties.
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
    protected Map<String, MutatingConfig> format;
    /**
     * The Raw data.
     */
    protected boolean rawData;
    /**
     * The Ignore.
     */
    protected List<String> ignore;
    /**
     * The Combined id.
     */
    protected CombinedFieldConfig[] combinedFields;

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
     * Instantiates a new Field properties.
     *
     * @param format         the format
     * @param rawData        the raw data
     * @param ignore         the ignore
     * @param combinedFields the combined id
     * @param dataType       the data type
     * @param dateFormat     the date format
     * @param unit           the unit
     * @param custom         the custom
     */
    public FieldConfig(Map<String, MutatingConfig> format, boolean rawData,
            List<String> ignore,
            CombinedFieldConfig[] combinedFields,
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
     * Apply properties map.
     *
     * @param fieldObjectMap the field object map
     * @return the map
     */
    public Map<String, Object> applyConfig(
            Map<String, Object> fieldObjectMap) {
        JMOptional.getOptional(format).ifPresent(
                fieldFormatConfigMap -> buildWithFormat(fieldFormatConfigMap,
                        fieldObjectMap));
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

    private Object transformValue(String field, Object value) {
        return JMOptional.getOptional(dateFormat, field)
                .map(dateFormatConfig -> dateFormatConfig.change(value))
                .orElseGet(() -> JMOptional.getOptional(dataType, field)
                        .map(dataType -> transformWithDataType(dataType,
                                value.toString()))
                        .orElse(value));
    }

    private void buildWithFormat(
            Map<String, MutatingConfig> fieldFormatConfigMap,
            Map<String, Object> fieldStringMap) {
        fieldFormatConfigMap.forEach((field, inputConfig) -> fieldStringMap
                .putAll(buildNestedFieldStringMap(inputConfig,
                        fieldStringMap.get(field).toString())));
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> buildNestedFieldStringMap(
            MutatingConfig mutatingConfig, String targetString) {
        return mutatingConfig.getMetricBuilder().buildFieldObjectMap(
                ConfigInterface.transformConfig(mutatingConfig,
                        mutatingConfig.getMutatingConfigType()
                                .getTypeReference()), targetString);
    }

    private Object transformWithDataType(DataType dataType, String data) {
        return DataType.NUMBER.equals(dataType) &&
                JMString.isNumber(data) ? Double.valueOf(data) : data;
    }

}
