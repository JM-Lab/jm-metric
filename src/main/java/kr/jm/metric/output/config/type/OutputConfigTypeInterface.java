package kr.jm.metric.output.config.type;

import com.fasterxml.jackson.core.type.TypeReference;
import kr.jm.metric.output.config.OutputConfigInterface;

public interface OutputConfigTypeInterface {
    <C extends OutputConfigInterface> TypeReference<C> getTypeReference();
}
