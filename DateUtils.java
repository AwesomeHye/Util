package kr.co.saramin.lab.searchevaluator.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

@Slf4j
public class DateUtils {
    public static final String TZ15H = "T15:00:00.000Z";

    public static final DateTimeFormatter parseDateTimeFormatter = new DateTimeFormatterBuilder()
            .append(DateTimeFormatter.ofPattern("yyyyMMdd", Locale.KOREA))
            .toFormatter();


    public static final DateTimeFormatter indexDateFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
    public static final DateTimeFormatter regDtFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter eventDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    public static final DateTimeFormatter eventDtFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static String getIndexDate(LocalDate currentDate, long amount) {
        return ChronoUnit.DAYS.addTo(currentDate, amount).format(indexDateFormatter);
    }

    /**
     * 요일 반환
     * 1: 월요일, 7: 일요일
     * @param localDate
     * @return
     */
    public static int getWeekDay(LocalDate localDate) {
        return localDate.getDayOfWeek().getValue();
    }

    /**
     * 평일/주말 여부 반환
     * @param localDate
     * @return 평일: true, 주말: false
     */
    public static boolean isWeekday(LocalDate localDate) {
        int weekDay = getWeekDay(localDate);

        return !(weekDay == 6 || weekDay == 7);
    }

    /**
     * 월의 몇 주차 인지 반환
     * 매월 1일이 1주차 시작이다.
     * 1~4주차는 반드시 7개 이며 5주차의 개수가 다르다.
     * @param localDate
     * @return
     */
    public static int weekOfMonth(LocalDate localDate) {
        return localDate.get(ChronoField.ALIGNED_WEEK_OF_MONTH);
    }


}
