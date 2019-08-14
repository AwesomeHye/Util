package kr.datasolution.tta.util;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;

@Slf4j
public class OkHttpClientUtils {

    /**
     * post 요청 후 Response 반환
     * @param url
     * @param requestJson
     * @return Response
     */
    public static Response postRequest(String url, String requestJson) {
        OkHttpClient okHttpClient = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(requestJson, MediaType.parse("application/json")))
                .build();
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        log.info("request: {}", request);
        log.info("response: {}", response);

        return response;
    }

}
