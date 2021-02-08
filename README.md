# Util
유틸 함수 파일 모음

        <!--url 요청해서 byte로 가져올때 씀.  ConfigurableUrlByteSource 클래스-->
        <dependency>
            <groupId>com.google.guava</groupId>
            <artifactId>guava</artifactId>
            <version>26.0-jre</version>
        </dependency>


        <!--파일인코딩알려줌. 인코딩에들어있는고유코드를 식별해서 인코딩판단. UniversalDetector 클래스-->
        <dependency>
            <groupId>com.googlecode.juniversalchardet</groupId>
            <artifactId>juniversalchardet</artifactId>
            <version>1.0.3</version>
        </dependency>

        <!--FileUtils, Charset-->
        <dependency>
            <groupId>commons-io</groupId>
            <artifactId>commons-io</artifactId>
            <version>2.5</version>
        </dependency>

        <!--json 파싱 및 생성에 사용되는 라이브러리-->
        <dependency>
            <groupId>com.google.code.gson</groupId>
            <artifactId>gson</artifactId>
            <version>2.8.5</version>
        </dependency>
              
        <!-- HTTP 통신할 때 사용되는 OkHttpClient 라이브러리 -->
        <!-- https://mvnrepository.com/artifact/com.squareup.okhttp3/okhttp -->
        <dependency>
           <groupId>com.squareup.okhttp3</groupId>
           <artifactId>okhttp</artifactId>
           <version>4.0.1</version>
        </dependency>
        
        <!-- ObjectMapper -->
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
            <version>2.10.3</version>
        </dependency>
        
            // ElasticSearch
    implementation 'org.springframework.data:spring-data-elasticsearch:3.2.0.RELEASE'
    implementation 'org.elasticsearch:elasticsearch:6.5.4'
    implementation 'org.elasticsearch.client:elasticsearch-rest-high-level-client:6.5.4'

