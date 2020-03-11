package com.company.parsers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateFactory {
    private final static Calendar calendar = Calendar.getInstance();
    private final static SimpleDateFormat simpleDateFormat= new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Singleton class using only static members
     */
    private DateFactory(){}

    /**
     * @param date string to be converted to a date
     * @return <code>Date</code> object representing the string date
     * @throws Exception
     */
    public static Date StringToDate(String date) throws Exception { return simpleDateFormat.parse(date); }

    /**
     * @param days - in a string format of yyyy-MM-dd
     * @param date - number of days you want to advance days
     * @return <code>Date</code> of the <code>String</code> date advanced by days
     * @throws Exception
     */
    public static Date addDays(int days, String date)
            throws Exception
    {

        Date _date = StringToDate(date);
        calendar.setTime(_date);
        calendar.add(Calendar.DAY_OF_MONTH, days);
        return calendar.getTime();

    }
    public static Date addDays(int days, Date date){
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, days);
        return calendar.getTime();

    }

    /**
     * Converts a <code>Date</code>object back to a String object
     * @param date - date object
     * @return converted to the String from date object
     */
    public static String DateToString(Date date){
        return simpleDateFormat.format(date);
    }

    /**
     * Gives the number of days between two <code>Date</code> objects
     * @param end - upper bound date
     * @param start - lower bound date
     * @return difference in dates in days
     */
    public static int daysBetween(Date end, Date start) {
        long difference =  (end.getTime()- start.getTime())/86400000;
        return (int)Math.abs(difference);
    }

    public static int daysBetween(String end, String start)
            throws Exception
    {
        Date _end = simpleDateFormat.parse(end);
        Date _start = simpleDateFormat.parse(start);
        long difference =  (_end.getTime()- _start.getTime())/86400000;
        return (int)Math.abs(difference);
    }


}
