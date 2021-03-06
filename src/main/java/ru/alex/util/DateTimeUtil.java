package ru.alex.util;

import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DateTimeUtil {

    private static final LocalDateTime MIN_DATE = LocalDateTime.of(1, 1, 1, 0, 0);
    private static final LocalDateTime MAX_DATE = LocalDateTime.of(3000, 1, 1, 0, 0);

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private DateTimeUtil() {}

    // atStartOfDay()
    // объединяет эту дату со временем полуночи, чтобы создать LocalDateTime в начале этой даты
    public static LocalDateTime atStartOfDayOrMin(LocalDate localDate) {
        // возвращаем начала дня
        return localDate != null ? localDate.atStartOfDay() : MIN_DATE;
    }

    // plus(long amountToAdd, TemporalUnit unit)
    // возвращает копию LocalDate с указанным количеством единиц, добавленных в LocalDate
    public static LocalDateTime atStartOfNextDayOrMax(LocalDate localDate) {
        // прибавляем к локальной дате 1 день и у нас получится начало следующего дня
        return localDate != null ? localDate.plus(1, ChronoUnit.DAYS).atStartOfDay() : MAX_DATE;
    }

    public static @Nullable
    LocalDate parseLocalDate(@Nullable String str) {
        return StringUtils.hasLength(str) ? LocalDate.parse(str) : null;
    }

    public static @Nullable
    LocalTime parseLocalTime(@Nullable String str) {
        return StringUtils.hasLength(str) ? LocalTime.parse(str) : null;
    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }
}
