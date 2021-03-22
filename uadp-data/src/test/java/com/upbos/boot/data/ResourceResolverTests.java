package com.upbos.boot.data;


import org.junit.jupiter.api.Test;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.test.context.ContextConfiguration;

import java.io.IOException;


@ContextConfiguration
public class ResourceResolverTests {

    @Test
    public void resolver() throws IOException {
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("classpath*:/mapper/*.xml");
        System.out.println(resources.length);
    }

}
