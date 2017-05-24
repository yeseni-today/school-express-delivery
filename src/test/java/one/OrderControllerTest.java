//package one;
//
//import com.delivery.Application;
//import com.delivery.common.Response;
//import com.delivery.export.HttpConstant;
//import com.google.gson.Gson;
//import com.google.gson.internal.LinkedTreeMap;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.context.web.WebAppConfiguration;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//
//import java.util.Arrays;
//
///**
// * @author finderlo
// * @date 23/04/2017
// */
//@RunWith(SpringJUnit4ClassRunner.class)
//@WebAppConfiguration
//@SpringBootTest(classes = Application.class)
//public class OrderControllerTest {
//
//    MockMvc mockMvc;
//
//    @Autowired
//    private
//    WebApplicationContext applicationContext;
//
//    String recpiaToken;
//
//    String replacementToken;
//
//    String adminToken;
//
//    String atimelineToken;
//
//    @Before
//    public void setUp() throws Exception {
//        mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext).build();
//        String uri = "/user/login";
//        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri)
//                .param("user_phone", "12345678901")
//                .param("user_password", "123456")
//                .accept(MediaType.APPLICATION_JSON))
//                .andReturn();
//
//        String content = mvcResult.getResponse().getContentAsString();
//        TestUtil.println();
//        System.out.println(content);
//        recpiaToken = getToken(content);
//        System.out.println("发件人token:" + recpiaToken);
//        TestUtil.println();
//        MvcResult resplaceReslt = mockMvc.perform(MockMvcRequestBuilders.post(uri)
//                .param("user_phone", "18217699895")
//                .param("user_password", "123456")
//                .accept(MediaType.APPLICATION_JSON))
//                .andReturn();
//        TestUtil.println();
//        System.out.println(resplaceReslt.getResponse().getContentAsString());
//        replacementToken = getToken(resplaceReslt.getResponse().getContentAsString());
//        System.out.println("代取人token：" + replacementToken);
//        TestUtil.println();
//
//        TestUtil.println();
//        MvcResult admin = mockMvc.perform(MockMvcRequestBuilders.post(uri)
//                .param("user_phone", "18217699894")
//                .param("user_password", "123456")
//                .accept(MediaType.APPLICATION_JSON))
//                .andReturn();
//        adminToken = getToken(admin.getResponse().getContentAsString());
//        System.out.println("管理员Token: " + adminToken);
//        TestUtil.println();
//
//
//        TestUtil.println();
//        MvcResult atimeline = mockMvc.perform(MockMvcRequestBuilders.post(uri)
//                .param("user_phone", "14424223232")
//                .param("user_password", "123456")
//                .accept(MediaType.APPLICATION_JSON))
//                .andReturn();
//        atimelineToken = getToken(admin.getResponse().getContentAsString());
//        System.out.println("timeline Token: " + atimelineToken);
//        TestUtil.println();
//    }
//
//
//    public String getToken(String json) {
//        Response result = new Gson().fromJson(json, Response.class);
//        LinkedTreeMap content = (LinkedTreeMap) result.getData();
//        for (Object key : content.keySet()) {
//            if ("token.TokenTest".equals(key.toString())) {
//                return (String) content.get(key);
//            }
//        }
//        return null;
//    }
//
//
//    //@Test
//    public void check() throws Exception {
//
//        String url = "/order/check_create";
//        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(url)
//                .param("token.TokenTest", recpiaToken)
//                .accept(MediaType.ALL)).andReturn();
//        TestUtil.println();
//        System.out.println("校对订单");
//        System.out.println(mvcResult.getResponse().getContentAsString());
//        TestUtil.println();
//    }
//
//    //@Test
//    public void create() throws Exception {
//
//        String url = "/order/create";
//        MvcResult result = mockMvc.perform(
//                MockMvcRequestBuilders.post(url)
//                        //必填
//                        .param("token.TokenTest", recpiaToken)
//                        .param("express_name", "中通")
//                        .param("pickup_address", "上市发东门口")
//                        .param("pickup_time", "2017-04-28 14:59:59")
//                        .param("delivery_address", "28号楼505")
//                        .param("delivery_time", "2017-04-28 14:59:59")
//                        //可选
//                        .param("express_code", "14012412412")
//                        .param("pickup_code", "124")
//                        .param("orders_remark", "备注无")
//
//                        .accept(MediaType.APPLICATION_JSON)
//        ).andReturn();
//
//        TestUtil.println();
//        System.out.println("创建订单");
//        String content = result.getResponse().getContentAsString();
//        System.out.println(content);
//        TestUtil.println();
//
//    }
//
//    //@Test
//    public void find() throws Exception {
//
//        String url = "/order/findByPhone";
//        MvcResult result = mockMvc.perform(
//                MockMvcRequestBuilders.get(url)
//                        //并列查询条件
//                        .param("token.TokenTest", adminToken)
//                        .param("recipient_ID", "213124124")
////                        .param("replacement_ID", "上市发东门口")
////                        .param("orders_ID", "2017-04-28 14:59:59")
//                        .accept(MediaType.APPLICATION_JSON)
//        ).andReturn();
//
//        TestUtil.println();
//        System.out.println("查找订单");
//        String content = result.getResponse().getContentAsString();
//        System.out.println(content);
//        TestUtil.println();
//
//    }
//
//    //@Test
//    public void findUserCompleteOrder() throws Exception {
//        String url = "/order/find_by_user_uncomplete";
//        MvcResult result = mockMvc.perform(
//                MockMvcRequestBuilders.get(url)
//                        //并列查询条件
//                        .param("token.TokenTest", recpiaToken)
////                        .param("recipient_ID", "213124124")
////                        .param("replacement_ID", "上市发东门口")
////                        .param("orders_ID", "2017-04-28 14:59:59")
//                        .accept(MediaType.APPLICATION_JSON)
//        ).andReturn();
//
//        TestUtil.println();
//        System.out.println("查找用户未完成订单");
//        String content = result.getResponse().getContentAsString();
//        System.out.println(content);
//        TestUtil.println();
//
//    }
//
////    @Test
//    //todo
//    public void timeline() throws Exception {
//        String url = "/order/timeline";
//        TestUtil.println();
//        MvcResult result = mockMvc.perform(
//                MockMvcRequestBuilders.get(url)
//                        //并列查询条件
//                        .param("token.TokenTest", atimelineToken)
//                        .accept(MediaType.APPLICATION_JSON)
//        ).andReturn();
//        System.out.println("查找用户当前可以接收订单");
//        String res = result.getResponse().toString();
//        System.out.println(res);
//        String content = result.getResponse().getContentAsString();
//        System.out.println(content);
//        TestUtil.println();
//
//    }
//
//    //@Test
//    public void accept() throws Exception {
//        String url = HttpConstant.ORDER_ACCEPT;
//
//        MvcResult result = mockMvc.perform(
//                MockMvcRequestBuilders.post(url)
//                        //并列查询条件
//                        .param("token.TokenTest", replacementToken)
//                        .param("order_id", "20170423100000")
//                        .accept(MediaType.APPLICATION_JSON)
//        ).andReturn();
//
//        TestUtil.println();
//        System.out.println("用户接收订单");
//        String content = result.getResponse().getContentAsString();
//        System.out.println(content);
//        TestUtil.println();
//
//    }
//
//    //    @Test
//    public void delivery() throws Exception {
//        String url = HttpConstant.ORDER_DELIVERY;
//
//        MvcResult result = mockMvc.perform(
//                MockMvcRequestBuilders.post(url)
//                        //并列查询条件
//                        .param("token.TokenTest", replacementToken)
//                        .param("order_id", "20170423100000")
//                        .accept(MediaType.APPLICATION_JSON)
//        ).andReturn();
//
//        TestUtil.println();
//        System.out.println("代取人订单状态设为待递送（拿到快递）");
//        String content = result.getResponse().getContentAsString();
//        System.out.println(content);
//        TestUtil.println();
//
//    }
//
//    @Test
//    public void ORDER_FIND_ORDER_AND_USER() throws Exception {
//
//        String url = HttpConstant.ORDER_FIND_ORDER_AND_USER;
//
//        MvcResult result = mockMvc.perform(
//                MockMvcRequestBuilders.get(url)
//                        //并列查询条件
//                        .param("token.TokenTest", replacementToken)
//                        .param("order_id", "20170423100000")
//                        .accept(MediaType.APPLICATION_JSON)
//        ).andReturn();
//
////        println();
//        String content = result.getResponse().getContentAsString();
////        System.out.println(content);
////        println();
//        TestUtil.printJson(url, "get", Arrays.asList("token.TokenTest","order_id"),Arrays.asList(replacementToken,"20170423100000"),content);
//
//    }
//
////    @Test
//    public void ORDER_FIND_ORDER_LOG() throws Exception{
//
//        String url = HttpConstant.ORDER_FIND_ORDER_LOG;
//
//        MvcResult result = mockMvc.perform(
//                MockMvcRequestBuilders.get(url)
//                        //并列查询条件
//                        .param("token.TokenTest", replacementToken)
//                        .param("order_id", "20170423100000")
//                        .accept(MediaType.APPLICATION_JSON)
//        ).andReturn();
//
////        println();
//        String content = result.getResponse().getContentAsString();
////        System.out.println(content);
////        println();
//        TestUtil.printJson(url, "get", Arrays.asList("token.TokenTest","order_id"),Arrays.asList(replacementToken,"20170423100000"),content);
//
//    }
//
//
//}
