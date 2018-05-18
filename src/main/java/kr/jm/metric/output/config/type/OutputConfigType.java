package kr.jm.metric.output.config.type;

import com.fasterxml.jackson.core.type.TypeReference;
import kr.jm.metric.output.config.ElasticsearchOutputConfig;
import kr.jm.metric.output.config.FileOutputConfig;
import kr.jm.metric.output.config.StdOutputConfig;

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
