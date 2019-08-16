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

        return response;
    }

    /**
     * Response 코드가 200(성공) 일 때 body 반환
     * @param response
     * @return responseString
     */
    public static String getResponseString(Response response){
        String responseString = "";

        if(response != null && response.code() == 200) {
            try {
                responseString = response.body().string();
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return responseString;
    }

    /**
     * post 요청에 대한 body 반환
     * @param url
     * @param requestJson
     * @return postResponseBody
     */
    public static String getPostRequestString(String url, String requestJson){
        return getResponseString(postRequest(url, requestJson));
    }
}
