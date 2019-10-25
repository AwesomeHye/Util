package kr.datasolution.practice.hyein.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class ListUtils {
    /**
     * list 소분하기
     * @param list
     * @param numberOfList
     * @return List<List<T>>
     */
    public static <T> List<List<T>> subListByNumber(List<T> list, int numberOfList){
        List<List<T>> subSubList = new ArrayList<>();
        int listSize = list.size();

        if(listSize == 0)
            return subSubList;
        if(numberOfList == 0) {
            System.err.println("subListByNumber(): number of list is 0");
            throw new IllegalArgumentException();
        }else if(numberOfList > listSize){
            System.err.println("number of list is larger than list size");
            throw new IllegalArgumentException();
        }

        int[] idx = new int[numberOfList];
        int unitSize = listSize / numberOfList;
        int rest = listSize % unitSize;

        for(int i = 0; i < idx.length; i++){ idx[i] += unitSize; }
        for(int i = 0; i < rest; i++){ idx[i] += 1; }

        int startIdx = 0;
        for(int i = 0; i < idx.length; i++){
            subSubList.add(list.subList(startIdx, startIdx + idx[i]));
            startIdx += idx[i];
        }
        return subSubList;
    }
}
