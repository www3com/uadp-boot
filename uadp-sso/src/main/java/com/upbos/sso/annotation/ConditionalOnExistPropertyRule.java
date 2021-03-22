package com.upbos.sso.annotation;


import org.springframework.boot.env.OriginTrackedMapPropertySource;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.type.AnnotatedTypeMetadata;


import java.util.Iterator;
import java.util.Map;

public class ConditionalOnExistPropertyRule implements Condition {
    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        Map<String, Object> annotationAttributes = annotatedTypeMetadata.getAnnotationAttributes(ConditionalOnExistProperty.class.getName());
        String prefix = (String) annotationAttributes.get("prefix");
        String name = (String) annotationAttributes.get("name");
        boolean noMathIfExist = (boolean) annotationAttributes.get("noMathIfExist");
        StandardEnvironment environment = (StandardEnvironment) conditionContext.getEnvironment();
        MutablePropertySources propertySources = environment.getPropertySources();
        Iterator iterator = propertySources.iterator();
        boolean isContain = false;
        while (iterator.hasNext()) {
            PropertySource propertySource = (PropertySource) iterator.next();
            if (propertySource instanceof OriginTrackedMapPropertySource) {
                OriginTrackedMapPropertySource op = (OriginTrackedMapPropertySource) propertySource;
                Map<String, Object> map = op.getSource();
                if (contain(map, prefix, name)) {
                    isContain = true;
                    break;
                }
            }
        }

        return isContain ? (noMathIfExist ? false : true) : false;
    }

    public boolean contain(Map<String, Object> source, String prefix, String name) {
        if (source == null) {
            return false;
        }
        for (String key : source.keySet()) {
            boolean isStart = (key + ".").startsWith(prefix + ("".equals(prefix) ? "" : ".") + name + ".");
            if (isStart) {
                return true;
            }
        }
        return false;
    }
}

