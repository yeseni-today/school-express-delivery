package order;

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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;

/**
 * @author finderlo
 * @date 15/05/2017
 */
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class OrderTest {


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
    public void order_user() throws Exception {

        String url = "/orders/user";
        HashMap<String, String> keyvals = new HashMap<>();
        TestUtil.Method method = TestUtil.Method.GET;
        keyvals.put(Constant.AUTHORIZATION, recipToken);

    }


    //@Test
    public void new_order() throws Exception {

        String url = "/orders";
        TestUtil.Method method = TestUtil.Method.POST;
        HashMap<String, String> keyvals = new HashMap<>();
        keyvals.put(Constant.AUTHORIZATION, recipToken);
        keyvals.put("express_name", "express_name");
        keyvals.put("pickup_address", "pickup_address");
        keyvals.put("delivery_address", "delivery_address");
        keyvals.put("delivery_time", "2017-04-28 14:59:59");
        keyvals.put("pickup_time", "2017-04-28 14:59:59");
        keyvals.put("price", "10");

        keyvals.put("pickup_code", "123");
        keyvals.put("express_code", "456");
        keyvals.put("remark", "remark");

        MvcResult result = TestUtil.getHttpResultContent(mockMvc, url, method, keyvals);


        System.out.println("----------------");
        System.out.println(url + "  " + method.name());
        System.out.println(result.getResponse().getStatus());
        System.out.println(result.getResponse().getContentAsString());
        System.out.println("----------------");

    }

    //@Test
    public void modify_order_msg() throws Exception {
        String uri = "/orders/100005";
        TestUtil.Method method = TestUtil.Method.PUT;
        RequestParams param = new RequestParams();
        param.put(Constant.AUTHORIZATION, adminToken);
        param.put("state", "9");

        TestUtil.requestAndPrint(mockMvc,uri,method,param);
        //8 代表申诉中的订单
    }

    @Test
    public void processlist() throws Exception {
        String uri = "/orders/100005/process";
        TestUtil.Method method = TestUtil.Method.GET;
        RequestParams param = new RequestParams();
        param.put(Constant.AUTHORIZATION, adminToken);
        TestUtil.requestAndPrint(mockMvc,uri,method,param);
    }

}
