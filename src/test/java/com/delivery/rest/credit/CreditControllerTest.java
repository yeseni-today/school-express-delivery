package com.delivery.rest.credit;

import com.delivery.Application;
import com.delivery.common.StringMap;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.xml.ws.spi.http.HttpContext;

import static org.junit.Assert.*;

/**
 * @author finderlo
 * @date 16/05/2017
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringBootTest(classes = Application.class)
public class CreditControllerTest {

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
        adminToken = TestUtil.getToken(mockMvc, "18217699895", "123456");
        System.out.println("收件人TOKEN：" + recipToken);
        System.out.println("代取人TOKEN：" + replaceToken);
        System.out.println("管理员TOKEN：" + adminToken);
    }

    //@Test
    public void userCreditValueAdmin() throws Exception {

        String uri = "/credits/213124129";
        TestUtil.Method method = TestUtil.Method.GET;
        RequestParams params = new RequestParams();
        params.put(Constant.AUTHORIZATION,adminToken);
        MvcResult result = TestUtil.requestAndPrint(mockMvc,uri,method,params);

        System.out.println(result.getResponse().getStatus());
    }

    @Test
    public void userRecords() throws Exception {
        String phone = "18200000004";
        String token = TestUtil.getToken(mockMvc,phone,"123456");
        String uri = "/credits/token";
        TestUtil.Method method = TestUtil.Method.GET;
        RequestParams params = new RequestParams();
        params.put(Constant.AUTHORIZATION,token);

        TestUtil.requestAndPrint(mockMvc,uri,method,params);
    }

}