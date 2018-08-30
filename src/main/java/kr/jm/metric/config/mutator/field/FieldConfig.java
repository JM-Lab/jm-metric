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

    public FieldConfig(boolean rawData) {
        super(null, null);
        this.rawData = rawData;
    }

    public FieldConfig(Map<String, String> unit,
            Map<String, Object> custom,
            Map<String, Map<String, Object>> format, boolean rawData,
            List<String> ignore,
            CombinedFieldConfig[] combinedFields,
            FormulaFieldConfig[] formulaFields,
            Map<String, DataType> dataType,
            Map<String, DateFormatConfig> dateFormat,
            Map<String, FilterConfig> filter) {
        super(unit, custom);
        this.format = format;
        this.rawData = rawData;
        this.ignore = ignore;
        this.combinedFields = combinedFields;
        this.formulaFields = formulaFields;
        this.dataType = dataType;
        this.dateFormat = dateFormat;
        this.filter = filter;
    }
}
