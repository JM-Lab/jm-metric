package kr.jm.metric.config.mutator;

import kr.jm.metric.config.ConfigTypeInterface;

@SuppressWarnings("ALL")
public enum MutatorConfigType implements ConfigTypeInterface {
    DELIMITER {
        @Override
        public Class<DelimiterMutatorConfig> getConfigClass() {
            return DelimiterMutatorConfig.class;
        }
    },
    KEY_VALUE_DELIMITER {
        @Override
        public Class<KeyValueDelimiterMutatorConfig> getConfigClass() {
            return KeyValueDelimiterMutatorConfig.class;
        }
    },
    FORMATTED {
        @Override
        public Class<FormattedMutatorConfig> getConfigClass() {
            return FormattedMutatorConfig.class;
        }
    },
    APACHE_ACCESS_LOG {
        @Override
        public Class<ApacheAccessLogMutatorConfig> getConfigClass() {
            return ApacheAccessLogMutatorConfig.class;
        }
    },
    NGINX_ACCESS_LOG {
        @Override
        public Class<NginxAccessLogMutatorConfig> getConfigClass() {
            return NginxAccessLogMutatorConfig.class;
        }
    },
    JSON {
        @Override
        public Class<JsonMutatorConfig> getConfigClass() {
            return JsonMutatorConfig.class;
        }
    },
    RAW {
        @Override
        public Class<RawMutatorConfig> getConfigClass() {
            return RawMutatorConfig.class;
        }

    };

}
