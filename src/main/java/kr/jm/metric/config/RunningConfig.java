package kr.jm.metric.config;

import lombok.*;

import java.util.Map;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RunningConfig extends AbstractConfig {
    private BindingConfig binding;
    private Map<String, Object>[] inputs;
    private Map<String, Object>[] mutators;
    private Map<String, Object>[] outputs;
}
