package kr.jm.metric.config.field;

import kr.jm.utils.time.JMTimeUtil;
import lombok.*;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * The type Date config.
 */
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DateConfig {
    private DateFormatType dateFormatType;
    private TimeUnit timeUnit;
    private String format;
    private String zoneOffset;
    private DateConfig changeDateConfig;

    /**
     * Change object.
     *
     * @param value the value
     * @return the object
     */
    public Object change(Object value) {
        return Optional.ofNullable(changeDateConfig).map
                (DateConfig::getDateFormatType).map(changeFormatType ->
                change(changeFormatType, value)).orElse(value);
    }

    private Object change(DateFormatType changeFormatType, Object value) {
        switch (changeFormatType) {
            case ISO:
                return dateFormatType.convertToIso(this, value);
            case EPOCH:
                return dateFormatType.convertToEpoch(this, value);
            case CUSTOM:
                return Optional.ofNullable(changeDateConfig.getFormat())
                        .map(format -> JMTimeUtil.getTime(
                                dateFormatType.convertToEpoch(this, value),
                                format, Optional.ofNullable(
                                        changeDateConfig.getZoneOffset())
                                        .orElse(JMTimeUtil.DEFAULT_ZONE_ID_STRING)))
                        .map(s -> (Object) s).orElse(value);
            default:
                return value;
        }
    }

}
