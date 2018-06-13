package kr.jm.metric.config.input;

import com.fasterxml.jackson.core.type.TypeReference;

public interface InputConfigTypeInterface {
    <C extends InputConfigInterface> TypeReference<C> getTypeReference();
}
