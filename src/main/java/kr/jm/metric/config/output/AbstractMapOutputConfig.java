package kr.jm.metric.config.output;

import kr.jm.utils.helper.JMJson;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Map;


@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractMapOutputConfig extends AbstractOutputConfig {
    @Override
    public Map<String, Object> extractConfigMap() {
        return JMJson.transformToMap(this);
    }

    @Override
    public Map<String, Object> getProperties() {
        return null;
    }

    @Override
    protected Map<String, Object> buildChildConfig() {
        return null;
    }

}
