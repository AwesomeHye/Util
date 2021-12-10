package kr.co.saramin.lab.autocompleteframework.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesRequest;
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesRequest.AliasActions;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.*;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;


@Repository
@RequiredArgsConstructor
@Setter
public class HighLevelESDao {
    private final ObjectMapper objectMapper;
    private RestHighLevelClient client;

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
    public <T> BulkResponse index(String indexName, List<T> documentList) throws IOException {
        BulkRequest bulkRequest = new BulkRequest();
        for (T document : documentList) {
            IndexRequest indexRequest = new IndexRequest(indexName, "_doc");
            indexRequest.source(objectMapper.writeValueAsString(document), XContentType.JSON);
            bulkRequest.add(indexRequest);
        }
        return client.bulk(bulkRequest, RequestOptions.DEFAULT);
    }


    /**
     * 벌크 업데이트
     * _id 값이 같아야 업데이트됨, 아님 인서트되버림
     * @param client
     * @param index
     * @param idList document의 _id. hits[i].getId() 로 구할수있다.
     * @param documentList
     * @param <T>
     * @return
     * @throws IOException
     */
    public <T> BulkResponse index(RestHighLevelClient client, String index, List<String> idList, List<T> documentList) throws IOException {
        BulkRequest bulkRequest = new BulkRequest();
        for (int i = 0; i < documentList.size(); i++) {
            IndexRequest indexRequest = new IndexRequest(index, "_doc", idList.get(i));
            indexRequest.source(objectMapper.writeValueAsString(documentList.get(i)), XContentType.JSON);
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
     * 인덱스 생성
     * @param client
     * @param indexName
     * @return
     * @throws IOException
     */
    private CreateIndexResponse createIndex(RestHighLevelClient client, String indexName, XContentBuilder settingsBuilder) throws IOException {
        CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexName);
        createIndexRequest.settings(settingsBuilder);
        return client.indices().create(createIndexRequest, RequestOptions.DEFAULT);
    }

    /**
     * 인덱스 생성
     * @param client
     * @param indexName
     * @return
     * @throws IOException
     */
    private CreateIndexResponse createIndex(RestHighLevelClient client, String indexName, XContentBuilder settingsBuilder, XContentBuilder mappingBuilder) throws IOException {
        CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexName);
        createIndexRequest.settings(settingsBuilder);
        createIndexRequest.mapping("_doc", mappingBuilder);
        return client.indices().create(createIndexRequest, RequestOptions.DEFAULT);
    }

    /**
     * 강제 인덱스 생성 (분석기 적용 가능)
     * 인덱스 이미 존재 시 삭제 후 생성
     * @param client
     * @param indexName
     * @param settingsBuilder
     * @param mappingBuilder
     * @return
     */
    public CreateIndexResponse createIndexForcing(RestHighLevelClient client, String indexName, XContentBuilder settingsBuilder, XContentBuilder mappingBuilder) throws IOException {
        deleteIndexIfExist(client, indexName);

        return createIndex(client, indexName, settingsBuilder, mappingBuilder);
    }

    /**
     * 인덱스 이미 존재 시 삭제
     * @param client
     * @param indexName
     * @throws IOException
     */
    public void deleteIndexIfExist(RestHighLevelClient client, String indexName) throws IOException {
        if(isExistIndex(client, indexName)) {
            deleteIndex(client, indexName);
        }
    }

    /**
     * 인덱스 삭제
     * @param client
     * @param indexName
     * @return
     */
    private AcknowledgedResponse deleteIndex(RestHighLevelClient client, String indexName) throws IOException {
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(indexName);
        return client.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
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
     * 인덱스 존재 여부
     * @param indexName
     * @return
     * @throws IOException
     */
    public boolean isExistIndex(String indexName) throws IOException {
        GetIndexRequest getIndexRequest = new GetIndexRequest().indices(indexName);
        return client.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
    }

    public SearchResponse startScroll(RestHighLevelClient client, String[] indexNames, SearchSourceBuilder searchSourceBuilder) throws IOException {
        searchSourceBuilder.trackTotalHits(true);
        SearchRequest searchRequest = getSearchRequest(indexNames, searchSourceBuilder);
        searchRequest.scroll(new Scroll(TimeValue.timeValueMillis(scrollTimeoutMs)));
        return client.search(searchRequest, RequestOptions.DEFAULT);
    }

    public SearchResponse startScroll(RestHighLevelClient client, String indexName, SearchSourceBuilder searchSourceBuilder) throws IOException {
        searchSourceBuilder.trackTotalHits(true);
        SearchRequest searchRequest = getSearchRequest(indexName, searchSourceBuilder);
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

    public long count(RestHighLevelClient client, String index) throws IOException {
        CountRequest countRequest = new CountRequest(index);
        return client.count(countRequest, RequestOptions.DEFAULT).getCount();
    }

    /**
     * aliasName 의 설정된 모든 인덱스 제거 후 indexName 만 add
     * @param aliasName
     * @param indexName
     * @return
     */
    public AcknowledgedResponse replaceAlias(String aliasName, String indexName) throws IOException {
        IndicesAliasesRequest indicesAliasesRequest = new IndicesAliasesRequest();

        // clear
        AliasActions clearAliasActions = new AliasActions(AliasActions.Type.REMOVE)
                .alias(aliasName)
                .index("*");
        indicesAliasesRequest.addAliasAction(clearAliasActions);
        // add
        AliasActions addAliasActions = new AliasActions(AliasActions.Type.ADD)
                .alias(aliasName)
                .index(indexName);
        indicesAliasesRequest.addAliasAction(addAliasActions);

        return client.indices().updateAliases(indicesAliasesRequest, RequestOptions.DEFAULT);
    }

    /**
     * alias 존재 여부 검사
     * @param aliasName
     * @return
     */
    public boolean isExistAlias(String aliasName) throws IOException {
        GetAliasesRequest getAliasesRequest = new GetAliasesRequest(aliasName);
        return client.indices().existsAlias(getAliasesRequest, RequestOptions.DEFAULT);
    }

}
