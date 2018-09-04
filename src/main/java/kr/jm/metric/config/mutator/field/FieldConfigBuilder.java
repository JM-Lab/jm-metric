package kr.jm.metric.config.mutator.field;

import java.util.List;
import java.util.Map;

public class FieldConfigBuilder {
    private Map<String, Map<String, Object>> format;
    private boolean rawData;
    private List<String> ignore;
    private CombinedFieldConfig[] combinedFields;
    private FormulaFieldConfig[] formulaFields;
    private Map<String, DataType> dataType;
    private Map<String, DateFormatConfig> dateFormat;
    private Map<String, FilterConfig> filter;

    public FieldConfigBuilder setFormat(
            Map<String, Map<String, Object>> format) {
        this.format = format;
        return this;
    }

    public FieldConfigBuilder setRawData(boolean rawData) {
        this.rawData = rawData;
        return this;
    }

    public FieldConfigBuilder setIgnore(List<String> ignore) {
        this.ignore = ignore;
        return this;
    }

    public FieldConfigBuilder setCombinedFields(
            CombinedFieldConfig[] combinedFields) {
        this.combinedFields = combinedFields;
        return this;
    }

    public FieldConfigBuilder setFormulaFields(
            FormulaFieldConfig[] formulaFields) {
        this.formulaFields = formulaFields;
        return this;
    }

    public FieldConfigBuilder setDataType(Map<String, DataType> dataType) {
        this.dataType = dataType;
        return this;
    }

    public FieldConfigBuilder setDateFormat(
            Map<String, DateFormatConfig> dateFormat) {
        this.dateFormat = dateFormat;
        return this;
    }

    public FieldConfigBuilder setFilter(Map<String, FilterConfig> filter) {
        this.filter = filter;
        return this;
    }

    public FieldConfig createFieldConfig() {
        return new FieldConfig(format, rawData, ignore, combinedFields,
                formulaFields, dataType, dateFormat, filter);
    }
}