package kr.jm.metric.config.mutator.field;

import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FieldConfig extends FieldMeta {
    public static final String RAW_DATA = "@rawData";

    protected Map<String, Map<String, Object>> format;

    protected boolean rawData;
    protected List<String> ignore;
    protected CombinedFieldConfig[] combinedFields;

    protected FormulaFieldConfig[] formulaFields;

    protected Map<String, DataType> dataType;
    protected Map<String, DateFormatConfig> dateFormat;

    protected Map<String, FilterConfig> filter;
    protected Map<String, String> alterFieldName;
    protected Map<String, Object> custom;

}
