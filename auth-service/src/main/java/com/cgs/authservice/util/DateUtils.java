package com.cgs.authservice.util;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class DateUtils {

    public static String currentDate(String pattern) {
        String date;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime now = LocalDateTime.now();
        System.out.println(dtf.format(now));
        date = dtf.format(now);
        return date.toUpperCase();
    }

}
