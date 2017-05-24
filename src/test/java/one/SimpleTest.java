package one;

import com.delivery.common.Response;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import java.sql.Timestamp;

/**
 * @author finderlo
 * @date 23/04/2017
 */
public class SimpleTest {

    public static void main(String[] args) {
        String time ="2017-04-28 14:59:59";
        System.out.println(time);
        System.out.println(Timestamp.valueOf(time+"").toString());
    }


    public static String getToken(String json) {
        Response result = new Gson().fromJson(json, Response.class);
        LinkedTreeMap content = (LinkedTreeMap) result.getData();
        for (Object key : content.keySet()) {
            System.out.println(key);
            if ("token.TokenTest".equals(key.toString())) {
                System.out.println("111");
                return (String) content.get(key);
            }
        }
        return null;
    }
}
