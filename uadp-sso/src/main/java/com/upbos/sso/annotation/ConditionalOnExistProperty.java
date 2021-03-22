package com.upbos.sso.annotation;

import org.springframework.context.annotation.Conditional;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
@Conditional({ConditionalOnExistPropertyRule.class})
public @interface ConditionalOnExistProperty {
    @AliasFor("name")
    String value() default "";

    String prefix() default "";

    @AliasFor("value")
    String name() default "";

    boolean noMathIfExist() default false;
}
