package kr.jm.metric.mutator;

import kr.jm.metric.config.mutator.RawMutatorConfig;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

/**
 * The type Raw field map mutator.
 */
@ToString(callSuper = true)
public class RawFieldMapMutator extends
        AbstractFieldMapMutator<RawMutatorConfig> {

    /**
     * Instantiates a new Raw field map mutator.
     *
     * @param mutatorConfig the mutator config
     */
    public RawFieldMapMutator(
            RawMutatorConfig mutatorConfig) {
        super(mutatorConfig);
    }

    /**
     * Instantiates a new Raw field map mutator.
     */
    public RawFieldMapMutator() {
        this(new RawMutatorConfig("Raw"));
    }

    @Override
    public Map<String, Object> buildFieldObjectMap(String targetString) {
        return new HashMap<>();
    }

}
