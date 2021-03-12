package com.nung.schedule.utils;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

@Component
@Data
public class DateUtil {
    private GregorianCalendar calendar = new GregorianCalendar();
    private DateFormat format = new SimpleDateFormat("dd.MM.yyyy");

    public String getTodayDate() {
        return format.format(calendar.getTime());
    }

    public String getTomorrowDate() {
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        return format.format(calendar.getTime());
    }

    public String getDateForNext(int dayOfWeek) {
        if(calendar.get(Calendar.DAY_OF_WEEK) > dayOfWeek) {
            int increment = 7 - (calendar.get(Calendar.DAY_OF_WEEK) - dayOfWeek);
            calendar.add(Calendar.DAY_OF_MONTH, increment);
        }
        return format.format(calendar.getTime());
    }
}
