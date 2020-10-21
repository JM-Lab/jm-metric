package kr.jm.metric.config.mutator.field;

import kr.jm.utils.time.JMTime;
import lombok.*;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DateFormatConfig {
    private DateFormatType dateFormatType;
    private TimeUnit timeUnit;
    private String format;
    private String zoneOffset;
    private String newFieldName;
    private DateFormatConfig changeDateConfig;

    public Object change(Object value) {
        return Optional.ofNullable(changeDateConfig).map(DateFormatConfig::getDateFormatType)
                .map(changeFormatType -> change(changeFormatType, value)).orElse(value);
    }

    private Object change(DateFormatType changeFormatType, Object value) {
        switch (changeFormatType) {
            case ISO:
                return dateFormatType.convertToIso(this, value);
            case EPOCH:
                return dateFormatType.convertToEpoch(this, value);
            case CUSTOM:
                return Optional.ofNullable(changeDateConfig.getFormat())
                        .map(format -> JMTime.getInstance()
                                .getTime(dateFormatType.convertToEpoch(this, value), format,
                                        extractZoneId()))
                        .map(s -> (Object) s).orElse(value);
            default:
                return value;
        }
    }

    ZoneId extractZoneId() {
        return Optional.ofNullable(changeDateConfig.getZoneOffset()).map(ZoneOffset::of)
                .map(ZoneOffset::normalized).orElseGet(ZoneId::systemDefault);
    }

}
