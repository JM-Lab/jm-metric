package kr.jm.metric.mutator;

import kr.jm.metric.config.mutator.JsonMutatorConfig;
import kr.jm.utils.datastructure.JMMap;
import kr.jm.utils.helper.JMJson;
import lombok.ToString;

import java.util.Map;

/**
 * The type Json field map mutator.
 */
@ToString(callSuper = true)
public class JsonMutator extends
        AbstractMutator<JsonMutatorConfig> {

    /**
     * Instantiates a new Json field map mutator.
     */
    public JsonMutator() {
        this(new JsonMutatorConfig("JsonMutator"));
    }

    /**
     * Instantiates a new Json field map mutator.
     *
     * @param jsonMutatorConfig the json mutator config
     */
    public JsonMutator(JsonMutatorConfig jsonMutatorConfig) {
        super(jsonMutatorConfig);
    }

    @Override
    public Map<String, Object> buildFieldObjectMap(String targetString) {
        return JMMap.newFlatKeyMap(JMJson.toMap(targetString));
    }

}
