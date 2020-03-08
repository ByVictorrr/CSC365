package com.company.structures;

import com.company.utilities.Pair;

import java.util.Calendar;
import java.util.Date;

abstract public class FR {
    abstract public void setField(String ColumnName, String value) throws Exception;
    public static Pair<Integer, Integer> split_days(Date start_d, Date end_d){

        Integer num_weekDays=0, num_weekendsDays=0;
        Calendar start = Calendar.getInstance();
        start.setTime(start_d);
        Calendar end = Calendar.getInstance();
        end.setTime(end_d);

        while(!start.after(end)){
            int day = start.get(Calendar.DAY_OF_WEEK);
            // to determine if workday
            if ((day != Calendar.SATURDAY) && (day != Calendar.SUNDAY)){
                num_weekDays++;
            }else{
                num_weekendsDays++;
            }
            // add one day to start
            start.add(Calendar.DATE, 1);
        }

        return new Pair<>(num_weekDays, num_weekendsDays);
    }
}
