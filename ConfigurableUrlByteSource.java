package hyein.app.riditwebproject.util;

import com.google.common.base.Preconditions;
import com.google.common.io.ByteSource;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/*
* URL 요청 시 connection 최대 대기 시간과 read 최대 대기 시간 커스터마이징 해주기 위해 이 클래스 만듦.
* */
@Slf4j
public class ConfigurableUrlByteSource extends ByteSource {
    private URL url;
    private int connectionTimeout = 20000;
    private int readTimeout = 20000;

    public ConfigurableUrlByteSource (URL url){
        this.url = Preconditions.checkNotNull(url, "url is null");
    }

    @Override
    public InputStream openStream() throws IOException {
        URLConnection connection = url.openConnection();
        connection.setConnectTimeout(connectionTimeout);
        connection.setReadTimeout(readTimeout);
        return connection.getInputStream();
    }
}
