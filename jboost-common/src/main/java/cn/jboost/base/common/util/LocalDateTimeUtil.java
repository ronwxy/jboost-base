package cn.jboost.base.common.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;

/**
 * @Author ronwxy
 * @Date 2020/5/21 18:00
 * @Version 1.0
 */
public class LocalDateTimeUtil {

    /**
     * 获取时间戳，单位为秒
     *
     * @return
     */
    public static long getEpochSecond() {
        return LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
    }

    /**
     * 获取时间戳，单位为毫秒
     *
     * @return
     */
    public static long getEpochMilli() {
        return LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    /**
     * 1591252878
     * 秒转换localDateTime
     *
     * @param timestamp
     * @return
     */
    public static LocalDateTime getLocalDateTimeToEpochSecond(Long timestamp) {
//        LocalDate localDate = Instant.ofEpochMilli(timestamp).atZone(ZoneOffset.ofHours(8)).toLocalDate();
        LocalDateTime localDateTime = Instant.ofEpochSecond(timestamp).atZone(ZoneOffset.ofHours(8)).toLocalDateTime();
        return localDateTime;
    }


    /**
     * LocalDateTime转换为Date
     *
     * @param time
     * @return
     */
    public static Date convertLDTToDate(LocalDateTime time) {
        return Date.from(time.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 获取一天的开始时间，2017,7,22 00:00
     *
     * @param time
     * @return
     */
    public static LocalDateTime getDayStart(LocalDateTime time) {
        return time.withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
    }

    /**
     * 获取一月的开始时间，2017,7,01 00:00
     *
     * @param time
     * @return
     */
    public static LocalDateTime getMothStart(LocalDateTime time) {
        return time.withDayOfMonth(01).withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
    }

}
