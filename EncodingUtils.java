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
    
    
    // 유니코드에서 String으로 변환
    public static String convertUnicode2String(String val) {
        StringBuffer sb = new StringBuffer();
        try {


            for (int i = 0; i < val.length(); i++) {
                // 조합이 \\u로 시작하면 6글자를 변환한다. \\uxxxx
                if ('\\' == val.charAt(i) && 'u' == val.charAt(i + 1) && i > 0 && '\\' != val.charAt(i-1)) {
                    // 그 뒤 네글자는 유니코드의 16진수 코드이다. int형으로 바꾸어서 다시 char 타입으로 강제 변환한다.
                    Character r = (char) Integer.parseInt(val.substring(i + 2, i + 6), 16);
                    sb.append(r);

                    i += 5; // for의 증가 값 1과 5를 합해 6글자를 점프
                } else {

                    sb.append(val.charAt(i)); // ascii코드면 그대로 버퍼에 넣는다.
                }
            }
        } catch (Exception e){
            e.printStackTrace();
            log.error(val);
        }
        return sb.toString();
    }
}
