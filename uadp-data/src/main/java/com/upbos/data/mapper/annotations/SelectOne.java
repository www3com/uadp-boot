package com.upbos.data.mapper.annotations;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface SelectOne {
    String sqlId() default "";

    String sql() default "";
}
