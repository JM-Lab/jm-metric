package kr.jm.metric.config.mutator;

import kr.jm.metric.config.ConfigTypeInterface;

/**
 * The enum Mutator config type.
 */
@SuppressWarnings("ALL")
public enum MutatorConfigType implements ConfigTypeInterface {
    /**
     * The Delimiter.
     */
    DELIMITER {
        @Override
        public Class<DelimiterMutatorConfig> getConfigClass() {
            return DelimiterMutatorConfig.class;
        }
    },
    /**
     * The Key value delimiter.
     */
    KEY_VALUE_DELIMITER {
        @Override
        public Class<KeyValueDelimiterMutatorConfig> getConfigClass() {
            return KeyValueDelimiterMutatorConfig.class;
        }
    },
    /**
     * The Formatted.
     */
    FORMATTED {
        @Override
        public Class<FormattedMutatorConfig> getConfigClass() {
            return FormattedMutatorConfig.class;
        }
    },
    /**
     * The Apache access log.
     */
    APACHE_ACCESS_LOG {
        @Override
        public Class<ApacheAccessLogMutatorConfig> getConfigClass() {
            return ApacheAccessLogMutatorConfig.class;
        }
    },
    /**
     * The Nginx access log.
     */
    NGINX_ACCESS_LOG {
        @Override
        public Class<NginxAccessLogMutatorConfig> getConfigClass() {
            return NginxAccessLogMutatorConfig.class;
        }
    },
    /**
     * The Json.
     */
    JSON {
        @Override
        public Class<JsonMutatorConfig> getConfigClass() {
            return JsonMutatorConfig.class;
        }
    },
    /**
     * The Raw.
     */
    RAW {
        @Override
        public Class<RawMutatorConfig> getConfigClass() {
            return RawMutatorConfig.class;
        }

    };

}
