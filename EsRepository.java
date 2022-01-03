import kr.co.saramin.lab.companyanalyzer.dto.CompanyAnalysisDto;
import kr.co.saramin.lab.companyanalyzer.properties.ElasticsearchProps;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.SearchScrollHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class EsRepository {
    private final ElasticsearchRestTemplate restTemplate;
    private final long scrollTimeInMillis;

    public EsRepository(ElasticsearchRestTemplate restTemplate, ElasticsearchProps elasticsearchProps) {
        this.restTemplate = restTemplate;
        this.scrollTimeInMillis = elasticsearchProps.getScrollTimeInMillis();
    }
    /**
     * 인덱스 생성 - Dto 클래스의 매핑정보 적용
     *
     * @param clazz
     */
    public void createIndexWithMapping(Class<?> clazz) {
        IndexOperations indexOperations = restTemplate.indexOps(clazz);
        indexOperations.create();
        indexOperations.putMapping(indexOperations.createMapping());
    }


    /**
     * 벌크 인덱스
     *
     * @param indexQueryList
     * @param indexCoordinates
     */
    public void bulkIndex(List<IndexQuery> indexQueryList, IndexCoordinates indexCoordinates) {
        restTemplate.bulkIndex(indexQueryList, indexCoordinates);
    }

    public <T> SearchScrollHits<T> startScroll(Query query, Class<T> clazz, IndexCoordinates indexCoordinates) {
        return restTemplate.searchScrollStart(scrollTimeInMillis, query, clazz, indexCoordinates);
    }

    public <T> SearchScrollHits<T> continueScroll(String scrollId, Class<T> clazz, IndexCoordinates indexCoordinates) {
        return restTemplate.searchScrollContinue(scrollId, scrollTimeInMillis, clazz, indexCoordinates);
    }

    public void clearScroll(String scrollId) {
        List<String> scrollIds = new ArrayList<>();
        scrollIds.add(scrollId);
        restTemplate.searchScrollClear(scrollIds);
    }

    /**
     * 인덱스 존재 여부
     *
     * @param index
     * @return
     */
    public boolean isExistIndex(String index) {
        return restTemplate.indexOps(IndexCoordinates.of(index)).exists();
    }
}
