package kr.jm.metric.config.output;

import com.fasterxml.jackson.core.type.TypeReference;

public enum OutputConfigType implements OutputConfigTypeInterface {
    STDOUT {
        private TypeReference<StdOutputConfig> typeReference =
                new TypeReference<>() {};

        @Override
        public TypeReference<StdOutputConfig> getTypeReference() {
            return typeReference;
        }
    }, FILE {
        private TypeReference<FileOutputConfig> typeReference =
                new TypeReference<>() {};

        @Override
        public TypeReference<FileOutputConfig> getTypeReference() {
            return typeReference;
        }
    }, ELASTICSEARCH {
        private TypeReference<ElasticsearchOutputConfig> typeReference =
                new TypeReference<>() {};

        @Override
        public TypeReference<ElasticsearchOutputConfig> getTypeReference() {
            return typeReference;
        }
    }
}
