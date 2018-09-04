package kr.jm.metric.config.mutator.field;

import lombok.*;

import java.util.List;
import java.util.Map;

/**
 * The type Field config.
 */
@Getter
@ToString(callSuper = true)
@AllArgsConstructor
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

}
