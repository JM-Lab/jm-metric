package kr.jm.metric.config.output;

import kr.jm.metric.config.ConfigTypeInterface;

/**
 * The enum Output config type.
 */
public enum OutputConfigType implements ConfigTypeInterface {
    /**
     * The Stdout.
     */
    STDOUT {
        @Override
        public Class<StdoutLineOutputConfig> getConfigClass() {
            return StdoutLineOutputConfig.class;
        }
    },
    /**
     * The File.
     */
    FILE {
        @Override
        public Class<FileOutputConfig> getConfigClass() {
            return FileOutputConfig.class;
        }
    },
    /**
     * The Elasticsearch.
     */
    ELASTICSEARCH {
        @Override
        public Class<ElasticsearchOutputConfig> getConfigClass() {
            return ElasticsearchOutputConfig.class;
        }
    },
    /**
     * The Kafka.
     */
    KAFKA {
        @Override
        public Class<KafkaOutputConfig> getConfigClass() {
            return KafkaOutputConfig.class;
        }
    }
}
