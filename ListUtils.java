package kr.datasolution.practice.hyein.utils;

import java.util.ArrayList;
import java.util.List;


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
        }

        int[] idx = new int[numberOfList];
        int unitSize = listSize / numberOfList;
        int rest = listSize - (numberOfList * unitSize);

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
