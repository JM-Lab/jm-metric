package kr.jm.metric.config.mutating;

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
        private DelimiterFieldMapBuilder delimiterFieldMapBuilder =
                new DelimiterFieldMapBuilder();

        @Override
        public DelimiterFieldMapBuilder getFieldMapBuilder() {
            return delimiterFieldMapBuilder;
        }

        @Override
        public Class<DelimiterMutatingConfig> getConfigClass() {
            return DelimiterMutatingConfig.class;
        }
    },
    /**
     * The Key value delimiter.
     */
    KEY_VALUE_DELIMITER {
        private KeyValueDelimiterFieldMapBuilder
                keyValueDelimiterFieldMapBuilder =
                new KeyValueDelimiterFieldMapBuilder();

        @Override
        public KeyValueDelimiterFieldMapBuilder getFieldMapBuilder() {
            return keyValueDelimiterFieldMapBuilder;
        }

        @Override
        public Class<KeyValueDelimiterMutatingConfig> getConfigClass() {
            return KeyValueDelimiterMutatingConfig.class;
        }
    },
    /**
     * The Formatted.
     */
    FORMATTED {
        private FormattedFieldMapBuilder formattedFieldMapBuilder =
                new FormattedFieldMapBuilder();

        @Override
        public FormattedFieldMapBuilder getFieldMapBuilder() {
            return formattedFieldMapBuilder;
        }

        @Override
        public Class<FormattedMutatingConfig> getConfigClass() {
            return FormattedMutatingConfig.class;
        }
    },
    /**
     * The Apache access log.
     */
    APACHE_ACCESS_LOG {
        private ApacheAccessLogFieldMapBuilder
                apacheAccessLogFieldMapBuilder =
                new ApacheAccessLogFieldMapBuilder();

        @Override
        public ApacheAccessLogFieldMapBuilder getFieldMapBuilder() {
            return apacheAccessLogFieldMapBuilder;
        }

        @Override
        public Class<ApacheAccessLogMutatingConfig> getConfigClass() {
            return ApacheAccessLogMutatingConfig.class;
        }
    },
    /**
     * The Nginx access log.
     */
    NGINX_ACCESS_LOG {
        private NginxAccessLogFieldMapBuilder
                nginxAccessLogFieldMapBuilder =
                new NginxAccessLogFieldMapBuilder();

        @Override
        public NginxAccessLogFieldMapBuilder getFieldMapBuilder() {
            return nginxAccessLogFieldMapBuilder;
        }

        @Override
        public Class<NginxAccessLogMutatingConfig> getConfigClass() {
            return NginxAccessLogMutatingConfig.class;
        }
    },
    /**
     * The Json.
     */
    JSON {
        private JsonFieldMapBuilder jsonFieldMapBuilder =
                new JsonFieldMapBuilder();

        @Override
        public JsonFieldMapBuilder getFieldMapBuilder() {
            return jsonFieldMapBuilder;
        }

        @Override
        public Class<JsonMutatingConfig> getConfigClass() {
            return JsonMutatingConfig.class;
        }
    },
    /**
     * The Raw.
     */
    RAW {
        private RawFieldMapBuilder rawFieldMapBuilder =
                new RawFieldMapBuilder();

        @Override
        public AbstractFieldMapBuilder<MutatingConfig> getFieldMapBuilder() {
            return rawFieldMapBuilder;
        }

        @Override
        public Class<JsonMutatingConfig> getConfigClass() {
            return JsonMutatingConfig.class;
        }

    };

}
