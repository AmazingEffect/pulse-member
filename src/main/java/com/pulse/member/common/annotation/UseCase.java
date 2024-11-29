package com.pulse.member.common.annotation;

import org.springframework.stereotype.Service;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @UseCase는 유즈케이스 또는 서비스 계층에 사용되는 어노테이션입니다.
 * 비즈니스 로직을 캡슐화하고 유즈케이스를 명확히 식별하는 데 도움을 줍니다.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Service
public @interface UseCase {
    String value() default "";
}
