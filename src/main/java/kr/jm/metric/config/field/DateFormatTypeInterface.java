package kr.jm.metric.config.field;

/**
 * The interface Date format type interface.
 */
public interface DateFormatTypeInterface {
    /**
     * Convert to iso string.
     *
     * @param dateConfig the date properties
     * @param data       the data
     * @return the string
     */
    String convertToIso(DateConfig dateConfig, Object data);

    /**
     * Convert to epoch long.
     *
     * @param dateConfig the date properties
     * @param data       the data
     * @return the long
     */
    long convertToEpoch(DateConfig dateConfig, Object data);
}
