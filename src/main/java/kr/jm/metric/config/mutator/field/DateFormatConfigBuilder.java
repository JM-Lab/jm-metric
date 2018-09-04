package kr.jm.metric.config.mutator.field;

import java.util.concurrent.TimeUnit;

public class DateFormatConfigBuilder {
    private DateFormatType dateFormatType;
    private TimeUnit timeUnit;
    private String format;
    private String zoneOffset;
    private String newFieldName;
    private DateFormatConfig changeDateConfig;

    public DateFormatConfigBuilder setDateFormatType(
            DateFormatType dateFormatType) {
        this.dateFormatType = dateFormatType;
        return this;
    }

    public DateFormatConfigBuilder setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
        return this;
    }

    public DateFormatConfigBuilder setFormat(String format) {
        this.format = format;
        return this;
    }

    public DateFormatConfigBuilder setZoneOffset(String zoneOffset) {
        this.zoneOffset = zoneOffset;
        return this;
    }

    public DateFormatConfigBuilder setNewFieldName(String newFieldName) {
        this.newFieldName = newFieldName;
        return this;
    }

    public DateFormatConfigBuilder setChangeDateConfig(
            DateFormatConfig changeDateConfig) {
        this.changeDateConfig = changeDateConfig;
        return this;
    }

    public DateFormatConfig createDateFormatConfig() {
        return new DateFormatConfig(dateFormatType, timeUnit, format,
                zoneOffset, newFieldName, changeDateConfig);
    }
}