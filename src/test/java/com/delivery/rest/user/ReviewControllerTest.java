package com.delivery.rest.user;

import com.delivery.Application;
import com.delivery.common.constant.Constant;
import one.RequestParams;
import one.TestUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.*;

/**
 * @author finderlo
 * @date 17/05/2017
 */
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class ReviewControllerTest {

    @Autowired
    WebApplicationContext applicationContext;


    MockMvc mockMvc;

    String recipToken;
    String replaceToken;
    String adminToken;

    @Before
    public void setup() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext).build();
        recipToken = TestUtil.getToken(mockMvc, "18217699894", "123456");
        replaceToken = TestUtil.getToken(mockMvc, "18217699800", "123456");
        adminToken = TestUtil.getTokenFromUid(mockMvc, "10001", "123456");
        System.out.println("收件人TOKEN：" + recipToken);
        System.out.println("代取人TOKEN：" + replaceToken);
        System.out.println("管理员TOKEN：" + adminToken);
    }

    @Test
    public void find() throws Exception {
        String uri = "/reviews";
        TestUtil.Method method = TestUtil.Method.GET;
        RequestParams params = new RequestParams();
        params.put(Constant.AUTHORIZATION, adminToken);
        params.put("uid", "");
        params.put("state", "0");

        TestUtil.requestAndPrint(mockMvc, uri, method, params);
    }

    @Test
    public void findOwn() throws Exception {
        String token = TestUtil.getTokenFromUid(mockMvc, "213124124", "123456");
        String uri = "/reviews/token";
        TestUtil.Method method = TestUtil.Method.GET;
        RequestParams params = new RequestParams();
        params.put(Constant.AUTHORIZATION, token);
        TestUtil.requestAndPrint(mockMvc, uri, method, params);

    }

    @Test
    public void modify() throws Exception {
        String uri = "/reviews/2017051700000";
        TestUtil.Method method = TestUtil.Method.PUT;
        RequestParams params = new RequestParams();
        params.put("result", "1");
        params.put("remark", "用户太美");
        params.put(Constant.AUTHORIZATION,adminToken);

        TestUtil.requestAndPrint(mockMvc,uri,method,params);
    }

    @Test
    public void newReview() throws Exception {
        String token = TestUtil.getTokenFromUid(mockMvc, "213124124", "123456");
        String uri = "/reviews";
        TestUtil.Method method = TestUtil.Method.POST;
        RequestParams params = new RequestParams();
        params.put(Constant.AUTHORIZATION, token);
        params.put("type", "0");
        TestUtil.requestAndPrint(mockMvc, uri, method, params);

    }

}