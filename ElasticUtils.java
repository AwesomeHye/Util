package kr.co.saramin.lab.pc_m_autocomplete_generator.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.search.SearchHit;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ElasticUtils {

    /**
     * targetDate 적용한 인덱스명 반환
     * @param indexTemplate
     * @param dateTemplate
     * @param targetDate
     * @param dtf
     * @return
     */
    public static String getIndexByDate(String indexTemplate, String dateTemplate, LocalDate targetDate, DateTimeFormatter dtf) {
        return indexTemplate.replace(dateTemplate, targetDate.format(dtf));
    }

    /**
     * hits 를 ORM 으로 역직렬화
     * @param hits
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> deserializeHits(SearchHit[] hits, Class<T> clazz, ObjectMapper objectMapper) {
        return Arrays.stream(hits)
                .map(hit -> objectMapper.convertValue(hit.getSourceAsMap(), clazz))
                .collect(Collectors.toList());
    }


}
