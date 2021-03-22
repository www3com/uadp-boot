package com.upbos.data.mapper;

import com.upbos.data.mapper.annotations.Mapper;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;

public class MapperBeanNameGenerator extends AnnotationBeanNameGenerator {

    @Override
    public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {

        //从自定义注解中拿name
        String name = getNameByServiceFindAnntation(definition, registry);
        if (name != null && !"".equals(name)) {
            return name;
        }
        //走原来的方法
        return super.generateBeanName(definition, registry);
    }

    private String getNameByServiceFindAnntation(BeanDefinition definition, BeanDefinitionRegistry registry) {
        String beanClassName = definition.getBeanClassName();
        try {
            Class<?> aClass = Class.forName(beanClassName);
            Mapper annotation = aClass.getAnnotation(Mapper.class);
            if (annotation == null) {
                return null;
            }
            return lowerFirstChar(aClass.getSimpleName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String lowerFirstChar(String str) {
        if (str == null || "".equals(str)) {
            return str;
        }

        if (str.length() == 1) {
            return str.toUpperCase();
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
