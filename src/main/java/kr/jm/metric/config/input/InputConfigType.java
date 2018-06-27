package kr.jm.metric.config.input;

import kr.jm.metric.config.ConfigTypeInterface;

public enum InputConfigType implements ConfigTypeInterface {
    STDIN {
        @Override
        public Class<StdInLineInputConfig> getConfigClass() {
            return StdInLineInputConfig.class;
        }
    }, FILE {
        @Override
        public Class<FileInputConfig> getConfigClass() {
            return FileInputConfig.class;
        }
    }, KAFKA {
        @Override
        public Class<KafkaInputConfig> getConfigClass() {
            return KafkaInputConfig.class;
        }
    }
}
