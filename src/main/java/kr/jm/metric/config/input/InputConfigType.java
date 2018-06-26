package kr.jm.metric.config.input;

import com.fasterxml.jackson.core.type.TypeReference;

public enum InputConfigType implements InputConfigTypeInterface {
    STDIN {
        private TypeReference<StdInLineInputConfig> typeReference =
                new TypeReference<>() {};

        @Override
        public TypeReference<StdInLineInputConfig> getTypeReference() {
            return typeReference;
        }
    }, FILE {
        private TypeReference<FileInputConfig> typeReference =
                new TypeReference<>() {};

        @Override
        public TypeReference<FileInputConfig> getTypeReference() {
            return typeReference;
        }
    }, KAFKA {
        private TypeReference<KafkaInputConfig> typeReference =
                new TypeReference<>() {};

        @Override
        public TypeReference<KafkaInputConfig> getTypeReference() {
            return typeReference;
        }
    }
}
