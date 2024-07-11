package Global;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Date {
    public static LocalDateTime parseDateTime(String date){
        String pattern = "yyyy-MM-dd H:mm:ss";
        DateTimeFormatter df = DateTimeFormatter.ofPattern(pattern);

        return LocalDateTime.parse(date, df);
    }
}
