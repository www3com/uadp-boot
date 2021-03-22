package com.upbos.boot.data;

import com.upbos.data.configuration.DaoAutoConfiguration;
import com.upbos.data.configuration.DataSourceAutoConfiguration;
import com.upbos.data.core.SingleDao;
import com.upbos.util.RandomUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest(classes = {DaoAutoConfiguration.class, DataSourceAutoConfiguration.class})
@ContextConfiguration
public class SingleDaoTests {

    @Resource
    private SingleDao singleDao;

    @Test
    public void insert() {
        User user = new User();
        user.setLoginName(RandomUtils.getCharacterAndNumber(9));
        user.setPassword("myzh1234");
        user.setName("Jason");
        int row = singleDao.insert(user);
//        Assert.assertEquals(1, row);
    }

    @Test
    public void queryList() {
        List<User> list = singleDao.queryList("UserDaoMapper.listUser");
        System.out.println(list.size());
    }
}
