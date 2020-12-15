package kr.co.saramin.lab.recruitassigntaskrepository.utils;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.IOException;

public class HtmlUnitUtils {
    /**
     * URL 커넥션 하는 WebClient 객체 생성
     * WebClient 는 멀티 스레드 지원 안함.
     * @return
     */
    public static WebClient createWebClient() {
        WebClient webClient = new WebClient();
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setDownloadImages(false);
        webClient.getOptions().setUseInsecureSSL(true); // for https connect
        webClient.getOptions().setThrowExceptionOnScriptError(false); // script exception 무시
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        return webClient;
    }

    /**
     * html 긁어오기
     * @param url
     * @return
     * @throws IOException
     */
    public static String getHtmlTextFromUrlForThread(String url) throws IOException {
        WebClient webClient = createWebClient();

        HtmlPage htmlPage = webClient.getPage(url);
        return htmlPage.asXml();
    }

    /**
     * URL 요청 후 응답받은 HtmlPage 반환
     * @param webClient
     * @param url
     * @return
     * @throws IOException
     */
    public static HtmlPage getHtmlPage(WebClient webClient, String url) throws IOException {
        return webClient.getPage(url);
    }


}

