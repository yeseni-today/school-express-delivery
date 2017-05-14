package one;

import com.delivery.Application;
import com.delivery.common.dao.UserDao;
import com.delivery.export.HttpConstant;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
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
 * @date 22/04/2017
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringBootTest(classes = Application.class)
public class UserControllerTest {

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
    public void login() throws Exception {
        String uri = "/user/login";
        MvcResult mockResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)
                .param("user_phone", "12345678901")
                .param("user_password", "123456")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        println();
        System.out.println("登陆测试");
        String content = mockResult.getResponse().getContentAsString();
        System.out.println(content);
        println();
    }

    //    @Test
    public void test() {
        String phone = "18217699893";
        System.out.println(
                userDao.findByUserPhone(phone).getSchoolAddress()
        );
    }

    //    @Test
    public void register() throws Exception {
        String url = "/user/register";

        String phone = "18217699800";

        MvcResult mockResult = mockMvc.perform(MockMvcRequestBuilders.post(url)
                .param("user_name", "Ticknick")
                .param("user_phone", phone)
                .param("user_password", "123456")
                .param("user_schoolcard", "123456789")
                .param("user_sex", "男")
                .param("user_schoolname", "上海下师大")
                .param("user_schooladdress", "默认地址100号")
                .accept(MediaType.ALL)
        ).andReturn();

        println();
        System.out.println("注册测试");
        System.out.println(mockResult.getResponse().getStatus());
        System.out.println(mockResult.getResponse().getContentAsString());
        System.out.println("sex from datebase:" + userDao.findByUserPhone(phone).getSex()
        );
        println();
    }

    @Autowired
    UserDao userDao;

    public void println() {
        System.out.println("------------------------------");
    }

    protected String Obj2Json(Object obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(obj);
    }

//    @Test
    public void upgrade() throws Exception {

        String url = HttpConstant.USER_UPGRADE;

        HashMap<String, String> keyvals = new HashMap<>();

        keyvals.put("token", recpiaToken);
        keyvals.put("review_type", "0");
        keyvals.put("school_card", "1000383871");
        keyvals.put("id_card", "2123123");
        keyvals.put("alipay", "adad");


        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post(url);

        for (Map.Entry<String, String> entry : keyvals.entrySet()) {
            builder.param(entry.getKey(), entry.getValue());
        }

        builder.accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(builder).andReturn();

        String content = result.getResponse().getContentAsString();
        OUtil.printJson(url, "post", keyvals, content);

    }


}
