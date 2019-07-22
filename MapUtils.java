package com.datasolution.ridit.datamigration.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MapUtils {

    /**
     * map에서 value가 List일때 key에 맞는 값 넣어주기
     * @param map
     * @param key
     * @param value
     * @param <K>
     * @param <V>
     */
    public static <K, V> void putValueListMap(Map<K, List<V>> map, K key, V value){
        List<V> valueList = map.get(key);
        if(valueList == null){
            map.put(key, new ArrayList<>(Arrays.asList(value)));
        } else{
            valueList.add(value);
            map.put(key, valueList);
        }
    }
}
