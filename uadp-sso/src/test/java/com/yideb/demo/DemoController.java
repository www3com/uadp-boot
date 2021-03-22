package com.yideb.demo;

import cn.hutool.core.map.CamelCaseMap;
import com.upbos.sso.SsoManager;
import com.upbos.sso.entity.Token;
import com.upbos.sso.ret.RetData;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("sso")
public class DemoController {

    @Resource
    private SsoManager ssoManager;

    @RequestMapping("login")
    public RetData login(HttpServletRequest request, HttpServletResponse response,  String username, String password) {

        System.out.printf("输入的参数：username: %s, password: %s\n", username, password);

        if (!"wangjz".equals(username) || !"test".equals(password)) {
            return RetData.fail("用户名或者密码输入有误");
        }

        Map<String, Object> m = new HashMap<>();
        m.put("name", "wangjz");
        m.put("orgId", "2232");

        Token token = ssoManager.login(request, response, "2");

        Token token1 = ssoManager.getToken(request);



        token1.getData();

        return RetData.success("登录成功", token);
    }

    @RequestMapping("queryUser")
    public RetData queryUser(HttpServletRequest request) {
        RetData retData = RetData.success();
        retData.putData("uid", ssoManager.getToken(request).getUid());
        retData.putData("name", "Jason");
        return retData;
    }

}
