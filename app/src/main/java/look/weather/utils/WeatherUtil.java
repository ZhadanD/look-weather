package look.weather.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class WeatherUtil {
    public static String getDate(String dateTime) {
        LocalDateTime localDateTime = LocalDateTime.parse(dateTime);

        LocalDate localDate = localDateTime.toLocalDate();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        return localDate.format(dateFormatter);
    }

    public static String getTime(String dateTime) {
        LocalDateTime localDateTime = LocalDateTime.parse(dateTime);

        LocalTime localTime = localDateTime.toLocalTime();

        return localTime.toString();
    }
}
