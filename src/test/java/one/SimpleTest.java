package one;

import com.delivery.common.Response;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import org.hibernate.secure.internal.JaccSecurityListener;

/**
 * @author finderlo
 * @date 23/04/2017
 */
public class SimpleTest {

    public static void main(String[] args) {
        String s = "{\"error_code\":200,\"message\":\"success\",\"content\":{\"users\":{\"userId\":\"213124124\",\"userName\":\"fin\",\"userPhone\":\"12345678901\",\"userPassword\":null,\"userIdentity\":\"RECIPIENT\",\"userSchoolcard\":\"1\",\"userIdcard\":\"12\",\"userPhoto\":\"1\",\"userAlipay\":\"1332\",\"userSex\":\"7\",\"userSchoolname\":\"6687\",\"userSchooladdress\":\"686\"},\"recpiaToken\":\"213124124%%fin\"}}";
        System.out.println(getToken(s));
    }

    public static String getToken(String json) {
        Response result = new Gson().fromJson(json, Response.class);
        LinkedTreeMap content = (LinkedTreeMap) result.getContent();
        for (Object key : content.keySet()) {
            System.out.println(key);
            if ("token".equals(key.toString())) {
                System.out.println("111");
                return (String) content.get(key);
            }
        }
        return null;
    }
}
