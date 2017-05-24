package order;

import com.delivery.Application;
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
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration()
public class TokenTest {

    @Autowired
    WebApplicationContext applicationContext;

    MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext).build();
        login();
    }

    String token;

//    @Test
    public void login() throws Exception {

        String url = "/tokens";
        HashMap<String, String> keyvals = new HashMap<>();
        keyvals.put("phone", "18217699800");
        keyvals.put("password", "123456");
        MvcResult result = TestUtil.getHttpResultContent(mockMvc, url, TestUtil.Method.POST, keyvals);
        System.out.println("----------------");
        for (String s : result.getResponse().getHeaderNames()) {
            System.out.println(s + ": " + result.getResponse().getHeaders(s));
        }
        System.out.println("----------------");
        System.out.println(result.getResponse().getStatus());
        System.out.println("----------------");
        System.out.println(result.getResponse().getContentAsString());
        token = TestUtil.getTokenFromJson(result.getResponse().getContentAsString());
        System.out.println("----------------");
        //{"code":200,"message":"ok","content":{"uid":"213124128","token":"213124128_9ffabcb2-43ad-426a-bd63-f209de6d7f1e"}}
        //{"code":90004,"message":"用户名或者密码不正确","content":null}

    }

    @Test
    public void logout() throws Exception {

        String url = "/tokens";
        HashMap<String, String> keyvals = new HashMap<>();
//        keyvals.put("phone", "18217699800");
//        keyvals.put("password", "123456");
        keyvals.put("token",token);
        TestUtil.Method method = TestUtil.Method.DELETE;

        MvcResult result = TestUtil.getHttpResultContent(mockMvc, url, method, keyvals);
        System.out.println("----------------");
        System.out.println(url+"  "+method);
        for (String s : result.getResponse().getHeaderNames()) {
            System.out.println(s + ": " + result.getResponse().getHeaders(s));
        }
        System.out.println(result.getResponse().getStatus());
        System.out.println(result.getResponse().getContentAsString());
        System.out.println("----------------");
        //{"code":200,"message":"ok","content":{"uid":"213124128","token":"213124128_9ffabcb2-43ad-426a-bd63-f209de6d7f1e"}}
        //{"code":90004,"message":"用户名或者密码不正确","content":null}

    }


}
