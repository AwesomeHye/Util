package kr.co.saramin.lab.recruitassigntaskrepository.utils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class FileUtils {
    /**
     * List 데이터를 toString() 으로 변환하여 파일에 쓴다.
     * @param resultFilePath 파일 경로
     * @param dataList List 형태의 데이터
     * @throws IOException
     */
    public static void writeList2File(Path resultFilePath, List<?> dataList) throws IOException {
        String dataString = dataList.toString();
        writeString2File(resultFilePath, dataString);
    }

    /**
     * String 데이터를 파일에 쓴다.
     * @param resultFilePath 파일 경로
     * @param dataString String 형태의 데이터
     * @throws IOException
     */
    public static void writeString2File(Path resultFilePath, String dataString) throws IOException {
        Files.write(resultFilePath, (dataString + System.lineSeparator()).getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND, StandardOpenOption.WRITE);
    }

}
