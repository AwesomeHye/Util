package kr.datasolution.ridit.uirestapi.util;

import com.google.common.io.ByteSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.Charsets;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/*
html, 웹 관련 유틸
*/
@Slf4j
public class HtmlUtils {

    /**
     * url 요청해서 응답 string 반환
     * 인코딩 모를 떄 사용
     * @param urlString
     * @return
     */
    public static String getHtmlTextFromUrl(String urlString) {
        String responseString = "";
        byte[] responseByteArray = null;
        InputStream byteSourceInputStream = null;

        ConfigurableUrlByteSource byteSource;
        log.info("url String : "+urlString);
        try {
            //URL 요청에서 바이트로 읽어옴
            byteSource = new ConfigurableUrlByteSource(new URL(urlString));
            responseByteArray = byteSource.read();

            //inputStream 가져오기
            byteSourceInputStream = ByteSource.wrap(responseByteArray).openStream();
            //바이트에서 인코딩 가져오기
            String charset = EncodingUtils.getEncodingFromInputStream(byteSourceInputStream);
            //바이트 인코딩
            responseString = new String(responseByteArray, charset);
        } catch (MalformedURLException e) {
            log.error(e.toString());
        } catch (IOException e) {
            log.error("Error is Occurred From Url connection");
            log.error(e.toString());
        }

        return responseString;
    }


    /**
     * url 요청해서 응답 string 반환
     * 인코딩 알 떄 사용
     * @param urlString
     * @return
     */
    public static String getHtmlTextFromUrl(String urlString, String charset) {
        String responseString = "";

        ConfigurableUrlByteSource configurableUrlByteSource;
        try {
            //URL 요청해서 해당 인코딩으로 문자열 가져오기
            configurableUrlByteSource = new ConfigurableUrlByteSource(new URL(urlString));
            responseString = configurableUrlByteSource.asCharSource(Charsets.toCharset(charset)).read();
        } catch (MalformedURLException e) {
            log.error(e.toString());
        } catch (IOException e) {
            log.error("Error is Occurred From Url connection");
            log.error(e.toString());
        }

        return responseString;
    }

    /**
     * URL인코딩 적용한 문자열 반환
     *
     * @param characterString
     * @return URLencoded characterString
     */
    public static String getUrlEncodedCharacter(String characterString){
        try {
            characterString = URLEncoder.encode(characterString, "utf-8");
        } catch (UnsupportedEncodingException e) {
            log.error("Failed URL Encoding FROM getUrlEncodedCharacter");
        }

        return characterString;
    }
}
