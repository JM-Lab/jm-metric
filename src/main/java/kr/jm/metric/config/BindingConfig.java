package kr.jm.metric.config;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class BindingConfig {
    private String inputId;
    private String mutatorId;
    private String[] outputIds;
}
