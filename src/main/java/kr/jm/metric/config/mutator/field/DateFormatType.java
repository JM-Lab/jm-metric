package kr.jm.metric.config.mutator.field;

import kr.jm.utils.time.JMTimeUtil;

import java.util.Optional;

public enum DateFormatType implements DateFormatTypeInterface {

    CUSTOM {
        @Override
        public String convertToIso(DateFormatConfig dateFormatConfig,
                Object data) {
            return JMTimeUtil.changeFormatAndTimeZoneToDefaultUtcFormat(
                    dateFormatConfig.getFormat(), data.toString());
        }

        @Override
        public long convertToEpoch(DateFormatConfig dateFormatConfig,
                Object data) {
            return JMTimeUtil
                    .changeTimestampToLong(dateFormatConfig.getFormat(),
                            data.toString(), dateFormatConfig.getZoneOffset());
        }
    },
    ISO {
        @Override
        public String convertToIso(DateFormatConfig dateFormatConfig,
                Object data) {
            return JMTimeUtil.changeIsoTimestampInUTC(data.toString());
        }

        @Override
        public long convertToEpoch(DateFormatConfig dateFormatConfig,
                Object data) {
            return JMTimeUtil.changeIsoTimestampToLong(data.toString());
        }
    },
    EPOCH {
        @Override
        public String convertToIso(DateFormatConfig dateFormatConfig,
                Object data) {
            return JMTimeUtil.getTimeAsDefaultUtcFormat(
                    Optional.ofNullable(dateFormatConfig.getTimeUnit())
                            .map(timeUnit -> timeUnit
                                    .toMillis(transformToLong(data)))
                            .orElseGet(() -> transformToLong(data)));
        }

        @Override
        public long convertToEpoch(DateFormatConfig dateFormatConfig,
                Object data) {
            return transformToLong(data);
        }

        private long transformToLong(Object data) {
            return Double.valueOf(data.toString()).longValue();
        }
    }

}

