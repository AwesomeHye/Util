package kr.datasolution.practice.hyein.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CDateUtils {
    /** Date를 string으로
     *
     * @Date date
     * @String dateFormat
     * @return String
     */
    public static String getDateString(Date date, String dateFormat){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        return simpleDateFormat.format(date);
    }
    
    /**날짜string 을 Date로
     *
     * @String dateString
     * @String dateFormat
     * @return Date
     */
    public static Date parseDate(String date,String dateFormat) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        return simpleDateFormat.parse(date);
    }
    
     /**
     * x일 전 날짜 스트링 구하기
     * ex) Date 1905 1일 전 -> return: Date 1904
     * @Date date
     * @return Date
     */
    public static Date getDateStringTheDayBefore(Date date, int daySize){
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"));
        calendar.setTime(date);
        calendar.add(Calendar.DATE, daySize);

        return calendar.getTime();
    }
}
