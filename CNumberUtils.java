package com.datasolution.ridit.datamigration.util;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CNumberUtils {
    /**
     * maxNum으로부터 아래로 size만큼의 숫자를 리스트로 반환
     * ex) maxNum: 2018, size:3 -> return: [2016, 2017, 2018]
     *
     * @int maxNum
     * @int size
     * @return List<Integer>
     */
    public static List<Integer> getContinuousNumberList(int maxNum, int size){
        return IntStream.rangeClosed(maxNum-size+1, maxNum).boxed().collect(Collectors.toList());
    }

    /**
     * 지수 형식의 숫자를 full 숫자로 변환
     * ex) "3.708873011E10" -> return: "37088730110"
     *      "90.87" -> return: "90"
     * @String exponentialNumber
     * @return String
     */
    public static String convertExponentialNumberToNumberString(String exponentialNumber){
        String numberString="";
        try{
            numberString = String.valueOf(Double.valueOf(exponentialNumber).longValue());
        } catch (NumberFormatException e){
            System.out.println("CANNOT CONVERT TO INTEGER: convertExponentialNumberToNumberString()");
            e.printStackTrace();
        }

        return numberString;
    }
}
