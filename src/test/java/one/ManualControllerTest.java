package one;

import com.delivery.Application;
import com.delivery.export.HttpConstant;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

import static one.SimpleTest.getToken;

/**
 * @author finderlo
 * @date 14/05/2017
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringBootTest(classes = Application.class)
public class ManualControllerTest {

    MockMvc mockMvc;

    @Autowired
    WebApplicationContext applicationContext;


    String expectedJson;


    String recpiaToken;

    String replacementToken;

    String adminToken;

    String atimelineToken;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext).build();
        String uri = "/user/login";
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri)
                .param("user_phone", "12345678901")
                .param("user_password", "123456")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        OUtil.println();
        System.out.println(content);
        recpiaToken = getToken(content);
        System.out.println("发件人token:" + recpiaToken);
        OUtil.println();
        MvcResult resplaceReslt = mockMvc.perform(MockMvcRequestBuilders.post(uri)
                .param("user_phone", "18217699895")
                .param("user_password", "123456")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        OUtil.println();
        System.out.println(resplaceReslt.getResponse().getContentAsString());
        replacementToken = getToken(resplaceReslt.getResponse().getContentAsString());
        System.out.println("代取人token：" + replacementToken);
        OUtil.println();

        OUtil.println();
        MvcResult admin = mockMvc.perform(MockMvcRequestBuilders.post(uri)
                .param("user_phone", "18217699894")
                .param("user_password", "123456")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        adminToken = getToken(admin.getResponse().getContentAsString());
        System.out.println("管理员Token: " + adminToken);
        OUtil.println();


        OUtil.println();
        MvcResult atimeline = mockMvc.perform(MockMvcRequestBuilders.post(uri)
                .param("user_phone", "14424223232")
                .param("user_password", "123456")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        atimelineToken = getToken(admin.getResponse().getContentAsString());
        System.out.println("timeline Token: " + atimelineToken);
        OUtil.println();
    }

//    @Test
    public void get_reviewlist() throws Exception {

        String url = HttpConstant.MANUAL_GET_REVIEW_LIST;

        HashMap<String, String> keyvals = new HashMap<>();

        keyvals.put("token", adminToken);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get(url);

        for (Map.Entry<String, String> entry : keyvals.entrySet()) {
            builder.param(entry.getKey(), entry.getValue());
        }

        builder.accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(builder).andReturn();

        String content = result.getResponse().getContentAsString();
        OUtil.printJson(url, "get", keyvals, content);

    }

    @Test
    public void get_review_BYuser() throws Exception {

        String url = HttpConstant.MANUAL_GET_REVIEW_BY_USER;

        HashMap<String, String> keyvals = new HashMap<>();

        keyvals.put("token", adminToken);
        keyvals.put("review_user_id", "213124124");

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get(url);

        for (Map.Entry<String, String> entry : keyvals.entrySet()) {
            builder.param(entry.getKey(), entry.getValue());
        }

        builder.accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(builder).andReturn();

        String content = result.getResponse().getContentAsString();
        OUtil.printJson(url, "get", keyvals, content);

    }
}
