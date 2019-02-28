package kr.datasolution.practice.hyein.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CDateUtils {
    //Date를 string으로
    public static String getDateString(Date date, String dateFormat){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        return simpleDateFormat.format(date);
    }
    //날짜string 을 Date로
    public static Date parseDate(String date,String dateFormat) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        return simpleDateFormat.parse(date);
    }
}
