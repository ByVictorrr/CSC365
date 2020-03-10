package com.company.structures;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateFactory {
    private final static Calendar calendar = Calendar.getInstance();
    private final static SimpleDateFormat simpleDateFormat= new SimpleDateFormat("yyyy-MM-dd");

    public static Date addDays(int days, String date)
            throws Exception
    {

        Date _date = simpleDateFormat.parse(date);
        calendar.setTime(_date);
        calendar.add(Calendar.DAY_OF_MONTH, days);
        return _date;

    }
    public static String stringFormat(Date date){
        return simpleDateFormat.format(date);
    }

    public static long daysBetween(Date one, Date two) {
        long difference =  (one.getTime()-two.getTime())/86400000;
        return Math.abs(difference);
    }


}
