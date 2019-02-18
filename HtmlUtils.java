package hyein.app.riditwebproject.util;

import com.google.common.io.ByteSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.Charsets;
import org.mozilla.universalchardet.UniversalDetector;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/*
html, 웹 관련 유틸
*/
@Slf4j
public class HtmlUtils {
    //url 요청해서 응답 string 반환
    public static String getHtmlTextFromUrl(String urlString) {
        String responseString = "";
        String charset = "MS949";
        byte[] responseByteArray = null;
        InputStream byteSourceInputStream = null;

        ConfigurableUrlByteSource byteSource;
        try {
            byteSource = new ConfigurableUrlByteSource(new URL(urlString));
            responseByteArray = byteSource.read();

            byteSourceInputStream = ByteSource.wrap(responseByteArray).openStream();
            UniversalDetector univerSalDetector = new UniversalDetector(null);

            int nread;
            byte buf[] = new byte[4096];
            while ((nread = byteSourceInputStream.read(buf)) > 0 && !univerSalDetector.isDone()) {
                univerSalDetector.handleData(buf, 0, nread);
            }
            univerSalDetector.dataEnd();

            String tmpEncoding = univerSalDetector.getDetectedCharset();
            if (tmpEncoding != null) {
                charset = tmpEncoding;
            }

            responseString = new String(responseByteArray, charset);
        } catch (MalformedURLException e) {
            log.error(e.toString());
        } catch (IOException e) {
            log.error("Error is Occurred From Url connection");
            log.error(e.toString());
        }

        return responseString;
    }


    public static String getHtmlTextFromUrl(String urlString, String charset) {
        String responseString = "";

        ConfigurableUrlByteSource configurableUrlByteSource;
        try {
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
}
