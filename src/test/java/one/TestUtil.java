package one;

import com.delivery.common.Response;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.Assert;

import javax.sound.midi.Soundbank;
import java.net.URL;
import java.util.*;

/**
 * @author finderlo
 * @date 11/05/2017
 */
public class TestUtil {

    public static String getToken(MockMvc mockMvc, String phone, String password) throws Exception {
        String url = "/tokens";
        HashMap<String, String> keyvals = new HashMap<>();
        keyvals.put("phone", phone);
        keyvals.put("password", password);
        MvcResult result = TestUtil.getHttpResultContent(mockMvc, url, TestUtil.Method.POST, keyvals);
        System.out.println(result.getResponse().getContentAsString());
        String token = getTokenFromJson(result.getResponse().getContentAsString());
        Assert.hasLength(token);
        return token;
        //{"code":200,"message":"ok","content":{"uid":"213124128","token":"213124128_9ffabcb2-43ad-426a-bd63-f209de6d7f1e"}}
        //{"code":90004,"message":"用户名或者密码不正确","content":null}

    }

    public static String getTokenFromUid(MockMvc mockMvc, String uid, String password) throws Exception {
        String url = "/tokens";
        RequestParams params = new  RequestParams();
        params.put("uid", uid);
        params.put("password", password);

        MvcResult result = TestUtil.getHttpResultContent(mockMvc, url, TestUtil.Method.POST, params);

        String token = getTokenFromJson(result.getResponse().getContentAsString());
        Assert.hasLength(token);
        return token;
    }


    public static String getTokenFromJson(String json) {
        Response result = new Gson().fromJson(json, Response.class);
        LinkedTreeMap content = (LinkedTreeMap) result.getData();
        for (Object key : content.keySet()) {
            if ("token".equals(key.toString())) {
                return (String) content.get(key);
            }
        }
        return null;
    }


    public static void println() {
        System.out.println("------------------------------");
    }


    public static void printJson(String url, String method, List<String> keys, List<String> parms, String response) {
        StringBuilder builder = new StringBuilder();
        builder.append("{")
                .append("\"url\":").append("\"").append(url).append("\"").append(',');
        builder.append("\"method\":").append("\"").append(method).append("\"").append(",");
        builder.append("\"param\":").append("{");
        for (int i = 0; i < keys.size(); i++) {
            builder.append("\"").append(keys.get(i)).append("\"").append(":").append("\"").append(parms.get(i)).append("\"").append(",");
        }
        builder.append("},");
        builder.append("\"return\":")
                .append(response);
        builder.append("}");
        println();
        System.out.println(builder.toString());
        println();
    }

    public static void printJson(String url, String method, Map<String, String> keyvals, String response) {
        StringBuilder builder = new StringBuilder();
        builder.append("{")
                .append("\"url\":").append("\"").append(url).append("\"").append(',');
        builder.append("\"method\":").append("\"").append(method).append("\"").append(",");
        builder.append("\"param\":").append("{");

        for (Map.Entry<String, String> entry : keyvals.entrySet()) {
            builder.append("\"").append(entry.getKey()).append("\"").append(":").append("\"").append(entry.getValue()).append("\"").append(",");

        }
        builder.append("},");
        builder.append("\"return\":")
                .append(response);
        builder.append("}");
        println();
        System.out.println(builder.toString());
        println();
    }

    public static MvcResult getHttpResultContent(MockMvc mockMvc, String uri, Method method, Map<String, String> keyvals) throws Exception {

        MockHttpServletRequestBuilder builder = null;
        switch (method) {
            case GET:
                builder = MockMvcRequestBuilders.get(uri);
                break;
            case POST:
                builder = MockMvcRequestBuilders.post(uri);
                break;
            case PUT:
                builder = MockMvcRequestBuilders.put(uri);
                break;
            case DELETE:
                builder = MockMvcRequestBuilders.delete(uri);
                break;
            default:
                builder = MockMvcRequestBuilders.get(uri);
        }
        for (Map.Entry<String, String> entry : keyvals.entrySet()) {
            builder = builder.param(entry.getKey(), entry.getValue());
        }
        MvcResult result = mockMvc.perform(builder.accept(MediaType.ALL)).andReturn();
//        result.getResponse().getHeaderNames();
        return result;
    }


    public static MvcResult requestAndPrint(MockMvc mockMvc, String uri, Method method, RequestParams params) throws Exception {


        MockHttpServletRequestBuilder builder = null;
        switch (method) {
            case GET:
                builder = MockMvcRequestBuilders.get(uri);
                break;
            case POST:
                builder = MockMvcRequestBuilders.post(uri);
                break;
            case PUT:
                builder = MockMvcRequestBuilders.put(uri);
                break;
            case DELETE:
                builder = MockMvcRequestBuilders.delete(uri);
                break;
            default:
                builder = MockMvcRequestBuilders.get(uri);
        }

        for (Map.Entry<String, String> entry : params.entrySet()) {
            builder = builder.param(entry.getKey(), entry.getValue());
        }
        MvcResult result = mockMvc.perform(builder.accept(MediaType.ALL)).andReturn();

        Map<String, Object> msg = new LinkedHashMap<>();
        Map<String, Object> json = new LinkedHashMap<>();

        System.out.println(uri + "  " + method.name());
        json.put("params", params);
        json.put("response_body", "null");
        msg.put(method.name(), json);

        System.out.println(new Gson().toJson(msg));
        System.out.println(result.getResponse().getContentAsString());
        return result;
    }

    public static void print(MockHttpServletResponse response, String uri, String method, Map<String, String> keyvals) {

        StringBuilder builder = new StringBuilder();
        builder.append("{")
                .append("\"url\":").append("\"").append(uri).append("\"").append(',');
        builder.append("\"method\":").append("\"").append(method).append("\"").append(",");
        builder.append("\"param\":").append("{");

        for (Map.Entry<String, String> entry : keyvals.entrySet()) {
            builder.append("\"").append(entry.getKey()).append("\"").append(":").append("\"").append(entry.getValue()).append("\"").append(",");
        }
        builder.append("},");
        builder.append("\"response\":").append("{");
        builder.append("}");
        println();
        System.out.println(builder.toString());
        println();
    }

    public enum Method {
        POST, PUT, GET, DELETE
    }
}

