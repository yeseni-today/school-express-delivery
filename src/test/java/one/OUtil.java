package one;

import java.util.List;
import java.util.Map;

/**
 * @author finderlo
 * @date 11/05/2017
 */
public class OUtil {

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
            builder.append("\"").append(keys.get(i) ).append("\"").append(":").append("\"").append(parms.get(i)).append("\"").append(",");
        }
        builder.append("},");
        builder.append("\"return\":")
                .append(response);
        builder.append("}");
        println();
        System.out.println(builder.toString());
        println();
    }

    public static void printJson(String url, String method, Map<String,String> keyvals, String response) {
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
}

