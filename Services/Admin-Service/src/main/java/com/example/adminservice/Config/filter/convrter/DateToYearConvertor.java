package com.example.adminservice.Config.filter.convrter;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class DateToYearConvertor {
    public static Integer convertDateToYear(Date date) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }
}
