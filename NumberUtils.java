package com.datasolution.ridit.datamigration.util;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class NumberUtils {
    /**
     * maxNum으로부터 아래로 size만큼의 숫자를 리스트로 반환
     *
     * @param maxNum
     * @param size
     * @return
     */
    public static List<Integer> getContinuousNumberList(int maxNum, int size){
        return IntStream.rangeClosed(maxNum-size+1, maxNum).boxed().collect(Collectors.toList());
    }
}
