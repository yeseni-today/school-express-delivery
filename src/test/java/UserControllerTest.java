import com.delivery.Application;
import com.delivery.common.dao.UsersDao;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import sun.reflect.generics.tree.VoidDescriptor;

/**
 * @author finderlo
 * @date 22/04/2017
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringBootTest(classes = Application.class)
public class UserControllerTest {

    MockMvc mvc;

    @Autowired
    WebApplicationContext webApplicationContext;


    String expectedJson;

    @Before
    public void setUp() throws JsonProcessingException {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void login() throws Exception {
        String uri = "/user/login";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .param("user_phone", "12345678901")
                .param("user_password", "123456")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        println();
        System.out.println("登陆测试");
        String content = mvcResult.getResponse().getContentAsString();
        System.out.println(content);
        println();
    }

    @Test
    public void test(){
        String phone = "18217699893";
        System.out.println(
                usersDao.findByUserPhone(phone).getUserSchooladdress()
        );
    }

    public void register() throws Exception {
        String url = "/user/register";

        String phone = "18217699895";

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(url)
                .param("user_name", "finderlo")
                .param("user_phone", "18217699895")
                .param("user_password", "123456")
                .param("user_schoolcard", "123456789")
                .param("user_sex", "男")
                .param("user_schoolname", "上海下师大")
                .param("user_schooladdress", "默认地址100号")
                .accept(MediaType.ALL)
        ).andReturn();

        println();
        System.out.println("注册测试");
        System.out.println(mvcResult.getResponse().getStatus());
        System.out.println(mvcResult.getResponse().getContentAsString());
        System.out.println("sex from datebase:" + usersDao.findByUserPhone(phone).getUserSex()
        );
        println();
    }

    @Autowired
    UsersDao usersDao;

    public void println() {
        System.out.println("------------------------------");
    }

    protected String Obj2Json(Object obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(obj);
    }

//    @Test
//    public void testShowDaoInt() throws Exception {
//        List<TestPOJO> testPOJOList = testServices.showDao(10);
//        expectedJson = Obj2Json(testPOJOList);
//
//        String uri="/showDao?age=10";
//        MvcResult mvcResult=mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON)).andReturn();
//        int status=mvcResult.getResponse().getStatus();
//        String content=mvcResult.getResponse().getContentAsString();
//
//        Assert.assertTrue("错误，正确的返回值为200", status == 200);
//        Assert.assertFalse("错误，正确的返回值为200", status != 200);
//        Assert.assertTrue("数据一致", expectedJson.equals(content));
//        Assert.assertFalse("数据不一致", !expectedJson.equals(content));
//    }
//
//    @Test
//    public void testShowDaoString() throws Exception {
//        List<HotelDto> hotelDtoList=testServices.findByCountry("US");
//        expectedJson = Obj2Json(hotelDtoList);
//
//        String uri="/country/US";
//        MvcResult mvcResult=mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON)).andReturn();
//        int status=mvcResult.getResponse().getStatus();
//        String content=mvcResult.getResponse().getContentAsString();
//
//        Assert.assertTrue("错误，正确的返回值为200", status == 200);
//        Assert.assertFalse("错误，正确的返回值为200", status != 200);
//        Assert.assertTrue("数据一致", expectedJson.equals(content));
//        Assert.assertFalse("数据不一致", !expectedJson.equals(content));
//    }
//

}
