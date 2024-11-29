package com.pulse.member.common.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @MongoAdapter는 MongoDB와 상호작용하는 아웃바운드 어댑터에 사용되는 어노테이션입니다.
 * MongoDB에 특화된 구현체를 명확히 식별하는 데 도움을 줍니다.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface MongoAdapter {
    String value() default "";
}
