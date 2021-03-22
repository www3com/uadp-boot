package com.upbos.data.mapper.annotations;

import com.upbos.data.mapper.MapperScanRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
//spring中的注解,加载对应的类
@Import(MapperScanRegistrar.class)
@Documented
public @interface MapperScan {

    String[] basePackages() default {};
}