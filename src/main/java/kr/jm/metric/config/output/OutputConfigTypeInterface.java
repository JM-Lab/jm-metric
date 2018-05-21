package kr.jm.metric.config.output;

import com.fasterxml.jackson.core.type.TypeReference;

public interface OutputConfigTypeInterface {
    <C extends OutputConfigInterface> TypeReference<C> getTypeReference();
}
