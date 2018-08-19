    /env--class-path bin/jm-metric.jar

import kr.jm.metric.JMMetric;
        JMMetric jmMetric=new JMMetric().testInput("Hello JMMetric !!!");

        import kr.jm.metric.config.mutator.ApacheAccessLogMutatorConfig;
        jmMetric=new JMMetric(jmMetric.getJmMetricConfigManager().insertMutatorConfig(new ApacheAccessLogMutatorConfig("CustomApacheLog","%h %l %u %t \"%r\" %>s %b \"%{Referer}i\" \"%{User-agent}i\" %D")),"CustomApacheLog").testInput("223.62.219.101 - - [08/Jun/2015:16:59:59 +0900] \"POST /app/5315 HTTP/1.1\" 200 1100 \"-\" \"Dalvik/1.6.0 (Linux; U; Android 4.4.2; SHV-E330S Build/KOT49H)\" 45195");
