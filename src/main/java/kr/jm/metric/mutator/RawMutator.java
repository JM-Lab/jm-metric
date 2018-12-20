package kr.jm.metric.mutator;

import kr.jm.metric.config.mutator.RawMutatorConfig;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@ToString(callSuper = true)
public class RawMutator extends
        AbstractMutator<RawMutatorConfig> {

    public RawMutator(
            RawMutatorConfig mutatorConfig) {
        super(mutatorConfig);
    }

    public RawMutator() {
        this(new RawMutatorConfig("Raw"));
    }

    @Override
    public Map<String, Object> buildFieldObjectMap(String targetString) {
        return new HashMap<>();
    }

}
