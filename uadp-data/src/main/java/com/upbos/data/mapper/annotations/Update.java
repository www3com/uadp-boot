package com.upbos.data.mapper.annotations;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Update {

    WhereType whereType() default WhereType.BY_ID;

    String where() default "";

    String[] ignoreProps() default {};
}
