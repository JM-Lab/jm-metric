package kr.jm.metric.config.mutator.field;

public interface DateFormatTypeInterface {
    String convertToIso(DateFormatConfig dateFormatConfig, Object data);

    long convertToEpoch(DateFormatConfig dateFormatConfig, Object data);
}
