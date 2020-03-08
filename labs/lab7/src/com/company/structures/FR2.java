package com.company.structures;

import com.company.utilities.Pair;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FR2 extends FR{
    public static final int LAST_NAME=0;
    public static final int FIRST_NAME=1;
    public static final int ROOM_CODE=2;
    public static final int BED=3;
    public static final int BEGIN_STAY=4;
    public static final int END_STAY=5;
    public static final int ADULTS=6;
    public static final int KIDS=7;


    private static Integer res_days;
    // For the actual object
    private String RoomName, RoomCode, Decor, BedType;
    private Date CheckIn, CheckOut;
    private Integer Adults, Kids;
    private Double basePrice;
    private double Rate;

    // set after getting above objects
    private String FirstName, LastName;

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private static final DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);

    public FR2(){
    }

    public static void setRes_days(Integer res_days) {
        FR2.res_days = res_days;
    }

    public void setField(String ColumnName, String value)
            throws Exception
    {
        switch (ColumnName){
            case "RoomName":
                this.RoomName=value;
                break;
            case "RoomCode":
                this.RoomCode= value;
                break;
            case "Checkout":
                this.CheckIn= sdf.parse(value);
                this.CheckOut = DateFactory.addDays(res_days, value);
                break;
            case "Adults":
                this.Adults=Integer.parseInt(value);
                break;
            case "Kids":
                this.Kids=Integer.parseInt(value);
                break;
            case "decor":
                this.Decor=value;
                break;
            case "basePrice":
                this.basePrice=Double.parseDouble(value);
                final Pair<Integer, Integer> days = split_days(CheckIn, CheckOut);
                this.Rate = Math.round((basePrice*days.getKey() + Rate*days.getValue() * 1.1)*1.18*100)/100;
                break;
            case "bedType":
                this.BedType=value;
                break;
        }

    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getCheckIn(){
        return sdf.format(CheckIn);
    }
    public String getCheckOut() {
        return sdf.format(CheckOut);
    }



    @Override
    public String toString() {
        return "FR2{" +
                "RoomName='" + RoomName + '\'' +
                ", RoomCode='" + RoomCode + '\'' +
                ", Decor='" + Decor + '\'' +
                ", BedType='" + BedType + '\'' +
                ", CheckIn=" + getCheckIn() +
                ", CheckOut=" + getCheckOut() +
                ", Adults=" + Adults +
                ", Kids=" + Kids +
                ", Rate=" + Rate +
                '}';
    }

    public String PickedToString() {
        return  "FirstName='"  + FirstName + '\'' +
                ", LastName='"  + LastName + '\'' +
                ", RoomName='" + RoomName + '\'' +
                ", RoomCode='" + RoomCode + '\'' +
                ", CheckIn=" + CheckIn +
                ", CheckOut=" + CheckOut +
                ", Adults=" + Adults +
                ", Kids=" + Kids +
                ", total_rate=" + Rate +
                '}';
    }


    public Double getRate() {
        return Rate;
    }

    public Integer getKids() {
        return Kids;
    }
    public  Integer getAdults(){
        return Adults;
    }

    public String getFirstName() {
        return FirstName;
    }

    public String getLastName() {
        return LastName;
    }

    public String getDecor() {
        return Decor;
    }

    public String getRoomCode() {
        return RoomCode;
    }
    public String getBedType(){
        return BedType;
    }
    // .get(0) = is the num weekdays
    // .get(1) = is the num weekend days

}

