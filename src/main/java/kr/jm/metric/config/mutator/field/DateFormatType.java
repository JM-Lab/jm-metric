package kr.jm.metric.config.mutator.field;

import kr.jm.utils.time.JMTime;

import java.util.Optional;

public enum DateFormatType implements DateFormatTypeInterface {

    CUSTOM {
        @Override
        public String convertToIso(DateFormatConfig dateFormatConfig, Object data) {
            return JMTime.getInstance().getTimeAsIsoInstantMills(data.toString(), dateFormatConfig.getFormat());
        }

        @Override
        public long convertToEpoch(DateFormatConfig dateFormatConfig, Object data) {
            return JMTime.getInstance().changeToEpochMillis(data.toString(), dateFormatConfig.getFormat(),
                    dateFormatConfig.extractZoneId());
        }
    },
    ISO {
        @Override
        public String convertToIso(DateFormatConfig dateFormatConfig, Object data) {
            return JMTime.getInstance().getTimeAsIsoInstantMills(data.toString());
        }

        @Override
        public long convertToEpoch(DateFormatConfig dateFormatConfig, Object data) {
            return JMTime.getInstance().changeToEpochMillis(data.toString());
        }
    },
    EPOCH {
        @Override
        public String convertToIso(DateFormatConfig dateFormatConfig,
                Object data) {
            return JMTime.getInstance().getTimeAsIsoInstantMills(Optional.ofNullable(dateFormatConfig.getTimeUnit())
                    .map(timeUnit -> timeUnit.toMillis(transformToLong(data))).orElseGet(() -> transformToLong(data)));
        }

        @Override
        public long convertToEpoch(DateFormatConfig dateFormatConfig, Object data) {
            return transformToLong(data);
        }

        private long transformToLong(Object data) {
            return Double.valueOf(data.toString()).longValue();
        }
    }

}

