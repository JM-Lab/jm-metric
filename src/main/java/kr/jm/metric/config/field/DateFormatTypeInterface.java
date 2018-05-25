package kr.jm.metric.config.field;

/**
 * The interface Date format type interface.
 */
public interface DateFormatTypeInterface {
    /**
     * Convert to iso string.
     *
     * @param dateFormatConfig the date properties
     * @param data       the data
     * @return the string
     */
    String convertToIso(DateFormatConfig dateFormatConfig, Object data);

    /**
     * Convert to epoch long.
     *
     * @param dateFormatConfig the date properties
     * @param data       the data
     * @return the long
     */
    long convertToEpoch(DateFormatConfig dateFormatConfig, Object data);
}
