package kr.co.saramin.lab.searchevaluator.dao.analysis;

import lombok.RequiredArgsConstructor;
import org.elasticsearch.ElasticsearchStatusException;
import org.elasticsearch.common.unit.TimeValue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ScrolledPage;
import org.springframework.data.elasticsearch.core.query.AliasBuilder;
import org.springframework.data.elasticsearch.core.query.AliasQuery;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ESOperationsDao {
    @Value("${elasticsearch.scroll-timout-ms}")
    private long scrollTimeoutMs;

//    final long SCROLL_TIME_IN_MILLIS = TimeValue.timeValueMinutes(10L).getMillis(); // 60000
    public final ElasticsearchOperations esOperations;

    public <T> ScrolledPage<T> startScroll(SearchQuery searchQuery, Class<T> clazz){
        return esOperations.startScroll(scrollTimeoutMs, searchQuery, clazz);
    }

    public <T> ScrolledPage<T> continueScroll(String scrollId, Class<T> clazz){
        return esOperations.continueScroll(scrollId, scrollTimeoutMs, clazz);
    }

    public void clearScroll(String scrollId){
        esOperations.clearScroll(scrollId);
    }

    /**
     * 인덱스 생성 후 필드 매핑
     * @param indexName
     * @param indexType
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> boolean createAndMappingIndex(String indexName, String indexType, Class<T> clazz){
        return esOperations.createIndex(indexName, clazz) &&
                esOperations.putMapping(indexName, indexType, clazz);
    }

    /**
     * aliasName 의 설정된 모든 인덱스 제거 후 indexName 만 add
     * @param aliasName
     * @param indexName
     */
    public boolean replaceAlias(String aliasName, String indexName){
        AliasQuery clearAliasQuery = new AliasBuilder().withAliasName(aliasName).withIndexName("*").build();
        try {
            esOperations.removeAlias(clearAliasQuery);
        } catch (ElasticsearchStatusException e){
            // aliases_not_found_exception - nothing to do
        }
        AliasQuery addAliasQuery = new AliasBuilder().withAliasName(aliasName).withIndexName(indexName).build();
        return esOperations.addAlias(addAliasQuery);
    }


    /**
     *
     * 1. 인덱스 생성
     * 2. 생성한 인덱스로 alias 교체
     * @param indexName 존재안할때만 생성
     * @param aliasName
     * @param indexClass
     */
    public <T> void createIndexAndReplaceAlias(String indexName, String aliasName, Class<T> indexClass) {
        if(!esOperations.indexExists(indexName)){
            createAndMappingIndex(indexName, "doc", indexClass);
        }
        replaceAlias(aliasName, indexName);
    }

}
