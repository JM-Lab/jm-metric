package kr.jm.metric.config.output;

import kr.jm.metric.config.ConfigTypeInterface;

public enum OutputConfigType implements ConfigTypeInterface {
    STDOUT {
        @Override
        public Class<StdoutLineOutputConfig> getConfigClass() {
            return StdoutLineOutputConfig.class;
        }
    },
    FILE {
        @Override
        public Class<FileOutputConfig> getConfigClass() {
            return FileOutputConfig.class;
        }
    },
    ELASTICSEARCH {
        @Override
        public Class<ElasticsearchOutputConfig> getConfigClass() {
            return ElasticsearchOutputConfig.class;
        }
    },
    KAFKA {
        @Override
        public Class<KafkaOutputConfig> getConfigClass() {
            return KafkaOutputConfig.class;
        }
    }
}
