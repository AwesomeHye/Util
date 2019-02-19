package hyein.app.riditwebproject.util;


import lombok.extern.slf4j.Slf4j;
import org.mozilla.universalchardet.UniversalDetector;

import java.io.IOException;
import java.io.InputStream;

/*
* 인코딩 관련 유틸
* */
@Slf4j
public class EncodingUtils {

    //InputStream에서 문자열의 인코딩 반환
    public static String getEncodingFromInputStream(InputStream inputStream){
        String charset = "MS949";

        UniversalDetector univerSalDetector = new UniversalDetector(null);

        int nread;
        byte buf[] = new byte[4096];
        while (true) {
            try {
                //inputstream에서 하나씩 읽으면서 특정 인코딩에만 출현하는 문자 형태 감지해 detectedCharset에 저장
                if (!((nread = inputStream.read(buf)) > 0 && !univerSalDetector.isDone())) break;
                univerSalDetector.handleData(buf, 0, nread);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        univerSalDetector.dataEnd();

        String tmpEncoding = univerSalDetector.getDetectedCharset();
        if (tmpEncoding != null) {
            charset = tmpEncoding;
        }
        return charset;
    }
}
