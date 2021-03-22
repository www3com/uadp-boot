package com.upbos.data.mapper;

import com.upbos.data.mapper.annotations.MapperScan;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.StandardAnnotationMetadata;

import java.util.Set;

public class MapperScanRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware {


    ResourceLoader resourceLoader;



    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {

        this.resourceLoader = resourceLoader;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        AnnotationAttributes annoAttrs = AnnotationAttributes.fromMap(annotationMetadata.getAnnotationAttributes(MapperScan.class.getName()));

        String[] basePackages = annoAttrs.getStringArray("basePackages");
        //没有设置 扫描路径,就扫描对应
        if(basePackages.length == 0){
            basePackages = new String[]{((StandardAnnotationMetadata) annotationMetadata).getIntrospectedClass().getPackage().getName()};
        }

        //自定义的 包扫描器
        MapperClassPathScanHandler scanHandle = new MapperClassPathScanHandler(beanDefinitionRegistry,false);

        if(resourceLoader != null){
            scanHandle.setResourceLoader(resourceLoader);
        }

        scanHandle.setBeanNameGenerator(new MapperBeanNameGenerator());
        //扫描指定路径下的接口
        Set<BeanDefinitionHolder> beanDefinitionHolders = scanHandle.doScan(basePackages);
    }
}