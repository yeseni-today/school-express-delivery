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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author finderlo
 * @date 16/05/2017
 */
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class UserTest {
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
    public void new_user() throws Exception {

        String uri = "/users";
        TestUtil.Method method = TestUtil.Method.POST;

        RequestParams params = new RequestParams();
        params.put("name", "register_test");
        params.put("phone", "12362789123");
        params.put("sex", "女");
        params.put("password", "123456");
        params.put("school_card", "1000000000");
        params.put("school_name", "Shnu");
        params.put("school_address", "100hao");

        TestUtil.requestAndPrint(mockMvc, uri, method, params);
    }

    //@Test
   public void admin_find() throws Exception {
        String uid = "1000000015";
        String uri = "/users/" + uid;
        TestUtil.Method method = TestUtil.Method.GET;

        RequestParams params = new RequestParams();
        params.put(Constant.AUTHORIZATION, adminToken);

        TestUtil.requestAndPrint(mockMvc, uri, method, params);
    }

    //@Test
    public void modify_user() throws Exception {
        String uri = "/users/token";
        TestUtil.Method method = TestUtil.Method.PUT;

        RequestParams params = new RequestParams();
        params.put(Constant.AUTHORIZATION, recipToken);
        params.put("school_card","adasdads");
        params.put("id_card","adasdads");
        params.put("photo","photourl");
        params.put("alipay","houyuooooo");

        TestUtil.requestAndPrint(mockMvc,uri,method,params);
    }

    @Test
    public void own() throws Exception {
        String uri = "/users/token";
        TestUtil.Method method = TestUtil.Method.GET;

        RequestParams params = new RequestParams();
        params.put(Constant.AUTHORIZATION, recipToken);
        TestUtil.requestAndPrint(mockMvc,uri,method,params);
    }
}
