package kr.jm.metric.config.mutator.field;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.Map;

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

    protected Map<String, FilterConfig> filter;

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
}
