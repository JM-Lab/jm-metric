package kr.jm.metric.config.mutating;

import com.fasterxml.jackson.core.type.TypeReference;
import kr.jm.metric.mutating.builder.*;

/**
 * The enum Metric properties type.
 */
@SuppressWarnings("ALL")
public enum MutatingConfigType implements MutatingConfigTypeInterface {
    /**
     * The Delimiter.
     */
    DELIMITER {
        private TypeReference<DelimiterMutatingConfig> typeReference =
                new TypeReference<>() {};
        private DelimiterFieldMapBuilder delimiterFieldMapBuilder =
                new DelimiterFieldMapBuilder();

        @Override
        public DelimiterFieldMapBuilder getFieldMapBuilder() {
            return delimiterFieldMapBuilder;
        }

        @Override
        public TypeReference<DelimiterMutatingConfig> getTypeReference() {
            return typeReference;
        }
    },
    /**
     * The Key value delimiter.
     */
    KEY_VALUE_DELIMITER {
        private TypeReference<KeyValueDelimiterMutatingConfig>
                typeReference =
                new TypeReference<>() {};
        private KeyValueDelimiterFieldMapBuilder
                keyValueDelimiterFieldMapBuilder =
                new KeyValueDelimiterFieldMapBuilder();

        @Override
        public KeyValueDelimiterFieldMapBuilder getFieldMapBuilder() {
            return keyValueDelimiterFieldMapBuilder;
        }

        @Override
        public TypeReference<KeyValueDelimiterMutatingConfig> getTypeReference() {
            return typeReference;
        }
    },
    /**
     * The Formatted.
     */
    FORMATTED {
        private TypeReference<FormattedMutatingConfig> typeReference =
                new TypeReference<>() {};
        private FormattedFieldMapBuilder formattedFieldMapBuilder =
                new FormattedFieldMapBuilder();

        @Override
        public FormattedFieldMapBuilder getFieldMapBuilder() {
            return formattedFieldMapBuilder;
        }

        @Override
        public TypeReference<FormattedMutatingConfig> getTypeReference() {
            return typeReference;
        }
    },
    /**
     * The Apache access log.
     */
    APACHE_ACCESS_LOG {
        private TypeReference<ApacheAccessLogMutatingConfig>
                typeReference =
                new TypeReference<>() {};
        private ApacheAccessLogFieldMapBuilder
                apacheAccessLogFieldMapBuilder =
                new ApacheAccessLogFieldMapBuilder();

        @Override
        public ApacheAccessLogFieldMapBuilder getFieldMapBuilder() {
            return apacheAccessLogFieldMapBuilder;
        }

        @Override
        public TypeReference<ApacheAccessLogMutatingConfig> getTypeReference() {
            return typeReference;
        }
    },
    /**
     * The Nginx access log.
     */
    NGINX_ACCESS_LOG {
        private TypeReference<NginxAccessLogMutatingConfig>
                typeReference =
                new TypeReference<>() {};
        private NginxAccessLogFieldMapBuilder
                nginxAccessLogFieldMapBuilder =
                new NginxAccessLogFieldMapBuilder();

        @Override
        public NginxAccessLogFieldMapBuilder getFieldMapBuilder() {
            return nginxAccessLogFieldMapBuilder;
        }

        @Override
        public TypeReference<NginxAccessLogMutatingConfig> getTypeReference() {
            return typeReference;
        }
    },
    /**
     * The Json.
     */
    JSON {
        private TypeReference<JsonMutatingConfig> typeReference =
                new TypeReference<>() {};
        private JsonFieldMapBuilder jsonFieldMapBuilder =
                new JsonFieldMapBuilder();

        @Override
        public JsonFieldMapBuilder getFieldMapBuilder() {
            return jsonFieldMapBuilder;
        }

        @Override
        public TypeReference<JsonMutatingConfig> getTypeReference() {
            return typeReference;
        }
    },
    /**
     * The Raw.
     */
    RAW {
        private TypeReference<MutatingConfig> typeReference =
                new TypeReference<>() {};
        private RawFieldMapBuilder rawFieldMapBuilder =
                new RawFieldMapBuilder();

        @Override
        public AbstractFieldMapBuilder<MutatingConfig> getFieldMapBuilder() {
            return rawFieldMapBuilder;
        }

        @Override
        public TypeReference<MutatingConfig> getTypeReference() {
            return typeReference;
        }
    };

}
