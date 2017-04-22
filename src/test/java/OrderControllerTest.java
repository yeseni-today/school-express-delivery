import com.delivery.Application;
import com.delivery.common.Response;
import com.delivery.common.entity.UsersEntity;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.thymeleaf.spring4.expression.Mvc;

import java.util.HashMap;

/**
 * @author finderlo
 * @date 23/04/2017
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringBootTest(classes = Application.class)
public class OrderControllerTest {

    MockMvc mockMvc;

    @Autowired
    private
    WebApplicationContext applicationContext;

    String token;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext).build();
        String uri = "/user/login";
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)
                .param("user_phone", "12345678901")
                .param("user_password", "123456")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        System.out.println(content);
        token = getToken(content);
        System.out.println(token);
    }


    public String getToken(String json) {
        Response result = new Gson().fromJson(json, Response.class);
        LinkedTreeMap content = (LinkedTreeMap) result.getContent();
        for (Object key : content.keySet()) {
            System.out.println(key);
            if ("token".equals(key.toString())) {
                return (String) content.get(key);
            }
        }
        return null;
    }


    @Test
    public void check() {
        
    }

}
