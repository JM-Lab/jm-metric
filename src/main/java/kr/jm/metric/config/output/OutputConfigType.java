package kr.jm.metric.config.output;

import com.fasterxml.jackson.core.type.TypeReference;

public enum OutputConfigType implements OutputConfigTypeInterface {
    STDOUT {
        @Override
        public TypeReference<StdOutputConfig> getTypeReference() {
            return new TypeReference<>() {};
        }
    }, FILE {
        @Override
        public TypeReference<FileOutputConfig> getTypeReference() {
            return new TypeReference<>() {};
        }
    }, ELASTICSEARCH {
        @Override
        public TypeReference<ElasticsearchOutputConfig> getTypeReference() {
            return new TypeReference<>() {};
        }
    }
}
