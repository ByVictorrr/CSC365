package com.company.reservations;

import com.company.parsers.DateFactory;
import com.company.utilities.Pair;

import java.util.Date;

public class FR2 extends FR{
    public static final int LAST_NAME=0;
    public static final int FIRST_NAME=1;
    public static final int ROOM_CODE=2;
    public static final int BED=3;
    public static final int BEGIN_STAY=4;
    public static final int END_STAY=5;
    public static final int ADULTS=6;
    public static final int KIDS=7;



    private static int userTimeStay;
    static public void  setUserTimeStay(int timeStay){FR2.userTimeStay = timeStay;}


    /**
     * Gives you the number of days between check in and checkout
     * @return the number of days between checkout and check in
     */
    public long numDaysOfStay() { return DateFactory.daysBetween(this.CheckOut, this.CheckIn);}
    /**
     * Sets the objects fields given a returned sql query
     * @param ColumnName - returned sql column name
     * @param value - value of the sql returned column
     * @throws Exception
     */
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
                this.CheckIn=  DateFactory.StringToDate(value);
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
                this.Rate = FR.totalRateOfStay(CheckIn, CheckOut, basePrice);
                break;
            case "bedType":
                this.BedType=value;
                break;
            case "diff":
                this.CheckOut = DateFactory.addDays(userTimeStay, this.CheckIn);
        }

    }



    @Override
    public String toString() {
        return "1{" +
                "RoomName='" + RoomName + '\'' +
                ", RoomCode='" + RoomCode + '\'' +
                ", Decor='" + Decor + '\'' +
                ", BedType='" + BedType + '\'' +
                ", CheckIn=" + CheckIn +
                ", CheckOut=" + CheckOut +
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





}

