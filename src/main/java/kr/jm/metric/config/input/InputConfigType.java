package kr.jm.metric.config.input;

import kr.jm.metric.config.ConfigTypeInterface;

/**
 * The enum Input config type.
 */
public enum InputConfigType implements ConfigTypeInterface {
    /**
     * The Stdin.
     */
    STDIN {
        @Override
        public Class<StdInLineInputConfig> getConfigClass() {
            return StdInLineInputConfig.class;
        }
    },
    /**
     * The File.
     */
    FILE {
        @Override
        public Class<FileInputConfig> getConfigClass() {
            return FileInputConfig.class;
        }
    },
    /**
     * The Kafka.
     */
    KAFKA {
        @Override
        public Class<KafkaInputConfig> getConfigClass() {
            return KafkaInputConfig.class;
        }
    }
}
