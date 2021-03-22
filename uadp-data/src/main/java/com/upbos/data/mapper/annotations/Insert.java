package com.upbos.data.mapper.annotations;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Insert {
    boolean isBatch() default false;

    String[] ignoreProps() default {};
}
