package com.company.structures;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FR2{
    public static final int LAST_NAME=0;
    public static final int FIRST_NAME=1;
    public static final int ROOM_CODE=2;
    public static final int BED=3;
    public static final int BEGIN_STAY=4;
    public static final int END_STAY=5;
    public static final int ADULTS=6;
    public static final int KIDS=7;

    public static Integer count=0;

    public Integer Id;
    // For the actual object
    private String RoomName, RoomCode, Decor;
    private Date CheckIn, CheckOut;
    private Integer Adults, Kids, DayAvail;
    private Double Rate;

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public FR2(){
        Id=count++;
    }

    public void setField(String ColumnName, String value)
            throws Exception
    {
        switch (ColumnName){
            case "RoomName":
                this.RoomName=value;
                break;
            case "Room":
                this.RoomCode= value;
                break;
            case "CheckOut":
                this.CheckIn= sdf.parse(value);
                Calendar.getInstance().setTime(this.CheckIn);
                Calendar.getInstance().add(Calendar.DAY_OF_MONTH, this.DayAvail);
                this.CheckOut = sdf.parse(sdf.format(Calendar.getInstance().getTime()));
                break;
            case "days_avail":
                this.DayAvail=Integer.parseInt(value);
                break;
            case "Adults":
                this.Adults=Integer.parseInt(value);
                break;
            case "Kids":
                this.Kids=Integer.parseInt(value);
                break;
            case "Decor":
                this.Decor=value;
                break;
            case "Rate":
                this.Rate=Double.parseDouble(value);
        }

    }

    public String getCheckIn() {
        Calendar.getInstance().setTime(this.CheckIn);
        return sdf.format(Calendar.getInstance().getTime());
    }
    public String getCheckOut() {
        Calendar.getInstance().setTime(this.CheckOut);
        return sdf.format(Calendar.getInstance().getTime());
    }
}

