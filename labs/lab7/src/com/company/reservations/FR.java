package com.company.reservations;

import com.company.parsers.DateFactory;
import com.company.utilities.Pair;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

abstract public class FR {

    protected String RoomName, RoomCode, Decor, BedType;
    protected Date CheckIn, CheckOut;
    protected Integer Adults, Kids, Beds, resCode;
    protected double Rate, basePrice;


    /**
     * Sets the different fields for each of the reservation objects
     * @param ColumnName - returned sql column name
     * @param value - value of the sql returned column
     * @throws Exception
     */
    abstract public void setField(String ColumnName, String value) throws Exception;


    /**
     * returns the a pair of integers representing the (number of
     * weekdays, number of weekend days) between two dates
     * @param start_d - start date
     * @param end_d - send date
     * @return <code>Pair<num week days, num of weekend days></code>
     */
    private static Pair<Integer, Integer> split_days(Date start_d, Date end_d){
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

    /**
     * Given checkin, checkout, and basePrice return the total rate
     * @return total rate
     */
    public static Double totalRateOfStay(Date CheckIn, Date CheckOut, double basePrice){

        // last day does not count
        CheckOut = DateFactory.addDays(-1, CheckOut);
        final Pair<Integer, Integer> days = split_days(CheckIn, CheckOut);
        double rate = roundTwoDecimal((basePrice*days.getKey() + basePrice*days.getValue() * 1.1)*1.18);
        return rate;
    }

    public static Double roundTwoDecimal(Double value){
        return Math.round(value*100.0)/100.0;
    }

    public String getRoomName() {
        return RoomName;
    }

    public String getRoomCode() {
        return RoomCode;
    }

    public String getBedType() {
        return BedType;
    }

    public Date getCheckIn() {
        return CheckIn;
    }

    public Date getCheckOut() {
        return CheckOut;
    }

    public double getRate() {
        return Rate;
    }

    public void setAdults(Integer adults) {
        Adults = adults;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public Integer getBeds() {
        return Beds;
    }


    public Integer getResCode() {
        return resCode;
    }
}
