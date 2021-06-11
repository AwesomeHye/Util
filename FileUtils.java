package kr.co.saramin.lab.recruitassigntaskrepository.utils;

        import java.io.IOException;
        import java.net.URISyntaxException;
        import java.nio.charset.StandardCharsets;
        import java.nio.file.Files;
        import java.nio.file.Path;
        import java.nio.file.Paths;
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
     * @param resultFilePathString String 파일 경로
     * @param dataString String 형태의 데이터
     * @throws IOException
     */
    public static void writeString2File(String resultFilePathString, String dataString) throws IOException {
        writeString2File(Paths.get(resultFilePathString), dataString);
    }

    /**
     * String 데이터 + 개행 을 파일에 쓴다.
     * @param resultFilePathString String 파일 경로
     * @param dataString String 형태의 데이터
     * @throws IOException
     */
    public static void writeStringNewLine2File(String resultFilePathString, String dataString) throws IOException {
        writeString2File(Paths.get(resultFilePathString), dataString + System.lineSeparator());
    }

    /**
     * String 데이터 + 개행을 파일에 쓴다.
     * @param resultFilePath Path 파일 경로
     * @param dataString String 형태의 데이터
     * @throws IOException
     */
    public static void writeStringNewLine2File(Path resultFilePath, String dataString) throws IOException {
        writeString2File(resultFilePath, dataString + System.lineSeparator());
    }
    /**
     * String 데이터를 파일에 쓴다.
     * @param resultFilePath Path 파일 경로
     * @param dataString String 형태의 데이터
     * @throws IOException
     */
    public static void writeString2File(Path resultFilePath, String dataString) throws IOException {
        Files.write(resultFilePath, dataString.getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND, StandardOpenOption.WRITE, StandardOpenOption.CREATE);
    }


    /**
     * 파일 존재하면 지우고 씀
     * @param resultFilePathString
     * @param dataString
     * @throws IOException
     */
    public static void writeString2NewFile(String resultFilePathString, String dataString) throws IOException {
        Path resultFilePath = Paths.get(resultFilePathString);
        Files.deleteIfExists(resultFilePath);

        writeString2File(resultFilePath, dataString);
    }
        
     /**
     * 다운로드URL로부터 파일을 다운받는다.
     * @param url
     * @param filePath
     */
    public static void downloadFileFromUrl(String url, String filePath) throws IOException {
        URL downloadUrl = new URL(url);
        ReadableByteChannel readableByteChannel = Channels.newChannel(downloadUrl.openStream());
        FileOutputStream fileOutputStream = new FileOutputStream(filePath);

        fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
    }
}
