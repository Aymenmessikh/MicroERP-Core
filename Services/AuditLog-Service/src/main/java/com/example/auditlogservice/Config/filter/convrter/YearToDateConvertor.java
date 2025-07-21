package com.example.auditlogservice.Config.filter.convrter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class YearToDateConvertor {
    public static Date convertYearToDate(Integer year) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = format.format(calendar.getTime());
        return format.parse(formattedDate);
    }
}
