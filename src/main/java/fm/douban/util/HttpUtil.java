package fm.douban.util;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.boot.autoconfigure.info.ProjectInfoProperties;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpUtil {

    public static Map<String,String> buildHeaderDate(String referer,String host){
        Map<String,String> headers = new HashMap<>();
        headers.put("Referer",referer);
        headers.put("Host",host);
        return headers;
    }

    public static String getContent(String url,Map<String,String> headers){
        OkHttpClient okHttpClient = new OkHttpClient();

        Request.Builder reqBuilder = new Request.Builder().url(url);

        // 如果传入 http header ，则放入 Request 中
        if (headers != null && !headers.isEmpty()) {
            for (String key : headers.keySet()) {
                reqBuilder.addHeader(key, headers.get(key));
            }
        }

        Request request = reqBuilder.build();
        Call call = okHttpClient.newCall(request);
        String result = null;
        try {
            result = call.execute().body().string();
        }catch (IOException e){
            System.out.println("request"+url+"error");
            e.printStackTrace();
        }
        return result;
    }
}
