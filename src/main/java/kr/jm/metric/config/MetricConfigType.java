package kr.jm.metric.config;

import com.fasterxml.jackson.core.type.TypeReference;
import kr.jm.metric.builder.*;

/**
 * The enum Metric properties type.
 */
@SuppressWarnings("ALL")
public enum MetricConfigType implements MetricConfigTypeInterface {
    /**
     * The Delimiter.
     */
    DELIMITER {
        private TypeReference<DelimiterMetricConfig> typeReference =
                new TypeReference<>() {};
        private DelimiterFieldMapBuilder delimiterFieldMapBuilder =
                new DelimiterFieldMapBuilder();

        @Override
        public DelimiterFieldMapBuilder getFieldMapBuilder() {
            return delimiterFieldMapBuilder;
        }

        @Override
        public TypeReference<DelimiterMetricConfig> getTypeReference() {
            return typeReference;
        }
    },
    /**
     * The Key value delimiter.
     */
    KEY_VALUE_DELIMITER {
                private TypeReference<KeyValueDelimiterMetricConfig>
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
                public TypeReference<KeyValueDelimiterMetricConfig> getTypeReference() {
                    return typeReference;
                }
            },
    /**
     * The Formatted.
     */
    FORMATTED {
                private TypeReference<FormattedMetricConfig> typeReference =
                        new TypeReference<>() {};
                private FormattedFieldMapBuilder formattedFieldMapBuilder =
                        new FormattedFieldMapBuilder();

                @Override
                public FormattedFieldMapBuilder getFieldMapBuilder() {
                    return formattedFieldMapBuilder;
                }

                @Override
                public TypeReference<FormattedMetricConfig> getTypeReference() {
                    return typeReference;
                }
            },
    /**
     * The Apache access log.
     */
    APACHE_ACCESS_LOG {
                private TypeReference<ApacheAccessLogMetricConfig>
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
                public TypeReference<ApacheAccessLogMetricConfig> getTypeReference() {
                    return typeReference;
                }
            },
    /**
     * The Nginx access log.
     */
    NGINX_ACCESS_LOG {
                private TypeReference<NginxAccessLogMetricConfig>
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
                public TypeReference<NginxAccessLogMetricConfig> getTypeReference() {
                    return typeReference;
                }
            },
    /**
     * The Json.
     */
    JSON {
        private TypeReference<JsonMetricConfig> typeReference =
                new TypeReference<>() {};
        private JsonFieldMapBuilder jsonFieldMapBuilder =
                new JsonFieldMapBuilder();

        @Override
        public JsonFieldMapBuilder getFieldMapBuilder() {
            return jsonFieldMapBuilder;
        }

        @Override
        public TypeReference<JsonMetricConfig> getTypeReference() {
            return typeReference;
        }
    },
    /**
     * The Raw.
     */
    RAW {
        private TypeReference<MetricConfig> typeReference =
                new TypeReference<>() {};
        private RawFieldMapBuilder rawFieldMapBuilder =
                new RawFieldMapBuilder();

        @Override
        public AbstractFieldMapBuilder<MetricConfig> getFieldMapBuilder() {
            return rawFieldMapBuilder;
        }

        @Override
        public TypeReference<MetricConfig> getTypeReference() {
            return typeReference;
        }
    }

}
