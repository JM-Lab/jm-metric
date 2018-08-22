package kr.jm.metric.config;

import lombok.*;

import java.util.Map;

@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class RunningConfig extends AbstractConfig {
    private Map<String, Object> input;
    private Map<String, Object> mutator;
    private Map<String, Object> output;
}
