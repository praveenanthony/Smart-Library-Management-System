package utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtils {
    public static String format(LocalDate date) {
        if (date == null) return "N/A";
        return date.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }
}