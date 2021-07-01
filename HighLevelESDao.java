package kr.co.saramin.lab.searchevaluator.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.*;
import org.elasticsearch.client.*;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class HighLevelESDao {
    private final ObjectMapper objectMapper;
    private final Gson gson;

    @Value("${elasticsearch.scroll-timout-ms}")
    private long scrollTimeoutMs;

    /**
     * 쿼리 검색
     * @param client
     * @param indexName
     * @param searchSourceBuilder
     * @return
     * @throws IOException
     */
    public SearchResponse search(RestHighLevelClient client, String indexName, SearchSourceBuilder searchSourceBuilder) throws IOException {
        SearchRequest searchRequest = getSearchRequest(indexName, searchSourceBuilder);
        return client.search(searchRequest, RequestOptions.DEFAULT);
    }

    /**
     * 쿼리 검색
     * @param client
     * @param indexNames
     * @param searchSourceBuilder
     * @return
     * @throws IOException
     */
    public SearchResponse search(RestHighLevelClient client, String[] indexNames, SearchSourceBuilder searchSourceBuilder) throws IOException {
        SearchRequest searchRequest = getSearchRequest(indexNames, searchSourceBuilder);
        return client.search(searchRequest, RequestOptions.DEFAULT);
    }

    /**
     *  SearchRequest 생성
     * @param indexName
     * @param searchSourceBuilder
     * @return
     */
    private SearchRequest getSearchRequest(String indexName, SearchSourceBuilder searchSourceBuilder) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(indexName).source(searchSourceBuilder);
        return searchRequest;
    }

    /**
     *  SearchRequest 생성
     * @param indexNames
     * @param searchSourceBuilder
     * @return
     */
    private SearchRequest getSearchRequest(String[] indexNames, SearchSourceBuilder searchSourceBuilder) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(indexNames).source(searchSourceBuilder);
        return searchRequest;
    }

    /**
     * ORM 싱글 인덱싱
     * @param <T>
     * @param client
     * @param indexName
     * @param document
     * @throws IOException
     * @return
     */
    public <T> IndexResponse index(RestHighLevelClient client, String indexName, T document) throws IOException {
        IndexRequest indexRequest = new IndexRequest(indexName, "_doc");
        indexRequest.source(objectMapper.writeValueAsString(document), XContentType.JSON);
        return client.index(indexRequest, RequestOptions.DEFAULT);
    }

    public <T> IndexResponse index(RestHighLevelClient client, String indexName, String id, T document) throws IOException {
        IndexRequest indexRequest = new IndexRequest(indexName, "_doc", id);
        indexRequest.source(objectMapper.writeValueAsString(document), XContentType.JSON);
        return client.index(indexRequest, RequestOptions.DEFAULT);
    }

    /**
     * ORM 벌크 인덱싱
     * @param client
     * @param indexName
     * @param documentList
     * @param <T>
     * @return
     * @throws IOException
     */
    public <T> BulkResponse index(RestHighLevelClient client, String indexName, List<T> documentList) throws IOException {
        BulkRequest bulkRequest = new BulkRequest();
        for (T document : documentList) {
            IndexRequest indexRequest = new IndexRequest(indexName, "_doc");
            indexRequest.source(objectMapper.writeValueAsString(document), XContentType.JSON);
            bulkRequest.add(indexRequest);
        }
        return client.bulk(bulkRequest, RequestOptions.DEFAULT);
    }

    /**
     * 인덱스 존재 안할 시 생성
     * @param client
     * @param indexName
     * @return
     * @throws IOException
     */
    public CreateIndexResponse createIndexIfEmpty(RestHighLevelClient client, String indexName) throws IOException {
        CreateIndexResponse createIndexResponse = null;
        if(!isExistIndex(client, indexName)) {
            createIndexResponse = createIndex(client, indexName);
        }
        return createIndexResponse;
    }

    /**
     * 인덱스 생성
     * @param client
     * @param indexName
     * @return
     * @throws IOException
     */
    private CreateIndexResponse createIndex(RestHighLevelClient client, String indexName) throws IOException {
        CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexName);
//        createIndexRequest.source(Files.readString(mappingRscPath), XContentType.JSON); // ES7에서는 type 문제 때문에 매핑 파일 적용 안함
        return client.indices().create(createIndexRequest, RequestOptions.DEFAULT);
    }

    /**
     * 인덱스 존재 여부
     * @param client
     * @param indexName
     * @return
     * @throws IOException
     */
    public boolean isExistIndex(RestHighLevelClient client, String indexName) throws IOException {
        GetIndexRequest getIndexRequest = new GetIndexRequest().indices(indexName);
        return client.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
    }

    /**
     * 인덱스 전체 count 반환
     * count 호출 전에 실행한 벌크 색인이 늦게 완료되면 count 동기화가 안될 수 있다..
     * Highlevelclient6 이 ES7에선 totalHits가 0으로 떠서 low level 사용함
     * @param client
     * @param indexName
     * @return
     * @throws IOException
     */
    public long count(RestHighLevelClient client, String indexName) throws IOException {
        RestClient lowLevelClient = client.getLowLevelClient();
        Response response = lowLevelClient.performRequest(new Request("GET", indexName + "/_count"));
        String jsonBody = EntityUtils.toString(response.getEntity());
        Map<String, Object> responseMap = gson.fromJson(jsonBody, Map.class);
        return Double.valueOf((double) responseMap.get("count")).longValue();
    }

    public SearchResponse startScroll(RestHighLevelClient client, String[] indexNames, SearchSourceBuilder searchSourceBuilder) throws IOException {
        searchSourceBuilder.trackTotalHits(true);
        SearchRequest searchRequest = getSearchRequest(indexNames, searchSourceBuilder);
        searchRequest.scroll(new Scroll(TimeValue.timeValueMillis(scrollTimeoutMs)));
        return client.search(searchRequest, RequestOptions.DEFAULT);
    }

    public SearchResponse continueScroll(RestHighLevelClient client, String scrollId) throws IOException {
        SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId);
        scrollRequest.scroll(new Scroll(TimeValue.timeValueMillis(scrollTimeoutMs)));
        return client.scroll(scrollRequest, RequestOptions.DEFAULT);
    }

    public ClearScrollResponse clearScroll(RestHighLevelClient client, String scrollId) throws IOException {
        ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
        clearScrollRequest.addScrollId(scrollId);
        return client.clearScroll(clearScrollRequest, RequestOptions.DEFAULT);
    }

    public long count(RestHighLevelClient client, String index, SearchSourceBuilder searchSourceBuilder) throws IOException {
        CountRequest countRequest = new CountRequest(index);
        countRequest.source(searchSourceBuilder);
        return client.count(countRequest, RequestOptions.DEFAULT).getCount();
    }

    public long count(RestHighLevelClient client, String[] indexArr, SearchSourceBuilder searchSourceBuilder) throws IOException {
        CountRequest countRequest = new CountRequest(indexArr);
        countRequest.source(searchSourceBuilder);
        return client.count(countRequest, RequestOptions.DEFAULT).getCount();
    }
}
