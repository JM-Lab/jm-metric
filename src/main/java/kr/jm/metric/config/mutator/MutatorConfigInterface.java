package kr.jm.metric.config.mutator;

import kr.jm.metric.config.ConfigInterface;
import kr.jm.metric.config.mutator.field.FieldConfig;
import kr.jm.metric.mutator.MutatorInterface;

public interface MutatorConfigInterface extends ConfigInterface {

    MutatorInterface buildMutator();

    String[] getFields();

    String getMutatorId();

    MutatorConfigType getMutatorConfigType();

    FieldConfig getFieldConfig();

    int getWorkers();
}
