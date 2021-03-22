package com.upbos.cache;

import org.junit.jupiter.api.AssertionsKt;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Arrays;


@SpringBootTest(classes = {SingleCacheManagerConfiguration.class, RedisAutoConfiguration.class})
@ContextConfiguration
public class SingleCacheTests {

    @Resource
    private SingleCacheManager singleCacheManager;

    @Resource
    private ApplicationContext applicationContext;

    private static String CACHE_NAME = "default";

    @Test
    public void testCache() throws InterruptedException {

        User user = new User();
        user.setId(111);
        user.setName("王五");

        singleCacheManager.getCache(CACHE_NAME).set("USER", user);
        singleCacheManager.getCache(CACHE_NAME).set("userName", "张三");


        Thread.sleep(3000);

        String userName = singleCacheManager.getCache(CACHE_NAME).get("userName");
        Assert.isNull(userName, "获取的缓存值必须为空");
        Thread.sleep(1000);

        userName = singleCacheManager.getCache(CACHE_NAME).get("userName");
        Assert.isNull(userName, "获取的值必须为空");
    }

    @Test
    public void testBean() {
        String[] beans = applicationContext.getBeanDefinitionNames();
        Arrays.sort(beans);
        for (String bean : beans) {
            System.out.println("bean名称：" + bean + "  :: " + applicationContext.getBean(bean).getClass());
        }
    }

}
