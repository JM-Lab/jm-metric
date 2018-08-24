package kr.jm.metric.config.mutator.field;


import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class FilterConfig {
    private String matchRegex;
    private boolean negate;
}
