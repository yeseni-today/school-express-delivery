package messages;

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
public class MessageTest {

    @Autowired
    private
    WebApplicationContext context;

    private MockMvc mockMvc;

    private String recipToken;
    private String replaceToken;
    private String adminToken;


    @Before
    public void setup() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        recipToken = TestUtil.getToken(mockMvc, "18217699894", "123456");
        replaceToken = TestUtil.getToken(mockMvc, "18217699800", "123456");
        adminToken = TestUtil.getToken(mockMvc, "18217699895", "123456");
        System.out.println("收件人TOKEN：" + recipToken);
        System.out.println("代取人TOKEN：" + replaceToken);
        System.out.println("管理员TOKEN：" + adminToken);
    }

    @Test
    public void newmessage() throws Exception {
        String url = "/messages";
        TestUtil.Method method = TestUtil.Method.POST;
        RequestParams params = new RequestParams();
        params.put(Constant.AUTHORIZATION, adminToken);
        params.put("receiver", "213124128");
        params.put("type", "1");
        params.put("title", "title");
        params.put("content", "content");

        TestUtil.requestAndPrint(mockMvc, url, method, params);
    }

    @Test
    public void find() throws Exception {
        String token = TestUtil.getToken(mockMvc, "18217699800", "123456");

        String url = "/messages";
        RequestParams params = new RequestParams();
        params.put(Constant.AUTHORIZATION, token);

        TestUtil.Method method = TestUtil.Method.GET;

        TestUtil.requestAndPrint(mockMvc, url, method, params);
    }

    @Test
    public void modify() throws Exception {
        String token = TestUtil.getToken(mockMvc, "18217699800", "123456");

        String url = "/messages/1";
        RequestParams params = new RequestParams();
        params.put(Constant.AUTHORIZATION, token);
        params.put("state", "2");

        TestUtil.Method method = TestUtil.Method.PUT;

        TestUtil.requestAndPrint(mockMvc, url, method, params);
    }


}
