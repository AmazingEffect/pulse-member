package com.pulse.member.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@ConfigurationProperties(prefix = "app")
@Component
public class AppProperties {

    private boolean includeStackTrace;

}
